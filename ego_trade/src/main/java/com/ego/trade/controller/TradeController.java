package com.ego.trade.controller;

import com.ego.commons.pojo.OrderPojo;
import com.ego.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping("order/create.html")
    public String showTrade(OrderPojo orderPojo, Model model){
        System.out.println(orderPojo.toString());
        Map<String, Object> map = tradeService.creatOrder(orderPojo);
        if (map!=null){
            model.addAllAttributes(map);
            return "success";
        }
        model.addAttribute("message","订单创建失败");
        return "error/exception";
    }
}
