package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

@Alias("industry")
public class Industry {
    /**
     * ER简图
     *                         Industry(n:m)
     *                              |
     *                              |
     *                         ind_ind_r(n:m)
     *                              |
     *                              |
     *                          Industry————ind_com_r(n:m)————company
     *                              |
     *                              |
     *                         des_ind_r(n:m)
     *                              |
     *                              |
     *                          description_mark
     */

    private int indId;

    private String indName;

    //产业链层级
    private int indLevel;

    private int levelKey;

    private int upLevelKey;

    //产业市值(单位/亿)
    private int marketValue;

    //产业内公司列表
    private List<Company> companyList;

    //该产业的描述信息
    private Description description;

    //该产业的上游产业列表
    private List<Industry> upIndustryList = new ArrayList<>();


    public int getIndLevel() {
        return indLevel;
    }

    public void setIndLevel(int indLevel) {
        this.indLevel = indLevel;
    }

    public int getLevelKey() {
        return levelKey;
    }

    public void setLevelKey(int levelKey) {
        this.levelKey = levelKey;
    }

    public int getUpLevelKey() {
        return upLevelKey;
    }

    public void setUpLevelKey(int upLevelKey) {
        this.upLevelKey = upLevelKey;
    }

    public int getIndId() {
        return indId;
    }

    public void setIndId(int indId) {
        this.indId = indId;
    }

    public String getIndName() {
        return indName;
    }

    public void setIndName(String indName) {
        this.indName = indName;
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

    public List<Industry> getUpIndustryList() {
        return upIndustryList;
    }

    public void setUpIndustryList(List<Industry> upIndustryList) {
        this.upIndustryList = upIndustryList;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }

    public int getCategory() {return 1;}

    public String getStringIndId() {return "ind"+this.indId;}
}
