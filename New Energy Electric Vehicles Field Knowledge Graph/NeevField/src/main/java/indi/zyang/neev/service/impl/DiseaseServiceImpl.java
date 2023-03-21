package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.DiseaseMapper;
import indi.zyang.neev.dao.HerbInfoMapper;
import indi.zyang.neev.dao.PrescriptionInfoMapper;
import indi.zyang.neev.entity.Disease;
import indi.zyang.neev.entity.HerbInfo;
import indi.zyang.neev.entity.PrescriptionInfo;
import indi.zyang.neev.service.DiseaseService;
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
public class DiseaseServiceImpl implements DiseaseService {
    @Autowired
    DiseaseMapper diseaseMapper;
    @Autowired
    PrescriptionInfoMapper prescriptionInfoMapper;
    @Autowired
    HerbInfoMapper herbInfoMapper;

    @Override
    public Disease findDiseaseByDiseaseId(int diseaseId) {
        return diseaseMapper.findDiseaseByDiseaseId(diseaseId);
    }

    @Override
    public Disease findFullDiseaseByDiseaseId(int diseaseId) {
        Disease disease = diseaseMapper.findDiseaseByDiseaseId(diseaseId);
        List<HerbInfo> herbInfoList = herbInfoMapper.findHerbInfoByDiseaseId(diseaseId);
        List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByDiseaseId(diseaseId);
        disease.setHerbInfoList(herbInfoList);
        disease.setPrescriptionInfoList(prescriptionInfoList);
        return disease;
    }

    @Override
    public List<Disease> findDiseaseByPage(int page, int size) {
        int start = (page - 1) * size;
        return diseaseMapper.findDiseaseByPage(start, size);
    }

    @Override
    public List<Disease> findDiseaseByDiseaseName(String diseaseName) {
        return diseaseMapper.findDiseaseByDiseaseName(diseaseName);
    }

    @Override
    public List<Disease> findDiseaseByHerbInfoId(int herbInfoId) {
        return diseaseMapper.findDiseaseByHerbInfoId(herbInfoId);
    }

    @Override
    public List<Disease> findDiseaseByPrescriptionInfoId(int prescriptionInfoId) {
        return diseaseMapper.findDiseaseByPrescriptionInfoId(prescriptionInfoId);
    }

    @Override
    public boolean insertDisease(Disease disease) {
        int count = diseaseMapper.insertDisease(disease);
        return count == 1;
    }

    @Override
    public boolean updateDisease(Disease disease) {
        int count = diseaseMapper.updateDisease(disease);
        return count == 1;
    }

    @Override
    public boolean deleteDiseaseByDiseaseId(int diseaseId) {
        int count = diseaseMapper.deleteDiseaseByDiseaseId(diseaseId);
        return count == 1;
    }

    @Override
    public GraphData getEChartGraphData(Disease disease) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<HerbInfo> herbInfoList = disease.getHerbInfoList();
        List<PrescriptionInfo> prescriptionInfoList = disease.getPrescriptionInfoList();
        List<String> prescriptionIdStringList = new ArrayList<>();

        nodes.add(Tool.buildNode(disease.getStringDiseaseId(), disease.getDiseaseName(), disease.getCategory()));
        for (HerbInfo herbInfo : herbInfoList){
            nodes.add(Tool.buildNode(herbInfo.getStringHerbInfoId(), herbInfo.getHerbName(), herbInfo.getCategory()));
            edges.add(Tool.buildEdge(disease.getStringDiseaseId(), herbInfo.getStringHerbInfoId()));
        }
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            if (!prescriptionIdStringList.contains(prescriptionInfo.getStringPrescriptionId())){
                nodes.add(Tool.buildNode(prescriptionInfo.getStringPrescriptionId(), prescriptionInfo.getPrescriptionName(), prescriptionInfo.getCategory()));
                prescriptionIdStringList.add(prescriptionInfo.getStringPrescriptionId());
                edges.add(Tool.buildEdge(disease.getStringDiseaseId(), prescriptionInfo.getStringPrescriptionId()));
            }
        }
        GraphData data = new GraphData(nodes, edges);
        return data;
    }

    @Override
    public void writeDiseaseDataFile(String path) {
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

            List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByPage(100, 50);
            List<Disease> diseaseList = new ArrayList<>();
            StringBuffer nodes = new StringBuffer();
            StringBuffer edges = new StringBuffer();
            Random valueRandom = new Random();
            Random sizeRandom = new Random();
            getNodeAndEdge(prescriptionInfoList, diseaseList, nodes, edges, -1, valueRandom, sizeRandom);
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

    private void getNodeAndEdge(List<PrescriptionInfo> prescriptionInfoList, List<Disease> diseaseList, StringBuffer nodes, StringBuffer edges, int nodeId, Random valueRandom, Random sizeRandom){
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            List<Disease> diseases = diseaseMapper.findDiseaseByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId());
            for (int i = 0; i < diseases.size(); i++) {
                if (!whetherHave(diseaseList, diseases.get(i))){
                    int value = valueRandom.nextInt(8);
                    int size = sizeRandom.nextInt(40);
                    String node = "<node id=\""+ diseases.get(i).getDiseaseId() +"\" label=\""+ diseases.get(i).getDiseaseName() +"\">\n" +
                            "        <attvalues>\n" +
                            "          <attvalue for=\"modularity_class\" value=\""+value+"\"></attvalue>\n" +
                            "        </attvalues>\n" +
                            "        <viz:size value=\""+size+"\"></viz:size>\n" +
                            "      </node>";
                    nodes.append(node);
                    diseaseList.add(diseases.get(i));
                }
            }
            for (int j = 0; j < diseases.size()-1; j++) {
                String edge = "<edge source=\""+ diseases.get(j).getDiseaseId() +"\" target=\""+ diseases.get(j+1).getDiseaseId() +"\">\n" +
                        "        <attvalues></attvalues>\n" +
                        "      </edge>";
                edges.append(edge);
            }
        }
    }

    private boolean whetherHave(List<Disease> diseaseList, Disease disease){
        for (Disease d : diseaseList){
            if (d.getDiseaseId() == disease.getDiseaseId()){
                return true;
            }
        }
        return false;
    }
}
