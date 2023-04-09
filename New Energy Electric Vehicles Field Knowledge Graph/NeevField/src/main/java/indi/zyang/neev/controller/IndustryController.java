package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Industry;
import indi.zyang.neev.service.IndustryService;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("industry")
public class IndustryController {
    @Autowired
    IndustryService industryService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getIndustryInfo(@PathVariable("id") int indId){
        Industry industry = industryService.findFullIndustryByIndId(indId);
        GraphData data = industryService.getEChartGraphData(industry);
        return Tool.formatData(industry,data,"industry");
    }
}
