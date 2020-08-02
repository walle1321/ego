package com.ego.dubbo.service;

import com.ego.pojo.TbItemCat;

import java.util.List;

public interface TbItemCatDubboService {

    /**
     * 通过父ID查询所有子类目
     * @param pid
     * @return
     */
    List<TbItemCat> selectItemCatByPid(long pid);

    /**
     * 通过ID查询该商品类目
     * @param id
     * @return
     */
    TbItemCat selectTbItemCatById(long id);

}
