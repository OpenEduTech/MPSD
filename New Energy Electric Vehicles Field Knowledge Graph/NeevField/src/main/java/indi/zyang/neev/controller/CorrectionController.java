//package indi.zyang.neev.controller;
//
//import indi.zyang.neev.entity.Correction;
//import indi.zyang.neev.service.CorrectionService;
//import indi.zyang.neev.unit.ImageInfo;
//import indi.zyang.neev.unit.Tool;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Controller
//@RequestMapping("/correction")
//public class CorrectionController {
//
//    @Autowired
//    CorrectionService correctionService;
//
//    @RequestMapping("/upload")
//    @ResponseBody
//    public ImageInfo uploadImage(List<MultipartFile> files){
//        return correctionService.uploadImage(files);
//    }
//
//    @RequestMapping("/insert")
//    @ResponseBody
//    public boolean insertCorrection(Correction correction, HttpServletRequest request){
//        String ip = Tool.getIpAddress(request);
//        correction.setIp(ip);
//        return correctionService.insertCorrection(correction);
//    }
//}
