package com.ego.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.commons.pojo.EasyUIDataGrid;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.sender.Send;
import com.ego.service.TbContentService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbContentServiceImpl implements TbContentService {

    @DubboReference
    private TbContentDubboService tbContenDubboService;

    @Autowired
    private Send send;
    // 获取内容队列名称
    @Value("${ego.rabbitmq.content.queueName}")
    private String contentQueue;
    @Value("${ego.bigad.categoryid}")
    private Long bigadId;

    @Override
    public EasyUIDataGrid selectByCategoryId(long categoryId, int page, int rows) {
        List<TbContent> select = tbContenDubboService.select(categoryId, page, rows);
        long count = tbContenDubboService.selectCountByCategoryId(categoryId);
        return new EasyUIDataGrid(select,count);

    }

    @Override
    public EgoResult insert(TbContent tbContent) {
        Date date = new Date();
        tbContent.setId(IDUtils.genItemId());
        tbContent.setUpdated(date);
        tbContent.setCreated(date);
        int index = tbContenDubboService.insert(tbContent);
        if (index>0){

            //消息队列新增,让recevie获得消息然后对redis进行数据更改等操作
            if(tbContent.getCategoryId().equals(bigadId)){
                send.send(contentQueue,"async");
            }


            return EgoResult.ok();
        }
        return EgoResult.error("新增失败");
    }

    @Override
    public EgoResult update(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        int update = tbContenDubboService.update(tbContent);
        if (update>0){

            //消息队列更改,让recevie获得消息然后对redis进行数据更改等操作
            if(tbContent.getCategoryId().equals(bigadId)){
                send.send(contentQueue,"async");
            }

            return EgoResult.ok();
        }
        return EgoResult.error("更改失败");
    }

    @Override
    public EgoResult delete(long[] ids) {

        boolean flag = false;
        for (long id :
                ids) {
            if (tbContenDubboService.selectById(id).getCategoryId() == bigadId) {
                flag = true;
                break;
            }
        }

        try {
            int delete = tbContenDubboService.delete(ids);
            if (delete>0){

                //消息队列更改,让recevie获得消息然后对redis进行数据更改等操作
                //第一个参数是消息队列，第二个参数无所谓
                send.send(contentQueue,"async");

                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("删除失败");
    }
}
