package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Book;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper {
    /**
     * 根据bookId查询book
     * @param bookId
     * @return
     */
    Book findBookByBookId(@Param("bookId") int bookId);

    /**
     * 根据bookName模糊查询book列表
     * @param bookName
     * @return
     */
    List<Book> findBookByBookName(@Param("bookName") String bookName);

    /**
     * 分页查询
     * @param start 偏移量
     * @param size 页长
     * @return
     */
    List<Book> findBookByPage(@Param("start") int start, @Param("size") int size);

    /**
     * 查询HerbInfo出自哪些书籍
     * @param herbInfoId
     * @return
     */
    List<Book> findBookByHerbInfoId(@Param("herbInfoId") int herbInfoId);

    /**
     * 查询PrescriptionInfo出自哪些书籍
     * @param prescriptionInfoId
     * @return
     */
    List<Book> findBookByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     * 插入一个Book对象
     * @param book
     * @return
     */
    int insertBook(Book book);

    /**
     * 更新一个Book对象
     * @param book
     * @return
     */
    int updateBook(Book book);

    /**
     * 删除指定bookId的Book对象
     * @param bookId
     * @return
     */
    int deleteBookByBookId(@Param("bookId") int bookId);
}
