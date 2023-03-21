package indi.zyang.neev.service;

import indi.zyang.neev.entity.Prescription;
import indi.zyang.neev.unit.GraphData;

import java.util.List;

public interface PrescriptionService {
    /**
     * 根据药剂唯一标识符prescriptionId查询prescription对象
     * @param prescriptionId
     * @return
     */
    Prescription findPrescriptionByPrescriptionId(int prescriptionId);

    /**
     * 根据药剂唯一标识符prescriptionId查询prescription对象，并填充关联对象
     * @param prescriptionId
     * @return
     */
    Prescription findFullPrescriptionByPrescriptionId(int prescriptionId);

    /**
     * 根据药剂详细信息唯一标识符prescriptionInfoId查询prescription对象
     * @param prescriptionInfoId
     * @return
     */
    Prescription findPrescriptionByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 根据药剂名称prescriptionName模糊查询药剂列表
     * @param prescriptionName
     * @return
     */
    List<Prescription> findPrescriptionByPrescriptionName(String prescriptionName);

    /**
     * 搜索药剂
     * @param content
     * @return
     */
    List<Prescription> searchPrescription(String content);

    /**
     * 搜索药剂总数
     * @param content
     * @return
     */
    int searchPrescriptionCount(String content);

    /**
     * 根据药剂名称prescriptionName模糊查询药剂列表，并填充prescriptionInfo对象
     * @param prescriptionName
     * @return
     */
    List<Prescription> findPrescriptionByPrescriptionNameFullPrescriptionInfo(String prescriptionName);

    /**
     * 分页查询
     * @param page 页数
     * @param size 页长
     * @return
     */
    List<Prescription> findPrescriptionByPage(int page, int size);

    /**
     * 插入一个prescription对象
     * @param prescription
     * @return
     */
    boolean insertPrescription(Prescription prescription);

    /**
     * 更新一个prescription对象
     * @param prescription
     * @return
     */
    boolean updatePrescription(Prescription prescription);

    /**
     * 删除指定prescriptionId的prescription对象
     * @param prescriptionId
     * @return
     */
    boolean deletePrescriptionByPrescriptionId(int prescriptionId);

    /**
     * 构建ECharts关系图所需要的数据结构
     * @param prescription
     * @return
     */
    GraphData getEChartGraphData(Prescription prescription);

    /**
     * 写首页药剂关系图数据文件
     */
    void writePrescriptionDataFile(String path);
}
