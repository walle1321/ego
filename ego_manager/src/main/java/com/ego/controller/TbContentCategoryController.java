package com.ego.controller;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;
import com.ego.service.TbContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TbContentCategoryController {

    @Autowired
    private TbContentCategoryService tbContentCategoryService;

    /**
     * 展示类目
     * @param id
     * @return
     */
    @RequestMapping("/content/category/list")
    private List<EasyUITree> selectByPid(@RequestParam(defaultValue = "0") long id){
        return tbContentCategoryService.selectByPid(id);
    }

    /**
     * 新建类目
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/content/category/create")
    private EgoResult insert(TbContentCategory tbContentCategory){
        return tbContentCategoryService.insert(tbContentCategory);
    }

    /**
     * 修改名字
     * @param tbContentCategory
     * @return
     */
    @RequestMapping("/content/category/update")
    private EgoResult update(TbContentCategory tbContentCategory){
        return tbContentCategoryService.updateNameById(tbContentCategory);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/content/category/delete/")
    private EgoResult updateStatus(long id){
        return tbContentCategoryService.delete(id);
    }
}
