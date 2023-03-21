package indi.zyang.neev.unit;

import java.util.ArrayList;
import java.util.List;

public class GraphData {
    private List<Node> nodes;
    private List<Edge> links;
    private List<Category> categories = new ArrayList<>();

    public GraphData(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.links = edges;
        this.categories.add(new Category("药剂"));
        this.categories.add(new Category("草药"));
        this.categories.add(new Category("病症"));
        this.categories.add(new Category("典籍"));
        this.categories.add(new Category("地理位置"));
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Edge> getLinks() {
        return links;
    }

    public void setLinks(List<Edge> links) {
        this.links = links;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
