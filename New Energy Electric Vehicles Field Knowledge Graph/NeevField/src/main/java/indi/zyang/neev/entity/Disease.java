package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//疾病类
@Alias("disease")
public class Disease {
    /**
     * ER简图
     * disease————hi_d_r(n:m)————herb_info
     *    |
     *    |
     * pi_d_r(n:m)
     *    |
     *    |
     * prescription_info
     */

    //疾病唯一标识符
    private int diseaseId;
    //疾病名称
    private String diseaseName;
    //能够治疗该disease的herbInfo列表
    private List<HerbInfo> herbInfoList;
    //能够治疗该disease的prescriptionInfo列表
    private List<PrescriptionInfo> prescriptionInfoList;

    public Disease() {
    }

    public Disease(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Disease(int diseaseId, String diseaseName) {
        this.diseaseId = diseaseId;
        this.diseaseName = diseaseName;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public List<HerbInfo> getHerbInfoList() {
        return herbInfoList;
    }

    public void setHerbInfoList(List<HerbInfo> herbInfoList) {
        this.herbInfoList = herbInfoList;
    }

    public List<PrescriptionInfo> getPrescriptionInfoList() {
        return prescriptionInfoList;
    }

    public void setPrescriptionInfoList(List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionInfoList = prescriptionInfoList;
    }

    public String getStringDiseaseId(){
        return "d" + this.diseaseId;
    }

    public int getCategory(){
        return 2;
    }

    @Override
    public String toString() {
        return "Disease{" +
                "diseaseId=" + diseaseId +
                ", diseaseName='" + diseaseName + '\'' +
                '}';
    }
}
