package com.ego.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItem;
import com.ego.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TbItemController {

    @Autowired
    private TbItemService tbItemService;

    //根据指定页数和数量查询商品
    //分页显示商品
    @RequestMapping("/item/list")
    public EasyUIDataGrid showTbItem(int page,int rows){

        return tbItemService.showTbItem(page, rows);
    }

    /**
     * 商品下架
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/instock")
    public EgoResult reshelf(long[] ids){
        return tbItemService.updateTbItemStatusByIds(ids,2);
    }

    /**
     *商品上架
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/reshelf")
    public EgoResult instock(long[] ids){
        return tbItemService.updateTbItemStatusByIds(ids,1);
    }

    /**
     * 商品删除
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/delete")
    public EgoResult delete(long[] ids){
        return tbItemService.updateTbItemStatusByIds(ids,3);
    }

    @RequestMapping("/item/save")
    public EgoResult saveTbItem(TbItem tbItem,String desc,String itemParams){
        return tbItemService.saveTbItem(tbItem, desc,itemParams);
    }

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public EgoResult updateTbItemAndTbItemDesc(TbItem tbItem,String desc,String itemParams,long itemParamId){
        return tbItemService.updateTbItemAndTbItemDesc(tbItem, desc,itemParams,itemParamId);
    }
}
