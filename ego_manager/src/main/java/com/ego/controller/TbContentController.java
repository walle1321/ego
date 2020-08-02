package com.ego.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContent;
import com.ego.service.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TbContentController {

    @Autowired
    private TbContentService tbContentService;

    /**
     * 通过categoryID查询所有子类目
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/content/query/list")
    public EasyUIDataGrid selectByCategoryId(long categoryId, int page, int rows){
        EasyUIDataGrid easyUIDataGrid = tbContentService.selectByCategoryId(categoryId, page, rows);
        return tbContentService.selectByCategoryId(categoryId, page, rows);

    }

    /**
     * 新增
     * @param tbContent
     * @return
     */
    @RequestMapping("/content/save")
    public EgoResult insert(TbContent tbContent){
        return tbContentService.insert(tbContent);
    }

    /**
     * 更改
     * @param tbContent
     * @return
     */
    @RequestMapping("/rest/content/edit")
    public EgoResult update(TbContent tbContent){
        return tbContentService.update(tbContent);
    }

    @RequestMapping("/content/delete")
    public EgoResult delete(long[] ids){
        return tbContentService.delete(ids);
    }
}
