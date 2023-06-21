package com.example.poem.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.poem.enums.CommonEnum;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 枚举工具类
 */
public class EnumUtil {

    /**
     * 返回指定编码的'枚举'
     *
     * @param code
     * @return SharedObjTypeEnum
     * @throws
     */
    public static <T extends CommonEnum> T getEnumBycode(Class<T> clazz, int code) {
        for (T _enum : clazz.getEnumConstants())
            if (code == _enum.getCode())
                return _enum;
        return null;
    }

    /**
     * 返回指定名称的'枚举'
     * @param name
     * @return SharedObjTypeEnum
     * @throws
     */
/*
    public static <T extends CommonEnum> T getEnumByName(Class<T> clazz, String name) {
        for(T _enum : clazz.getEnumConstants())
            if(_enum.getCode().equals(name))
                return _enum;
        return null;
    }
*/

    /**
     * 返回指定描述的'枚举'
     *
     * @param type
     * @return SharedObjTypeEnum
     * @throws
     */
    public static <T extends CommonEnum> T getEnumByDesc(Class<T> clazz, String type) {
        for (T _enum : clazz.getEnumConstants()) {
            if (Objects.equals(_enum.getType(), type))
                return (T)_enum;
        }
        return null;
    }


    /**
     * 根据type组成的list列表解析为Enum列表
     *
     * @param clazz
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends CommonEnum> List<T> getEnumListByStr(Class<T> clazz, String type) {
        if (StringUtils.isBlank(type)) {
            return Collections.emptyList();
        }
        if (Objects.equals(type.charAt(0), '[') && Objects.equals(type.charAt(type.length() - 1), ']')) {
            type = type.substring(1, type.length() - 1);
            if(StringUtils.isBlank(type)){
                return Collections.emptyList();
            }

            String[] arr = type.replaceAll("\"","").split(",");
            List<T> res = new ArrayList<>();
            for (String a : arr) {
                T r = getEnumByDesc(clazz, a);
                if (Objects.nonNull(r)) {
                    res.add(r);
                }
            }

            return res;
        }


        return null;
    }
}
