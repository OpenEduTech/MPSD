package indi.zyang.neev.controller;

import indi.zyang.neev.entity.HerbInfo;
import indi.zyang.neev.service.HerbInfoService;
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
@RequestMapping("/herbInfo")
public class HerbInfoController {
    @Autowired
    private HerbInfoService herbInfoService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getHerbInfoInfo(@PathVariable("id") int herbInfoId){
        HerbInfo herbInfo = herbInfoService.findHerbInfoByHerbInfoId(herbInfoId);
        GraphData data = herbInfoService.getEChartGraphData(herbInfo);
        return Tool.formatData(herbInfo, data, "herbInfo");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> searchHerbInfo(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<HerbInfo> herbInfoList = herbInfoService.findHerbInfoByHerbName(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, herbInfoList);
            return Tool.formatSearchReturn(searchResult, herbInfoList.size());
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getHerbInfoName(@PathVariable("id") int herbInfoId){
        HerbInfo herbInfo = herbInfoService.findHerbInfoByHerbInfoId(herbInfoId);
        return Tool.formatObjName("草药", herbInfo.getHerbName());
    }
}
