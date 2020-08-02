package com.ego.cart.service;

import com.ego.cart.pojo.OrderCartPojo;
import com.ego.commons.pojo.CartPojo;
import com.ego.commons.pojo.EgoResult;

import java.util.List;

public interface CartService {
    /**
     * 往购物车中添加数据
     * @param id
     * @param num
     */
    void addCart(Long id,int num);

    /**
     * 显示购物车
     * @return
     */
    List<CartPojo> showCart();

    /**
     * 修改购物车中商品数量
     * @param id
     * @param num
     * @return
     */
    EgoResult updateCartNum(Long id,int num);

    /**
     * 删除购物车商品
     * @param id
     * @return
     */
    EgoResult deleteCart(Long id);

    /**
     * 显示订单页面的商品
     * @return
     */
    List<OrderCartPojo> showOrderCart(List<Long> ids);

    /**
     * 批量删除Redis中的购物车商品
     * @param userId
     * @param itemIds
     * @return
     */
    int deleteCartByIds(Long userId,Long[] itemIds);
}
