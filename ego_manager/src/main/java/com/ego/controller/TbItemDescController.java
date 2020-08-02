package com.ego.controller;

import com.ego.commons.pojo.EgoResult;
import com.ego.service.TbItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemDescController {

    @Autowired
    private TbItemDescService tbItemDescService;

    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody
    public EgoResult showTbItemDesc(@PathVariable long id){
        return tbItemDescService.selectTbItemDescById(id);
    }
}
