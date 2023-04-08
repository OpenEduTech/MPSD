package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Description;
import indi.zyang.neev.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    /**
      * @Author Zyang
      * @Desctription 根据proId查询Product
      * @Date 2023/4/4 15:19
      * @Param [proId]
      * @return Product
      */
    Product findProductByProId(@Param("proId") int proId);

    /**
      * @Author Zyang
      * @Desctription 根据ProductName模糊查询Product列表
      * @Date 2023/4/4 16:29
      * @Param [proName]
      * @return java.util.List<indi.zyang.neev.entity.Product>
      */
    List<Product> findProductByProName(@Param("proName") String proName);

    /**
      * @Author Zyang
      * @Desctription 分页查询
      * @Date 2023/4/4 16:30
      * @Param [start,size]
      * @return java.util.List<indi.zyang.neev.entity.Product>
      */
    List<Product> findProductByPage(@Param("start") int start,@Param("size") int size);

    /**
      * @Author Zyang
      * @Desctription 查询某一proCategory的所有产品
      * @Date 2023/4/4 16:34
      * @Param [proCategory]
      * @return java.util.List<indi.zyang.neev.entity.Product>
      */
    List<Product> findProductByProCategory(@Param("proCategory") String proCategory);

    /**
      * @Author Zyang
      * @Desctription 查询Company所有Product列表
      * @Date 2023/4/6 10:20
      * @Param [comId]
      * @return java.util.List<indi.zyang.neev.entity.Product>
      */
    List<Product> findProductByComId(@Param("comId") int comId);

    /**
      * @Author Zyang
      * @Desctription 查询Product的CptProduct
      * @Date 2023/4/6 19:35
      * @Param [proId]
      * @return java.util.List<indi.zyang.neev.entity.Product>
      */
    List<Product> findCptProductByProId(@Param("proId") int proId);

}
