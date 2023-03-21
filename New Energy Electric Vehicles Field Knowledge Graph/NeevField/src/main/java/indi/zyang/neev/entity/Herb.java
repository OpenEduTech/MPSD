package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//草药类
@Alias("herb")
public class Herb {
    /**
     * ER简图
     * herb————(1:n)————herb_info
     *    |
     *    |
     * pi_h_r(n:m)
     *    |
     *    |
     * prescription_info
     */

    //草药唯一标识符
    private int herbId;
    //草药名称
    private String herbName;
    //一个草药可能对应多个草药信息
    private List<HerbInfo> herbInfoList;

    private List<PrescriptionInfo> prescriptionInfoList;

    public Herb() {
    }

    public Herb(String herbName) {
        this.herbName = herbName;
    }

    public Herb(int herbId, String herbName) {
        this.herbId = herbId;
        this.herbName = herbName;
    }

    public int getHerbId() {
        return herbId;
    }

    public void setHerbId(int herbId) {
        this.herbId = herbId;
    }

    public String getHerbName() {
        return herbName;
    }

    public void setHerbName(String herbName) {
        this.herbName = herbName;
    }

    public List<HerbInfo> getHerbInfoList() {
        return herbInfoList;
    }

    public void setHerbInfoList(List<HerbInfo> herbInfoList) {
        this.herbInfoList = herbInfoList;
    }

    public String getStringHerbId(){
        return "h" + this.herbId;
    }

    public List<PrescriptionInfo> getPrescriptionInfoList() {
        return prescriptionInfoList;
    }

    public void setPrescriptionInfoList(List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionInfoList = prescriptionInfoList;
    }

    public int getCategory(){
        return 1;
    }

    @Override
    public String toString() {
        return "Herb{" +
                "herbId=" + herbId +
                ", herbName='" + herbName + '\'' +
                ", herbInfoList=" + herbInfoList +
                '}';
    }
}
