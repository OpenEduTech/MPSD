package indi.zyang.neev.unit;

public class Node {
    private String id;
    private String name;
    private double symbolSize;
    private double x;
    private double y;
    private double value;
    private int category;
    private int group;

    public Node() {
    }

    public Node(String id, String name, double symbolSize, double x, double y, double value, int category) {
        this.id = id;
        this.name = name;
        this.symbolSize = symbolSize;
        this.x = x;
        this.y = y;
        this.value = value;
        this.category = category;
    }

    public Node(String id, String name, int group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getSymbolSize() {
        return symbolSize;
    }
    public void setSymbolSize(double symbolSize) {
        this.symbolSize = symbolSize;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
