package com.ego.dubbo.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbOrderItemMapper;
import com.ego.mapper.TbOrderMapper;
import com.ego.mapper.TbOrderShippingMapper;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@DubboService
public class TbOrderDubboServiceImpl implements TbOrderDubboService {
    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Override
    @Transactional()
    public int insertTbOrder(TbOrder tbOrder, List<TbOrderItem> list, TbOrderShipping tbOrderShipping) throws DaoException {
        int index = tbOrderMapper.insertSelective(tbOrder);
        if(index==1){
            int index2 = 0 ;
            int index22 = 0 ;
            for(TbOrderItem tbOrderItem : list){
                index2 +=tbOrderItemMapper.insert(tbOrderItem);
                Long itemId = Long.parseLong(tbOrderItem.getItemId());
                TbItem tbItemDB = tbItemMapper.selectByPrimaryKey(itemId);
                // 修改商品库存
                TbItem tbItem = new TbItem();
                tbItem.setId(Long.parseLong(tbOrderItem.getItemId()));
                tbItem.setNum(tbItemDB.getNum()-tbOrderItem.getNum());
                tbItem.setUpdated(tbOrder.getCreateTime());
                index22 += tbItemMapper.updateByPrimaryKeySelective(tbItem);
            }
            if(index2==list.size()&&index22==list.size()){
                int index3 = tbOrderShippingMapper.insertSelective(tbOrderShipping);
                if(index3==1){

                    return 1;
                }
            }
        }
        throw new DaoException("新增失败");
    }
}
