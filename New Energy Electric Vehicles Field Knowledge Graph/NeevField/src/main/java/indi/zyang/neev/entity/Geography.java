package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//地理位置类
@Alias("geography")
public class Geography {
    /**
     * ER简图
     * geography————hi_g_r(n:m)————herb_info
     */

    //地理位置唯一标识符
    private int geographyId;
    //大洲
    private String continent;
    //国家
    private String country;
    //省份
    private String province;
    //城市
    private String city;
    //上级地区
    private int superior;
    //标识符，0表示精确到大洲，1表示精确到国家，2表示精确到省份，3表示精确到城市
    private int sign;
    //该geography生长有哪些herbInfo
    private List<HerbInfo> herbInfoList;
    //地址简称
    private String geographySimply;
    //地址全称
    private String geographyFull;

    public Geography() {
        this.sign = -1;
    }

    public Geography(String continent, String country, String province, String city, int superior) {
        this.continent = continent;
        this.country = country;
        this.province = province;
        this.city = city;
        this.superior = superior;
        this.sign = computeSign();
    }

    public Geography(int geographyId, String continent, String country, String province, String city, int superior, int sign) {
        this.geographyId = geographyId;
        this.continent = continent;
        this.country = country;
        this.province = province;
        this.city = city;
        this.superior = superior;
        this.sign = sign;
    }

    public int getGeographyId() {
        return geographyId;
    }

    public void setGeographyId(int geographyId) {
        this.geographyId = geographyId;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSuperior() {
        return superior;
    }

    public void setSuperior(int superior) {
        this.superior = superior;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public List<HerbInfo> getHerbInfoList() {
        return herbInfoList;
    }

    public void setHerbInfoList(List<HerbInfo> herbInfoList) {
        this.herbInfoList = herbInfoList;
    }

    public String getStringGeographyId(){
        return "g" + this.geographyId;
    }

    public int getCategory(){
        return 4;
    }

    public void setGeographyFull(String geographyFull){
        this.geographyFull = geographyFull;
    }

    public String getGeographyFull(){
        String geo = "";
        switch (sign){
            case 0:
                geo = this.continent;
                break;
            case 1:
                geo = this.continent + this.country;
                break;
            case 2:
                geo = this.continent + this.country + this.province;
                break;
            case 3:
                geo = this.continent + this.country + this.province + this.city;
                break;
        }
        return geo;
    }

    public void setGeographySimply(String geographySimply){
        this.geographySimply = geographySimply;
    }

    public String getGeographySimply(){
        String geo = "";
        switch (sign){
            case 0:
                geo = this.continent;
                break;
            case 1:
                geo = this.country;
                break;
            case 2:
                geo = this.province;
                break;
            case 3:
                geo = this.city;
                break;
        }
        return geo;
    }

    private int computeSign(){
        int s = -1;
        if (this.city != null && !this.city.equals("")){
            s = 3;
        }else if(this.province != null && !this.province.equals("")){
            s = 2;
        }else if(this.country != null && !this.country.equals("")){
            s = 1;
        }else {
            s = 0;
        }
        return s;
    }

    @Override
    public String toString() {
        return "Geography{" +
                "geographyId=" + geographyId +
                ", continent='" + continent + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", superior=" + superior +
                ", sign=" + sign +
                '}';
    }
}
