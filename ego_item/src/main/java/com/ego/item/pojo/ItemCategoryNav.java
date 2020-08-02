package com.ego.item.pojo;

import java.util.List;


//返回的汇总，大类
public class ItemCategoryNav {
    public ItemCategoryNav(List<Object> data) {
        this.data = data;
    }

    public ItemCategoryNav() {
    }

    private List<Object> data;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
