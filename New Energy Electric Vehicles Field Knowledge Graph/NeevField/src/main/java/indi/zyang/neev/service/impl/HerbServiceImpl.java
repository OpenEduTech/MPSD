package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.*;
import indi.zyang.neev.entity.*;
import indi.zyang.neev.service.HerbService;
import indi.zyang.neev.unit.Edge;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Node;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HerbServiceImpl implements HerbService {
    @Autowired
    HerbMapper herbMapper;
    @Autowired
    HerbInfoMapper herbInfoMapper;
    @Autowired
    PrescriptionInfoMapper prescriptionInfoMapper;
    @Autowired
    GeographyMapper geographyMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    DiseaseMapper diseaseMapper;

    @Override
    public Herb findHerbByHerbId(int herbId) {
        return herbMapper.findHerbByHerbId(herbId);
    }

    @Override
    public Herb findFullHerbByHerbId(int herbId) {
        Herb herb = herbMapper.findHerbByHerbId(herbId);
        List<HerbInfo> herbInfoList = herbInfoMapper.findHerbInfoByHerbName(herb.getHerbName());
        herb.setHerbInfoList(herbInfoList);
        List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByHerbId(herbId);
        herb.setPrescriptionInfoList(prescriptionInfoList);
        return herb;
    }

    @Override
    public List<Herb> findHerbByPrescriptionInfoId(int prescriptionInfoId) {
        return herbMapper.findHerbByPrescriptionInfoId(prescriptionInfoId);
    }

    @Override
    public List<Herb> findHerbByHerbName(String herbName) {
        return herbMapper.findHerbByHerbName(herbName);
    }

    @Override
    public boolean insertHerb(Herb herb) {
        int count = herbMapper.insertHerb(herb);
        return count == 1;
    }

    @Override
    public boolean updateHerb(Herb herb) {
        int count = herbMapper.updateHerb(herb);
        return count == 1;
    }

    @Override
    public boolean deleteHerbByHerbId(int herbId) {
        int count = herbMapper.deleteHerbByHerbId(herbId);
        return count == 1;
    }

    @Override
    public GraphData getEChartGraphData(Herb herb) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        List<PrescriptionInfo> prescriptionInfoList = herb.getPrescriptionInfoList();
        List<String> prescriptionIdStringList = new ArrayList<>();
        List<String> geographyIdStringList = new ArrayList<>();
        List<String> bookIdStringList = new ArrayList<>();
        List<String> diseaseIdStringList = new ArrayList<>();

        nodes.add(Tool.buildNode(herb.getStringHerbId(), herb.getHerbName(), herb.getCategory()));
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            if (!prescriptionIdStringList.contains(prescriptionInfo.getStringPrescriptionId())) {
                nodes.add(Tool.buildNode(prescriptionInfo.getStringPrescriptionId(), prescriptionInfo.getPrescriptionName(), prescriptionInfo.getCategory()));
                prescriptionIdStringList.add(prescriptionInfo.getStringPrescriptionId());
                edges.add(Tool.buildEdge(herb.getStringHerbId(), prescriptionInfo.getStringPrescriptionId()));
            }
        }

        List<HerbInfo> herbInfoList = herb.getHerbInfoList();
        for (HerbInfo herbInfo : herbInfoList){
            addHerbInfoNodeAndEdge(herbInfo, herb, nodes, edges, geographyIdStringList, bookIdStringList, diseaseIdStringList);
        }

        GraphData data = new GraphData(nodes, edges);
        return data;
    }

    private void addHerbInfoNodeAndEdge(HerbInfo herbInfo, Herb herb, List<Node> nodes, List<Edge> edges, List<String> geographyIdStringList, List<String> bookIdStringList, List<String> diseaseIdStringList){
        List<Geography> geographyList = geographyMapper.findGeographyByHerbInfoId(herbInfo.getHerbInfoId());
        List<Book> bookList = bookMapper.findBookByHerbInfoId(herbInfo.getHerbInfoId());
        List<Disease> diseaseList = diseaseMapper.findDiseaseByHerbInfoId(herbInfo.getHerbInfoId());
        String hid = "";
        if (herbInfo.getHerbName().equals(herb.getHerbName())){
            hid = herb.getStringHerbId();
        }else {
            hid = herbInfo.getStringHerbInfoId();
            nodes.add(Tool.buildNode(herbInfo.getStringHerbInfoId(), herbInfo.getHerbName(), herbInfo.getCategory()));
            edges.add(Tool.buildEdge(herb.getStringHerbId(), herbInfo.getStringHerbInfoId()));
        }

        for (Geography geography : geographyList){
            if (!geographyIdStringList.contains(geography.getStringGeographyId())){
                nodes.add(Tool.buildNode(geography.getStringGeographyId(), geography.getGeographySimply(), geography.getCategory()));
                geographyIdStringList.add(geography.getStringGeographyId());
            }
            edges.add(Tool.buildEdge(hid, geography.getStringGeographyId()));
        }
        for (Book book : bookList){
            if (!bookIdStringList.contains(book.getStringBookId())){
                nodes.add(Tool.buildNode(book.getStringBookId(), book.getBookName(), book.getCategory()));
                bookIdStringList.add(book.getStringBookId());
            }
            edges.add(Tool.buildEdge(hid, book.getStringBookId()));
        }
        for (Disease disease : diseaseList){
            if (!diseaseIdStringList.contains(disease.getStringDiseaseId())){
                nodes.add(Tool.buildNode(disease.getStringDiseaseId(), disease.getDiseaseName(), disease.getCategory()));
                diseaseIdStringList.add(disease.getStringDiseaseId());
            }
            edges.add(Tool.buildEdge(hid, disease.getStringDiseaseId()));
        }
    }

    @Override
    public void writeHerbDataFile(String path) {
        try{
            String head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\" xmlns:viz=\"http://www.gexf.net/1.2draft/viz\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\">\n" +
                    "  <meta lastmodifieddate=\"2014-01-30\">\n" +
                    "    <creator>Gephi 0.8.1</creator>\n" +
                    "    <description></description>\n" +
                    "  </meta>\n" +
                    "  <graph defaultedgetype=\"undirected\" mode=\"static\">\n" +
                    "    <attributes class=\"node\" mode=\"static\">\n" +
                    "      <attribute id=\"modularity_class\" title=\"Modularity Class\" type=\"integer\"></attribute>\n" +
                    "    </attributes>";
            String foot = "  </graph>\n" +
                    "</gexf>";

            //File file = new File(new ClassPathResource("/static/file/geography.gexf").getFile().getAbsolutePath());
            File file = new File(path);
            //清空原文件内容
            FileWriter fw1 = new FileWriter(file);
            fw1.write("");
            fw1.flush();
            fw1.close();

            List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByPage(300, 20);
            List<Herb> herbList = new ArrayList<>();
            StringBuffer nodes = new StringBuffer();
            StringBuffer edges = new StringBuffer();
            Random valueRandom = new Random();
            Random sizeRandom = new Random();
            getNodeAndEdge(prescriptionInfoList, herbList, nodes, edges, -1, valueRandom, sizeRandom);
            //写文件数据
            FileWriter fw = new FileWriter(file, true);
            fw.append(head);
            fw.append("<nodes>");
            fw.append(nodes.toString());
            fw.append("</nodes><edges>");
            fw.append(edges.toString());
            fw.append("</edges>");
            fw.append(foot);
            fw.flush();
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void getNodeAndEdge(List<PrescriptionInfo> prescriptionInfoList, List<Herb> herbList, StringBuffer nodes, StringBuffer edges, int nodeId, Random valueRandom, Random sizeRandom){
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            List<Herb> herbs = herbMapper.findHerbByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId());
            for (int i = 0; i < herbs.size(); i++) {
                if (!whetherHave(herbList, herbs.get(i))){
                    int value = valueRandom.nextInt(8);
                    int size = sizeRandom.nextInt(20) + 10;
                    String node = "<node id=\""+ herbs.get(i).getHerbId() +"\" label=\""+ herbs.get(i).getHerbName() +"\">\n" +
                            "        <attvalues>\n" +
                            "          <attvalue for=\"modularity_class\" value=\""+value+"\"></attvalue>\n" +
                            "        </attvalues>\n" +
                            "        <viz:size value=\""+size+"\"></viz:size>\n" +
                            "      </node>";
                    nodes.append(node);
                    herbList.add(herbs.get(i));
                }
            }
            for (int j = 0; j < herbs.size()-1; j++) {
                String edge = "<edge source=\""+ herbs.get(j).getHerbId() +"\" target=\""+ herbs.get(j+1).getHerbId() +"\">\n" +
                        "        <attvalues></attvalues>\n" +
                        "      </edge>";
                edges.append(edge);
            }
        }
    }

    private boolean whetherHave(List<Herb> herbList, Herb herb){
        for (Herb h : herbList){
            if (h.getHerbId() == herb.getHerbId()){
                return true;
            }
        }
        return false;
    }
}
