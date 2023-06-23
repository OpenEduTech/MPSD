#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: mock_utils.py
@date: 2023/6/16 上午10:46
@description: 
"""
import os
import csv
import random
import numpy as np
import jieba

jieba.initialize()

# 噪声数据, 用于数据增强
NOISES = [
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    '，', '。', '、', '；', '!', '~',
    '啊', '呀', '哦', '噢', '哈', '嘿', '嗯', '唔'
]

# 同义词, 用于构造语句数据
WORDS_SEQ = {
    'how': ['怎么样', '如何', '何以', '怎么', '怎么', '怎么', '怎样', '怎么', '怎样', '咋样', '咋样', '咋样', '咋地', '咋地', '咋地'],
    'why': ['为什么', '为何', '何故', '何以', '为啥', '何以故'],
    'what': ['什么', '何', '甚', '哪', '哪些', '那', '有何', '何事', '何物', '什么事', '什么物'],
    'do': ['办', '办', '办', '办', '办', '做', '做', '做', '做', '做', '处理', '解决', '操作'],
    'is': ['是', '在', '属于', '在于', '代表', '表示', '指的是', '意味着', '包括', '归属于'],
    'may': ['可能', '或许', '可能会', '恐怕', '大概', '也许', '有可能', '很可能'],
    'should': ['应该', '必须', '需要', '要', '希望', '愿意', '可以', '理应', '当然', '有必要'],
    'feel': ['感受', '觉得', '意识到', '认为', '知觉', '发觉', '感应', '体验', '体识', '感觉', '察觉', '出现', '显现'],
    'cure': ['治疗', '医治', '治愈', '康复', '痊愈', '治癒', '止痊', '治好', '缓解', '减轻'],
    'illness': ['疾病', '病症', '病患', '疾患', '病病', '疾疾', '流行病', '病毒性疾病', '传染病'],
    'suffer': ['得', '患', '感染', '患染', '发病', '罹患', '患有'],
    'explain': ['介绍', '了解', '告知', '理解', '解释', '详情', '信息', '定义'],
    'prevent': ['预防', '防范', '抵制', '抵御', '防止', '避免', '免得', '避开', '免于'],
    'check': ['检查', '检测', '体检', '化验', '验证', '查验', '查询', '查', '测', '检', '监测'],
    'take': ['吃', '食', '服', '摄入', '饮', '喝', '食用', '服用', '饮食', '饮用', '伙食', '膳食', '忌口', '补品',
             '食谱', '菜谱', '食物', '补品'],
    'good': ['好', '有益', '改善', '提升', '增加', '增进', '增强', '益处', '好处', '适合', '适宜', '宜', '宜于'],
    'bad': ['不好', '不益', '差', '降低', '减少', '不良', '糟糕', '弊处', '恶劣', '有害', '不宜', '弊'],
    'suggest': ['建议', '提示', '忠告', '注意', '推荐', '帮助', '指南', '提醒']
}

# 问句超模板, 用于构造语句数据
TEMPLATES_SEQ = {
    '症状问办法': [
        ('entity#symptom', 'how', 'do'),
        ('how', 'do', 'entity#symptom'),
        ('entity#symptom', 'how', 'cure'),
        ('how', 'cure', 'entity#symptom'),
        ('how', 'cure', 'entity#symptom'),
        ('entity#symptom', 'what', 'do'),
        ('what', 'do', 'entity#symptom'),
        ('entity#symptom', 'what', 'cure'),
        ('what', 'cure', 'entity#symptom'),
        ('what', 'cure', 'entity#symptom'),
    ],
    '症状问疾病': [
        ('entity#symptom', 'suffer', 'what', 'illness'),
        ('suffer', 'what', 'illness', 'entity#symptom'),
        ('why', 'feel', 'entity#symptom'),
        ('feel', 'entity#symptom', 'why'),
    ],
    '症状问预防': [
        ('how', 'prevent', 'entity#symptom'),
        ('entity#symptom', 'how', 'prevent'),
        ('what', 'prevent', 'entity#symptom'),
        ('prevent', 'entity#symptom', 'what'),
        ('do', 'what', 'prevent', 'entity#symptom'),
        ('prevent', 'entity#symptom', 'do', 'what'),
    ],
    '症状问检查': [
        ('entity#symptom', 'check', 'what'),
        ('check', 'what', 'entity#symptom'),
        ('entity#symptom', 'what', 'check'),
        ('what', 'check', 'entity#symptom'),
        ('entity#symptom', 'how', 'check'),
        ('how', 'check', 'entity#symptom'),
    ],
    '症状问饮食': [
        ('entity#symptom', 'take', 'what'),
        ('take', 'what', 'entity#symptom'),
        ('what', 'good', 'entity#symptom'),
        ('what', 'bad', 'entity#symptom'),
        ('entity#symptom', 'good', 'what'),
        ('entity#symptom', 'bad', 'what'),
    ],
    '疾病问办法': [
        ('entity#disease', 'how', 'do'),
        ('how', 'do', 'entity#disease'),
        ('entity#disease', 'how', 'cure'),
        ('how', 'cure', 'entity#disease'),
        ('how', 'cure', 'entity#disease'),
        ('entity#disease', 'what', 'do'),
        ('what', 'do', 'entity#disease'),
        ('entity#disease', 'what', 'cure'),
        ('what', 'cure', 'entity#disease'),
        ('what', 'cure', 'entity#disease'),
    ],
    '疾病问详情': [
        ('explain', 'entity#disease'),
        ('entity#disease', 'explain'),
        ('what', 'is', 'entity#disease'),
        ('entity#disease', 'is', 'what'),
        ('what', 'entity#disease', 'explain'),
        ('explain', 'entity#disease', 'what'),
        ('explain', 'what', 'entity#disease'),
        ('entity#disease', 'explain', 'what'),
    ],
    '疾病问药食': [
        ('entity#disease', 'take', 'what'),
        ('take', 'what', 'entity#disease'),
        ('what', 'good', 'entity#disease'),
        ('what', 'bad', 'entity#disease'),
        ('entity#disease', 'good', 'what'),
        ('entity#disease', 'bad', 'what'),
    ],
    '疾病问建议': [
        ('entity#disease', 'suggest', 'what'),
        ('suggest', 'what', 'entity#disease'),
        ('entity#disease', 'suggest', 'how', 'do'),
        ('suggest', 'how', 'do', 'entity#disease'),
        ('entity#disease', 'what', 'suggest'),
        ('what', 'suggest', 'entity#disease'),
    ]
}

# 不同实体在超模板中的index range
DIFFER_INDEX = [0, 32, 62]


def load_entities(file_path):
    """
    从文件中读取实体数据
    文件格式：每行为一个实体
    :param file_path: 文件路径
    :return: list(str)
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        return [line.strip() for line in lines]


# 实体数据
ENTITIES_SEQ = {
    'entity#symptom': None,
    'entity#disease': None
}


def produce_cls_template(pick_from: dict):
    """
    以同义词为基础，对超模板进行笛卡尔积运算，生产模板数据，用于分类
    :param pick_from: 超模板
    :return: 模板数据
    """
    result = {}
    for key, templates in TEMPLATES_SEQ.items():
        cls_template = []
        for temp in templates:
            words = []
            for slot in temp:
                # pick_from中能找到对应的slot则将其对应数据加入候选序列，否则保持slot不变，后续在做替换
                if slot in pick_from:
                    words.append(pick_from[slot])
                else:
                    words.append([slot])
            # 笛卡尔积运算
            mesh = np.meshgrid(*words)
            cart = np.array(mesh).T.reshape(-1, len(words))
            cls_template.extend(cart.tolist())
        result[key] = cls_template
    return result


def render_cls_template(cls_template: dict, pick_from: dict, augment_from: list, output_dir: str, rate=0.9):
    train_cls_data = []
    test_cls_data = []
    labels = list(cls_template.keys())
    for key, templates in cls_template.items():
        # key标签的数据
        cls_data = []
        for t_item in templates:
            # 随机选择一个实体替换模板中的实体标签
            fragments = [
                # random.choice(pick_from.get(slot))
                '{}{}'.format(random.choice(pick_from.get(slot)), slot.replace('entity#', ''))     # 嵌入实体类型特征，提高准确率
                if 'entity#' in slot else slot
                for slot in t_item
            ]
            # 数据增强，随机插入 0~2 个噪声
            for _ in range(*random.choices([0, 1, 2], weights=[0.4, 0.3, 0.3], k=1)):
                noise = random.choice(augment_from)
                at = random.randint(0, len(fragments))
                fragments.insert(at, noise)
            # append
            cls_data.append((''.join(fragments), key))
        # 乱序
        random.shuffle(cls_data)
        # 拆分训练集和测试集
        indices = int(len(cls_data) * rate)
        train_cls_data.extend(cls_data[:indices])
        test_cls_data.extend(cls_data[indices:])
    # 词典
    cls_vocab = []
    # 存储
    csv_header = ('text', 'label')
    with open(os.path.join(output_dir, 'train.csv'), 'w', newline='', encoding='utf-8') as train_file:
        writer = csv.writer(train_file)
        writer.writerow(csv_header)
        for row in train_cls_data:
            writer.writerow(row)
            # 句子再分词，建词表
            cls_vocab.extend(list(jieba.cut(row[0])))
    with open(os.path.join(output_dir, 'test.csv'), 'w', newline='', encoding='utf-8') as test_file:
        writer = csv.writer(test_file)
        writer.writerow(csv_header)
        for row in test_cls_data:
            writer.writerow(row)
            # 句子再分词，建词表
            cls_vocab.extend(list(jieba.cut(row[0])))
    with open(os.path.join(output_dir, 'label.txt'), 'w', newline='\n', encoding='utf-8') as label_file:
        for row in labels:
            label_file.write('{}\n'.format(row))
    with open(os.path.join(output_dir, 'vocab.txt'), 'w', newline='\n', encoding='utf-8') as vocab_file:
        for row in set(cls_vocab):
            vocab_file.write('{}\n'.format(row))
    print('train: {}\ntest: {}'.format(len(train_cls_data), len(test_cls_data)))


def produce_ner_template(pick_from: dict, differ: list):
    """
    以实体为基础，为每一个实体从超模板中随机生成一个模板，用于命名实体抽取
    :param pick_from: 实体
    :param differ: 不同实体间的间隔index，用于随机采样模板
    :return: 模板数据
    """
    ner_template = []
    for key, templates in TEMPLATES_SEQ.items():
        for temp in templates:
            words = []
            bio = []
            for idx, slot in enumerate(temp):
                # pick_from中能找到对应的slot则将其对应数据加入候选序列，否则保持slot不变，后续在做替换
                if slot in pick_from:
                    words.append(pick_from[slot])
                    bio.append(slot.replace('entity#', '').upper())
                else:
                    words.append([slot])
                    bio.append('O')
            # 笛卡尔积运算
            mesh = np.meshgrid(*words)
            cart = np.array(mesh).T.reshape(-1, len(words))
            ner_template.append([cart.tolist(), bio])
    sample_ner_template = []
    # 按实体进行随机采样
    for diff_idx in range(len(differ) - 1):
        sample_out = []
        for column in range(len(ner_template[differ[diff_idx]][0])):
            row = random.choice(range(differ[diff_idx], differ[diff_idx + 1]))
            sample_out.append([ner_template[row][0][column], ner_template[row][1]])
        sample_ner_template.extend(sample_out)
    return sample_ner_template


def render_ner_template(ner_template: list, pick_from: dict, augment_from: list, output_dir: str, rate=0.9):
    train_cls_data = []
    test_cls_data = []
    labels = ['O']
    ner_data = []
    for template, bio in ner_template:
        fragments = {
            'texts': [],
            'tags': []
        }
        for idx, tag in enumerate(bio):
            if tag == 'O':
                # 随机选择 'O' 数据替换模板中的'O'标签
                sample = random.choice(pick_from.get(template[idx]))
                fragments['texts'].append(sample)
                fragments['tags'].extend(['O'] * len(sample))
            else:
                # 'BI' 标签数据保持不动
                fragments['texts'].append(template[idx])
                b_tag = 'B-{}'.format(tag)
                i_tag = 'I-{}'.format(tag)
                if b_tag not in labels:
                    labels.append(b_tag)
                if i_tag not in labels:
                    labels.append(i_tag)
                fragments['tags'].extend([b_tag] + [i_tag] * (len(template[idx]) - 1))
                # 数据增强，随机插入 0~2 个噪声
        for _ in range(*random.choices([0, 1, 2], weights=[0.4, 0.3, 0.3], k=1)):
            noise = random.choice(augment_from)
            at = random.randint(0, len(fragments['texts']))
            o_at = sum([len(fragments['texts'][i]) for i in range(at)])
            fragments['texts'].insert(at, noise)
            fragments['tags'].insert(o_at, 'O')
        # 文本之间拼接，标记用空格拼接
        ner_data.append((''.join(fragments['texts']), ' '.join(fragments['tags'])))
    # 乱序
    random.shuffle(ner_data)
    # 拆分训练集和测试集
    indices = int(len(ner_data) * rate)
    train_cls_data.extend(ner_data[:indices])
    test_cls_data.extend(ner_data[indices:])
    # 字典
    ner_vocab = []
    # 存储
    csv_header = ('text', 'label')
    with open(os.path.join(output_dir, 'train.csv'), 'w', newline='', encoding='utf-8') as train_file:
        writer = csv.writer(train_file)
        writer.writerow(csv_header)
        for row in train_cls_data:
            writer.writerow(row)
            # 句子再分字，建字表
            ner_vocab.extend(list(row[0]))
    with open(os.path.join(output_dir, 'test.csv'), 'w', newline='', encoding='utf-8') as test_file:
        writer = csv.writer(test_file)
        writer.writerow(csv_header)
        for row in test_cls_data:
            writer.writerow(row)
            # 句子再分字，建字表
            ner_vocab.extend(list(row[0]))
    with open(os.path.join(output_dir, 'label.txt'), 'w', newline='\n', encoding='utf-8') as label_file:
        for row in labels:
            label_file.write(row + '\n')
    with open(os.path.join(output_dir, 'vocab.txt'), 'w', newline='\n', encoding='utf-8') as vocab_file:
        for row in set(ner_vocab):
            vocab_file.write('{}\n'.format(row))
    print('train: {}\ntest: {}'.format(len(train_cls_data), len(test_cls_data)))


if __name__ == '__main__':
    # 加载实体数据
    ENTITIES_SEQ['entity#symptom'] = load_entities('../data/kg/dictionary/symptoms.txt')
    ENTITIES_SEQ['entity#disease'] = load_entities('../data/kg/dictionary/diseases.txt')

    render_cls_template(
        produce_cls_template(WORDS_SEQ),
        ENTITIES_SEQ,
        NOISES,
        '../data/cls'
    )
    render_ner_template(
        produce_ner_template(ENTITIES_SEQ, DIFFER_INDEX),
        WORDS_SEQ,
        NOISES,
        '../data/ner'
    )
