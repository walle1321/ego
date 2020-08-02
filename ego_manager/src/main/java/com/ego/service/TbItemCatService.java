package com.ego.service;

import com.ego.commons.pojo.EasyUITree;

import java.util.List;

public interface TbItemCatService {
    /**
     * 显示商品分类子类目
     * @param pid 当前类目的ID
     * @return 通过当前类目ID查询到的子类目，即查询父ID为当前类目ID的商品分类
     */
    List<EasyUITree> showTree(long pid);
}
