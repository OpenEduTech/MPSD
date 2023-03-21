package indi.zyang.neev.service;

import indi.zyang.neev.entity.PrescriptionInfo;

import java.util.List;

public interface PrescriptionInfoService {
    /**
     * 分页查询
     * @param page 页数
     * @param size 页长
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPage(int page, int size);
    /**
     *根据prescriptionInfoId查询prescriptionInfo对象
     * @param prescriptionInfoId
     * @return
     */
    PrescriptionInfo findPrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId);

    /**
     * 根据prescriptionInfoId查询prescriptionInfo对象，并填充关联对象
     * @param prescriptionInfoId
     * @return
     */
    PrescriptionInfo findFullPrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId);

    /**
     *根据prescriptionId查询prescriptionInfo列表
     * @param prescriptionId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPrescriptionId(int prescriptionId);

    /**
     *根据prescriptionName模糊查询prescriptionInfo对象
     * @param prescriptionName
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByPrescriptionName(String prescriptionName);

    /**
     *根据bookId查询prescriptionInfo对象，即查找古籍中收录的药剂详细信息列表
     * @param bookId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByBookId(int bookId);

    /**
     *根据diseaseId查询prescriptionInfo列表，即查找治疗对应病症的药剂列表
     * @param diseaseId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByDiseaseId(int diseaseId);

    /**
     *根据herbId查询prescriptionInfo列表，即查找包含该草药的药剂列表
     * @param herbId
     * @return
     */
    List<PrescriptionInfo> findPrescriptionInfoByHerbId(int herbId);

    /**
     *插入一个prescriptionInfo对象
     * @param prescriptionInfo
     * @return
     */
    boolean insertPrescriptionInfo(PrescriptionInfo prescriptionInfo);

    /**
     *更新一个prescriptionInfo对象
     * @param prescriptionInfo
     * @return
     */
    boolean updatePrescriptionInfo(PrescriptionInfo prescriptionInfo);

    /**
     *删除指定prescriptionInfoId的药剂对象
     * @param prescriptionInfoId
     * @return
     */
    boolean deletePrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId);

    /**
     *删除指定prescriptionId的药剂列表
     * @param prescriptionId
     * @return
     */
    boolean deletePrescriptionInfoByPrescriptionId(int prescriptionId);

}
