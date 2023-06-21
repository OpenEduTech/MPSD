package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.poem.mapper.AuthorsInfoMapper;
import com.example.poem.mapper.SongciMapper;
import com.example.poem.pojo.AuthorsInfo;
import com.example.poem.pojo.Songci;
import com.example.poem.service.AuthorsInfoService;
import com.example.poem.service.SongciService;
import org.springframework.stereotype.Service;

@Service
public class SongciServiceImpl extends ServiceImpl<SongciMapper, Songci> implements SongciService {
}
