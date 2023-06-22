#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: operation.py
@date: 2023/6/16 上午12:18
@description: 
"""
import os
import torch
import numpy as np
from tqdm import tqdm
from sklearn.metrics import precision_score, recall_score, f1_score, accuracy_score, confusion_matrix


def train(dataloader, model, optimizer, device):
    model.train()
    count_loss = 0
    for inputs, idxs, masks in tqdm(dataloader):
        # tensor 转移到 device
        inputs = inputs.to(device)
        idxs = idxs.to(device)
        masks = masks.to(device)
        # 清空梯度
        optimizer.zero_grad()
        # 计算负对数似然损失
        loss = model.neg_log_likelihood(inputs, idxs, masks)
        # 反向传播
        loss.backward()
        # 优化
        optimizer.step()
        # 累积loss
        count_loss += loss.item()
    return count_loss / len(dataloader)


def validate(dataloader, model, device):
    model.eval()
    count_loss = 0
    for inputs, idxs, masks in tqdm(dataloader):
        # tensor 转移到 device
        inputs = inputs.to(device)
        idxs = idxs.to(device)
        masks = masks.to(device)
        # 计算负对数似然损失
        loss = model.neg_log_likelihood(inputs, idxs, masks)
        # 累积loss
        count_loss += loss.item()
    return count_loss / len(dataloader)


def collect_entities(tags):
    entities = []
    # 遍历tags中的每个标签，寻找符合条件的实体
    idx = 0
    while idx < len(tags):
        offset = 1
        if tags[idx].startswith('B'):
            # 找结尾
            while True:
                if idx + offset < len(tags) and tags[idx + offset].startswith('I'):
                    offset += 1
                    continue
                break
            # 记录数据
            entities.append({'start': idx, 'end': idx + offset, 'entity': tags[idx].replace('B-', '')})
        # 跳过不必要的循环
        idx += offset
    return entities


def entities_score(y_true, y_pred, tolerance=0):
    result = {}
    t_idx = 0
    p_idx = 0
    while True:
        if t_idx >= len(y_true) or p_idx >= len(y_pred):
            break
        t_item = y_true[t_idx]
        p_item = y_pred[p_idx]
        if t_item['start'] < p_item['start']:   # t 在 p 的左边
            record = result.setdefault(t_item['entity'], {
                'TP': 0, 'FP': 0, 'FN': 0
            })
            record['FN'] += 1
            if t_idx < len(y_true) - 1:
                t_idx += 1
            else:
                p_idx += 1
        elif t_item['start'] > p_item['start']:   # t 在 p 的右边
            record = result.setdefault(t_item['entity'], {
                'TP': 0, 'TN': 0, 'FP': 0, 'FN': 0
            })
            record['FP'] += 1
            if p_idx < len(y_pred) - 1:
                p_idx += 1
            else:
                t_idx += 1
        else:
            t_idx += 1
            p_idx += 1
            if t_item['entity'] == p_item['entity'] and abs(t_item['end'] - p_item['end']) <= tolerance:
                record = result.setdefault(t_item['entity'], {
                    'TP': 0, 'TN': 0, 'FP': 0, 'FN': 0
                })
                record['TP'] += 1
    outputs = {}
    for key, value in result.items():
        outputs[key] = (
            value['TP'] / (value['TP'] + value['FP']),
            value['TP'] / (value['TP'] + value['FN'])
        )
    return outputs


def test(dataloader, model, device, util):
    all_real_idxs = []
    all_pred_idxs = []
    all_entities_score = {}
    model.eval()
    with torch.no_grad():
        for inputs, idxs, masks in tqdm(dataloader):
            # tensor 转移到 device
            inputs = inputs.to(device)
            idxs = idxs.to(device)
            masks = masks.to(device)
            pred_idxs = model(inputs, masks)
            pred_idxs = [idx for line_idx in pred_idxs for idx in line_idx]
            real_idxs = idxs[masks].cpu().numpy()
            # 用于统计实体指标
            batch_entities_score = entities_score(
                collect_entities(util.itos(pred_idxs)),
                collect_entities(util.itos(real_idxs))
            )
            for bes_key, bes_value in batch_entities_score.items():
                record = all_entities_score.setdefault(bes_key, [])
                record.append(bes_value)
            # 记录所有idx，用于统计每一类的指标
            all_pred_idxs.extend(pred_idxs)
            all_real_idxs.extend(real_idxs)
    for entity_name, pr_value in all_entities_score.items():
        pr_avg = np.mean(np.array(pr_value), axis=0)
        print('{}\tprecision: {}\trecall: {}\n'.format(entity_name, pr_avg[0], pr_avg[1]))
    precision = precision_score(all_real_idxs, all_pred_idxs, average='weighted')
    recall = recall_score(all_real_idxs, all_pred_idxs, average='weighted')
    f1 = f1_score(all_real_idxs, all_pred_idxs, average='weighted')
    accuracy = accuracy_score(all_pred_idxs, all_real_idxs)
    print('precision: {}\trecall: {}\tf1: {}\taccuracy: {}'.format(precision, recall, f1, accuracy))
    cm = confusion_matrix(all_real_idxs, all_pred_idxs)
    return accuracy, cm


def predict(text, model, device, util):
    model.eval()
    inputs, _, masks = util.stoi(text, ['O'] * len(text))
    inputs = torch.tensor([inputs], dtype=torch.long).to(device)
    masks = torch.tensor([masks], dtype=torch.bool).to(device)
    pred_idxs = model(inputs, masks)
    pred_idxs = [idx for line_idx in pred_idxs for idx in line_idx]
    return collect_entities(util.itos(pred_idxs))


def save(model, output_path, model_name):
    if not os.path.isdir(output_path):
        os.makedirs(output_path)
    save_path = os.path.join(output_path, model_name)
    torch.save(model.state_dict(), save_path)
