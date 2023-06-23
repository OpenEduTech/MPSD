#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: dataset.py
@date: 2023/6/19 11:24
@desc: 
"""
import jieba
import torch
import torchtext.transforms as T
import torchtext.vocab as V
from torch.utils.data import Dataset


class ProcessUtil(object):
    UNK = '<unk>'
    PAD = '<pad>'

    def __init__(self, vocab_path, label_path, max_padding=20):
        self.max_padding = max_padding

        def load_by_line(filepath):
            result = []
            with open(filepath, encoding='utf-8') as vf:
                for line in vf:
                    result.append(line.strip())
            return result

        vocab = V.build_vocab_from_iterator([load_by_line(vocab_path)], specials=[self.UNK, self.PAD])
        vocab.set_default_index(0)
        self.vocab_transform = T.VocabTransform(vocab)
        self.label_transform = T.LabelToIndex(label_names=load_by_line(label_path))
        self.truncate = T.Truncate(max_seq_len=max_padding)

    def stoi(self, text, label=None):
        indices = len(text)
        # padding
        text.extend([self.PAD] * max(0, self.max_padding - indices))
        # transform and truncate
        text_idx = self.truncate(self.vocab_transform(text))
        # 训练时需要label数据
        if label is not None:
            label_idx = self.label_transform(label)
            return text_idx, label_idx
        # 推理时不需要label数据
        return text_idx

    def itos(self, label_idx):
        return self.label_transform.label_names[label_idx]


class ChineseTextDataset(Dataset):

    def __init__(self, texts, labels, util):
        # texts example: [['你'， '好', ..., '!'], ..., ['开'， '心', ..., '!']]
        # tags example: [['O'， 'B', ..., 'I'], ..., ['B'， 'I', ..., 'O']]
        assert len(texts) == len(labels), 'TextDataset Error: data not match !'
        self.texts = texts
        self.labels = labels
        self.util = util

    def __len__(self):
        return len(self.texts)

    def __getitem__(self, index):
        text = self.texts[index][:]
        label = self.labels[index][:]
        text_idx, label_idx = self.util.stoi(text, label)
        return torch.tensor(text_idx, dtype=torch.long), torch.tensor(label_idx, dtype=torch.long)
