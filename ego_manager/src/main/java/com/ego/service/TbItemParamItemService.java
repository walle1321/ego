package com.ego.service;

import com.ego.commons.pojo.EgoResult;

public interface TbItemParamItemService {
    /**
     * 通过itemID查询商品详细描述信息
     * @param id
     * @return
     */
    EgoResult selectByItemId(long id);
}
