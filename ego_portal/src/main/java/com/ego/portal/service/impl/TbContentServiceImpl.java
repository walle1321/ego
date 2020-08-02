package com.ego.portal.service.impl;

import com.ego.commons.pojo.BigAd;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbContentServiceImpl implements TbContentService {

    @DubboReference
    private TbContentDubboService tbContentDubboService;
    @Value("${ego.bigad.categoryid}")
    private long categoryId;

    @Override
    //如果缓存里有直接调用，不会去数据库更新实时数据，需要另外写代码对redis更新
    @Cacheable(cacheNames = "com.ego.potal",key = "'bigAd'")
    public List<BigAd> showBig() {
        List<TbContent> contentList = tbContentDubboService.selectByCategoryId(categoryId);
        List<BigAd> bigAdList = new ArrayList<>();
        for (TbContent tb :
                contentList) {
            BigAd bigAd = new BigAd();
            bigAd.setAlt("");
            bigAd.setHeight(240);
            bigAd.setHeightB(240);
            bigAd.setHref(tb.getUrl());
            bigAd.setSrc(tb.getPic());
            bigAd.setSrcB(tb.getPic2());
            bigAd.setWidth(670);
            bigAd.setWidthB(550);
            bigAdList.add(bigAd);
        }
        return bigAdList;
    }

    @Override
    //更新Redis中的大广告数据
    @CachePut(cacheNames = "com.ego.potal",key = "'bigAd'")
    public List<BigAd> showBig2() {
        List<TbContent> contentList = tbContentDubboService.selectByCategoryId(categoryId);
        List<BigAd> bigAdList = new ArrayList<>();
        for (TbContent tb :
                contentList) {
            BigAd bigAd = new BigAd();
            bigAd.setAlt("");
            bigAd.setHeight(240);
            bigAd.setHeightB(240);
            bigAd.setHref(tb.getUrl());
            bigAd.setSrc(tb.getPic());
            bigAd.setSrcB(tb.getPic2());
            bigAd.setWidth(670);
            bigAd.setWidthB(550);
            bigAdList.add(bigAd);
        }
        return bigAdList;
    }


}
