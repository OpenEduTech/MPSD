#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: run_cls.py
@date: 2023/6/11 0:51
@desc: 
"""
import argparse
import json
import os.path
import time

import jieba
import pandas as pd
import torch
from torch.nn import CrossEntropyLoss
from torch.optim import Adam
from torch.utils.data import DataLoader

from model import TextCNN
from dataset import ProcessUtil, ChineseTextDataset
from operation import train, validate, test, predict, save


def parse_arguments():
    parser = argparse.ArgumentParser(description='CNN text classificer')
    # learning
    parser.add_argument('-lr', type=float, default=0.001, help='initial learning rate [default: 0.001]')
    parser.add_argument('-epochs', type=int, default=8, help='number of epochs for train [default: 10]')
    parser.add_argument('-batch-size', type=int, default=128, help='batch size for training [default: 128]')
    parser.add_argument('-use-cuda', type=bool, default=True, help='if use cuda to accelerate')
    parser.add_argument('-log-interval', type=int, default=100,
                        help='how many steps to wait before logging training status [default: 100]')
    parser.add_argument('-test-interval', type=int, default=200,
                        help='how many steps to wait before testing [default: 200]')
    parser.add_argument('-save-interval', type=int, default=1000,
                        help='how many steps to wait before saving [default: 1000]')
    parser.add_argument('-output-path', type=str, default='../../outputs/cls_model', help='directory to save the snapshot')
    # model
    parser.add_argument('-dropout', type=float, default=0.5, help='dropout probability [default: 0.5]')
    parser.add_argument('-embed-dim', type=int, default=248, help='number of embedding dimension [default: 128]')
    parser.add_argument('-kernel-num', type=int, default=64, help='number of kernels')
    parser.add_argument('-kernel-sizes', type=str, default='3,4,5',
                        help='comma-separated kernel size to use for convolution')
    # option
    parser.add_argument('-snapshot', type=str, default=None, help='filename of model snapshot [default: None]')
    parser.add_argument('-train', action='store_true', default=False, help='train a new model')
    parser.add_argument('-test', action='store_true', default=False,
                        help='test on testset, combined with -snapshot to load model')
    parser.add_argument('-predict', action='store_true', default=False, help='predict label of console input')
    args = parser.parse_args()

    return args


if __name__ == '__main__':
    args = parse_arguments()

    # load
    train_df = pd.read_csv('../../data/cls/train.csv')
    test_df = pd.read_csv('../../data/cls/test.csv')
    train_df['text'] = train_df['text'].apply(lambda row: list(jieba.cut(row)))
    test_df['text'] = test_df['text'].apply(lambda row: list(jieba.cut(row)))
    # util
    util = ProcessUtil(
        vocab_path='../../data/cls/vocab.txt',
        label_path='../../data/cls/label.txt'
    )
    # dataset
    train_dataset = ChineseTextDataset(train_df['text'], train_df['label'], util)
    test_dataset = ChineseTextDataset(test_df['text'], test_df['label'], util)
    # dataloader
    train_dataloader = DataLoader(train_dataset, batch_size=args.batch_size, shuffle=True)
    test_dataloader = DataLoader(test_dataset, batch_size=args.batch_size)
    # model
    model = TextCNN(
        embed_num=len(util.vocab_transform.vocab),
        embed_dim=args.embed_dim,
        class_num=len(util.label_transform.label_names),
        kernel_num=args.kernel_num,
        kernel_sizes=[int(k) for k in args.kernel_sizes.split(',')]
    )
    # 交叉熵损失
    criterion = CrossEntropyLoss()
    # Adam 优化器
    optimizer = Adam(model.parameters(), lr=args.lr, weight_decay=1e-3)
    # 计算设备
    device = torch.device('cuda' if args.use_cuda and torch.cuda.is_available() else 'cpu')
    model = model.to(device)
    best_acc = 0
    time_prefix = time.strftime('%Y%m%d%H%M', time.localtime())
    for epoch in range(1, args.epochs+1):
        print('\nepoch: {}\n'.format(epoch))
        t_loss = train(train_dataloader, model, criterion, optimizer, device)
        v_loss = validate(train_dataloader, model, criterion, device)
        print('\ntrain loss: {}\nvalidate loss: {}\n'.format(t_loss, v_loss))
        acc = test(test_dataloader, model, device, util.label_transform.label_names)
        # 保存acc最高的模型&参数
        if best_acc < acc:
            save(model, os.path.join(args.output_path, time_prefix), 'acc_{}.pt'.format(acc))
            with open(os.path.join(args.output_path, time_prefix, 'config.json'), 'w', encoding='utf-8') as jf:
                json.dump({
                    'embed_num': len(util.vocab_transform.vocab),
                    'embed_dim': args.embed_dim,
                    'class_num': len(util.label_transform.label_names),
                    'kernel_num': args.kernel_num,
                    'kernel_sizes': [int(k) for k in args.kernel_sizes.split(',')]
                }, jf)
            best_acc = acc
    # 用户输入预测
    while True:
        user_input = input().strip()
        if user_input == 'exit()':
            break
        score, label = predict(list(jieba.cut(user_input)), model, device, util)
        print('{}-{}'.format(score, label))
    print('enjoy!')
