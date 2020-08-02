package com.ego.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItemParam;
import com.ego.service.TbItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemParamController {

    @Autowired
    private TbItemParamService tbItemParamService;

    /**
     * 显示商品类目信息
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/param/list")
    @ResponseBody
    public EasyUIDataGrid selectIbItemParam(int page,int rows){
        return tbItemParamService.selectTbItemParam(page, rows);
    }

    /**
     * 再更改商品类目页面显示
     * @param id
     * @return
     */
    @RequestMapping("/item/param/query/itemcatid/{id}")
    @ResponseBody
    public EgoResult selectTbItemById(@PathVariable long id){
        return tbItemParamService.selectTbItemById(id);
    }

    /**
     * 新增
     * @param tbItemParam
     * @param
     * @return
     */
    @RequestMapping("/item/param/save/{cid}")
    @ResponseBody
    public EgoResult insert(TbItemParam tbItemParam,@PathVariable long cid){
        tbItemParam.setItemCatId(cid);
        return tbItemParamService.inset(tbItemParam);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/item/param/delete")
    @ResponseBody
    public EgoResult delete(long[] ids){
        return tbItemParamService.delete(ids);
    }
}
