package indi.zyang.neev.controller;

import indi.zyang.neev.entity.Book;
import indi.zyang.neev.service.*;
import indi.zyang.neev.unit.GraphData;
import indi.zyang.neev.unit.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;

    @RequestMapping("/{id}")
    @ResponseBody
    public Map<String, Object> getBookInfo(@PathVariable("id") int bookId){
        Book book = bookService.findBookByBookIdFullPrescriptionAndHerb(bookId);
        GraphData data = bookService.getEChartGraphData(book);
        return Tool.formatData(book, data, "book");
    }

    @RequestMapping("/search")
    @ResponseBody
    public Map<String, Object> searchBook(String content){
        if (content == null || content.equals("")){
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, null, null, null);
            return Tool.formatSearchReturn(searchResult, 0);
        }else {
            List<Book> bookList = bookService.findBookByBookName(content);
            Map<String, Object> searchResult = Tool.formatSearchResult(null, null, null, bookList, null, null);
            return Tool.formatSearchReturn(searchResult, bookList.size());
        }
    }

    @RequestMapping("/name/{id}")
    @ResponseBody
    public Map<String, String> getBookName(@PathVariable("id") int bookId){
        Book book = bookService.findBookByBookId(bookId);
        return Tool.formatObjName("典籍", book.getBookName());
    }
}
