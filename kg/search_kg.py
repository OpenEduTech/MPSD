#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: search_kg.py
@date: 2023/6/20 21:21
@desc: 
"""
from py2neo import Graph, Node

# neo4j
db_handle = Graph('http://localhost:7474', auth=('neo4j', '12345678'), name='neo4j')

cypher_map = {
    '症状问介绍': "MATCH (m:Symptom) where m.name = '{}' return m.name, m.summary",
    '症状问预防': "MATCH (m:Symptom) where m.name = '{}' return m.name, m.precaution",
    '症状问疾病': "MATCH (m:Symptom)-[r:ASSOCIATE_DISEASE]->(n:Disease) where m.name = '{}' return m.name, r.name, n.name",
    '症状问检查': "MATCH (m:Symptom)-[r:DO_INSPECTION]->(n:Inspection) where m.name = '{}' return m.name, r.name, n.name",
    '症状问宜吃': "MATCH (m:Symptom)-[r:EAT_GOOD]->(n:Food) where m.name = '{}' return m.name, r.name, n.name",
    '症状问忌吃': "MATCH (m:Symptom)-[r:EAT_BAD]->(n:Food) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问介绍': "MATCH (m:Disease) where m.name = '{}' return m.name, m.summary",
    '疾病问医保': "MATCH (m:Disease) where m.name = '{}' return m.name, m.is_insured",
    '疾病问患病': "MATCH (m:Disease) where m.name = '{}' return m.name, m.prevalence",
    '疾病问人群': "MATCH (m:Disease) where m.name = '{}' return m.name, m.population",
    '疾病问传染': "MATCH (m:Disease) where m.name = '{}' return m.name, m.infection",
    '疾病问办法': "MATCH (m:Disease) where m.name = '{}' return m.name, m.cure_mode",
    '疾病问周期': "MATCH (m:Disease) where m.name = '{}' return m.name, m.cure_cycle",
    '疾病问治愈': "MATCH (m:Disease) where m.name = '{}' return m.name, m.cure_probability",
    '疾病问花费': "MATCH (m:Disease) where m.name = '{}' return m.name, m.cure_cost",
    '疾病问提示': "MATCH (m:Disease) where m.name = '{}' return m.name, m.kind_tips",
    '疾病问并发': "MATCH (m:Disease)-[r:HAS_COMPLICATION]->(n:Complication) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问挂号': "MATCH (m:Disease)-[r:ATTEND_DEPARTMENT]->(n:Department) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问药物': "MATCH (m:Disease)-[r:TREAT_DRUG]->(n:Drug) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问宜吃': "MATCH (m:Disease)-[r:EAT_GOOD]->(n:Food) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问忌吃': "MATCH (m:Disease)-[r:EAT_BAD]->(n:Food) where m.name = '{}' return m.name, r.name, n.name",
    '疾病问食谱': "MATCH (m:Disease)-[r:RECOMMEND_RECIPE]->(n:Recipe) where m.name = '{}' return m.name, r.name, n.name",
}

answer_map = {
    '症状问介绍': "以下是{m#name}的概要信息：{m#summary}",
    '症状问预防': "{m#name}的预防措施包括：{m#precaution}",
    '症状问疾病': "{m#name}可能患上的疾病有：{n#name}",
    '症状问检查': "{m#name}建议进行以下医疗检查：{n#name}",
    '症状问宜吃': "{m#name}宜吃的食物有：{n#name}",
    '症状问忌吃': "{m#name}忌吃的食物有：{n#name}",
    '疾病问介绍': "以下是{m#name}的概要信息：{m#summary}",
    '疾病问医保': "{m#name}是否纳入医保范围：{m#is_insured}",
    '疾病问患病': "{m#name}的患病概率：{m#prevalence}",
    '疾病问人群': "{m#name}的易感人群：{m#population}",
    '疾病问传染': "{m#name}的传染方式：{m#infection}",
    '疾病问办法': "{m#name}可以尝试如下治疗：{m#cure_mode}",
    '疾病问周期': "{m#name}治疗可能持续的周期为：{m#cure_cycle}",
    '疾病问治愈': "{m#name}治愈的概率为（仅供参考）：{m#cure_probability}",
    '疾病问花费': "{m#name}的花销：{m#cure_cost}",
    '疾病问提示': "关于{m#name}有如下的温馨提示：{m#kind_tips}",
    '疾病问并发': "{m#name}可能出现的并发症有：{n#name}",
    '疾病问挂号': "{m#name}可前往{n#name}进行挂号和治疗",
    '疾病问药物': "{m#name}通常的使用的药品包括：{n#name}",
    '疾病问宜吃': "{m#name}宜吃的食物有：{n#name}",
    '疾病问忌吃': "{m#name}忌吃的食物有：{n#name}",
    '疾病问食谱': "{m#name}推荐食谱有：{n#name}",
}

have_to_join = [
    '症状问疾病', '症状问检查', '症状问宜吃', '症状问忌吃', '疾病问并发', '疾病问挂号', '疾病问药物', '疾病问宜吃', '疾病问忌吃', '疾病问食谱'
]


def transform_sql(intents, entities):
    cls_sqls = {}
    for intent in intents:
        sql_template = cypher_map.get(intent)
        sqls = [sql_template.format(entity) for entity in entities]
        cls_sqls[intent] = sqls
    return cls_sqls


def transform_answer(intent, resources):
    answers = []
    answer_template = answer_map.get(intent)
    for res in resources:
        line_res = {key: '、'.join(value) for key, value in res.items()}
        answers.append(answer_template.format_map(line_res))
    return '\n'.join(answers)


def output_result(cls_sqls):
    output_answers = []
    for cls, sqls in cls_sqls.items():
        resources = []
        for sql in sqls:
            res = db_handle.run(sql).data()
            print(sql)
            print(res)
            answer = {}
            for line in res:
                for lk, lv in line.items():
                    # 点号替换
                    answer.setdefault(lk.replace('.', '#'), set()).add(lv)
            resources.append(answer)
        output_answers.append(transform_answer(cls, resources))
    return '\n'.join(output_answers)


if __name__ == '__main__':
    ars = output_result(transform_sql(['症状问检查', '症状问预防'], ['休克', '伏脉']))
    print(ars)
