package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

//纠错信息类
@Alias("correction")
public class Correction {
    //自增主键
    private int correctionId;
    //对哪种数据进行纠错（book、disease、geography、herb、herbInfo、prescription、prescriptionInfo）
    private String errorType;
    //纠错对象的主键
    private int errorId;
    //纠错对象名称
    private String errorName;
    //纠错描述
    private String suggestion;
    //状态，0代表未处理，1代表已处理
    private int status = 0;
    //ip地址
    private String ip = "";
    //手机号
    private String phone = "";

    public Correction() {
    }

    public Correction(String errorType, int errorId, String errorName, String suggestion, int status) {
        this.errorType = errorType;
        this.errorId = errorId;
        this.errorName = errorName;
        this.suggestion = suggestion;
        this.status = status;
    }

    public Correction(int correctionId, String errorType, int errorId, String errorName, String suggestion, int status) {
        this.correctionId = correctionId;
        this.errorType = errorType;
        this.errorId = errorId;
        this.errorName = errorName;
        this.suggestion = suggestion;
        this.status = status;
    }

    public Correction(String errorType, int errorId, String errorName, String suggestion, int status, String ip, String phone) {
        this.errorType = errorType;
        this.errorId = errorId;
        this.errorName = errorName;
        this.suggestion = suggestion;
        this.status = status;
        this.ip = ip;
        this.phone = phone;
    }

    public int getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(int correctionId) {
        this.correctionId = correctionId;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Correction{" +
                "correctionId=" + correctionId +
                ", errorType='" + errorType + '\'' +
                ", errorId=" + errorId +
                ", errorName='" + errorName + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", status=" + status +
                ", ip='" + ip + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
