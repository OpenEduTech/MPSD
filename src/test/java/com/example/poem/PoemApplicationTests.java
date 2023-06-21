package com.example.poem;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.poem.mapper.TangshiMapper;
import com.example.poem.pojo.Tangshi;
import com.example.poem.utils.Base64Util;
import com.example.poem.utils.WordCloudUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PoemApplication.class)
class PoemApplicationTests {

    @Autowired
    TangshiMapper tangshiMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void generateWordCloud() throws IOException {
        WordCloudUtil.generate(null,null);
        System.out.println("WordCloudUtil generate over!");
    }

    @Test
    public void generateAuthorWordCloud() throws IOException {
        LambdaQueryWrapper<Tangshi> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Tangshi::getAuthor,"子兰");
        List<Tangshi> tangshiList = tangshiMapper.selectList(queryWrapper);
        System.out.println(tangshiList);
        List<String> contents = new ArrayList<>();
        for (Tangshi tangshi : tangshiList) {
            contents.add(tangshi.getContent());
        }
        WordCloudUtil.generate("子兰",contents);
        System.out.println("generateAuthorWordCloud generate over!");
    }

    @Test
    public void picBase64() throws IOException {
        Base64Util.ImageToBase64("wordCloud-songci.png");
        System.out.println("generateAuthorWordCloud generate over!");
    }


}
