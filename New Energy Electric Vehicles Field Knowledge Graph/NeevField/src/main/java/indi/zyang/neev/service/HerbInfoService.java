package indi.zyang.neev.service;

import indi.zyang.neev.entity.HerbInfo;
import indi.zyang.neev.unit.GraphData;

import java.util.List;

public interface HerbInfoService {
    /**
     * 查询中医药剂prescriptionInfo包含的草药herbInfo有哪些
     * @param prescriptionInfoId
     * @return
     */
    List<HerbInfo> findHerbInfoByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 根据herbId查询一个herbInfo对象
     * @param herbInfoId
     * @return
     */
    HerbInfo findHerbInfoByHerbInfoId(int herbInfoId);

    /**
     * 根据草药名查询草药详细信息，like查询herbName、herbAlias
     * @param herbName
     * @return
     */
    List<HerbInfo> findHerbInfoByHerbName(String herbName);

    /**
     * 查询能够治疗disease的herbInfo列表
     * @param diseaseId
     * @return
     */
    List<HerbInfo> findHerbInfoByDiseaseId(int diseaseId);

    /**
     * 查询book记录的herbInfo列表
     * @param bookId
     * @return
     */
    List<HerbInfo> findHerbInfoByBookId(int bookId);

    /**
     * 查询geography生长的有哪些草药herbInfo列表
     * @param geographyId
     * @return
     */
    List<HerbInfo> findHerbInfoByGeographyId(int geographyId);

    /**
     * 插入一个herbInfo对象
     * @param herbInfo
     * @return
     */
    boolean insertHerbInfo(HerbInfo herbInfo);

    /**
     * 根据herbId删除一个herbInfo对象
     * @param herbInfoId
     * @return
     */
    boolean deleteHerbInfoByHerbInfoId(int herbInfoId);

    /**
     * 更新一个herbInfo对象
     * @param herbInfo
     * @return
     */
    boolean updateHerbInfo(HerbInfo herbInfo);

    /**
     * 构建ECharts关系图所需要的数据结构
     * @param herbInfo
     * @return
     */
    GraphData getEChartGraphData(HerbInfo herbInfo);
}
