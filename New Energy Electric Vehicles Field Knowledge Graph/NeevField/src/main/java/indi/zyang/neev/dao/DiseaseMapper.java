package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Disease;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseMapper {
    /**
     * 根据diseaseId查询指定disease对象
     * @param diseaseId
     * @return
     */
    Disease findDiseaseByDiseaseId(@Param("diseaseId") int diseaseId);

    /**
     * 分页查询疾病
     * @param start 偏移量
     * @param size 页长
     * @return
     */
    List<Disease> findDiseaseByPage(@Param("start") int start, @Param("size") int size);

    /**
     * 根据diseaseName模糊查询disease列表
     * @param diseaseName
     * @return
     */
    List<Disease> findDiseaseByDiseaseName(@Param("diseaseName") String diseaseName);

    /**
     * 查询HerbInfo治疗的disease列表
     * @param herbInfoId
     * @return
     */
    List<Disease> findDiseaseByHerbInfoId(@Param("herbInfoId") int herbInfoId);

    /**
     * 查询PrescriptionInfo治疗的disease列表
     * @param prescriptionInfoId
     * @return
     */
    List<Disease> findDiseaseByPrescriptionInfoId(@Param("prescriptionInfoId") int prescriptionInfoId);

    /**
     * 插入一个disease对象
     * @param disease
     * @return
     */
    int insertDisease(Disease disease);

    /**
     * 更新一个disease对象
     * @param disease
     * @return
     */
    int updateDisease(Disease disease);

    /**
     * 删除指定diseaseId的disease对象
     * @param diseaseId
     * @return
     */
    int deleteDiseaseByDiseaseId(@Param("diseaseId") int diseaseId);
}
