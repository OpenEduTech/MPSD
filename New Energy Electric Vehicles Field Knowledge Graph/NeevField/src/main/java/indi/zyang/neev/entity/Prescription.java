package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//药剂类
@Alias("prescription")
public class Prescription {
    /**
     * ER简图
     * prescription————(1:n)————prescription_info
     */
    //药剂唯一标识符
    private int prescriptionId;
    //药剂名称
    private String prescriptionName;
    //药剂详细信息列表
    private List<PrescriptionInfo> prescriptionInfoList;

    public Prescription() {
    }

    public Prescription(String prescriptionName) {
        this.prescriptionName = prescriptionName;
    }

    public Prescription(int prescriptionId, String prescriptionName) {
        this.prescriptionId = prescriptionId;
        this.prescriptionName = prescriptionName;
    }

    public Prescription(int prescriptionId, String prescriptionName, List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionId = prescriptionId;
        this.prescriptionName = prescriptionName;
        this.prescriptionInfoList = prescriptionInfoList;
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

    public List<PrescriptionInfo> getPrescriptionInfoList() {
        return prescriptionInfoList;
    }

    public void setPrescriptionInfoList(List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionInfoList = prescriptionInfoList;
    }

    public String getStringPrescriptionId(){
        return "p" + this.prescriptionId;
    }

    public int getCategory(){
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Prescription p = (Prescription) obj;
        return p.getPrescriptionId() == this.prescriptionId;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", prescriptionName='" + prescriptionName + '\'' +
                ", prescriptionInfoList=" + prescriptionInfoList +
                '}';
    }
}
