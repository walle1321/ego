package com.ego.service.impl;

import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.pojo.TbItemParamItem;
import com.ego.service.TbItemParamItemService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {

    @DubboReference
    private TbItemParamItemDubboService tbItemParamItemDubboService;

    @Override
    public EgoResult selectByItemId(long id) {
        TbItemParamItem tbItemParamItem = tbItemParamItemDubboService.selectByItemId(id);
        System.out.println(tbItemParamItem.getParamData());
        if (tbItemParamItem!=null){
            return EgoResult.ok(tbItemParamItem);
        }
        return EgoResult.error("添加失败");
    }
}
