package com.example.poem.service;

import com.example.poem.Dto.PoemDto;
import com.example.poem.Dto.StarDto;
import com.example.poem.Dto.WordCloudDto;
import com.example.poem.Vo.StarVo;
import com.example.poem.commonresponse.CommonResponse;
import com.example.poem.enums.PoemEnum;
import com.example.poem.pojo.AuthorsInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FindService {


    CommonResponse getPoems(PoemDto poemDto);

     AuthorsInfo getAuthorInfo(String name);

     StarVo getStar(StarDto starDto);

    Map<Object, Long> getRank();

    Map<Object, Long> getQuantity(PoemEnum poemEnum);

    String generateAuthorWordCloud(WordCloudDto dto) throws IOException;
}
