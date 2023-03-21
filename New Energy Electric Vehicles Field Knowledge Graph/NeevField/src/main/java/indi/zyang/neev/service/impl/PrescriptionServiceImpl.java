package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.*;
import indi.zyang.neev.entity.*;
import indi.zyang.neev.service.PrescriptionService;
import indi.zyang.neev.unit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    PrescriptionMapper prescriptionMapper;
    @Autowired
    PrescriptionInfoMapper prescriptionInfoMapper;
    @Autowired
    DiseaseMapper diseaseMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    HerbMapper herbMapper;

    @Override
    public Prescription findPrescriptionByPrescriptionId(int prescriptionId) {
        Prescription prescription = prescriptionMapper.findPrescriptionByPrescriptionId(prescriptionId);
        return prescription;
    }

    @Override
    public Prescription findFullPrescriptionByPrescriptionId(int prescriptionId) {
        Prescription prescription = prescriptionMapper.findPrescriptionByPrescriptionId(prescriptionId);
        List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByPrescriptionId(prescriptionId);
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList) {
            prescriptionInfo.setBookList(bookMapper.findBookByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId()));
        }
        prescription.setPrescriptionInfoList(prescriptionInfoList);
        return prescription;
    }

    @Override
    public Prescription findPrescriptionByPrescriptionInfoId(int prescriptionInfoId) {
        return prescriptionMapper.findPrescriptionByPrescriptionInfoId(prescriptionInfoId);
    }

    @Override
    public List<Prescription> findPrescriptionByPrescriptionName(String prescriptionName) {
        return prescriptionMapper.findPrescriptionByPrescriptionName(prescriptionName);
    }

    @Override
    public List<Prescription> searchPrescription(String content) {
        List<Prescription> prescriptionList = prescriptionMapper.searchPrescription(content);
        return prescriptionList;
    }

    @Override
    public int searchPrescriptionCount(String content) {
        return prescriptionMapper.searchPrescriptionCount(content);
    }

    @Override
    public List<Prescription> findPrescriptionByPrescriptionNameFullPrescriptionInfo(String prescriptionName) {
        List<Prescription> prescriptionList = prescriptionMapper.findPrescriptionByPrescriptionName(prescriptionName);
        for (Prescription prescription : prescriptionList){
            prescription.setPrescriptionInfoList(prescriptionInfoMapper.findPrescriptionInfoByPrescriptionId(prescription.getPrescriptionId()));
        }
        return prescriptionList;
    }

    @Override
    public List<Prescription> findPrescriptionByPage(int page, int size) {
        int start = (page - 1) * size;
        return prescriptionMapper.findPrescriptionByPage(start, size);
    }

    @Override
    public boolean insertPrescription(Prescription prescription) {
        int count = prescriptionMapper.insertPrescription(prescription);
        return count == 1;
    }

    @Override
    public boolean updatePrescription(Prescription prescription) {
        int count = prescriptionMapper.updatePrescription(prescription);
        return count == 1;
    }

    @Transactional
    @Override
    public boolean deletePrescriptionByPrescriptionId(int prescriptionId) {
        int count = prescriptionMapper.deletePrescriptionByPrescriptionId(prescriptionId);
        prescriptionInfoMapper.deletePrescriptionInfoByPrescriptionId(prescriptionId);
        return count == 1;
    }

    @Override
    public GraphData getEChartGraphData(Prescription prescription) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<PrescriptionInfo> prescriptionInfoList = prescription.getPrescriptionInfoList();
        List<String> bookIdStringList = new ArrayList<>();
        List<String> herbIdStringList = new ArrayList<>();
        List<String> diseaseIdStringList = new ArrayList<>();
        //Node的id属性使用（字母+数字）的形式，数字保证了同类实体中无重复id，字母保证了不同类实体中无重复id
        nodes.add(Tool.buildNode(prescription.getStringPrescriptionId(), prescription.getPrescriptionName(), prescription.getCategory()));
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            List<Book> bookList = bookMapper.findBookByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId());
            //按照典籍进行分组
            for (Book book : bookList){
                if (!bookIdStringList.contains(book.getStringBookId())){
                    nodes.add(Tool.buildNode(book.getStringBookId(), (prescriptionInfo.getPrescriptionName()+book.getBookName()), book.getCategory()));
                    bookIdStringList.add(book.getStringBookId());
                }
                edges.add(Tool.buildEdge(prescription.getStringPrescriptionId(), book.getStringBookId()));
                //连线相关草药
                List<Herb> herbList = herbMapper.findHerbByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId());
                for (Herb herb : herbList){
                    if (!herbIdStringList.contains(herb.getStringHerbId())){
                        nodes.add(Tool.buildNode(herb.getStringHerbId(), herb.getHerbName(), herb.getCategory()));
                        herbIdStringList.add(herb.getStringHerbId());
                    }
                    edges.add(Tool.buildEdge(book.getStringBookId(), herb.getStringHerbId()));
                }
                //连线对应病症
                List<Disease> diseaseList = diseaseMapper.findDiseaseByPrescriptionInfoId(prescriptionInfo.getPrescriptionInfoId());
                for (Disease disease : diseaseList){
                    if (!diseaseIdStringList.contains(disease.getStringDiseaseId())){
                        nodes.add(Tool.buildNode(disease.getStringDiseaseId(), disease.getDiseaseName(), disease.getCategory()));
                        diseaseIdStringList.add(disease.getStringDiseaseId());
                    }
                    edges.add(Tool.buildEdge(book.getStringBookId(), disease.getStringDiseaseId()));
                }
            }
        }

        GraphData data = new GraphData(nodes, edges);
        return data;
    }

    @Override
    public void writePrescriptionDataFile(String path) {
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

            List<Disease> diseaseList = diseaseMapper.findDiseaseByPage(175, 300);
            List<Integer> prescriptionList = new ArrayList<>();
            StringBuffer nodes = new StringBuffer();
            StringBuffer edges = new StringBuffer();
            Random valueRandom = new Random();
            Random sizeRandom = new Random();
            getNodeAndEdge(diseaseList, prescriptionList, nodes, edges, -1, valueRandom, sizeRandom);
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

    private void getNodeAndEdge(List<Disease> diseaseList, List<Integer> prescriptionList, StringBuffer nodes, StringBuffer edges, int nodeId, Random valueRandom, Random sizeRandom){
        for (Disease disease : diseaseList){
            List<PrescriptionInfo> prescriptionInfos = prescriptionInfoMapper.findPrescriptionInfoByDiseaseId(disease.getDiseaseId());
            if (prescriptionInfos.size() == 0 || prescriptionInfos.size() == 1){
                continue;
            }
            Set<Integer> pidSet = new HashSet<>();
            for (int i = 0; i < prescriptionInfos.size(); i++) {
                pidSet.add(prescriptionInfos.get(i).getPrescriptionId());
                if (!prescriptionList.contains(prescriptionInfos.get(i).getPrescriptionId())){
                    int value = valueRandom.nextInt(8);
                    int size = sizeRandom.nextInt(20) + 10;
                    String node = "<node id=\""+ prescriptionInfos.get(i).getPrescriptionId() +"\" label=\""+ prescriptionInfos.get(i).getPrescriptionName() +"\">\n" +
                            "        <attvalues>\n" +
                            "          <attvalue for=\"modularity_class\" value=\""+value+"\"></attvalue>\n" +
                            "        </attvalues>\n" +
                            "        <viz:size value=\""+size+"\"></viz:size>\n" +
                            "      </node>";
                    nodes.append(node);
                    prescriptionList.add(prescriptionInfos.get(i).getPrescriptionId());
                }
            }

            List<Integer> idList = new ArrayList<>();
            for (Integer id : pidSet){
                idList.add(id);
            }
            //System.out.println(idList.toString());
            for (int j = 0; j < idList.size()-1; j++) {
                String edge = "<edge source=\""+ idList.get(j) +"\" target=\""+ idList.get(j+1) +"\">\n" +
                        "        <attvalues></attvalues>\n" +
                        "      </edge>";
                edges.append(edge);
            }
        }
    }

}
