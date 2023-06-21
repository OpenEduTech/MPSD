package com.example.poem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.poem.pojo.AuthorsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorsInfoMapper extends BaseMapper<AuthorsInfo> {
}
