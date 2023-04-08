package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

@Alias("description")
public class Description {
    private int desId;

    private String desName;

    private String desContent;

    private String desCategory;

    public String getDesCategory() {
        return desCategory;
    }

    public void setDesCategory(String desCategory) {
        this.desCategory = desCategory;
    }

    public int getDesId() {
        return desId;
    }

    public void setDesId(int desId) {
        this.desId = desId;
    }

    public String getDesName() {
        return desName;
    }

    public void setDesName(String desName) {
        this.desName = desName;
    }

    public String getDesContent() {
        return desContent;
    }

    public void setDesContent(String desContent) {
        this.desContent = desContent;
    }
}
