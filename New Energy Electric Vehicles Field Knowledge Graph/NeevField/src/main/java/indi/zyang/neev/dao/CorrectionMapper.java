package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Correction;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectionMapper {
    /**
     * 插入一个Correction对象
     * @param correction
     * @return
     */
    int insertCorrection(Correction correction);
}
