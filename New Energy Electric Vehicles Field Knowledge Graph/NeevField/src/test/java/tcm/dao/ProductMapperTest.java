package tcm.dao;

import indi.zyang.neev.dao.ProductMapper;
import indi.zyang.neev.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductMapperTest {

    @Autowired
    ProductMapper productMapper;

    @Test
    public void findProductByProId(){
        int productId = 10001;
        Product product = productMapper.findProductByProId(productId);
        System.out.println(product.toString());
    }

    @Test
    public void findCptProductByProId(){
        int productId = 10001;
        List<Product> productList = productMapper.findCptProductByProId(productId);
        for ( Product product : productList){
            System.out.println(product.toString());
        }
    }

    @Test
    public void findProductByComId(){
        int comId = 1001;
        List<Product> productList = productMapper.findProductByComId(comId);
        for ( Product product : productList){
            System.out.println(product.toString());
        }
    }
}
