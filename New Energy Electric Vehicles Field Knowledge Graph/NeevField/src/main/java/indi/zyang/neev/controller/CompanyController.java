package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Company;
import indi.zyang.neev.service.CompanyService;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getCompanyInfo(@PathVariable("id") int comId){
        Company company = companyService.findFullCompanyByComId(comId);
        GraphData data = companyService.getEChartGraphData(company);
        return Tool.formatData(company,data,"company");
    }


}
