package indi.zyang.neev.service.impl;

/**
 * 没有对象存储服务器 暂时不实现
 */

//import indi.zyang.neev.dao.CorrectionMapper;
//import indi.zyang.neev.entity.Correction;
//import indi.zyang.neev.service.CorrectionService;
//import indi.zyang.neev.service.cos.CosService;
//import indi.zyang.neev.unit.ImageInfo;
//import indi.zyang.neev.unit.Tool;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//@Service
//public class CorrectionServiceImpl implements CorrectionService {
//    @Autowired
//    CorrectionMapper correctionMapper;
//
//    @Autowired
//    CosService cosService;
//    private static final String COVER_PATH = "tcm/%d.jpg";
//    private static final Pattern COVER_PATTERN = Pattern.compile("/(tcm/.*jpg)");
//
//    @Override
//    public boolean insertCorrection(Correction correction) {
//        int count = correctionMapper.insertCorrection(correction);
//        return count == 1;
//    }
//
//    @Override
//    public ImageInfo uploadImage(List<MultipartFile> files) {
//        List<Map<String, String>> data = new ArrayList<>();
//        for (MultipartFile file : files) {
//            String url = cosService.add(file, COVER_PATH);
//            data.add(Tool.formatImageInfo(url, "图片", url));
//        }
//        ImageInfo imageInfo = new ImageInfo();
//        imageInfo.setErrno(0);
//        imageInfo.setData(data);
//        return imageInfo;
//    }
//}
