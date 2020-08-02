package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.mapper.TbItemCatMapper;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemCatExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class TbItemCatDubboServiceImpl implements TbItemCatDubboService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> selectItemCatByPid(long pid) {
        TbItemCatExample catExample = new TbItemCatExample();
        catExample.createCriteria().andParentIdEqualTo(pid);
        List<TbItemCat> catList = tbItemCatMapper.selectByExample(catExample);
        return catList;
    }

    @Override
    public TbItemCat selectTbItemCatById(long id) {

        return tbItemCatMapper.selectByPrimaryKey(id);
    }
}
