package indi.zyang.neev.service;

import indi.zyang.neev.entity.Book;
import indi.zyang.neev.unit.GraphData;

import java.util.List;

public interface BookService {
    /**
     * 根据bookId查询book
     * @param bookId
     * @return
     */
    Book findBookByBookId(int bookId);

    /**
     * 根据bookId查询book，并填充该典籍记录的药剂列表和草药列表
     * @param bookId
     * @return
     */
    Book findBookByBookIdFullPrescriptionAndHerb(int bookId);

    /**
     * 根据bookName模糊查询book列表
     * @param bookName
     * @return
     */
    List<Book> findBookByBookName(String bookName);

    /**
     * 分页查询
     * @param page 页数
     * @param size 页长
     * @return
     */
    List<Book> findBookByPage(int page, int size);

    /**
     * 查询HerbInfo出自哪些书籍
     * @param herbInfoId
     * @return
     */
    List<Book> findBookByHerbInfoId(int herbInfoId);

    /**
     * 查询PrescriptionInfo出自哪些书籍
     * @param prescriptionInfoId
     * @return
     */
    List<Book> findBookByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 插入一个Book对象
     * @param book
     * @return
     */
    boolean insertBook(Book book);

    /**
     * 更新一个Book对象
     * @param book
     * @return
     */
    boolean updateBook(Book book);

    /**
     * 删除指定bookId的Book对象
     * @param bookId
     * @return
     */
    boolean deleteBookByBookId(int bookId);

    /**
     * 构建ECharts关系图所需要的数据结构
     * @param book
     * @return
     */
    GraphData getEChartGraphData(Book book);

    /**
     * 写首页书籍关系图数据文件
     */
    void writeBookDataFile(String path);
}
