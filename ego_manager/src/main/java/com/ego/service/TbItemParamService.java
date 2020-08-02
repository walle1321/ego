package com.ego.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;

public interface TbItemParamService {
    /**
     * 查询所有的商品类目和商品规格参数
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid selectTbItemParam(int page,int rows);

    /**
     * 通过商品类目ID查询商品规格模板信息
     * @param id
     * @return
     */
    EgoResult selectTbItemById(long id);

    /**
     * 新增
     * @param tbItemParam
     * @return
     */
    EgoResult inset(TbItemParam tbItemParam);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    EgoResult delete(long[] ids);

}
