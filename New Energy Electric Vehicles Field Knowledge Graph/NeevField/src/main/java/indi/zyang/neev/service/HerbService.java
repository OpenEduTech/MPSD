package indi.zyang.neev.service;

import indi.zyang.neev.entity.Herb;
import indi.zyang.neev.unit.GraphData;

import java.util.List;

public interface HerbService {
    /**
     * 根据id查询herb
     * @param herbId
     * @return
     */
    Herb findHerbByHerbId(int herbId);

    /**
     * 根据id查询herb，并级联查询出该草药构成哪些药剂
     * @param herbId
     * @return
     */
    Herb findFullHerbByHerbId(int herbId);

    /**
     * 查询一个prescriptionInfo的草药成分列表，一个prescriptionInfo由多种草药组成
     * @param prescriptionInfoId 药剂信息id
     * @return
     */
    List<Herb> findHerbByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 根据草药名查询草药列表，like查询
     * @param herbName
     * @return
     */
    List<Herb> findHerbByHerbName(String herbName);

    /**
     * 插入一个草药对象
     * @param herb
     * @return
     */
    boolean insertHerb(Herb herb);

    /**
     * 更新herb对象
     * @param herb
     * @return
     */
    boolean updateHerb(Herb herb);

    /**
     * 根据id删除herb对象
     * @param herbId
     * @return
     */
    boolean deleteHerbByHerbId(int herbId);

    /**
     * 构建ECharts关系图所需要的数据结构
     * @param herb
     * @return
     */
    GraphData getEChartGraphData(Herb herb);

    /**
     * 写首页草药关系图数据文件
     */
    void writeHerbDataFile(String path);
}
