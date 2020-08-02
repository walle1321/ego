package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

import java.util.List;

public interface TbOrderDubboService {
    /**
     * 提交订单之后操作，对三个表新增
     * @param tbOrder
     * @param tbOrderItemList
     * @param tbOrderShipping
     * @return
     */
    int insertTbOrder(TbOrder tbOrder, List<TbOrderItem> tbOrderItemList, TbOrderShipping tbOrderShipping) throws DaoException;
}
