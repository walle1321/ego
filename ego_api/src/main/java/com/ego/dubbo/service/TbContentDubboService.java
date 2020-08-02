package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.TbContent;

import java.util.List;

public interface TbContentDubboService {

    /**
     * 分页查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    List<TbContent> select(long categoryId,int page,int rows);

    /**
     * 查询总数
     * @param categoryId
     * @return
     */
    long selectCountByCategoryId(long categoryId);

    /**
     * 单个新增
     * @param tbContent
     * @return
     */
    int insert(TbContent tbContent);

    /**
     * 更改
     * @param tbContent
     * @return
     */
    int update(TbContent tbContent);

    /**
     * 通过ID批量删除
     * @param ids
     * @return
     */
    int delete(long[] ids) throws DaoException;

    /**
     * 通过categoryID查询所有旗下的页面广告
     * @param id
     * @return
     */
    List<TbContent> selectByCategoryId(long id);

    /**
     * 通过主键查询
     * @return
     */
    TbContent selectById(long id);
}
