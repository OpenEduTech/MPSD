package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Product;
import indi.zyang.neev.service.ProductService;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductService productService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getProductInfo(@PathVariable("id") int productId){
        Product product = productService.findFullProductByProId(productId);
        GraphData data = productService.getEChartGraphData(product);
        return Tool.formatData(product, data, "product");
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getProName(@PathVariable("id") int proId){
        Product product = productService.findProductByProId(proId);
        return Tool.formatObjName("产品",product.getProName());
    }
}
