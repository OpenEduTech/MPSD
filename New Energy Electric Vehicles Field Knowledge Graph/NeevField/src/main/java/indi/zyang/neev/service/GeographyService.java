package indi.zyang.neev.service;

import indi.zyang.neev.entity.Geography;

import java.util.List;

public interface GeographyService {
    /**
     *根据地理位置唯一标识符geographyId查询geography对象
     * @param geographyId
     * @return
     */
    Geography findGeographyByGeographyId(int geographyId);

    /**
     *根据地理位置名称模糊查询geography列表
     * @param geographyName
     * @return
     */
    List<Geography> findGeographyByGeographyName(String geographyName);

    /**
     *根据地理位置精度sign查询geography列表
     * @param sign
     * @return
     */
    List<Geography> findGeographyBySign(int sign);

    /**
     *根据地理位置名称和地理位置精度查询geography列表
     * @param geographyName
     * @param sign
     * @return
     */
    List<Geography> findGeographyByNameAndSign(String geographyName, int sign);

    /**
     *根据herbInfoId查询草药生长在哪些地理位置
     * @param herbInfoId
     * @return
     */
    List<Geography> findGeographyByHerbInfoId(int herbInfoId);

    /**
     *插入一个geography对象
     * @param geography
     * @return
     */
    boolean insertGeography(Geography geography);

    /**
     *更新一个geography对象
     * @param geography
     * @return
     */
    boolean updateGeography(Geography geography);

    /**
     * 删除指定geographyId的geography记录
     * @param geographyId
     * @return
     */
    boolean deleteGeographyByGeographyId(int geographyId);

    /**
     * 写首页地理位置关系图数据文件
     */
    void writeGeographyDataFile(String path);
}
