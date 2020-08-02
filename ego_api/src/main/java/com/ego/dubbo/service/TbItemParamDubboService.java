package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.TbItemParam;

import java.util.List;

public interface TbItemParamDubboService {
    /**
     * PageInfo分页查询商品规格参数
     * @param page
     * @param rows
     * @return
     */
    List<TbItemParam> selectTbItemParam(int page,int rows);

    /**
     * 查询总条数
     * @return
     */
    int selectCount();

    /**
     * 通过商品类目ID查询商品规格模板信息
     * @param id
     * @return
     */
    TbItemParam selectTbItemById(long id);

    /**
     * 新增
     * @param tbItemParam
     * @return
     */
    int insert(TbItemParam tbItemParam) throws DaoException;

    /**
     * 批量删除
     * @param
     * @return
     * @throws DaoException
     */
    int delete(long[] ids) throws DaoException;
}
