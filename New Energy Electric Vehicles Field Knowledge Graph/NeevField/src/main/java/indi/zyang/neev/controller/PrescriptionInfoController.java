package indi.zyang.neev.controller;

import indi.zyang.neev.entity.PrescriptionInfo;
import indi.zyang.neev.service.PrescriptionInfoService;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/prescriptionInfo")
public class PrescriptionInfoController {
    @Autowired
    PrescriptionInfoService prescriptionInfoService;

    @RequestMapping("/{id}")
    public String getPrescriptionInfo(@PathVariable("id") int prescriptionInfoId){
        PrescriptionInfo prescriptionInfo = prescriptionInfoService.findPrescriptionInfoByPrescriptionInfoId(prescriptionInfoId);
        return "redirect:/prescription/"+prescriptionInfo.getPrescriptionId();
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getPrescriptionInfoName(@PathVariable("id") int prescriptionInfoId){
        PrescriptionInfo prescriptionInfo = prescriptionInfoService.findPrescriptionInfoByPrescriptionInfoId(prescriptionInfoId);
        return Tool.formatObjName("药剂", prescriptionInfo.getPrescriptionName());
    }
}
