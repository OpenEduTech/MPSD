package com.example.poem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.poem.Dto.PoemDto;
import com.example.poem.Dto.StarDto;
import com.example.poem.Vo.StarVo;
import com.example.poem.commonresponse.CommonResponse;
import com.example.poem.enums.PoemEnum;
import com.example.poem.mapper.SongciMapper;
import com.example.poem.mapper.SongshiMapper;
import com.example.poem.mapper.TangshiMapper;
import com.example.poem.mapper.YuanquMapper;
import com.example.poem.pojo.*;
import com.example.poem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FindServiceImpl implements FindService {

    @Autowired
    SongshiService songshiService;
    @Autowired
    SongciService songciService;
    @Autowired
    YuanquService yuanquService;
    @Autowired
    TangshiService tangshiService;
    @Autowired
    AuthorsInfoService authorsInfoService;
    @Autowired
    SongciMapper songciMapper;
    @Autowired
    SongshiMapper songshiMapper;
    @Autowired
    TangshiMapper tangshiMapper;
    @Autowired
    YuanquMapper yuanquMapper;

    @Override
    public CommonResponse getPoems(PoemDto poemDto) {

        String keyWord = new String();
        if (Objects.isNull(poemDto)) {
            return null;
        }

        if (poemDto.getPoemEnum().equals(PoemEnum.SONGCI)){

            LambdaQueryWrapper<Songci> queryWrapper = new LambdaQueryWrapper<>();

            if (Objects.nonNull(poemDto.getKeyWord())){
                keyWord = poemDto.getKeyWord();
                queryWrapper.like(Songci::getTitle,keyWord).or().like(Songci::getAuthor,keyWord);
            }

            Page<Songci> page = new Page(poemDto.getPageNum(), poemDto.getPageSize());
            IPage<Songci> songciPage = songciService.page(page, queryWrapper);

            return CommonResponse.success(songciPage);

        }
        else if (poemDto.getPoemEnum().equals(PoemEnum.SONGSHI)){

            LambdaQueryWrapper<Songshi> queryWrapper = new LambdaQueryWrapper<>();

            if (Objects.nonNull(poemDto.getKeyWord())){
                keyWord = poemDto.getKeyWord();
                queryWrapper.like(Songshi::getTitle,keyWord).or().like(Songshi::getAuthor,keyWord);
            }

            Page<Songshi> page = new Page(poemDto.getPageNum(), poemDto.getPageSize());
            IPage<Songshi> songshiPage = songshiService.page(page, queryWrapper);

            return CommonResponse.success(songshiPage);
        }
        else if (poemDto.getPoemEnum().equals(PoemEnum.TANGSHI)){

            LambdaQueryWrapper<Tangshi> queryWrapper = new LambdaQueryWrapper<>();

            if (Objects.nonNull(poemDto.getKeyWord())){
                keyWord = poemDto.getKeyWord();
                queryWrapper.like(Tangshi::getTitle,keyWord).or().like(Tangshi::getAuthor,keyWord);
            }

            Page<Tangshi> page = new Page(poemDto.getPageNum(), poemDto.getPageSize());
            IPage<Tangshi> tangshiPage = tangshiService.page(page, queryWrapper);

            return CommonResponse.success(tangshiPage);
        }

        else if (poemDto.getPoemEnum().equals(PoemEnum.YUANQU)){
            LambdaQueryWrapper<Yuanqu> queryWrapper = new LambdaQueryWrapper<>();

            if (Objects.nonNull(poemDto.getKeyWord())){
                keyWord = poemDto.getKeyWord();
                queryWrapper.like(Yuanqu::getTitle,keyWord).or().like(Yuanqu::getAuthor,keyWord);
            }

            Page<Yuanqu> page = new Page(poemDto.getPageNum(), poemDto.getPageSize());
            IPage<Yuanqu> yuanquPage = yuanquService.page(page, queryWrapper);

            return CommonResponse.success(yuanquPage);

        }
        else {
            return null;
        }

    }

    @Override
    public AuthorsInfo getAuthorInfo(String name) {
        List<AuthorsInfo> list = authorsInfoService.lambdaQuery().eq(AuthorsInfo::getName, name).list();

        if (list.size() == 0){
            return null;
        }

        return list.get(0);
    }

    @Override
    public StarVo getStar(StarDto starDto) {

        if (Objects.isNull(starDto)){
            return new StarVo();
        }
        StarVo starVo = new StarVo();

        if (starDto.getPoemEnum().equals(PoemEnum.SONGSHI)){
            Songshi one = songshiService.lambdaQuery().eq(Songshi::getId, starDto.getId()).one();
            one.setStar(one.getStar() + 1);
            songshiService.updateById(one);
            starVo.setStar(one.getStar());
            starVo.setId(one.getId());
            return starVo;

        }
        else if (starDto.getPoemEnum().equals(PoemEnum.SONGCI)){
            Songci one = songciService.lambdaQuery().eq(Songci::getId, starDto.getId()).one();
            one.setStar(one.getStar() + 1);
            songciService.updateById(one);
            starVo.setStar(one.getStar());
            starVo.setId(one.getId());
            return starVo;
        }
        else if (starDto.getPoemEnum().equals(PoemEnum.TANGSHI)){
            Tangshi one = tangshiService.lambdaQuery().eq(Tangshi::getId, starDto.getId()).one();
            one.setStar(one.getStar() + 1);
            tangshiService.updateById(one);
            starVo.setStar(one.getStar());
            starVo.setId(one.getId());
            return starVo;
        }
        else if (starDto.getPoemEnum().equals(PoemEnum.YUANQU)){
            Yuanqu one = yuanquService.lambdaQuery().eq(Yuanqu::getId, starDto.getId()).one();
            one.setStar(one.getStar() + 1);
            yuanquService.updateById(one);
            starVo.setStar(one.getStar());
            starVo.setId(one.getId());
            return starVo;
        }
        else {
            return new StarVo();
        }
    }

    @Override
    public Map<Object, Long> getRank() {

        Map<Object, Long> titleMap = new LinkedHashMap<>();
        List<Map<String, Object>> titleList = songciMapper.selectMaps(new QueryWrapper<Songci>().select("title", "count(*) as cnt")
                .groupBy("title").orderByDesc("cnt").last("limit 5"));

        for (Map<String, Object> map : titleList) {
            Object title = map.get("title");
            Long cnt = (Long) map.get("cnt");
            titleMap.put(title, cnt);
        }
        List<Map<String, Object>> AtitleList = songshiMapper.selectMaps(new QueryWrapper<Songshi>().select("title", "count(*) as cnt")
                .groupBy("title").orderByDesc("cnt").last("limit 5"));

        for (Map<String, Object> map : AtitleList) {
            Object title = map.get("title");
            Long cnt = (Long) map.get("cnt");
            titleMap.put(title, cnt);
        }
        List<Map<String, Object>> BtitleList = tangshiMapper.selectMaps(new QueryWrapper<Tangshi>().select("title", "count(*) as cnt")
                .groupBy("title").orderByDesc("cnt").last("limit 5"));

        for (Map<String, Object> map : BtitleList) {
            Object title = map.get("title");
            Long cnt = (Long) map.get("cnt");
            titleMap.put(title, cnt);
        }
        List<Map<String, Object>> CtitleList = yuanquMapper.selectMaps(new QueryWrapper<Yuanqu>().select("title", "count(*) as cnt")
                .groupBy("title").orderByDesc("cnt").last("limit 5"));

        for (Map<String, Object> map : CtitleList) {
            Object title = map.get("title");
            Long cnt = (Long) map.get("cnt");
            titleMap.put(title, cnt);
        }

        System.out.println(titleMap);

        Map<Object, Long> result = titleMap.entrySet().stream()
                .sorted(Map.Entry.<Object, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        System.out.println(result);

        return result;
    }

    @Override
    public Map<Object, Long> getQuantity(PoemEnum poemEnum) {

        Map<Object, Long> titleMap = new LinkedHashMap<>();
        if (poemEnum.equals(PoemEnum.SONGCI)){
            List<Map<String, Object>> titleList = songciMapper.selectMaps(new QueryWrapper<Songci>().select("author", "count(*) as cnt")
                    .groupBy("author").orderByDesc("cnt").last("limit 5").ne("author", "无名氏").isNotNull("author"));

            for (Map<String, Object> map : titleList) {
                Object title = map.get("author");
                Long cnt = (Long) map.get("cnt");
                titleMap.put(title, cnt);
            }
        }
        else if (poemEnum.equals(PoemEnum.SONGSHI)){
            List<Map<String, Object>> AtitleList = songshiMapper.selectMaps(new QueryWrapper<Songshi>().select("author", "count(*) as cnt")
                    .groupBy("author").orderByDesc("cnt").last("limit 5").ne("author", "无名氏").isNotNull("author"));

            for (Map<String, Object> map : AtitleList) {
                Object title = map.get("author");
                Long cnt = (Long) map.get("cnt");
                titleMap.put(title, cnt);
            }
        }
        else if (poemEnum.equals(PoemEnum.TANGSHI)){
            List<Map<String, Object>> BtitleList = tangshiMapper.selectMaps(new QueryWrapper<Tangshi>().select("author", "count(*) as cnt")
                    .groupBy("author").orderByDesc("cnt").last("limit 5").ne("author", "无名氏").isNotNull("author"));

            for (Map<String, Object> map : BtitleList) {
                Object title = map.get("author");
                Long cnt = (Long) map.get("cnt");
                titleMap.put(title, cnt);
            }
        }
        else if (poemEnum.equals(PoemEnum.YUANQU)){
            List<Map<String, Object>> CtitleList = yuanquMapper.selectMaps(new QueryWrapper<Yuanqu>().select("author", "count(*) as cnt")
                    .groupBy("author").orderByDesc("cnt").last("limit 5").ne("author", "无名氏").isNotNull("author"));

            for (Map<String, Object> map : CtitleList) {
                Object title = map.get("author");
                Long cnt = (Long) map.get("cnt");
                titleMap.put(title, cnt);
            }
        }

        return titleMap;
    }


    public final <T> Page<T> emptyPage() {
        Page<T> page = new Page<>();
        page.setTotal(0);
        page.setCurrent(0);
        page.setRecords((List<T>) Collections.emptyList());
        page.setPages(0);
        page.setSize(0);
        return page;
    }

}
