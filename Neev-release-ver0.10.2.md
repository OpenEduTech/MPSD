新能源汽车领域知识图谱Demo 0.10.2-SNAPSHOT

## 版本信息

| **信息** | **描述**                                               |
| -------- |------------------------------------------------------|
| 发布时间 | 2023-06-04                                           |
| 版本号   | V0.10.2                                              |
| 提交号   | [`fbe2ce7`](fbe2ce74fbc41a9e6010d67ee6076c6831b7a99b) |

## About this version

不稳定版本，更改Tool工具类，使节点具备自定义大小的能力

## New

1. Tool类下新增buildNodeWithFeature静态方法，制造节点且具有Node特性
2. 将市值大小按照中位medianValue成比例缩放，默认值为100亿，默认Node大小为18
3. info.js大小从后端传值中取值，default依旧取18

## Fixed issues

1. .gitignore忽视.idea和target目录

## Notes

1. 用户透明

