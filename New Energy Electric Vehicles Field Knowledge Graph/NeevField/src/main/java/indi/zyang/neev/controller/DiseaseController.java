package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Disease;
import indi.zyang.neev.service.DiseaseService;
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
@RequestMapping("disease")
public class DiseaseController {
    @Autowired
    DiseaseService diseaseService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getDiseaseInfo(@PathVariable("id") int diseaseId){
        Disease disease = diseaseService.findFullDiseaseByDiseaseId(diseaseId);
        GraphData data = diseaseService.getEChartGraphData(disease);
        return Tool.formatData(disease, data, "disease");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> searchDisease(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<Disease> diseaseList = diseaseService.findDiseaseByDiseaseName(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, diseaseList, null, null, null);
            return Tool.formatSearchReturn(searchResult, diseaseList.size());
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getDiseaseName(@PathVariable("id") int diseaseId){
        Disease disease = diseaseService.findDiseaseByDiseaseId(diseaseId);
        return Tool.formatObjName("病症", disease.getDiseaseName());
    }
}
