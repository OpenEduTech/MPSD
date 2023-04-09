package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.CompanyMapper;
import indi.zyang.neev.dao.DescriptionMapper;
import indi.zyang.neev.dao.IndustryMapper;
import indi.zyang.neev.dao.ProductMapper;
import indi.zyang.neev.entity.Company;
import indi.zyang.neev.entity.Description;
import indi.zyang.neev.entity.Industry;
import indi.zyang.neev.entity.Product;
import indi.zyang.neev.service.CompanyService;
import indi.zyang.neev.unit.Edge;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Node;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    DescriptionMapper descriptionMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    IndustryMapper industryMapper;

    @Override
    public Company findCompanyByComId(int comId) {
        return companyMapper.findCompanyByComId(comId);
    }

    @Override
    public Company findFullCompanyByComId(int comId) {
        Company company = companyMapper.findCompanyByComId(comId);
        Description description = descriptionMapper.findDescriptionByDesId(comId);
        List<Company> cptCompanyList = companyMapper.findCptCompanyByComId(comId);
        List<Product> productList = productMapper.findProductByComId(comId);
        List<Industry> industryList = industryMapper.findIndustryByComId(comId);
        company.setCompanyList(cptCompanyList);
        company.setDescription(description);
        company.setProductList(productList);
        company.setIndustryList(industryList);
        return company;
    }

    @Override
    public GraphData getEChartGraphData(Company company) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<Company> cptCompanyList = company.getCompanyList();
        List<Product> productList = company.getProductList();
        List<Industry> industryList = company.getIndustryList();

        nodes.add(Tool.buildNode(company.getStringComId(),company.getComName(),company.getCategory()));
        for(Company cptCompany : cptCompanyList){
            nodes.add(Tool.buildNode(cptCompany.getStringComId(),cptCompany.getComName(),cptCompany.getCategory()));
            edges.add(Tool.buildEdge(cptCompany.getStringComId(),company.getStringComId()));
        }
        for(Product product : productList){
            nodes.add(Tool.buildNode(product.getStringProId(),product.getProName(),product.getCategory()));
            edges.add(Tool.buildEdge(product.getStringProId(),company.getStringComId()));
        }
        for (Industry industry : industryList){
            nodes.add(Tool.buildNode(industry.getStringIndId(),industry.getIndName(),industry.getCategory()));
            edges.add(Tool.buildEdge(industry.getStringIndId(),company.getStringComId()));
        }
        GraphData data = new GraphData(nodes,edges);
        return data;
    }
}
