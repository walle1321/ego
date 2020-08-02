package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.*;

import java.util.List;

public interface TbItemDubboService {

    /**
     * 通过PageHelper查询分页数据
     * @param pageSize  每页的条数
     * @param pageNumber  第多少页
     * @return
     */
    List<TbItem> selectByPage(int pageSize,int pageNumber);

    /**
     * 查询商品数量
     * @return
     */
    long selectCount();

    /**
     * 通过id批量更改商品状态
     * @param ids
     * @param status
     * @return
     */
    int updateTbItemByIds(long[] ids,int status) throws DaoException;

    /**
     * 新增商品和商品描述和商品类目
     * @param tbItem
     * @param tbItemDesc
     * @return
     */
    int insert(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParam) throws DaoException;

    /**
     * 更改商品和商品描述
     * @param tbItem
     * @param tbItemDesc
     * @return
     * @throws DaoException
     */
    int update(TbItem tbItem,TbItemDesc tbItemDesc,TbItemParamItem tbItemParamItem) throws DaoException;


    /**
     * 通过主键查询
     * @param id
     * @return
     */
    TbItem selectById(long id);
}
