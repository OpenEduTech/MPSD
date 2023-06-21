package com.example.poem.Dto;

import com.example.poem.enums.PoemEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoemDto {

    @ApiModelProperty(value = "页数", required = true, dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(value = "大小", required = true, dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(value = "查询类型", required = true, dataType = "PoemEnum")
    private PoemEnum poemEnum;

    @ApiModelProperty(value = "关键词", required = false, dataType = "keyWord")
    private String keyWord;

}
