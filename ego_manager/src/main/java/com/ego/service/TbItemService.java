package com.ego.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItem;

public interface TbItemService {
    //展示指定页数和数量的商品
    EasyUIDataGrid showTbItem(int page,int rows);

    //更改商品的上架等状态
    EgoResult updateTbItemStatusByIds(long[] ids, int status);

    //新增商品（包括商品描述）
    EgoResult saveTbItem(TbItem tbItem,String desc,String itemParams);

    //修改商品信息
    EgoResult updateTbItemAndTbItemDesc(TbItem tbItem,String desc,String itemParams,long itemParamId);
}
