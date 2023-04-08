package indi.zyang.neev.service;

import indi.zyang.neev.entity.Product;
import indi.zyang.neev.unit.GraphData;

public interface ProductService {
    /**
      * @Author Zyang
      * @Desctription 根据ProductId查询Product
      * @Date 2023/4/4 15:27
      * @Param [proId]
      * @return Product
      */
    Product findProductByProId(int proId);

    /**
      * @Author Zyang
      * @Desctription 根据ProductId查询Product关联信息
      * @Date 2023/4/6 19:29
      * @Param [proId]
      * @return indi.zyang.neev.entity.Product
      */
    Product findFullProductByProId(int proId);

    /**
      * @Author Zyang
      * @Desctription 构建ECharts关系图所需要的数据结构
      * @Date 2023/4/6 19:50
      * @Param [product]
      * @return indi.zyang.neev.unit.GraphData
      */
    GraphData getEChartGraphData(Product product);
}
