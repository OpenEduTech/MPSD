#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: build_kg.py
@date: 2023/5/9 下午3:10
@description: 
"""
import os
import glob
import json
from py2neo import Graph, Node

# neo4j
db_handle = Graph('http://localhost:7474', auth=('neo4j', '12345678'), name='neo4j')


def load_json(filepath):
    with open(filepath, encoding='utf-8') as f:
        return json.load(f)


def read_data_from(symptoms_dir, diseases_dir):
    """
    载入指定路径下所有json数据
    :param symptoms_dir: 症状目录
    :param diseases_dir: 疾病目录
    :return: 数据
    """
    # 症状数据
    symptom_information_list = []
    # 疾病数据
    disease_information_list = []

    # 实体
    symptom_list = []
    disease_list = []
    inspection_list = []
    complication_list = []
    department_list = []
    drug_list = []
    food_list = []
    recipe_list = []

    # 关系
    symptom2disease = []
    symptom2inspection = []
    symptom2good_food = []
    symptom2bad_food = []
    disease2complication = []
    disease2department = []
    disease2drug = []
    disease2good_food = []
    disease2bad_food = []
    disease2recipe = []

    def call4batch(targets: list, entities: list, launcher: str, relations: list):
        """
        批量处理
        :param targets: 待处理的实体列表
        :param entities: 归类的实体
        :param launcher: 关系出发点
        :param relations: 归类的关系
        :return: None
        """
        if targets is None:
            return
        entities.extend(targets)
        for obj in targets:
            relations.append((launcher, obj))

    # 处理症状json文件
    for symptom_file in glob.glob(os.path.join(symptoms_dir, '*.json')):
        # json对象
        symptom_obj = load_json(symptom_file)
        # 症状数据
        symptom_information_list.append({
            'name': symptom_obj['name'],  # 症状名称
            'summary': symptom_obj['summary'],  # 症状简介
            'precaution': symptom_obj['precaution']  # 预防措施
        })
        # 症状
        symptom_list.append(symptom_obj['name'])
        # 疾病
        call4batch(symptom_obj['rlt_disease'], disease_list, symptom_obj['name'], symptom2disease)
        # 检查项
        call4batch(symptom_obj['inspection'], inspection_list, symptom_obj['name'], symptom2inspection)
        # 食物
        call4batch(symptom_obj['food']['good'], food_list, symptom_obj['name'], symptom2good_food)
        call4batch(symptom_obj['food']['bad'], food_list, symptom_obj['name'], symptom2bad_food)

    # 处理疾病json文件
    for disease_file in glob.glob(os.path.join(diseases_dir, '*.json')):
        # json对象
        disease_obj = load_json(disease_file)
        # 疾病数据
        disease_information_list.append({
            'name': disease_obj['name'],
            'summary': disease_obj['summary'],
            'is_insured': disease_obj.get('is_insured'),
            'prevalence': disease_obj.get('prevalence'),
            'population': disease_obj.get('population'),
            'infection': disease_obj.get('infection'),
            'cure_mode': disease_obj.get('cure_mode'),
            'cure_cycle': disease_obj.get('cure_cycle'),
            'cure_probability': disease_obj.get('cure_probability'),
            'cure_cost': disease_obj.get('cure_cost'),
            'kind_tips': disease_obj.get('kind_tips'),
        })
        # 疾病
        disease_list.append(disease_obj['name'])
        # 并发症
        call4batch(disease_obj.get('complication'), complication_list, disease_obj['name'], disease2complication)
        # 科室
        call4batch(disease_obj.get('department'), department_list, disease_obj['name'], disease2department)
        # 药物
        call4batch(disease_obj.get('drug'), drug_list, disease_obj['name'], disease2drug)
        # 食物 实体
        call4batch(disease_obj['food']['good'], food_list, disease_obj['name'], disease2good_food)
        call4batch(disease_obj['food']['bad'], food_list, disease_obj['name'], disease2bad_food)
        # 食谱 实体
        call4batch(disease_obj['food']['recipe'], recipe_list, disease_obj['name'], disease2recipe)

    return {
        'information': [symptom_information_list, disease_information_list],
        'entities': [
            set(symptom_list), set(disease_list), set(inspection_list), set(complication_list),
            set(department_list), set(drug_list), set(food_list), set(recipe_list)
        ],
        'relations': [
            symptom2disease, symptom2inspection, symptom2good_food, symptom2bad_food,
            disease2complication, disease2department, disease2drug, disease2good_food, disease2bad_food, disease2recipe
        ]
    }


def create_node(label, **kwargs):
    db_handle.create(Node(label, **kwargs))


def create_rlt(edges, lunch_node, target_node, rlt_type, rlt_name):
    count = 0
    edges_set = set()
    for item in edges:
        edges_set.add('$$$'.join(item))
    for item in edges_set:
        p, q = item.split('$$$', 1)
        sql_str = "match(p:{}),(q:{}) where p.name='{}' and q.name='{}' create (p)-[rlt:{}{{name:'{}'}}]->(q)".format(
            lunch_node, target_node, p, q, rlt_type, rlt_name
        )
        try:
            db_handle.run(sql_str)
            count += 1
        except Exception as err:
            print(err)
    return count


def init_graphdb(symptoms_dir, diseases_dir):
    data_package = read_data_from(symptoms_dir=symptoms_dir, diseases_dir=diseases_dir)
    print('数据加载完成，开始创建知识图谱...')
    for symptom in data_package['information'][0]:
        create_node('Symptom', **symptom)
    print('Symptom 实体创建完成：{} '.format(len(data_package['information'][0])))
    for disease in data_package['information'][1]:
        create_node('Disease', **disease)
    print('Disease 实体创建完成：{} '.format(len(data_package['information'][1])))
    others_label = ['Inspection', 'Complication', 'Department', 'Drug', 'Food', 'Recipe']
    for index, entities in enumerate(data_package['entities'][2:]):
        for entity_name in entities:
            create_node(others_label[index], name=entity_name)
        print('{} 实体创建完成：{}'.format(others_label[index], len(entities)))
    relations_labels = [
        ('Symptom', 'Disease', 'ASSOCIATE_DISEASE', '关联疾病'),
        ('Symptom', 'Inspection', 'DO_INSPECTION', '需做检查'),
        ('Symptom', 'Food', 'EAT_GOOD', '宜吃食物'),
        ('Symptom', 'Food', 'EAT_BAD', '忌吃食物'),
        ('Disease', 'Complication', 'HAS_COMPLICATION', '并发症'),
        ('Disease', 'Department', 'ATTEND_DEPARTMENT', '挂号科室'),
        ('Disease', 'Drug', 'TREAT_DRUG', '治疗药物'),
        ('Disease', 'Food', 'EAT_GOOD', '宜吃食物'),
        ('Disease', 'Food', 'EAT_BAD', '忌吃食物'),
        ('Disease', 'Recipe', 'RECOMMEND_RECIPE', '推荐食谱'),
    ]
    for index, relations in enumerate(data_package['relations']):
        rlt_count = create_rlt(relations, *relations_labels[index])
        print('{}-{} ### {}/{} 关系创建完成：{}'.format(*relations_labels[index], rlt_count))


if __name__ == '__main__':
    init_graphdb('../data/kg/entity/symptoms', '../data/kg/entity/diseases')

