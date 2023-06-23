#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: model.py
@date: 2023/6/15 下午1:12
@description: 
"""
import torch
from torch import nn
from torchcrf import CRF


class BiLSTM_CRF(nn.Module):

    def __init__(self, vocab_size: int, num_tags: int,
                 embedding_dim: int, hidden_dim: int, num_layers: int, device: torch.device):
        super().__init__()
        self.vocab_size = vocab_size
        self.num_tags = num_tags
        self.embedding_dim = embedding_dim
        self.hidden_dim = hidden_dim
        self.num_layers = num_layers
        self.device = device
        self.word_embeds = nn.Embedding(vocab_size, embedding_dim)
        self.lstm = nn.LSTM(
            input_size=embedding_dim,
            hidden_size=hidden_dim // 2,
            num_layers=num_layers,
            bidirectional=True,
            batch_first=True
        )
        self.dropout = nn.Dropout(0.5)
        self.hidden2tag = nn.Linear(hidden_dim, self.num_tags)
        self.crf = CRF(self.num_tags, batch_first=True)

    def init_hidden(self, batch_size):
        init_hidden_dim = self.hidden_dim // 2
        init_first_dim = self.num_layers * 2
        return (
            torch.randn(init_first_dim, batch_size, init_hidden_dim).to(self.device),
            torch.randn(init_first_dim, batch_size, init_hidden_dim).to(self.device)
        )

    def get_lstm_features(self, inputs):
        batch_size = inputs.size(0)
        hidden = self.init_hidden(batch_size)
        inputs = self.word_embeds(inputs)  # size: batch * padding_length * embedding_dim
        outputs, hidden = self.lstm(inputs, hidden)
        outputs = self.dropout(outputs)
        feats = self.hidden2tag(outputs)
        return feats

    def neg_log_likelihood(self, inputs, tags, masks):
        emissions = self.get_lstm_features(inputs)
        nll_loss = -self.crf(emissions, tags, masks)
        return nll_loss

    def forward(self, inputs, masks):
        emissions = self.get_lstm_features(inputs)
        tags = self.crf.decode(emissions, masks)
        return tags
