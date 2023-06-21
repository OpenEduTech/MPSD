package com.example.poem.Dto;

import com.example.poem.enums.PoemEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StarDto {

    @ApiModelProperty(value = "id", required = true, dataType = "Integer")
    private Integer id;


    @ApiModelProperty(value = "poemEnum", required = true, dataType = "PoemEnum")
    private PoemEnum poemEnum;
}
