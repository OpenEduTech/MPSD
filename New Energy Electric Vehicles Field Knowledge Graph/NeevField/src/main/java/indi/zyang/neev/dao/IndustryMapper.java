package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Industry;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndustryMapper {
    /**
      * @Author Zyang
      * @Desctription 根据IndustryId查询Industry
      * @Date 2023/4/5 12:34
      * @Param [indId]
      * @return indi.zyang.neev.entity.Industry
      */
    Industry findIndustryByIndId(@Param("indId") int indId);

    /**
      * @Author Zyang
      * @Desctription 根据IndustryName模糊查询Industry列表
      * @Date 2023/4/5 12:37
      * @Param [indName]
      * @return java.util.List<indi.zyang.neev.entity.Industry>
      */
    List<Industry> findIndustryByIndName(@Param("indName") String indName);

    /**
      * @Author Zyang
      * @Desctription 分页查询
      * @Date 2023/4/5 12:39
      * @Param [start]
      * @return java.util.List<indi.zyang.neev.entity.Industry>
      */
    List<Industry> findIndustryByPage(@Param("start") int start);

    /**
      * @Author Zyang
      * @Desctription 查询某一IndustryLevel的所有上游industry
      * @Date 2023/4/5 12:44
      * @Param [indLevel]
      * @return java.util.List<indi.zyang.neev.entity.Industry>
      */
    List<Industry> findIndustryByUpIndLevel(@Param("indId") int indId);

    /**
      * @Author Zyang
      * @Desctription 查询某一IndustryLevel的所有下游industry
      * @Date 2023/4/5 12:46
      * @Param [indLevel]
      * @return java.util.List<indi.zyang.neev.entity.Industry>
      */
    List<Industry> findIndustryByDownIndLevel(@Param("indId") int indId);

    /**
     * @Author Zyang
     * @Desctription 查询某一IndustryLevel的同游industry
     * @Date 2023/4/5 12:46
     * @Param [indLevel]
     * @return java.util.List<indi.zyang.neev.entity.Industry>
     */
    List<Industry> findIndustryByIndLevel(@Param("indId") int indId);

    /**
      * @Author Zyang
      * @Desctription 查询Company所属Industry列表
      * @Date 2023/4/6 10:19
      * @Param [comId]
      * @return java.util.List<indi.zyang.neev.entity.Industry>
      */
    List<Industry> findIndustryByComId(@Param("comId") int comId);
}
