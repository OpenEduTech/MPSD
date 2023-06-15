package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.poem.mapper.SongshiMapper;
import com.example.poem.mapper.TangshiMapper;
import com.example.poem.pojo.Songshi;
import com.example.poem.pojo.Tangshi;
import com.example.poem.service.SongshiService;
import com.example.poem.service.TangshiService;
import org.springframework.stereotype.Service;

@Service
public class TangshiServiceImpl extends ServiceImpl<TangshiMapper, Tangshi> implements TangshiService {
}
