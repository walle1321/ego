package com.ego.service;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemDesc;

public interface TbItemDescService {
    /**
     * 通过主键查询商品详细信息
     * @param id
     * @return
     */
    EgoResult selectTbItemDescById(long id);

}
