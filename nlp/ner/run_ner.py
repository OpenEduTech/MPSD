#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: run_cls.py
@date: 2023/6/17 19:23
@desc: 
"""
import json
import os
import time
import pandas as pd
import torch
from torch.utils.data import DataLoader
import torch.optim as optim
import seaborn as sns
import matplotlib.pyplot as plt


from dataset import ProcessUtil, ChineseBioDataset
from model import BiLSTM_CRF
from operation import train, test, validate, predict, save

if __name__ == '__main__':
    output_dir = os.path.join('../../outputs/ner_model', time.strftime('%Y%m%d%H%M', time.localtime()))
    if not os.path.isdir(output_dir):
        os.makedirs(output_dir)
    # load
    train_df = pd.read_csv('../../data/ner/train.csv')
    test_df = pd.read_csv('../../data/ner/test.csv')
    # pre_process
    train_df['text'] = train_df['text'].apply(lambda row: list(row))
    train_df['label'] = train_df['label'].apply(lambda row: row.split())
    test_df['text'] = test_df['text'].apply(lambda row: list(row))
    test_df['label'] = test_df['label'].apply(lambda row: row.split())
    # util
    util = ProcessUtil(
        vocab_path='../../data/ner/vocab.txt',
        label_path='../../data/ner/label.txt'
    )
    # dataset
    train_dataset = ChineseBioDataset(train_df['text'], train_df['label'], util)
    test_dataset = ChineseBioDataset(test_df['text'], test_df['label'], util)
    # dataloader
    train_dataloader = DataLoader(train_dataset, batch_size=16, shuffle=True)
    test_dataloader = DataLoader(test_dataset, batch_size=16)
    # device
    device = torch.device('cuda')
    model = BiLSTM_CRF(
        len(util.vocab_transform.vocab),
        len(util.label_transform.label_names),
        200, 128, 2, device
    )
    args = {
        'lr': 0.01,
        'momentum': 0.9,
        'weight_decay': 1e-3,
        'epochs': 10
    }
    model.to(device)
    optimizer = optim.SGD(
        model.parameters(),
        lr=args['lr'], weight_decay=args['weight_decay']
    )
    best_acc = 0
    for epoch in range(args['epochs']):
        print('epoch: {}\n'.format(epoch))
        t_loss = train(train_dataloader, model, optimizer, device)
        v_loss = validate(train_dataloader, model, device)
        print('train loss: {}\nvalidate loss: {}\n'.format(t_loss, v_loss))
        acc, cm = test(test_dataloader, model, device, util)
        sns.heatmap(cm, annot=True, cmap='Blues')
        plt.xlabel('Predicted labels')
        plt.ylabel('True labels')
        plt.savefig(os.path.join(output_dir, 'cm4epoch{}.png'.format(epoch)))
        plt.clf()
        if best_acc < acc:
            save(model, output_dir, 'acc_{}.pt'.format(acc))
            with open(os.path.join(output_dir, 'config.json'), 'w', encoding='utf-8') as jf:
                json.dump({
                    'vocab_size': len(util.vocab_transform.vocab),
                    'num_tags': len(util.label_transform.label_names),
                    'embedding_dim': 200,
                    'hidden_dim': 128,
                    'num_layers': 2
                }, jf)
    # 用户输入预测
    while True:
        user_input = input().strip()
        if user_input == 'exit()':
            break
        entities = predict(list(user_input), model, device, util)
        print(entities)
    print('enjoy!')
