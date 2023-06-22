#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: dataset.py
@date: 2023/6/15 下午6:44
@description: 
"""
import torch
import torchtext.transforms as T
import torchtext.vocab as V
from torch.utils.data import Dataset


class ProcessUtil(object):

    UNK = '<unk>'
    PAD = '<pad>'
    O_TAG = 'O'

    def __init__(self, vocab_path, label_path, max_padding=20, vocab_padding=PAD, label_padding=O_TAG):
        self.max_padding = max_padding
        self.vocab_padding = vocab_padding
        self.label_padding = label_padding
        with open(vocab_path, encoding='utf-8') as vf:
            words = []
            for line in vf:
                words.append(line.strip())
        vocab = V.build_vocab_from_iterator(words, specials=[self.UNK, self.PAD])
        vocab.set_default_index(0)
        self.vocab_transform = T.VocabTransform(vocab)
        self.label_transform = T.LabelToIndex(label_path=label_path)
        self.truncate = T.Truncate(max_seq_len=max_padding)

    def stoi(self, text, tag):
        assert len(text) == len(tag), 'ProcessUtil Error: item not match !'
        indices = len(text)
        # mask
        mask = [True] * min(indices, self.max_padding) + [False] * max(0, self.max_padding - indices)
        # padding
        text.extend([self.vocab_padding] * max(0, self.max_padding - indices))
        tag.extend([self.label_padding] * max(0, self.max_padding - indices))
        # transform and truncate
        text_idx = self.truncate(self.vocab_transform(text))
        tag_idx = self.truncate(self.label_transform(tag))
        return text_idx, tag_idx, mask

    def itos(self, tag_idx):
        return [self.label_transform.label_names[ti] for ti in tag_idx]


class ChineseBioDataset(Dataset):

    def __init__(self, texts, tags, util):
        # texts example: [['你'， '好', ..., '!'], ..., ['开'， '心', ..., '!']]
        # tags example: [['O'， 'B', ..., 'I'], ..., ['B'， 'I', ..., 'O']]
        assert len(texts) == len(tags), 'BioDataset Error: data not match !'
        self.texts = texts
        self.tags = tags
        self.util = util

    def __len__(self):
        return len(self.texts)

    def __getitem__(self, index):
        text = self.texts[index][:]
        tag = self.tags[index][:]
        text_idx, tag_idx, mask = self.util.stoi(text, tag)
        return torch.tensor(
            text_idx, dtype=torch.long
        ), torch.tensor(
            tag_idx, dtype=torch.long
        ), torch.tensor(
            mask, dtype=torch.bool
        )
