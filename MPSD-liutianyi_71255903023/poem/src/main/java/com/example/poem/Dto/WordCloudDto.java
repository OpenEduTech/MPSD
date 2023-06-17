package com.example.poem.Dto;

import com.example.poem.enums.PoemEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordCloudDto {

    @ApiModelProperty(value = "查询类型", required = true, dataType = "PoemEnum")
    private PoemEnum poemEnum;

    @ApiModelProperty(value = "作者名字", required = true, dataType = "AuthorName")
    private String name;
}
