package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.*;
import indi.zyang.neev.entity.*;
import indi.zyang.neev.service.HerbInfoService;
import indi.zyang.neev.unit.Edge;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Node;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HerbInfoServiceImpl implements HerbInfoService {
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
    public List<HerbInfo> findHerbInfoByPrescriptionInfoId(int prescriptionInfoId) {
        return null;
    }

    @Override
    public HerbInfo findHerbInfoByHerbInfoId(int herbInfoId) {
        return herbInfoMapper.findHerbInfoByHerbInfoId(herbInfoId);
    }

    @Override
    public List<HerbInfo> findHerbInfoByHerbName(String herbName) {
        return herbInfoMapper.findHerbInfoByHerbName(herbName);
    }

    @Override
    public List<HerbInfo> findHerbInfoByDiseaseId(int diseaseId) {
        return herbInfoMapper.findHerbInfoByDiseaseId(diseaseId);
    }

    @Override
    public List<HerbInfo> findHerbInfoByBookId(int bookId) {
        return herbInfoMapper.findHerbInfoByBookId(bookId);
    }

    @Override
    public List<HerbInfo> findHerbInfoByGeographyId(int geographyId) {
        return herbInfoMapper.findHerbInfoByGeographyId(geographyId);
    }

    @Override
    public boolean insertHerbInfo(HerbInfo herbInfo) {
        int count = herbInfoMapper.insertHerbInfo(herbInfo);
        return count == 1;
    }

    @Override
    public boolean deleteHerbInfoByHerbInfoId(int herbInfoId) {
        int count = herbInfoMapper.deleteHerbInfoByHerbInfoId(herbInfoId);
        return count == 1;
    }

    @Override
    public boolean updateHerbInfo(HerbInfo herbInfo) {
        int count = herbInfoMapper.updateHerbInfo(herbInfo);
        return count == 1;
    }

    @Override
    public GraphData getEChartGraphData(HerbInfo herbInfo) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        List<String> prescriptionIdStringList = new ArrayList<>();
        List<String> geographyIdStringList = new ArrayList<>();
        List<String> bookIdStringList = new ArrayList<>();
        List<String> diseaseIdStringList = new ArrayList<>();

        nodes.add(Tool.buildNode(herbInfo.getStringHerbInfoId(), herbInfo.getHerbName(), herbInfo.getCategory()));

        Set<String> herbNameSet = new HashSet<>();
        herbNameSet.add(herbInfo.getHerbName());
        String[] herbAliasArray = herbInfo.getHerbAlias().split("、");
        for (String herbAlias : herbAliasArray){
            herbNameSet.add(herbAlias);
        }

        for (String herbName : herbNameSet){
            Herb h = herbMapper.findHerbByHerbNameAccurate(herbName);
            if (h != null){
                List<PrescriptionInfo> piList = prescriptionInfoMapper.findPrescriptionInfoByHerbId(h.getHerbId());
                for (PrescriptionInfo prescriptionInfo : piList){
                    if (!prescriptionIdStringList.contains(prescriptionInfo.getStringPrescriptionId())){
                        nodes.add(Tool.buildNode(prescriptionInfo.getStringPrescriptionId(), prescriptionInfo.getPrescriptionName(), prescriptionInfo.getCategory()));
                        prescriptionIdStringList.add(prescriptionInfo.getStringPrescriptionId());
                        edges.add(Tool.buildEdge(herbInfo.getStringHerbInfoId(), prescriptionInfo.getStringPrescriptionId()));
                    }
                }
            }
        }

        addHerbInfoNodeAndEdge(herbInfo, nodes, edges, geographyIdStringList, bookIdStringList, diseaseIdStringList);

        GraphData data = new GraphData(nodes, edges);
        return data;
    }

    private void addHerbInfoNodeAndEdge(HerbInfo herbInfo, List<Node> nodes, List<Edge> edges, List<String> geographyIdStringList, List<String> bookIdStringList, List<String> diseaseIdStringList){
        List<Geography> geographyList = geographyMapper.findGeographyByHerbInfoId(herbInfo.getHerbInfoId());
        List<Book> bookList = bookMapper.findBookByHerbInfoId(herbInfo.getHerbInfoId());
        List<Disease> diseaseList = diseaseMapper.findDiseaseByHerbInfoId(herbInfo.getHerbInfoId());
        for (Geography geography : geographyList){
            if (!geographyIdStringList.contains(geography.getStringGeographyId())){
                nodes.add(Tool.buildNode(geography.getStringGeographyId(), geography.getGeographySimply(), geography.getCategory()));
                geographyIdStringList.add(geography.getStringGeographyId());
            }
            edges.add(Tool.buildEdge(herbInfo.getStringHerbInfoId(), geography.getStringGeographyId()));
        }
        for (Book book : bookList){
            if (!bookIdStringList.contains(book.getStringBookId())){
                nodes.add(Tool.buildNode(book.getStringBookId(), book.getBookName(), book.getCategory()));
                bookIdStringList.add(book.getStringBookId());
            }
            edges.add(Tool.buildEdge(herbInfo.getStringHerbInfoId(), book.getStringBookId()));
        }
        for (Disease disease : diseaseList){
            if (!diseaseIdStringList.contains(disease.getStringDiseaseId())){
                nodes.add(Tool.buildNode(disease.getStringDiseaseId(), disease.getDiseaseName(), disease.getCategory()));
                diseaseIdStringList.add(disease.getStringDiseaseId());
            }
            edges.add(Tool.buildEdge(herbInfo.getStringHerbInfoId(), disease.getStringDiseaseId()));
        }
    }
}
