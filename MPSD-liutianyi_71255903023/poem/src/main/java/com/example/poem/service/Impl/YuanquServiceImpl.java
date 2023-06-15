package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.poem.mapper.SongshiMapper;
import com.example.poem.mapper.YuanquMapper;
import com.example.poem.pojo.Songshi;
import com.example.poem.pojo.Yuanqu;
import com.example.poem.service.SongshiService;
import com.example.poem.service.YuanquService;
import org.springframework.stereotype.Service;

@Service
public class YuanquServiceImpl extends ServiceImpl<YuanquMapper, Yuanqu> implements YuanquService {
}
