package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.CompanyMapper;
import indi.zyang.neev.dao.DescriptionMapper;
import indi.zyang.neev.dao.ProductMapper;
import indi.zyang.neev.entity.Company;
import indi.zyang.neev.entity.Description;
import indi.zyang.neev.entity.Product;
import indi.zyang.neev.service.ProductService;
import indi.zyang.neev.unit.Edge;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Node;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    DescriptionMapper descriptionMapper;

    @Override
    public Product findProductByProId(int proId) {
        return productMapper.findProductByProId(proId);
    }

    @Override
    public Product findFullProductByProId(int proId) {
        Product product = productMapper.findProductByProId(proId);
        List<Company> companyList = companyMapper.findCompanyByProId(proId);
        List<Product> cptProductList = productMapper.findCptProductByProId(proId);
        Description description = descriptionMapper.findDescriptionByDesId(proId);
        product.setCompanyList(companyList);
        product.setProductList(cptProductList);
        product.setDescription(description);
        return product;
    }

    @Override
    public GraphData getEChartGraphData(Product product) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<Product> cptProList = product.getProductList();
        List<Company> companyList = product.getCompanyList();

        nodes.add(Tool.buildNode(product.getStringProId(),product.getProName(),product.getCategory()));
        for(Product cptPro : cptProList){
            nodes.add(Tool.buildNode(cptPro.getStringProId(),cptPro.getProName(),cptPro.getCategory()));
            edges.add(Tool.buildEdge(cptPro.getStringProId(),product.getStringProId()));
        }
        for(Company com : companyList){
            nodes.add(Tool.buildNode(com.getStringComId(),com.getComName(), com.getCategory()));
            edges.add(Tool.buildEdge(com.getStringComId(),product.getStringProId()));
        }
        GraphData data = new GraphData(nodes,edges);
        return data;
    }
}
