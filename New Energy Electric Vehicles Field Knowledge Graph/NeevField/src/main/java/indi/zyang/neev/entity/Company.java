package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("company")
public class Company {
    /**
     * ER简图
     *                         pi_com_r(n:m)
     *                              |
     *                              |
     *                         pro_com_r(n:m)
     *                              |
     *                              |
     * ind————ind_com_r(n:m)————company————com_com_r(n:m)————company
     *                              |
     *                              |
     *                         des_com_r(n:m)
     *                              |
     *                              |
     *                          description
     */

    private int comId;

    private String comName;

    //该公司的产品列表
    private List<Product> productList;

    //该公司所属产业列表
    private List<Industry> industryList;

    //该公司的竞争公司列表
    private List<Company> companyList;

    //该公司的描述信息
    private Description description;

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Industry> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<Industry> industryList) {
        this.industryList = industryList;
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

    public int getCategory() {return 0;}

    public String getStringComId() {return "com"+this.comId;}
}
