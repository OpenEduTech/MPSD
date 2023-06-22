#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: apis.py
@date: 2023/6/21 0:27
@desc: 
"""
import json

import jieba
import numpy as np
import torch
import ahocorasick
from flask import Flask, render_template, request

from kg.search_kg import output_result, transform_sql
from nlp.cls.dataset import ProcessUtil as CLSProcessUtil
from nlp.cls.model import TextCNN
from nlp.cls.operation import predict as cls_predict
from nlp.ner.dataset import ProcessUtil as NERProcessUtil
from nlp.ner.model import BiLSTM_CRF
from nlp.ner.operation import predict as ner_predict

# jieba 分词预加载
jieba.initialize()


def load_json(filepath):
    with open(filepath, encoding='utf-8') as jf:
        return json.load(jf)


def load_txt(filepath):
    with open(filepath, encoding='utf-8') as tf:
        return [line.strip() for line in tf]


class AhocorasickNer:
    def __init__(self, user_dict_path, feature):
        self.user_dict_path = user_dict_path
        self.feature = feature
        self.actree = ahocorasick.Automaton()
        self._add_keywords()

    def _add_keywords(self):
        words = []
        with open(self.user_dict_path, "r", encoding="utf-8") as file:
            words = [line.strip() for line in file]
        # 根据模式串长度排序
        patterns = sorted(words, key=len, reverse=True)
        for idx, ptn in enumerate(patterns):
            self.actree.add_word(ptn, (idx, ptn))
        self.actree.make_automaton()

    def get_ner_results(self, sentence):
        ner_results = []
        # i的形式为(index1,(index2,word))
        # index1: 提取后的结果在sentence中的末尾索引
        # index2: 提取后的结果在self.actree中的索引
        last_index = 0
        for end_index, (p_id, p_string) in self.actree.iter(sentence):
            if end_index - last_index > 0:
                ner_results.append({
                    'start': end_index + 1 - len(p_string),
                    'end': end_index + 1,
                    'entity': self.feature
                })
                last_index = end_index
        return ner_results


app = Flask(__name__, static_folder='static/image', template_folder='static/html')

device = torch.device('cuda')

cls_config = load_json('./outputs/cls_model/202306211751/config.json')
cls_model = TextCNN(**cls_config)
cls_model.load_state_dict(torch.load('./outputs/cls_model/202306211751/acc_0.9776380153738644.pt'))
cls_process_util = CLSProcessUtil('./data/cls/vocab.txt', './data/cls/label.txt')
cls_model = cls_model.to(device)

ner_config = load_json('./outputs/ner_model/202306212348/config.json')
ner_model = BiLSTM_CRF(**ner_config, device=device)
ner_model.load_state_dict(torch.load('./outputs/ner_model/202306212348/acc_0.9432563631592757.pt'))
ner_process_util = NERProcessUtil('./data/ner/vocab.txt', './data/ner/label.txt')
ner_model = ner_model.to(device)


kg_dictionary = {
    'SYMPTOM': load_txt('./data/kg/dictionary/symptoms.txt'),
    'DISEASE': load_txt('./data/kg/dictionary/diseases.txt')
}

SYMPTOM_AC = AhocorasickNer('./data/kg/dictionary/symptoms.txt', 'SYMPTOM')
DISEASE_AC = AhocorasickNer('./data/kg/dictionary/diseases.txt', 'DISEASE')


intent_aggregation = {
    '症状问办法': (['症状问介绍', '症状问疾病', '症状问检查', '症状问宜吃', '症状问忌吃'], 'SYMPTOM'),
    '症状问疾病': (['症状问疾病', '症状问检查'], 'SYMPTOM'),
    '症状问预防': (['症状问预防'], 'SYMPTOM'),
    '症状问检查': (['症状问检查'], 'SYMPTOM'),
    '症状问饮食': (['症状问宜吃', '症状问忌吃'], 'SYMPTOM'),
    # '疾病问介绍': (['疾病问介绍', '疾病问办法', '疾病问治愈', '疾病问提示'], 'DISEASE'),
    '疾病问办法': (['疾病问办法', '疾病问周期', '疾病问治愈', '疾病问挂号', '疾病问药物'], 'DISEASE'),
    '疾病问详情': ([
        '疾病问介绍', '疾病问医保', '疾病问患病', '疾病问人群', '疾病问传染', '疾病问办法', '疾病问周期', '疾病问治愈',
        '疾病问花费', '疾病问并发', '疾病问挂号'
    ], 'DISEASE'),
    '疾病问药食': (['疾病问药物', '疾病问宜吃', '疾病问忌吃', '疾病问食谱'], 'DISEASE'),
    '疾病问建议': (['疾病问提示', '疾病问患病', '疾病问人群', '疾病问传染'], 'DISEASE')
}


def preprocess_str(s):
    """将字符串预处理，将每个字符拆成两部分：字符本身和其位置"""
    preprocessed_s = [c+str(i*2+1) if c == s[i-1][0] else c+str(i*2+1)
                      for i, c in enumerate(s)]
    return preprocessed_s


def jaccard_similarity(s1, s2):
    """基于Jaccard相似度计算两个字符串之间的相似度"""
    # 将字符拆分成字符和位置，方便后续进行位置权重计算
    s1 = preprocess_str(s1)
    s2 = preprocess_str(s2)
    # 计算字符集合、交集、并集
    set_s1 = set(s1)
    set_s2 = set(s2)
    intersection = len(set_s1.intersection(set_s2))
    union = len(set_s1.union(set_s2))
    # 计算Jaccard相似度
    jaccard_score = intersection / union if union else 0
    return jaccard_score


@app.get('/')
def index():
    return render_template('index.html')


@app.post("/api/chatbot")
def chatbot():
    data = request.get_json()
    message = data['message']
    # 语句预处理
    punctuation = "·；’、，。！？,!?"     # 定义处理字符
    translator = str.maketrans("", "", punctuation)     # 创建一个空的字符串映射表
    message = message.translate(translator)     # 删除标点符号
    print(message)  # 输出处理后的信息
    # 首先用AC自动机进行命名实体识别
    entities = SYMPTOM_AC.get_ner_results(message) + DISEASE_AC.get_ner_results(message)
    print("AC自动机预测结果：{}".format(entities))
    if len(entities) == 0:
        # AC自动机识别失败时用模型进行预测
        entities = ner_predict(list(message), ner_model, device, ner_process_util)
        print("NER模型预测结果：{}".format(entities))
    rf_message = []
    # 嵌入人工特征，提高分类准确率
    last_cut_idx = 0
    for ett in entities:
        rf_message.append(message[last_cut_idx:ett['start']])
        rf_message.append(message[ett['start']:ett['end']])
        rf_message.append(ett['entity'].lower())
        last_cut_idx = ett['end']
    rf_message.append(message[min(len(message), last_cut_idx):])
    rf_message = ''.join(rf_message)
    print('嵌入ner特征的句子：{}'.format(rf_message))
    if len(entities) <= 0:
        return '您的咨询中未包含任何症状或疾病的描述，能请您提供更准确的描述吗？'
    score, intent = cls_predict(list(jieba.cut(rf_message)), cls_model, device, cls_process_util)
    print('{}-{}'.format(score, intent))
    if score < 0.3:
        return '我猜您想的是通过{}, 但我太明白您的意图, 能请您提供更准确的描述吗？'.format(intent)
    # 意图聚合查询
    effective_intents = intent_aggregation[intent]
    # 实际有效的实体
    effective_entities = []
    # 实际无效的实体
    ineffective_entities = []
    for ett in entities:
        print(message[ett['start']:ett['end']])
        if ett['entity'] == effective_intents[1]:
            sim_prob = [
                jaccard_similarity(real_ett, message[ett['start']:ett['end']])
                for real_ett in kg_dictionary[effective_intents[1]]
            ]
            max_idx = np.argmax(np.array(sim_prob))
            if sim_prob[max_idx] > 0.34:
                effective_entities.append(kg_dictionary[effective_intents[1]][max_idx])
            else:
                ineffective_entities.append(message[ett['start']:ett['end']])
    if len(effective_entities) <= 0:
        return '我猜您想问\'{}\'有关{}的相关信息，非常抱歉，未在知识图谱中找到您想要的答案，您可以尝试其他问法或提供更全面的描述。'.format(
            '、'.join(ineffective_entities), intent[3:])
    return output_result(transform_sql(effective_intents[0], effective_entities))


if __name__ == '__main__':
    app.run(host='0.0.0.0')