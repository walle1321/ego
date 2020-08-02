package com.ego.dubbo.service;

import com.ego.pojo.TbItemParamItem;

public interface TbItemParamItemDubboService {

    /**
     * 通过itemID查询商品详细规格参数
     * @param id
     * @return
     */
    TbItemParamItem selectByItemId(long id);
}
