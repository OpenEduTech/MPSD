package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.DescriptionMapper;
import indi.zyang.neev.entity.Description;
import indi.zyang.neev.service.DescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DescriptionServiceImpl implements DescriptionService {
    @Autowired
    DescriptionMapper descriptionMapper;

    @Override
    public Description findDescriptionByDesId(int desId) {
        return descriptionMapper.findDescriptionByDesId(desId);
    }
}
