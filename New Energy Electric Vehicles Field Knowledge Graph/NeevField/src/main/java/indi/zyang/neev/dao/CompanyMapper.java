package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Company;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyMapper {
    /**
      * @Author Zyang
      * @Desctription 根据CompanyId查询Company
      * @Date 2023/4/6 10:12
      * @Param [comId]
      * @return indi.zyang.neev.entity.Company
      */
    Company findCompanyByComId(@Param("comId") int comId);

    /**
      * @Author Zyang
      * @Desctription 根据CompanyName模糊查询Company列表
      * @Date 2023/4/6 10:13
      * @Param [comName]
      * @return java.util.List<indi.zyang.neev.entity.Company>
      */
    List<Company> findCompanyByComName(@Param("comName") String comName);

    /**
      * @Author Zyang
      * @Desctription 分页查询
      * @Date 2023/4/6 10:14
      * @Param [start]
      * @return java.util.List<indi.zyang.neev.entity.Company>
      */
    List<Company> findCompanyByPage(@Param("start") int start);

    /**
      * @Author Zyang
      * @Desctription 查询Company的竞争关系Company
      * @Date 2023/4/6 10:32
      * @Param [comId]
      * @return java.util.List<indi.zyang.neev.entity.Company>
      */
    List<Company> findCptCompanyByComId(@Param("comId") int comId);

    /**
      * @Author Zyang
      * @Desctription 查询Product所属Company
      * @Date 2023/4/6 19:27
      * @Param [proId]
      * @return indi.zyang.neev.entity.Company
      */
    List<Company> findCompanyByProId(@Param("proId") int proId);

    /**
      * @Author Zyang
      * @Desctription 查询Industry所有Company
      * @Date 2023/4/7 0:39
      * @Param [indId]
      * @return java.util.List<indi.zyang.neev.entity.Company>
      */
    List<Company> findCompanyByIndId(@Param("indId") int indId);

    //测试ci
}
