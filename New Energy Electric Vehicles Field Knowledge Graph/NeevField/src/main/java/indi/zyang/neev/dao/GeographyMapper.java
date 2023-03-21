package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Geography;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeographyMapper {
    /**
     *根据地理位置唯一标识符geographyId查询geography对象
     * @param geographyId
     * @return
     */
    Geography findGeographyByGeographyId(@Param("geographyId") int geographyId);

    /**
     *根据地理位置名称模糊查询geography列表
     * @param geographyName
     * @return
     */
    List<Geography> findGeographyByGeographyName(@Param("geographyName") String geographyName);

    /**
     * 查询该地址下级地址
     * @param superior
     * @return
     */
    List<Geography> findGeographySonBySuperior(@Param("superior") int superior);

    /**
     *根据地理位置精度sign查询geography列表
     * @param sign
     * @return
     */
    List<Geography> findGeographyBySign(@Param("sign") int sign);

    /**
     *根据地理位置名称和地理位置精度查询geography列表
     * @param geographyName
     * @param sign
     * @return
     */
    List<Geography> findGeographyByNameAndSign(@Param("geographyName") String geographyName, @Param("sign") int sign);

    /**
     *根据herbInfoId查询草药生长在哪些地理位置
     * @param herbInfoId
     * @return
     */
    List<Geography> findGeographyByHerbInfoId(@Param("herbInfoId") int herbInfoId);

    /**
     *插入一个geography对象
     * @param geography
     * @return
     */
    int insertGeography(Geography geography);

    /**
     *更新一个geography对象
     * @param geography
     * @return
     */
    int updateGeography(Geography geography);

    /**
     * 删除指定geographyId的geography记录
     * @param geographyId
     * @return
     */
    int deleteGeographyByGeographyId(@Param("geographyId") int geographyId);

}
