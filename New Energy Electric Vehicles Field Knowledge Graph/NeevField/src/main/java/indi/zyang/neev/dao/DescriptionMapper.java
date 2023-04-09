package indi.zyang.neev.dao;

import indi.zyang.neev.entity.Description;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionMapper {
    /**
      * @Author Zyang
      * @Desctription
      * @Date 2023/4/6 18:53
      * @Param [desId]
      * @return indi.zyang.neev.entity.Description
      */
    Description findDescriptionByDesId(@Param("desId") int desId);
}
