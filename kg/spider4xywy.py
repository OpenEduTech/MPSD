#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
@author: feizzz
@contact: https://fei-zzz.github.io/
@file: spider4xywy.py
@date: 2023/5/8 上午9:28
@description: 
"""
import re
import random
import requests
import json
from urllib import parse
from queue import Queue, Empty
from bloom_filter import BloomFilter
from typing import Callable
from concurrent.futures import ThreadPoolExecutor
from bs4 import BeautifulSoup

# 定义请求头
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36 QIHU 360SE'
}

# 定义代理池
proxy_list = [
    '114.102.47.248:8089',
    '114.231.82.74:8089',
    '120.26.50.145:8989',
    '123.182.58.178:8089',
    '117.69.236.165:8089',
    '1.117.79.73:3128'
]

SPIDER_SYMPTOM_LINK = 'symptom_link'
SPIDER_SYMPTOM_ENTITY = 'symptom_entity'
SPIDER_DISEASE_ENTITY = 'disease_entity'

task_pool = Queue()
spider_manager = {}
bloom = BloomFilter(max_elements=100000, error_rate=0.01)


def task_enqueue(spider_name: str, url: str, *args, **kwargs):
    task = {
        'spider_name': spider_name,
        'url': url,
        'args': args,
        'kwargs': kwargs
    }
    if task['url'] in bloom:
        print('过滤 {}'.format(task['url']))
        return
    task_pool.put(task)


def task_dequeue(timeout: int):
    return task_pool.get(timeout=timeout)


def spider_register(spider_name: str, spider: Callable):
    spider_manager[spider_name] = spider


def handle_exception(worker):
    err = worker.exception()
    if err:
        print(err)


def start(max_workers: int = 8, timeout: int = 60):
    with ThreadPoolExecutor(max_workers) as executor:
        while True:
            try:
                task = task_dequeue(timeout)
            except Empty:
                break
            else:
                spider = spider_manager.get(task['spider_name'])
                fc = executor.submit(spider, task['url'], *task['args'], **task['kwargs'])
                fc.add_done_callback(handle_exception)
        executor.shutdown()


def batch_replace(obj: str, rule: list):
    result = obj
    for item in rule:
        result = result.replace(*item)
    return result


def bs_resource(url: str, encoding: str = 'gbk', features: str = 'templates.parser'):
    # 随机选择一个代理
    proxy = random.choice(proxy_list)
    response = requests.get(
        url,
        headers=headers,
        # proxies={
        #     'http': 'http://' + proxy,
        #     'https': 'https://' + proxy
        # }
    )
    return BeautifulSoup(response.content.decode(encoding, errors='ignore'), features)


# 症状链接爬虫
def spider_all_symptom_url(url):
    """
    查找所有症状的链接
    :return: list
    """
    # 按字母分类的索引
    page_index = [
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    ]
    # url渲染模板
    url_template = parse.urljoin(url, 'p/{}.templates')
    # 查找链接
    count = 0
    for index in page_index:
        index_url = url_template.format(index)
        bs_html = bs_resource(index_url)
        # 选择目标标签
        for link in bs_html.find_all('a', attrs={'href': re.compile(r'^/\d+_gaishu\.templates$')}):
            # 提取链接、名称，入队列
            task_enqueue(SPIDER_SYMPTOM_ENTITY, parse.urljoin(url, link.get('href')), link.get('title'))
            count += 1
    print('症状链接搜索完成，共 {} 个链接！'.format(count))


# 症状实体爬虫
def spider_symptom_entity(url, name):
    data = {'name': name}
    # 概述html资源
    gaishu_bs_html = bs_resource(url)
    # 相关疾病
    rlt_disease = []
    for link in gaishu_bs_html.select('li.loop-tag-name.mr20 a'):
        task_enqueue(SPIDER_DISEASE_ENTITY, link.get('href'), link.text.strip())
        rlt_disease.append(link.text)
    data['rlt_disease'] = rlt_disease

    # 介绍html资源
    jieshao_bs_html = bs_resource(url.replace('gaishu', 'jieshao'))
    # 基本介绍
    summary = []
    for txt in jieshao_bs_html.select('div.zz-articl.fr.f14 p'):
        summary.append(
            batch_replace(
                txt.text.strip(),   # 删除开头结尾空格
                [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]  # 批量替换
            )
        )
    data['summary'] = ''.join(summary)

    # 预防html资源
    yufang_bs_html = bs_resource(url.replace('gaishu', 'yufang'))
    # 预防措施
    precaution = []
    for txt in yufang_bs_html.select('div.zz-articl.fr.f14 p'):
        precaution.append(
            batch_replace(
                txt.text.strip(),
                [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]
            )
        )
    data['precaution'] = ''.join(precaution)

    # 检查html资源
    jiancha_bs_html = bs_resource(url.replace('gaishu', 'jiancha'))
    # 相关检查
    inspection = []
    for txt in jiancha_bs_html.select('div.zz-articl.fr.f14 p.f12.mt5 a'):
        inspection.append(
            batch_replace(
                txt.text.strip(),
                [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]
            )
        )
    data['inspection'] = inspection

    # 食物html资源
    food_bs_html = bs_resource(url.replace('gaishu', 'food'))
    # 食物疗养
    food = {
        'good': [],
        'bad': []
    }
    containers = food_bs_html.select('div.diet-item')
    # 抓取适宜忌讳食物
    if len(containers) == 2:
        for txt in containers[0].select('div.diet-imgbox.pr.bor.fl.mr10 p'):
            food['good'].append(
                batch_replace(
                    txt.text.strip(),
                    [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]
                )
            )
        for txt in containers[1].select('div.diet-imgbox.pr.bor.fl.mr10 p'):
            food['bad'].append(
                batch_replace(
                    txt.text.strip(),
                    [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]
                )
            )
    data['food'] = food

    with open('./symptoms/{}.json'.format(name.replace('/', '／').replace('\\', '＼')),
              'w', encoding='utf-8') as jsonfile:
        json.dump(data, jsonfile, ensure_ascii=False)

    print('{} 症状实体抓取完成！'.format(name))


# 疾病实体爬虫
def spider_disease_entity(url, name):
    label_map = {
        '医保疾病': ('is_insured', False),
        '患病比例': ('prevalence', False),
        '易感人群': ('population', False),
        '传染方式': ('infection', False),
        '并发症': ('complication', True),
        '就诊科室': ('department', True),
        '治疗方式': ('cure_mode', False),
        '治疗周期': ('cure_cycle', False),
        '治愈率': ('cure_probability', False),
        '常用药品': ('drug', True),
        '治疗费用': ('cure_cost', False)
    }
    data = {'name': name}

    # 概述html资源
    gaishu_bs_html = bs_resource(url.replace('il_sii_', 'il_sii/gaishu/'))
    # 基本描述
    summary = gaishu_bs_html.select_one('div.jib-articl-con.jib-lh-articl p')
    data['summary'] = batch_replace(
        summary.text.strip(),
        [('\t', ''), ('\r', ''), ('\n', ''), ('\xa0', ''), ('  ', '')]
    )
    # 其他信息
    others = gaishu_bs_html.select('div.mt20.articl-know')
    for obj in others:
        title = obj.select_one('strong').text.strip()
        if title in ['基本知识', '治疗常识']:
            for item in obj.select('p'):
                line_txt = batch_replace(
                    item.text.strip(),
                    [('\t', ' '), ('\r', ' '), ('\n', ' '), ('\xa0', ' '), ('  ', ' ')]
                ).split('：', 1)
                # 断言 根据冒号拆分的数据长度必定为2
                assert len(line_txt) == 2, '疾病抓取过程中，信息拆分出错\nurl: {}'.format(url)
                key_and_split = label_map.get(line_txt[0])
                if key_and_split is None:
                    continue
                if key_and_split[1]:
                    data[key_and_split[0]] = line_txt[1].strip().split()
                else:
                    data[key_and_split[0]] = line_txt[1].strip()
        elif title == '温馨提示':
            data['kind_tips'] = obj.select_one('p').text.strip()
        else:
            print('不关注的模块：{}'.format(title))
    # 食物疗养
    food_bs_html = bs_resource(url.replace('il_sii_', 'il_sii/food/'))
    containers = food_bs_html.select('div.diet-item.none')
    assert len(containers) == 3, '疾病抓取过程中，食物容器部分出错\nurl: {}'.format(url)
    food = {
        'good': [item.text for item in containers[0].find_all('p')],
        'bad': [item.text for item in containers[1].find_all('p')],
        'recipe': [item.text for item in containers[2].find_all('p')]
    }
    data['food'] = food

    with open('./diseases/{}.json'.format(name.replace('/', '／').replace('\\', '＼')),
              'w', encoding='utf-8') as jsonfile:
        json.dump(data, jsonfile, ensure_ascii=False)

    print('{} 疾病实体抓取完成！'.format(name))


# 爬虫注册
spider_register(SPIDER_SYMPTOM_LINK, spider_all_symptom_url)
spider_register(SPIDER_SYMPTOM_ENTITY, spider_symptom_entity)
spider_register(SPIDER_DISEASE_ENTITY, spider_disease_entity)


if __name__ == '__main__':
    # 设置抓取入口，从症状数据开始
    task_enqueue(SPIDER_SYMPTOM_LINK, 'http://zzk.xywy.com')
    # 注意！！！在未设置代理池的情况下，max_workers不要超过2,否则很容易封ip
    start(max_workers=2)
    # 抓取结束
    print('spider for xywy finished !')
