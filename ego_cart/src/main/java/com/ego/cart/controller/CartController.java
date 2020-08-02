package com.ego.cart.controller;

import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 往购物车中添加数据
     * @param id
     * @param num
     * @return
     */
    @RequestMapping("/cart/add/{id}.html")
    public String addCart(@PathVariable Long id,int num){
        cartService.addCart(id, num);
        return "cartSuccess";
    }

    /**
     * 显示购物车
     * @param model
     * @return
     */
    @RequestMapping("/cart/cart.html")
    public String showCart(Model model){
        model.addAttribute("cartList",cartService.showCart());
        return "cart";
    }

    /**
     * 修改购物车商品数量
     * @param id
     * @param num
     * @return
     */
    @RequestMapping(value = {"/cart/update/num/{id}/{num}.action","/service/cart/update/num/{id}/{num}"})
    @ResponseBody
    public EgoResult updateCartNum(@PathVariable Long id,@PathVariable int num){
        return cartService.updateCartNum(id, num);
    }


    /**
     * 删除商品
     * @param id
     * @return
     */
    @RequestMapping("/cart/delete/{id}.action")
    @ResponseBody
    public EgoResult updateCartNum(@PathVariable Long id){

        return cartService.deleteCart(id);
    }


    /**
     * 显示订单
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/cart/order-cart.html")
    //请求参数是:?id=159575547860068&id=159575388563078
    public String showOrderCart(@RequestParam List<Long> id,Model model){
        model.addAttribute("cartList",cartService.showOrderCart(id));
        return "order-cart";
    }

    /**
     * 批量的删除用户已经购买的购物车中的商品
     * @param userId
     * @param ids
     * @return
     */
    @RequestMapping("/cart/deleteByIds")
    @ResponseBody
    //请求参数是：?userId=159598520930496&ids=159575547860068,159575388563078
    public int deleteCartByIds(Long userId,Long[] ids){
        return cartService.deleteCartByIds(userId, ids);
    }
}
