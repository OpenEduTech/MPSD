#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: operation.py
@date: 2023/6/11 0:50
@desc: 
"""
import os
import torch
from torch.nn import functional as F
from tqdm import tqdm
from sklearn.metrics import accuracy_score, classification_report


def train(dataloader, model, criterion, optimizer, device):
    # train
    model.train()
    count_loss = 0
    for text_idx, label_idx in tqdm(dataloader):
        # 数据移动到计算设备
        text_idx = text_idx.to(device)
        label_idx = label_idx.to(device)
        # 清空梯度
        optimizer.zero_grad()
        # forward
        logit = model(text_idx)
        # 交叉熵loss
        loss = criterion(logit, label_idx)
        # backward
        loss.backward()
        optimizer.step()
        # 累积loss
        count_loss += loss.item()
    return count_loss / len(dataloader)


def validate(dataloader, model, criterion, device):
    # validate
    model.eval()
    count_loss = 0
    for text_idx, label_idx in tqdm(dataloader):
        # 数据移动到计算设备
        text_idx = text_idx.to(device)
        label_idx = label_idx.to(device)
        # forward
        logit = model(text_idx)
        # 交叉熵loss
        loss = criterion(logit, label_idx)
        # 累积loss
        count_loss += loss.item()
    return count_loss / len(dataloader)


def test(dataloader, model, device, label_names):
    # test
    model.eval()
    all_real = []
    all_pred = []
    with torch.no_grad():
        for text_idx, label_idx in tqdm(dataloader):
            # 数据移动到计算设备
            text_idx = text_idx.to(device)
            # 推理
            logit = model(text_idx)
            # max
            pred_result = torch.max(logit, dim=1)[1]
            # append
            all_pred.extend(pred_result.cpu().numpy())
            all_real.extend(label_idx.cpu().numpy())
    print(classification_report(all_real, all_pred, target_names=label_names, zero_division=0))
    acc = accuracy_score(all_real, all_pred)
    return acc


def predict(text, model, device, util):
    model.eval()
    text_idx = util.stoi(text)
    inputs = torch.tensor([text_idx], dtype=torch.long).to(device)
    # 推理
    outputs = model(inputs)
    probs = F.softmax(outputs, dim=1)
    score, label_idx = torch.max(probs, 1)
    return score.item(), util.itos(label_idx.item())


def save(model, output_path, model_name):
    if not os.path.isdir(output_path):
        os.makedirs(output_path)
    save_path = os.path.join(output_path, model_name)
    torch.save(model.state_dict(), save_path)

