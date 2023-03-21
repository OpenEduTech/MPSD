package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.*;
import indi.zyang.neev.entity.Book;
import indi.zyang.neev.entity.HerbInfo;
import indi.zyang.neev.entity.PrescriptionInfo;
import indi.zyang.neev.service.BookService;
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
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;
    @Autowired
    PrescriptionInfoMapper prescriptionInfoMapper;
    @Autowired
    HerbInfoMapper herbInfoMapper;

    @Override
    public Book findBookByBookId(int bookId) {
        return bookMapper.findBookByBookId(bookId);
    }

    @Override
    public Book findBookByBookIdFullPrescriptionAndHerb(int bookId) {
        Book book = bookMapper.findBookByBookId(bookId);
        List<PrescriptionInfo> prescriptionInfoList = prescriptionInfoMapper.findPrescriptionInfoByBookId(bookId);
        List<HerbInfo> herbInfoList = herbInfoMapper.findHerbInfoByBookId(bookId);
        book.setHerbInfoList(herbInfoList);
        book.setPrescriptionInfoList(prescriptionInfoList);
        return book;
    }

    @Override
    public List<Book> findBookByBookName(String bookName) {
        return bookMapper.findBookByBookName(bookName);
    }

    @Override
    public List<Book> findBookByPage(int page, int size) {
        int start = (page - 1) * size;
        return bookMapper.findBookByPage(start, size);
    }

    @Override
    public List<Book> findBookByHerbInfoId(int herbInfoId) {
        return bookMapper.findBookByHerbInfoId(herbInfoId);
    }

    @Override
    public List<Book> findBookByPrescriptionInfoId(int prescriptionInfoId) {
        return bookMapper.findBookByPrescriptionInfoId(prescriptionInfoId);
    }

    @Override
    public boolean insertBook(Book book) {
        int count = bookMapper.insertBook(book);
        return count == 1 ;
    }

    @Override
    public boolean updateBook(Book book) {
        int count = bookMapper.updateBook(book);
        return count == 1;
    }

    @Override
    public boolean deleteBookByBookId(int bookId) {
        int count = bookMapper.deleteBookByBookId(bookId);
        return count == 1;
    }

    @Override
    public GraphData getEChartGraphData(Book book) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        List<HerbInfo> herbInfoList = book.getHerbInfoList();
        List<PrescriptionInfo> prescriptionInfoList = book.getPrescriptionInfoList();
        List<String> prescriptionIdStringList = new ArrayList<>();

        nodes.add(Tool.buildNode(book.getStringBookId(), book.getBookName(), book.getCategory()));
        for (HerbInfo herbInfo : herbInfoList){
            nodes.add(Tool.buildNode(herbInfo.getStringHerbInfoId(), herbInfo.getHerbName(), herbInfo.getCategory()));
            edges.add(Tool.buildEdge(herbInfo.getStringHerbInfoId(), book.getStringBookId()));
        }
        for (PrescriptionInfo prescriptionInfo : prescriptionInfoList){
            if (!prescriptionIdStringList.contains(prescriptionInfo.getStringPrescriptionId())){
                nodes.add(Tool.buildNode(prescriptionInfo.getStringPrescriptionId(), prescriptionInfo.getPrescriptionName(), prescriptionInfo.getCategory()));
                prescriptionIdStringList.add(prescriptionInfo.getStringPrescriptionId());
                edges.add(Tool.buildEdge(book.getStringBookId(), prescriptionInfo.getStringPrescriptionId()));
            }
        }
        GraphData data = new GraphData(nodes, edges);
        return data;
    }

    @Override
    public void writeBookDataFile(String path) {
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

            List<Book> bookList = bookMapper.findBookByPage(200, 70);
            StringBuffer nodes = new StringBuffer();
            StringBuffer edges = new StringBuffer();
            Random random = new Random();
            getNodeAndEdge(bookList, nodes, edges, random, 30);
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

    private void getNodeAndEdge(List<Book> bookList, StringBuffer nodes, StringBuffer edges, Random random, int size){
        for (Book book : bookList){
            int value = random.nextInt(8);
            String node = "<node id=\""+ book.getBookId() +"\" label=\""+ book.getBookName() +"\">\n" +
                    "        <attvalues>\n" +
                    "          <attvalue for=\"modularity_class\" value=\""+value+"\"></attvalue>\n" +
                    "        </attvalues>\n" +
                    "        <viz:size value=\""+size+"\"></viz:size>\n" +
                    "      </node>";
            nodes.append(node);
        }
//        for (int i = 0; i < bookList.size()-10; i++) {
//            for (int j = i; j < bookList.size(); j = j + 10) {
//                String edge = "<edge source=\""+ bookList.get(i).getBookId() +"\" target=\""+bookList.get(j).getBookId()+"\">\n" +
//                        "        <attvalues></attvalues>\n" +
//                        "      </edge>";
//                edges.append(edge);
//            }
//        }
    }
}
