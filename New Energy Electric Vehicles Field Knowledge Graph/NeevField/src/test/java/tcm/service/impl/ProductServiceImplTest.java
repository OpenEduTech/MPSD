package tcm.service.impl;

import indi.zyang.neev.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    ProductService productService;

    @Test
    public void writeIndDataFile() {
        //bookService.writeBookDataFile("D:/develop/workspace/idea-workspace/practice/tcm/src/main/resources/static/file/book.gexf");
    }
}
