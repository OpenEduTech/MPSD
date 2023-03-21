package indi.zyang.neev.service;

import indi.zyang.neev.entity.Disease;
import indi.zyang.neev.unit.GraphData;

import java.util.List;

public interface DiseaseService {
    /**
     * 根据diseaseId查询指定disease对象
     * @param diseaseId
     * @return
     */
    Disease findDiseaseByDiseaseId(int diseaseId);

    /**
     * 级联查询，查询出该疾病对应的草药与药剂
     * @param diseaseId
     * @return
     */
    Disease findFullDiseaseByDiseaseId(int diseaseId);

    /**
     * 分页查询疾病
     * @param page 页数
     * @param size 页长
     * @return
     */
    List<Disease> findDiseaseByPage(int page, int size);

    /**
     * 根据diseaseName模糊查询disease列表
     * @param diseaseName
     * @return
     */
    List<Disease> findDiseaseByDiseaseName(String diseaseName);

    /**
     * 查询HerbInfo治疗的disease列表
     * @param herbInfoId
     * @return
     */
    List<Disease> findDiseaseByHerbInfoId(int herbInfoId);

    /**
     * 查询PrescriptionInfo治疗的disease列表
     * @param prescriptionInfoId
     * @return
     */
    List<Disease> findDiseaseByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 插入一个disease对象
     * @param disease
     * @return
     */
    boolean insertDisease(Disease disease);

    /**
     * 更新一个disease对象
     * @param disease
     * @return
     */
    boolean updateDisease(Disease disease);

    /**
     * 删除指定diseaseId的disease对象
     * @param diseaseId
     * @return
     */
    boolean deleteDiseaseByDiseaseId(int diseaseId);

    /**
     * 构建ECharts关系图所需要的数据结构
     * @param disease
     * @return
     */
    GraphData getEChartGraphData(Disease disease);

    /**
     * 写首页疾病关系图数据文件
     */
    void writeDiseaseDataFile(String path);
}
