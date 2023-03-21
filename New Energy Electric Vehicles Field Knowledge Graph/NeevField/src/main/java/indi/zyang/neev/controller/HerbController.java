package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Herb;
import indi.zyang.neev.service.HerbService;
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
@RequestMapping("/herb")
public class HerbController {
    @Autowired
    private HerbService herbService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getHerbInfo(@PathVariable("id") int herbId){
        Herb herb = herbService.findFullHerbByHerbId(herbId);
        GraphData data = herbService.getEChartGraphData(herb);
        //减少传输的数据量
        herb.setPrescriptionInfoList(null);
        return Tool.formatData(herb, data, "herb");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> searchHerb(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<Herb> herbList = herbService.findHerbByHerbName(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(null, herbList, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, herbList.size());
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getHerbName(@PathVariable("id") int herbId){
        Herb herb = herbService.findHerbByHerbId(herbId);
        return Tool.formatObjName("草药", herb.getHerbName());
    }
}
