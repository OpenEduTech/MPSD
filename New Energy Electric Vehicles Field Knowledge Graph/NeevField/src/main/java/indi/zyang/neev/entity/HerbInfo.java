package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//草药详细信息类
@Alias("herbInfo")
public class HerbInfo {
    /**
     * ER简图
     *                              book
     *                               |
     *                               |
     *                            hi_b_r(n:m)
     *                               |
     *                               |
     * disease————hi_d_r(n:m)————herb_info——————————————————(n:1)————herb
     *                               |                  |
     *                               |                  |
     *                            pi_hi_r(n:m)       hi_g_r(n:m)
     *                               |                  |
     *                               |                  |
     *                         prescription_info    geography
     */

    //草药信息唯一标识符
    private int herbInfoId;
    //草药名
    private String herbName;
    //草药名拼音
    private String herbSpellName;
    //草药别名
    private String herbAlias;
    //草药来源
    private String herbSource;
    //草药原形态
    private String herbOriginalForm;
    //草药生境分布
    private String herbGeography;
    //草药性味
    private String herbCharacterSmell;
    //草药功能主治
    private String herbFunction;
    //草药用法用量
    private String herbUsageDosage;
    //草药炮制方法
    private String herbMaking;
    //草药摘录
    private String herbExcerpt;
    //草药对应病症列表，一个草药可能治疗多种病症
    private List<Disease> diseaseList;
    //草药信息来自哪些书籍
    private List<Book> bookList;
    //草药生长在哪些地方
    private List<Geography> geographyList;
    //该草药是哪些中医药剂的成分
    private List<PrescriptionInfo> prescriptionInfoList;

    public HerbInfo() {
    }

    public HerbInfo(String herbName, String herbSpellName, String herbAlias, String herbSource, String herbOriginalForm, String herbGeography, String herbCharacterSmell, String herbFunction, String herbUsageDosage, String herbMaking, String herbExcerpt) {
        this.herbName = herbName;
        this.herbSpellName = herbSpellName;
        this.herbAlias = herbAlias;
        this.herbSource = herbSource;
        this.herbOriginalForm = herbOriginalForm;
        this.herbGeography = herbGeography;
        this.herbCharacterSmell = herbCharacterSmell;
        this.herbFunction = herbFunction;
        this.herbUsageDosage = herbUsageDosage;
        this.herbMaking = herbMaking;
        this.herbExcerpt = herbExcerpt;
    }

    public HerbInfo(int herbInfoId, String herbName, String herbSpellName, String herbAlias, String herbSource, String herbOriginalForm, String herbGeography, String herbCharacterSmell, String herbFunction, String herbUsageDosage, String herbMaking, String herbExcerpt) {
        this.herbInfoId = herbInfoId;
        this.herbName = herbName;
        this.herbSpellName = herbSpellName;
        this.herbAlias = herbAlias;
        this.herbSource = herbSource;
        this.herbOriginalForm = herbOriginalForm;
        this.herbGeography = herbGeography;
        this.herbCharacterSmell = herbCharacterSmell;
        this.herbFunction = herbFunction;
        this.herbUsageDosage = herbUsageDosage;
        this.herbMaking = herbMaking;
        this.herbExcerpt = herbExcerpt;
    }

    public int getHerbInfoId() {
        return herbInfoId;
    }

    public void setHerbInfoId(int herbInfoId) {
        this.herbInfoId = herbInfoId;
    }

    public String getHerbName() {
        return herbName;
    }

    public void setHerbName(String herbName) {
        this.herbName = herbName;
    }

    public String getHerbSpellName() {
        return herbSpellName;
    }

    public void setHerbSpellName(String herbSpellName) {
        this.herbSpellName = herbSpellName;
    }

    public String getHerbAlias() {
        return herbAlias;
    }

    public void setHerbAlias(String herbAlias) {
        this.herbAlias = herbAlias;
    }

    public String getHerbSource() {
        return herbSource;
    }

    public void setHerbSource(String herbSource) {
        this.herbSource = herbSource;
    }

    public String getHerbOriginalForm() {
        return herbOriginalForm;
    }

    public void setHerbOriginalForm(String herbOriginalForm) {
        this.herbOriginalForm = herbOriginalForm;
    }

    public String getHerbGeography() {
        return herbGeography;
    }

    public void setHerbGeography(String herbGeography) {
        this.herbGeography = herbGeography;
    }

    public String getHerbCharacterSmell() {
        return herbCharacterSmell;
    }

    public void setHerbCharacterSmell(String herbCharacterSmell) {
        this.herbCharacterSmell = herbCharacterSmell;
    }

    public String getHerbFunction() {
        return herbFunction;
    }

    public void setHerbFunction(String herbFunction) {
        this.herbFunction = herbFunction;
    }

    public String getHerbUsageDosage() {
        return herbUsageDosage;
    }

    public void setHerbUsageDosage(String herbUsageDosage) {
        this.herbUsageDosage = herbUsageDosage;
    }

    public String getHerbMaking() {
        return herbMaking;
    }

    public void setHerbMaking(String herbMaking) {
        this.herbMaking = herbMaking;
    }

    public String getHerbExcerpt() {
        return herbExcerpt;
    }

    public void setHerbExcerpt(String herbExcerpt) {
        this.herbExcerpt = herbExcerpt;
    }

    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Geography> getGeographyList() {
        return geographyList;
    }

    public void setGeographyList(List<Geography> geographyList) {
        this.geographyList = geographyList;
    }

    public List<PrescriptionInfo> getPrescriptionInfoList() {
        return prescriptionInfoList;
    }

    public void setPrescriptionInfoList(List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionInfoList = prescriptionInfoList;
    }

    public String getStringHerbInfoId(){
        return "hi" + this.herbInfoId;
    }

    public int getCategory(){
        return 1;
    }

    @Override
    public String toString() {
        return "HerbInfo{" +
                "herbInfoId=" + herbInfoId +
                ", herbName='" + herbName + '\'' +
                ", herbSpellName='" + herbSpellName + '\'' +
                ", herbAlias='" + herbAlias + '\'' +
                ", herbSource='" + herbSource + '\'' +
                ", herbOriginalForm='" + herbOriginalForm + '\'' +
                ", herbGeography='" + herbGeography + '\'' +
                ", herbCharacterSmell='" + herbCharacterSmell + '\'' +
                ", herbFunction='" + herbFunction + '\'' +
                ", herbUsageDosage='" + herbUsageDosage + '\'' +
                ", herbMaking='" + herbMaking + '\'' +
                ", herbExcerpt='" + herbExcerpt + '\'' +
                '}';
    }
}
