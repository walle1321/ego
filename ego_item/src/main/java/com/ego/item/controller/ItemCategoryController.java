package com.ego.item.controller;

import com.ego.item.pojo.ItemCategoryNav;
import com.ego.item.service.TbItemCategoryNavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemCategoryController {

    @Autowired
    private TbItemCategoryNavService tbItemCategoryNavService;

    /**
     * 显示所有类目 并加入到redis缓存
     * @return
     */
    @RequestMapping("/rest/itemcat/all")
    //跨域请求注解
    @CrossOrigin
    @ResponseBody
    public ItemCategoryNav showCategory(){

        return tbItemCategoryNavService.showCategory();

    }

    /**
     * 显示商品详情页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/item/{id}.html")
    public String showItem(@PathVariable Long id, Model model){

        model.addAttribute("item",tbItemCategoryNavService.showItem(id));

        return "item";
    }

    /**
     * 展示商品详情
     * @param id
     * @return
     */

    @RequestMapping("/item/desc/{id}.html")
    @ResponseBody
    public String showDesc(@PathVariable Long id){
        return tbItemCategoryNavService.showDesc(id);
    }

    /**
     * 显示商品规格参数信息
     * @param id
     * @return
     */
    @RequestMapping("/item/param/{id}.html")
    @ResponseBody
    public String showParamItem(@PathVariable Long id){
        return tbItemCategoryNavService.showParamItem(id);
    }

}
