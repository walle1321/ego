package com.ego.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamChild;
import com.ego.service.TbItemParamService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TbItemParamServiceImpl implements TbItemParamService {

    @DubboReference
    private TbItemParamDubboService tbItemParamDubboService;
    @DubboReference
    private TbItemCatDubboService tbItemCatDubboService;

    @Override
    public EasyUIDataGrid selectTbItemParam(int page, int rows) {

        List<TbItemParam> tbItemParams = tbItemParamDubboService.selectTbItemParam(page, rows);
        ArrayList<TbItemParamChild> childs = new ArrayList<>();
        for (TbItemParam tbItemParam :
                tbItemParams) {

            TbItemParamChild tbItemParamChild = new TbItemParamChild();
            BeanUtils.copyProperties(tbItemParam,tbItemParamChild);
            tbItemParamChild.setItemCatName(tbItemCatDubboService.selectTbItemCatById(tbItemParam.getItemCatId()).getName());
            childs.add(tbItemParamChild);
            System.out.println();

        }

        int count = tbItemParamDubboService.selectCount();

        return new EasyUIDataGrid(childs,count);
    }

    @Override
    public EgoResult selectTbItemById(long id) {
        TbItemParam tbItemParam = tbItemParamDubboService.selectTbItemById(id);
        return EgoResult.ok(tbItemParam);
    }

    @Override
    public EgoResult inset(TbItemParam tbItemParam) {

        long id = IDUtils.genItemId();
        Date date = new Date();
        tbItemParam.setCreated(date);
        tbItemParam.setUpdated(date);
        tbItemParam.setId(id);
        try {
            int index = tbItemParamDubboService.insert(tbItemParam);
            if (index>0){
                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("新增失败");
    }

    @Override
    public EgoResult delete(long[] ids) {
        try {
            int index = tbItemParamDubboService.delete(ids);
            if (index>0){
                return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("批量删除失败");
    }

}
