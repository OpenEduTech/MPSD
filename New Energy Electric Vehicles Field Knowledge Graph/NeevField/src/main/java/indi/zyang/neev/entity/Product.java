package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("product")
public class Product {
    /**
     * ER简图
     *                          product
     *                              |
     *                              |
     *                         pro_pro_r(n:m)
     *                              |
     *                              |
     *                          product————pro_com_r(n:1)————company
     *                              |
     *                              |
     *                         des_pro_r(n:m)
     *                              |
     *                              |
     *                          description
     */

    private int proId;

    private String proName;

    //该产品的竞品产品
    private List<Product> productList;

    //该产品所属公司
    private List<Company> companyList;

    //该产品描述信息
    private Description description;

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public int getCategory() {return 2;}

    public String getStringProId() {return "pro"+this.proId;}
}
