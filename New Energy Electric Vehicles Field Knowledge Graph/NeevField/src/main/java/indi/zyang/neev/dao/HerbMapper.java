package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Herb;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HerbMapper {
    /**
     * 根据id查询herb
     * @param herbId
     * @return
     */
    Herb findHerbByHerbId(@Param("herbId") int herbId);

    /**
     * 查询一个prescriptionInfo的草药成分列表，一个prescriptionInfo由多种草药组成
     * @param prescriptionInfoId 药剂信息id
     * @return
     */
    List<Herb> findHerbByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     * 根据草药名查询草药列表，like查询
     * @param herbName
     * @return
     */
    List<Herb> findHerbByHerbName(@Param("herbName") String herbName);

    /**
     * 根据草药名精确查找一个herb对象
     * @param herbName
     * @return
     */
    Herb findHerbByHerbNameAccurate(@Param("herbName") String herbName);
    /**
     * 插入一个草药对象
     * @param herb
     * @return
     */
    int insertHerb(Herb herb);

    /**
     * 更新herb对象
     * @param herb
     * @return
     */
    int updateHerb(Herb herb);

    /**
     * 根据id删除herb对象
     * @param herbId
     * @return
     */
    int deleteHerbByHerbId(@Param("herbId") int herbId);
}
