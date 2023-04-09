package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.CompanyMapper;
import indi.zyang.neev.dao.DescriptionMapper;
import indi.zyang.neev.dao.IndustryMapper;
import indi.zyang.neev.entity.Company;
import indi.zyang.neev.entity.Description;
import indi.zyang.neev.entity.Industry;
import indi.zyang.neev.service.IndustryService;
import indi.zyang.neev.unit.Edge;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Node;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Autowired
    IndustryMapper industryMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    DescriptionMapper descriptionMapper;

    @Override
    public Industry findIndustryByIndId(int indId) {
        return industryMapper.findIndustryByIndId(indId);
    }

    @Override
    public Industry findFullIndustryByIndId(int indId) {
        Industry industry = industryMapper.findIndustryByIndId(indId);
        Description description = descriptionMapper.findDescriptionByDesId(indId);
        List<Company> companyList = companyMapper.findCompanyByIndId(indId);
        List<Industry> upIndustryList = industryMapper.findIndustryByUpIndLevel(indId);
        List<Industry> downIndustryList = industryMapper.findIndustryByDownIndLevel(indId);
        List<Industry> industryList = industryMapper.findIndustryByIndLevel(indId);
        industry.setDescription(description);
        industry.setCompanyList(companyList);
        industry.setUpIndustryList(upIndustryList);
        industry.setDownIndustryList(downIndustryList);
        industry.setIndustryList(industryList);
        return industry;
    }

    @Override
    public GraphData getEChartGraphData(Industry industry) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<Company> companyList = industry.getCompanyList();
        List<Industry> upIndustryList = industry.getUpIndustryList();
        List<Industry> downIndustryList = industry.getDownIndustryList();
        List<Industry> industryList = industry.getIndustryList();

        nodes.add(Tool.buildNode(industry.getStringIndId(),industry.getIndName(),industry.getCategory()));
        for(Company company : companyList){
            nodes.add(Tool.buildNode(company.getStringComId(),company.getComName(),company.getCategory()));
            edges.add(Tool.buildEdge(company.getStringComId(),industry.getStringIndId()));
        }
        for(Industry upIndustry : upIndustryList){
            nodes.add(Tool.buildNode(upIndustry.getStringIndId(),upIndustry.getIndName(),4));
            edges.add(Tool.buildEdge(upIndustry.getStringIndId(),industry.getStringIndId()));
        }
        for(Industry downIndustry : downIndustryList){
            nodes.add(Tool.buildNode(downIndustry.getStringIndId(),downIndustry.getIndName(),3));
            edges.add(Tool.buildEdge(downIndustry.getStringIndId(),industry.getStringIndId()));
        }
        for(Industry midIndustry : industryList){
            nodes.add(Tool.buildNode(midIndustry.getStringIndId(),midIndustry.getIndName(),midIndustry.getCategory()));
            edges.add(Tool.buildEdge(midIndustry.getStringIndId(),industry.getStringIndId()));
        }
        GraphData data = new GraphData(nodes,edges);
        return data;
    }
}
