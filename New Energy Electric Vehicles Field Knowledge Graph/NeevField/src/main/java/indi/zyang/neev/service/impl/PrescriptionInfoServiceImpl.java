package indi.zyang.neev.service.impl;

import indi.zyang.neev.dao.BookMapper;
import indi.zyang.neev.dao.DiseaseMapper;
import indi.zyang.neev.dao.HerbMapper;
import indi.zyang.neev.dao.PrescriptionInfoMapper;
import indi.zyang.neev.entity.*;
import indi.zyang.neev.service.PrescriptionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionInfoServiceImpl implements PrescriptionInfoService {
    @Autowired
    PrescriptionInfoMapper prescriptionInfoMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    DiseaseMapper diseaseMapper;
    @Autowired
    HerbMapper herbMapper;


    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByPage(int page, int size) {
        int start = (size - 1) * page;
        return prescriptionInfoMapper.findPrescriptionInfoByPage(start, size);
    }

    @Override
    public PrescriptionInfo findPrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId) {
        return prescriptionInfoMapper.findPrescriptionInfoByPrescriptionInfoId(prescriptionInfoId);
    }

    @Override
    public PrescriptionInfo findFullPrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId) {
        PrescriptionInfo prescriptionInfo = prescriptionInfoMapper.findPrescriptionInfoByPrescriptionInfoId(prescriptionInfoId);
        prescriptionInfo.setBookList(bookMapper.findBookByPrescriptionInfoId(prescriptionInfoId));
        prescriptionInfo.setDiseaseList(diseaseMapper.findDiseaseByPrescriptionInfoId(prescriptionInfoId));
        prescriptionInfo.setHerbList(herbMapper.findHerbByPrescriptionInfoId(prescriptionInfoId));
        return prescriptionInfo;
    }

    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByPrescriptionId(int prescriptionId) {
        return prescriptionInfoMapper.findPrescriptionInfoByPrescriptionId(prescriptionId);
    }

    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByPrescriptionName(String prescriptionName) {
        return prescriptionInfoMapper.findPrescriptionInfoByPrescriptionName(prescriptionName);
    }

    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByBookId(int bookId) {
        return prescriptionInfoMapper.findPrescriptionInfoByBookId(bookId);
    }

    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByDiseaseId(int diseaseId) {
        return prescriptionInfoMapper.findPrescriptionInfoByDiseaseId(diseaseId);
    }

    @Override
    public List<PrescriptionInfo> findPrescriptionInfoByHerbId(int herbId) {
        return prescriptionInfoMapper.findPrescriptionInfoByHerbId(herbId);
    }

    @Override
    public boolean insertPrescriptionInfo(PrescriptionInfo prescriptionInfo) {
        int count = prescriptionInfoMapper.insertPrescriptionInfo(prescriptionInfo);
        return count == 1;
    }

    @Override
    public boolean updatePrescriptionInfo(PrescriptionInfo prescriptionInfo) {
        int count = prescriptionInfoMapper.updatePrescriptionInfo(prescriptionInfo);
        return count == 1;
    }

    @Override
    public boolean deletePrescriptionInfoByPrescriptionInfoId(int prescriptionInfoId) {
        int count = prescriptionInfoMapper.deletePrescriptionInfoByPrescriptionInfoId(prescriptionInfoId);
        return count == 1;
    }

    @Override
    public boolean deletePrescriptionInfoByPrescriptionId(int prescriptionId) {
        int count = prescriptionInfoMapper.deletePrescriptionInfoByPrescriptionId(prescriptionId);
        return count != 0;
    }
}
