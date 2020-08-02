package com.ego.service;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryService {

    /**
     * 通过父ID查询子类目
     * @param id
     * @return
     */
    List<EasyUITree> selectByPid(long id);

    /**
     * 新增
     * @param tbContentCategory
     * @return
     */
    EgoResult insert(TbContentCategory tbContentCategory);

    /**
     * 更改名称
     * @param tbContentCategory
     * @return
     */
    EgoResult updateNameById(TbContentCategory tbContentCategory);

    /**
     * 逻辑删除，修改status
     * @param id
     * @return
     */
    EgoResult delete(long id);
}
