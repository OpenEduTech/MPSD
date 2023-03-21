package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//药剂详细信息类
@Alias("prescriptionInfo")
public class PrescriptionInfo {
    /**
     * ER简图
     *                              book
     *                               |
     *                               |
     *                            pi_b_r(n:m)
     *                               |
     *                               |
     * disease————pi_d_r(n:m)————prescription_info———pi_h_r(n:m)————herb
     *                               |
     *                               |
     *                             (n:1)
     *                               |
     *                               |
     *                         prescription
     */
    //药剂信息唯一标识符
    private int prescriptionInfoId;
    //药剂唯一标识符，一个药剂对应多个药剂信息
    private int prescriptionId;
    //药剂名称
    private String prescriptionName;
    //药剂组成
    private String prescriptionCompose;
    //药剂出处
    private String prescriptionProvenance;
    //药剂主治
    private String prescriptionAttend;
    //药剂用法用量
    private String prescriptionUsageDosage;
    //药剂制备方法
    private String prescriptionPreparationFunction;
    //组成中药
    private String compose;
    //草药成分列表，一个药剂由多个草药组成
    private List<Herb> herbList;
    //药剂对应病症列表，一个药剂可能治疗多种病症
    private List<Disease> diseaseList;
    //药剂信息来自哪些书籍
    private List<Book> bookList;
    //药剂对象
    private Prescription prescription;

    public PrescriptionInfo() {
    }

    public PrescriptionInfo(String prescriptionName, String prescriptionCompose, String prescriptionProvenance, String prescriptionAttend, String prescriptionUsageDosage, String prescriptionPreparationFunction, String compose) {
        this.prescriptionName = prescriptionName;
        this.prescriptionCompose = prescriptionCompose;
        this.prescriptionProvenance = prescriptionProvenance;
        this.prescriptionAttend = prescriptionAttend;
        this.prescriptionUsageDosage = prescriptionUsageDosage;
        this.prescriptionPreparationFunction = prescriptionPreparationFunction;
        this.compose = compose;
    }

    public PrescriptionInfo(int prescriptionId, String prescriptionName, String prescriptionCompose, String prescriptionProvenance, String prescriptionAttend, String prescriptionUsageDosage, String prescriptionPreparationFunction, String compose) {
        this.prescriptionId = prescriptionId;
        this.prescriptionCompose = prescriptionCompose;
        this.prescriptionProvenance = prescriptionProvenance;
        this.prescriptionAttend = prescriptionAttend;
        this.prescriptionUsageDosage = prescriptionUsageDosage;
        this.prescriptionPreparationFunction = prescriptionPreparationFunction;
        this.compose = compose;
    }

    public PrescriptionInfo(int prescriptionInfoId, int prescriptionId, String prescriptionName, String prescriptionCompose, String prescriptionProvenance, String prescriptionAttend, String prescriptionUsageDosage, String prescriptionPreparationFunction, String compose) {
        this.prescriptionInfoId = prescriptionInfoId;
        this.prescriptionName = prescriptionName;
        this.prescriptionId = prescriptionId;
        this.prescriptionCompose = prescriptionCompose;
        this.prescriptionProvenance = prescriptionProvenance;
        this.prescriptionAttend = prescriptionAttend;
        this.prescriptionUsageDosage = prescriptionUsageDosage;
        this.prescriptionPreparationFunction = prescriptionPreparationFunction;
        this.compose = compose;
    }

    public int getPrescriptionInfoId() {
        return prescriptionInfoId;
    }

    public void setPrescriptionInfoId(int prescriptionInfoId) {
        this.prescriptionInfoId = prescriptionInfoId;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionName() {
        return prescriptionName;
    }

    public void setPrescriptionName(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public String getPrescriptionCompose() {
        return prescriptionCompose;
    }

    public void setPrescriptionCompose(String prescriptionCompose) {
        this.prescriptionCompose = prescriptionCompose;
    }

    public String getPrescriptionProvenance() {
        return prescriptionProvenance;
    }

    public void setPrescriptionProvenance(String prescriptionProvenance) {
        this.prescriptionProvenance = prescriptionProvenance;
    }

    public String getPrescriptionAttend() {
        return prescriptionAttend;
    }

    public void setPrescriptionAttend(String prescriptionAttend) {
        this.prescriptionAttend = prescriptionAttend;
    }

    public String getPrescriptionUsageDosage() {
        return prescriptionUsageDosage;
    }

    public void setPrescriptionUsageDosage(String prescriptionUsageDosage) {
        this.prescriptionUsageDosage = prescriptionUsageDosage;
    }

    public String getPrescriptionPreparationFunction() {
        return prescriptionPreparationFunction;
    }

    public void setPrescriptionPreparationFunction(String prescriptionPreparationFunction) {
        this.prescriptionPreparationFunction = prescriptionPreparationFunction;
    }

    public String getCompose() {
        return compose;
    }

    public void setCompose(String compose) {
        this.compose = compose;
    }

    public List<Herb> getHerbList() {
        return herbList;
    }

    public void setHerbList(List<Herb> herbList) {
        this.herbList = herbList;
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

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public String getStringPrescriptionInfoId(){
        return "pi" + this.prescriptionInfoId;
    }

    public String getStringPrescriptionId(){
        return "p" + this.prescriptionId;
    }

    public int getCategory(){
        return 0;
    }

    @Override
    public String toString() {
        return "PrescriptionInfo{" +
                "prescriptionInfoId=" + prescriptionInfoId +
                ", prescriptionId=" + prescriptionId +
                ", prescriptionName='" + prescriptionName + '\'' +
                ", prescriptionCompose='" + prescriptionCompose + '\'' +
                ", prescriptionProvenance='" + prescriptionProvenance + '\'' +
                ", prescriptionAttend='" + prescriptionAttend + '\'' +
                ", prescriptionUsageDosage='" + prescriptionUsageDosage + '\'' +
                ", prescriptionPreparationFunction='" + prescriptionPreparationFunction + '\'' +
                ", compose='" + compose + '\'' +
                '}';
    }
}
