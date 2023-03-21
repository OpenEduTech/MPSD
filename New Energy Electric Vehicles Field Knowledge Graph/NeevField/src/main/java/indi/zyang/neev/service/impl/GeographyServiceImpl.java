package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.GeographyMapper;
import indi.zyang.neev.entity.Geography;
import indi.zyang.neev.service.GeographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class GeographyServiceImpl implements GeographyService {
    @Autowired
    GeographyMapper geographyMapper;

    @Override
    public Geography findGeographyByGeographyId(int geographyId) {
        return geographyMapper.findGeographyByGeographyId(geographyId);
    }

    @Override
    public List<Geography> findGeographyByGeographyName(String geographyName) {
        return geographyMapper.findGeographyByGeographyName(geographyName);
    }

    @Override
    public List<Geography> findGeographyBySign(int sign) {
        return geographyMapper.findGeographyBySign(sign);
    }

    @Override
    public List<Geography> findGeographyByNameAndSign(String geographyName, int sign) {
        return geographyMapper.findGeographyByNameAndSign(geographyName, sign);
    }

    @Override
    public List<Geography> findGeographyByHerbInfoId(int herbInfoId) {
        return geographyMapper.findGeographyByHerbInfoId(herbInfoId);
    }

    @Override
    public boolean insertGeography(Geography geography) {
        int count = geographyMapper.insertGeography(geography);
        return count == 1;
    }

    @Override
    public boolean updateGeography(Geography geography) {
        int count = geographyMapper.updateGeography(geography);
        return count == 1;
    }

    @Override
    public boolean deleteGeographyByGeographyId(int geographyId) {
        int count = geographyMapper.deleteGeographyByGeographyId(geographyId);
        return count == 1;
    }

    @Override
    public void writeGeographyDataFile(String path){
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

            List<Geography> geographyList = geographyMapper.findGeographyBySign(1);
            StringBuffer nodes = new StringBuffer();
            StringBuffer edges = new StringBuffer();
            Random random = new Random();
            getAllSonGeography(geographyList, nodes, edges, -1, 1, random, 40);
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

    private void getAllSonGeography(List<Geography> geographyList, StringBuffer nodes, StringBuffer edges, int nodeId, int edgeId, Random random, int size){
        size = size - 10;
        for (Geography geography : geographyList){
            int value = random.nextInt(8);
            String node = "<node id=\""+ geography.getGeographyId() +"\" label=\""+ geography.getGeographySimply() +"\">\n" +
                    "        <attvalues>\n" +
                    "          <attvalue for=\"modularity_class\" value=\""+value+"\"></attvalue>\n" +
                    "        </attvalues>\n" +
                    "        <viz:size value=\""+size+"\"></viz:size>\n" +
                    "        <viz:position x=\"-81.46074\" y=\"-204.20204\" z=\"0.0\"></viz:position>\n" +
                    "        <viz:color r=\"243\" g=\"124\" b=\"201\"></viz:color>\n" +
                    "      </node>";
            nodes.append(node);
            if (nodeId != -1){
                String edge = "<edge source=\""+ nodeId +"\" target=\""+geography.getGeographyId()+"\">\n" +
                        "        <attvalues></attvalues>\n" +
                        "      </edge>";
                edges.append(edge);
                edgeId++;
            }
            List<Geography> geographies = geographyMapper.findGeographySonBySuperior(geography.getGeographyId());
            if (geographies != null && geographies.size() != 0){
                getAllSonGeography(geographies, nodes, edges, geography.getGeographyId(), edgeId, random, size);
            }
        }
    }
}
