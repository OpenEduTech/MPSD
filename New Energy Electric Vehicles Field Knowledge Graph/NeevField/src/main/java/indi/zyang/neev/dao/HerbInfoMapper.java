package indi.zyang.neev.dao;

import indi.zyang.neev.entity.HerbInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HerbInfoMapper {
    /**
     * 根据herbId查询一个herbInfo对象
     * @param herbInfoId
     * @return
     */
    HerbInfo findHerbInfoByHerbInfoId(@Param("herbInfoId") int herbInfoId);

    /**
     * 根据草药名查询草药详细信息，like查询herbName、herbAlias
     * @param herbName
     * @return
     */
    List<HerbInfo> findHerbInfoByHerbName(@Param("herbName") String herbName);

    /**
     * 查询能够治疗disease的herbInfo列表
     * @param diseaseId
     * @return
     */
    List<HerbInfo> findHerbInfoByDiseaseId(@Param("diseaseId") int diseaseId);

    /**
     * 查询book记录的herbInfo列表
     * @param bookId
     * @return
     */
    List<HerbInfo> findHerbInfoByBookId(@Param("bookId") int bookId);

    /**
     * 查询geography生长的有哪些草药herbInfo列表
     * @param geographyId
     * @return
     */
    List<HerbInfo> findHerbInfoByGeographyId(@Param("geographyId") int geographyId);

    /**
     * 根据草药名查询一个HerbInfo对象
     * @param herbName
     * @return
     */
    HerbInfo findOneHerbInfoByHerbName(@Param("herbName") String herbName);

    /**
     * 根据草药别名查询HerbInfo列表
     * @param herbAlias
     * @return
     */
    List<HerbInfo> findHerbInfoByHerbAlias(@Param("herbAlias") String herbAlias);

    /**
     * 插入一个herbInfo对象
     * @param herbInfo
     * @return
     */
    int insertHerbInfo(HerbInfo herbInfo);

    /**
     * 根据herbId删除一个herbInfo对象
     * @param herbInfoId
     * @return
     */
    int deleteHerbInfoByHerbInfoId(@Param("herbInfoId") int herbInfoId);

    /**
     * 更新一个herbInfo对象
     * @param herbInfo
     * @return
     */
    int updateHerbInfo(HerbInfo herbInfo);
}
