package com.ego.service.impl;

import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.pojo.TbItemDesc;
import com.ego.service.TbItemDescService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class TbItemDescServiceImpl implements TbItemDescService {

    @DubboReference
    private TbItemDescDubboService tbItemDescDubboService;
    @Override
    public EgoResult selectTbItemDescById(long id) {
        TbItemDesc tbItemDesc = tbItemDescDubboService.selectTbItemDescById(id);
        return EgoResult.ok(tbItemDesc);
    }
}
