package com.example.poem.controller;


import com.example.poem.Dto.PoemDto;
import com.example.poem.Dto.StarDto;
import com.example.poem.Dto.WordCloudDto;
import com.example.poem.Vo.StarVo;
import com.example.poem.Vo.WordCloudVo;
import com.example.poem.commonresponse.CommonResponse;
import com.example.poem.enums.PoemEnum;
import com.example.poem.pojo.AuthorsInfo;
import com.example.poem.service.FindService;
import com.example.poem.utils.Base64Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "V1.0 Api")
@RequestMapping("/poem")
public class poemController {


    @Autowired
    FindService findService;

    @GetMapping("/query/poemList")
    @ApiOperation("查询唐诗，宋曲，宋词，元曲")
    public CommonResponse getPoems(PoemDto poemDto){
        return findService.getPoems(poemDto);
    };


    @GetMapping("/query/authorInfo")
    @ApiOperation("查询作者名")
    public CommonResponse<AuthorsInfo> getAuthorInfo(String name){
        AuthorsInfo authorInfo = findService.getAuthorInfo(name);
        return CommonResponse.success(authorInfo);
    };


    @GetMapping("/favour")
    @ApiOperation("点赞功能")
    public CommonResponse<StarVo> getStar(StarDto starDto){
        StarVo star = findService.getStar(starDto);
        return CommonResponse.success(star,"点赞成功");
    }

    @GetMapping("/query/hotTitle")
    @ApiOperation("热门词排名(前五)")
    public CommonResponse<Map<Object, Long>> getRank(){
        Map<Object, Long> rank = findService.getRank();
        return CommonResponse.success(rank,"查询成功");
    }


    @GetMapping("/query/quantity")
    @ApiOperation("作品数量排行(前五)")
    public CommonResponse<Map<Object, Long>> getQuantity(PoemEnum poemEnum){
        Map<Object, Long> quantity = findService.getQuantity(poemEnum);
        return CommonResponse.success(quantity,"查询成功");
    }

    @GetMapping("/query/wordCloud")
    @ApiOperation("查询作者词云")
    public CommonResponse<WordCloudVo> getWordCloud(WordCloudDto dto){
        String wordCloud = null;
        try {
            wordCloud = findService.generateAuthorWordCloud(dto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String code = Base64Util.ImageToBase64(wordCloud);
        WordCloudVo wordCloudVo = new WordCloudVo();
        wordCloudVo.setName(dto.getName());
        wordCloudVo.setType(dto.getPoemEnum().getType());
        wordCloudVo.setImage(code);
        return CommonResponse.success(wordCloudVo);
    }
}
