package com.ego.controller;

import com.ego.commons.pojo.EgoResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    @RequestMapping("/")
    public String showLogin(){
        return "login";
    }

    @RequestMapping("/main")
    public String showIndex(){
        return "index";
    }

    //显示主界面
    @RequestMapping("/loginSuccess")
    @ResponseBody
    public EgoResult loginSuccess(){
        return  EgoResult.ok();
    }

    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }

    //显示更改商品信息页面
    @RequestMapping("/rest/page/item-edit")
    public String showDesc(){
        return "item-edit";
    }

}
