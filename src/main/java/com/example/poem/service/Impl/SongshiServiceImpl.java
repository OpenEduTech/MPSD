package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.poem.mapper.SongshiMapper;
import com.example.poem.pojo.Songshi;
import com.example.poem.service.SongshiService;
import org.springframework.stereotype.Service;

@Service
public class SongshiServiceImpl extends ServiceImpl<SongshiMapper, Songshi> implements SongshiService {
}
