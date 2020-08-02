package com.ego.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContent;

import java.util.List;

public interface TbContentService {
    /**
     * 分表查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid selectByCategoryId(long categoryId, int page, int rows);

    /**
     * 单个新增
     * @param tbContent
     * @return
     */
    EgoResult insert(TbContent tbContent);

    /**
     * 更改
     * @param tbContent
     * @return
     */
    EgoResult update(TbContent tbContent);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    EgoResult delete(long[] ids);

}
