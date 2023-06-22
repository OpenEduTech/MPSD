package com.example.poem.utils;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Base64Util {
    public static String ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        // BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        String code ;
        code = "data:image/png;base64,"+ Base64.getEncoder().encodeToString(data);
        // code = "data:image/png;base64,"+ encoder.encode(Objects.requireNonNull(data));
        // System.out.println("词云图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
        return code;
    }

}
