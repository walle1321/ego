package com.ego.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamItem;
import com.ego.sender.Send;
import com.ego.service.TbItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbItemServiceImpl implements TbItemService {

    @DubboReference
    private TbItemDubboService tbItemDubboService;
    @Autowired
    private Send send;
    @Value("${ego.rabbitmq.item.insert}")
    private String itemQueue;
    @Value("${ego.rabbitmq.item.delete}")
    private String deleteItemQueue;

    @Override
    public EasyUIDataGrid showTbItem(int page, int rows) {

        List<TbItem> list = tbItemDubboService.selectByPage(page, rows);
        long total = tbItemDubboService.selectCount();
        return new EasyUIDataGrid(list,total);
    }

    @Override
    public EgoResult updateTbItemStatusByIds(long[] ids, int status) {
        try {
            int i = tbItemDubboService.updateTbItemByIds(ids, status);
            if (i==1){
                System.out.println(status);
                if (status == 1){

                    //新增到search,添加到队列
                    //商品上架就是新增
                    //将IDS数组转换为"*,*,"的字符串
                    send.send(itemQueue, StringUtils.join(ids,','));
                }else{
                    //下架和删除
                    send.send(deleteItemQueue, StringUtils.join(ids,','));
                }
                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("更改状态失败");
    }

    @Override
    public EgoResult saveTbItem(TbItem tbItem, String desc,String itemParams){

        Date date = new Date();
        long id = IDUtils.genItemId();

        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setCreated(date);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemId(id);

        TbItemParamItem tbItemParam = new TbItemParamItem();
        tbItemParam.setId(IDUtils.genItemId());
        tbItemParam.setCreated(date);
        tbItemParam.setUpdated(date);
        tbItemParam.setParamData(itemParams);
        tbItemParam.setItemId(id);

        try {
            int i = tbItemDubboService.insert(tbItem, tbItemDesc,tbItemParam);
            if (i>0){

                //新增到search,添加到队列
                send.send(itemQueue,id);

                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("新增失败");
    }

    @Override
    public EgoResult updateTbItemAndTbItemDesc(TbItem tbItem, String desc,String itemParams,long itemParamId) {

        Date date = new Date();
        tbItem.setUpdated(date);

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setUpdated(date);
        tbItemDesc.setItemDesc(desc);

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setUpdated(date);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setId(itemParamId);

        try {
            int index = tbItemDubboService.update(tbItem, tbItemDesc,tbItemParamItem);
            if (index>0){

                //新增到search,添加到队列
                send.send(itemQueue, tbItem.getId());

                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("更改失败");
    }
}
