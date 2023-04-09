package indi.zyang.neev.service;

import indi.zyang.neev.entity.Industry;
import indi.zyang.neev.unit.GraphData;

public interface IndustryService {
    /**
      * @Author Zyang
      * @Desctription 根据IndustryId查询Industry
      * @Date 2023/4/6 9:58
      * @Param [indId]
      * @return indi.zyang.neev.entity.Industry
      */
    Industry findIndustryByIndId(int indId);

    /**
      * @Author Zyang
      * @Desctription 根据IndustryId查询Industry关联信息
      * @Date 2023/4/7 0:33
      * @Param [IndId]
      * @return indi.zyang.neev.entity.Industry
      */
    Industry findFullIndustryByIndId(int indId);

    /**
      * @Author Zyang
      * @Desctription 构建ECharts关系图所需要的数据结构
      * @Date 2023/4/7 0:34
      * @Param [industry]
      * @return indi.zyang.neev.unit.GraphData
      */
    GraphData getEChartGraphData(Industry industry);
}
