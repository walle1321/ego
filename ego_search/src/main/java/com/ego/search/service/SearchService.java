package com.ego.search.service;

import java.util.Map;

public interface SearchService {

    /**
     * 在solr中查询，根据查询词，页面展示数，多少页
     * @param q
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> search(String q,int page,int size);

    /**
     * 批量的往solr中添加查询库
     * @param ids
     * @return
     */
    int insert(long[] ids);

    /**
     * 批量删除solr
     * @param ids
     * @return
     */
    int delete(String[] ids);

}
