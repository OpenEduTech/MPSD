package indi.zyang.neev.unit;

public class Edge {
    private String id;
    private String source;
    private String target;
    private int value;
    private String val;

    public Edge() {
    }

    public Edge(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public Edge(String id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Edge(String id, String source, String target, int value, String val) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.value = value;
        this.val = val;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
