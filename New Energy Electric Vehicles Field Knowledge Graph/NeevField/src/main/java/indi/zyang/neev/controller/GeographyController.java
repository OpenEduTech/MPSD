package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Geography;
import indi.zyang.neev.service.GeographyService;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("geography")
public class GeographyController {
    @Autowired
    private GeographyService geographyService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getBookInfo(@PathVariable("id") int geographyId){

        return null;
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> searchGeography(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<Geography> geographyList = geographyService.findGeographyByGeographyName(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, geographyList, null);
            return Tool.formatSearchReturn(searchResult, geographyList.size());
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getGeographyName(@PathVariable("id") int geographyId){
        Geography geography = geographyService.findGeographyByGeographyId(geographyId);
        return Tool.formatObjName("地理位置", geography.getGeographyFull());
    }
}
