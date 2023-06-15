package com.example.poem.enums;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.example.poem.utils.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *  碳排参数展示列表
 */
public enum PoemEnum implements CommonEnum, IEnum<Integer> {
    SONGCI(1,"宋词"),
    SONGSHI(2,"宋诗"),
    TANGSHI(3,"唐诗"),
    YUANQU(4,"元曲");

    private PoemEnum(Integer code, String type){
        this.code = code;
        this.type = type;
    }

    @EnumValue
    public int code;

    @JsonValue
    public String type;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @JSONField
    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Integer getValue() {
        return this.getCode();
    }

    public static PoemEnum getByCode(Integer code) {

        return EnumUtil.getEnumBycode(PoemEnum.class, code);
    }

    @JsonCreator
    public static PoemEnum getByType(String type) {

        return EnumUtil.getEnumByDesc(PoemEnum.class, type);
    }
}
