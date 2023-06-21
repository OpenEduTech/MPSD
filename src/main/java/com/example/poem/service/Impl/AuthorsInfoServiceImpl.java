package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.poem.mapper.AuthorsInfoMapper;
import com.example.poem.pojo.AuthorsInfo;
import com.example.poem.service.AuthorsInfoService;
import org.springframework.stereotype.Service;

@Service
public class AuthorsInfoServiceImpl extends ServiceImpl<AuthorsInfoMapper, AuthorsInfo> implements AuthorsInfoService {
}
