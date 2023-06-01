新能源汽车领域知识图谱Demo 0.10.1-SNAPSHOT

## 版本信息

| **信息** | **描述**                                             |
| -------- | ---------------------------------------------------- |
| 发布时间 | 2023-05-30                                           |
| 版本号   | V0.10.1                                              |
| 提交号   | [`7a8c77a`](7a8c77a64190f46bbc0805dc27eea5ab08e23de) |

## About this version

不稳定版本，重构IndustryDO结构，ind-ind关系只构成总分关系，移除产业链关系。产业链上下游关系以成员变量String形式存储至DO中。

## New

1. 各Service下getEChartGraphData(Do entity)方法新增无参重载方法，Deprecated当前方法。Controller进入不再指定传参对应实体，而是直接访问全部Industry DO
2. getEChartGraphData处理私有变量indLevel来划分各产业关系
3. gexf不再展示关系，只作为进入图谱的入口展示实体

## Fixed issues

1. 功能改造不涉及Fix

## Notes

1. 单元测试运行生成gexf格式化文件强制要求三元组形式
1. gexf格式文件不再强制三元组形式，关系将不再展示

