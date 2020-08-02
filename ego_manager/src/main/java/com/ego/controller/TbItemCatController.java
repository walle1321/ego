package com.ego.controller;

import com.ego.commons.pojo.EasyUITree;
import com.ego.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TbItemCatController {

    @Autowired
    private TbItemCatService tbItemCatService;

    @RequestMapping("/item/cat/list")
    public List<EasyUITree> showTree(@RequestParam(defaultValue = "0")long id){
        return tbItemCatService.showTree(id);
    }

}
