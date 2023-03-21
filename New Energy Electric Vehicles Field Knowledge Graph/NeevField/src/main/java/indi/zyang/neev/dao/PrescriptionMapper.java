package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Prescription;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionMapper {
    /**
     * 根据药剂唯一标识符prescriptionId查询prescription对象
     * @param prescriptionId
     * @return
     */
    Prescription findPrescriptionByPrescriptionId(@Param("prescriptionId") int prescriptionId);

    /**
     * 根据药剂详细信息唯一标识符prescriptionInfoId查询prescription对象
     * @param prescriptionInfoId
     * @return
     */
    Prescription findPrescriptionByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     * 根据药剂名称prescriptionName模糊查询药剂列表
     * @param prescriptionName
     * @return
     */
    List<Prescription> findPrescriptionByPrescriptionName(@Param("prescriptionName") String prescriptionName);

    /**
     * 分页查询
     * @param start 偏移量
     * @param size 页长
     * @return
     */
    List<Prescription> findPrescriptionByPage(@Param("start") int start, @Param("size") int size);

    /**
     * 搜索药剂，分页查询
     * @param content
     * @return
     */
    List<Prescription> searchPrescription(@Param("content") String content);

    /**
     * 搜索药剂总数
     * @param content
     * @return
     */
    int searchPrescriptionCount(@Param("content") String content);

    /**
     * 插入一个prescription对象
     * @param prescription
     * @return
     */
    int insertPrescription(Prescription prescription);

    /**
     * 更新一个prescription对象
     * @param prescription
     * @return
     */
    int updatePrescription(Prescription prescription);

    /**
     * 删除指定prescriptionId的prescription对象
     * @param prescriptionId
     * @return
     */
    int deletePrescriptionByPrescriptionId(@Param("prescriptionId") int prescriptionId);
}
