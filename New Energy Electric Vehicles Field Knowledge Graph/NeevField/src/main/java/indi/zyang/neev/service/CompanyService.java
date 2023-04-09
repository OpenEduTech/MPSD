package indi.zyang.neev.service;

import indi.zyang.neev.entity.Company;
import indi.zyang.neev.unit.GraphData;

public interface CompanyService {
    /**
      * @Author Zyang
      * @Desctription 根据CompanyId查询Company
      * @Date 2023/4/6 10:37
      * @Param [comId]
      * @return indi.zyang.neev.entity.Company
      */
    Company findCompanyByComId(int comId);

    /**
      * @Author Zyang
      * @Desctription 根据CompanyId查询Company关联信息
      * @Date 2023/4/6 23:41
      * @Param [comId]
      * @return indi.zyang.neev.entity.Company
      */
    Company findFullCompanyByComId(int comId);

    /**
      * @Author Zyang
      * @Desctription 构建ECharts关系图所需要的数据结构
      * @Date 2023/4/6 23:42
      * @Param [company]
      * @return indi.zyang.neev.unit.GraphData
      */
    GraphData getEChartGraphData(Company company);
}
