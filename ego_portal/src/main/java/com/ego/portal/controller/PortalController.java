package com.ego.portal.controller;

import com.ego.commons.utils.JsonUtils;
import com.ego.portal.service.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PortalController {

    @Autowired
    private TbContentService tbContentService;

    @RequestMapping("/")
    public String showIndex(Model model){
        model.addAttribute("ad1", JsonUtils.objectToJson(tbContentService.showBig()));
        return "index";
    }

    /**
     * 更新大广告到redis，被rabbitMQ_receive的HTTPClient调用
     * @return
     */
    @RequestMapping("/bigad")
    @ResponseBody
    public String RedisAddBigAd(){
        tbContentService.showBig2();
        System.out.println("Controller");
        return "ok";
    }

}
