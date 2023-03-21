package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Prescription;
import indi.zyang.neev.service.PrescriptionInfoService;
import indi.zyang.neev.service.PrescriptionService;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController {
    @Autowired
    PrescriptionService prescriptionService;
    @Autowired
    PrescriptionInfoService prescriptionInfoService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getPrescriptionInfo(@PathVariable("id") int prescriptionId){
        //填充详细属性
        Prescription prescription = prescriptionService.findFullPrescriptionByPrescriptionId(prescriptionId);
        //为力引导布局准备数据
        GraphData data = prescriptionService.getEChartGraphData(prescription);
        return Tool.formatData(prescription, data, "prescription");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> prescriptionSearch(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<Prescription> prescriptionList = prescriptionService.searchPrescription(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(prescriptionList, null, null, null, null, null);
            int count = prescriptionService.searchPrescriptionCount(content);
            return Tool.formatSearchReturn(searchResult, count);
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getPrescription(@PathVariable("id") int prescriptionId){
        Prescription prescription = prescriptionService.findPrescriptionByPrescriptionId(prescriptionId);
        return Tool.formatObjName("药剂", prescription.getPrescriptionName());
    }
}
