#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: model.py
@date: 2023/6/10 23:59
@desc:
"""
import torch
import torch.nn as nn
import torch.nn.functional as F


class TextCNN(nn.Module):
    def __init__(self, embed_num, embed_dim, class_num, kernel_num, kernel_sizes, dropout=0.5):
        super(TextCNN, self).__init__()
        in_channels = 1
        out_channels = kernel_num
        self.embed = nn.Embedding(embed_num, embed_dim)
        self.convs = nn.ModuleList([
            nn.Conv2d(in_channels, out_channels, (f, embed_dim), padding=(2, 0))
            for f in kernel_sizes
        ])
        self.dropout = nn.Dropout(dropout)
        self.fc = nn.Linear(out_channels * len(kernel_sizes), class_num)

    def forward(self, x):
        x = self.embed(x)       # (N, token_num, embed_dim)
        x = x.unsqueeze(1)      # (N, in_channels, token_num, embed_dim)
        x = [F.relu(conv(x)).squeeze(3) for conv in self.convs]     # [(N, out_channels, token_num) * len(kernel_sizes)]
        x = [F.max_pool1d(i, i.size(2)).squeeze(2) for i in x]      # [(N, out_channels) * len(kernel_sizes)]
        x = torch.cat(x, 1)     # (N, out_channels * len(kernel_sizes))
        x = self.dropout(x)     # (N, out_channels * len(kernel_sizes))
        logit = self.fc(x)      # (N, class_num)
        return logit
