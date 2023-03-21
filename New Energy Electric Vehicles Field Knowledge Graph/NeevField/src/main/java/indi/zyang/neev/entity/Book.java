package indi.zyang.neev.entity;

import org.apache.ibatis.type.Alias;

import java.util.List;

//中医典籍类
@Alias("book")
public class Book {
    /**
     * ER简图
     * book————hi_b_r(n:m)————herb_info
     *    |
     *    |
     * pi_b_r(n:m)
     *    |
     *    |
     * prescription_info
     */

    //中医典籍唯一标识符
    private int bookId;
    //中医典籍名称
    private String bookName;
    //该典籍记录的草药信息列表
    private List<HerbInfo> herbInfoList;
    //该典籍记录的药剂信息列表
    private List<PrescriptionInfo> prescriptionInfoList;

    public Book() {
    }

    public Book(String bookName) {
        this.bookName = bookName;
    }

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public List<HerbInfo> getHerbInfoList() {
        return herbInfoList;
    }

    public void setHerbInfoList(List<HerbInfo> herbInfoList) {
        this.herbInfoList = herbInfoList;
    }

    public List<PrescriptionInfo> getPrescriptionInfoList() {
        return prescriptionInfoList;
    }

    public void setPrescriptionInfoList(List<PrescriptionInfo> prescriptionInfoList) {
        this.prescriptionInfoList = prescriptionInfoList;
    }

    public String getStringBookId(){
        return "b" + this.bookId;
    }

    public int getCategory(){
        return 3;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                '}';
    }
}
