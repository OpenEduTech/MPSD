package indi.zyang.neev.dao;

import indi.zyang.neev.entity.PrescriptionInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionInfoMapper {
    /**
     * 分页查询
     * @param start 偏移量
     * @param size 页长
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPage(@Param("start") int start, @Param("size") int size);

    /**
     *根据prescriptionInfoId查询prescriptionInfo对象
     * @param prescriptionInfoId
     * @return
     */
    PrescriptionInfo findPrescriptionInfoByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     *根据prescriptionId查询prescriptionInfo列表
     * @param prescriptionId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPrescriptionId(@Param("prescriptionId") int prescriptionId);

    /**
     * 根据prescriptionInfoId查询prescriptionInfo对象的部分数据
     * @param prescriptionInfoId
     * @return
     */
    PrescriptionInfo findPrescriptionInfoSimply(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     *根据prescriptionName模糊查询prescriptionInfo对象
     * @param prescriptionName
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPrescriptionName(@Param("prescriptionName") String prescriptionName);

    /**
     *根据bookId查询prescriptionInfo对象，即查找古籍中收录的药剂详细信息列表
     * @param bookId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByBookId(@Param("bookId") int bookId);

    /**
     *根据diseaseId查询prescriptionInfo列表，即查找治疗对应病症的药剂列表
     * @param diseaseId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByDiseaseId(@Param("diseaseId") int diseaseId);

    /**
     *根据herbId查询prescriptionInfo列表，即查找包含该草药的药剂列表
     * @param herbId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByHerbId(@Param("herbId") int herbId);

    /**
     *插入一个prescriptionInfo对象
     * @param prescriptionInfo
     * @return
     */
    int insertPrescriptionInfo(PrescriptionInfo prescriptionInfo);

    /**
     *更新一个prescriptionInfo对象
     * @param prescriptionInfo
     * @return
     */
    int updatePrescriptionInfo(PrescriptionInfo prescriptionInfo);

    /**
     *删除指定prescriptionInfoId的药剂对象
     * @param prescriptionInfoId
     * @return
     */
    int deletePrescriptionInfoByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     *删除指定prescriptionId的药剂列表
     * @param prescriptionId
     * @return
     */
    int deletePrescriptionInfoByPrescriptionId(@Param("prescriptionId") int prescriptionId);
}
