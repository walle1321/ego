package com.ego.item.service;

import com.ego.commons.pojo.TbItemDetails;
import com.ego.item.pojo.ItemCategoryNav;
import com.ego.pojo.TbItemDesc;


public interface TbItemCategoryNavService {

    /**
     * 在门户网站跨域请求展示商品分类
     * @return
     */
    ItemCategoryNav showCategory();

    /**
     * 通过id显示商品页面
     * @param id
     * @return
     */
    TbItemDetails showItem(Long id);

    /**
     * 显示详细信息
     * @param itemId
     * @return
     */
    String showDesc(Long itemId);

    /**
     * 显示商品分类信息
     * @param id
     * @return
     */
    String showParamItem(long id);
}
