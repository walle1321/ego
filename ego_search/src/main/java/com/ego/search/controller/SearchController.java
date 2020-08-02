package com.ego.search.controller;

import com.ego.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     *  在solr中查询，根据查询词，页面展示数，多少页
     * @param q
     * @param model
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/search.html")
    public String showSearch(String q, Model model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "12") int size){
        model.addAllAttributes(searchService.search(q,page,size));
        return "search";
    }

    /**
     * 通过HTTPClient进行调用从而对search进行新增
     * @param ids
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public int insetSearch(long[] ids){
        return searchService.insert(ids);
    }

    /**
     * 通过HTTPClient进行调用从而对search进行新增
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public int deleteSearch(String[] ids){
        return searchService.delete(ids);
    }

}
