package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.TbItemDesc;

public interface TbItemDescDubboService {
    /**
     * 通过id查询商品详细描述信息
     * @param id
     * @return
     */
    TbItemDesc selectTbItemDescById(long id);
}
