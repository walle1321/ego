package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItemParamItem;
import com.ego.pojo.TbItemParamItemExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService {

    @Autowired
    TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItemParamItem selectByItemId(long id) {

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        TbItemParamItemExample example = new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(id);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list!=null&&list.size()>0){
            tbItemParamItem = list.get(0);
        }

        return tbItemParamItem;
    }
}
