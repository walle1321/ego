package com.ego.dubbo.service;

import com.ego.commons.exception.DaoException;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryDubboService {

    /**
     * 通过父ID查询子项目
     * @param pid
     * @return
     */
    List<TbContentCategory> selectByPid(long pid);

    /**
     * 新增
     * @param tbContentCategory
     * @return
     */
    int insert(TbContentCategory tbContentCategory) throws DaoException;

    /**
     * 修改名字
     * @param tbContentCategory
     * @return
     */
    int updateNameById(TbContentCategory tbContentCategory);

    /**
     * 逻辑删除，更改status为0
     * @param id
     * @return
     */
    int deleteById(long id) throws DaoException;

}
