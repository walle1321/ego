package com.ego.cart.service.impl;

import com.ego.cart.pojo.OrderCartPojo;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.CartPojo;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemDetails;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.commons.utils.ServletUtil;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Value("${ego.redis.item}")
    private String itemRedisKey;
    @Value("${ego.cookie.tempCart}")
    private String tempCartKey;
    @Value("${ego.redis.cartKey}")
    private String cartRedisKey;

    @Autowired
    private RedisTemplate redisTemplate;
    @DubboReference
    private TbItemDubboService tbItemDubboService;

    @Override
    public void addCart(Long id, int num) {

        TbUser tbUser = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");

        //有用户登陆，往redis中存值
        if (tbUser != null) {

            //用户购物车在redis中的Key,
            String cartKey = cartRedisKey + tbUser.getId();
            //list必须在这里创建，为了获得原先的list的值，！！
            List<CartPojo> list = new ArrayList<>();

            //验证是否是第一次创建购物车
            if (redisTemplate.hasKey(cartKey)) {
                //已经存在购物车
                //判断是否有该商品
                list = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
                for (CartPojo cp :
                        list) {
                    if (cp.getId().equals(id)) {
                        cp.setNum(cp.getNum() + num);
                        System.out.println(cartKey);
                        redisTemplate.opsForValue().set(cartKey, list);
                        return;
                    }
                }
            }
            //如果没有，则创建，并传入值，第一次创建购物车
            //因为之前的显示商品页面已经将内容存入到了redis中，直接在redis中获取商品信息
            //商品在redis的缓存Key
            String itemKey = itemRedisKey + id;
            TbItemDetails details = (TbItemDetails) redisTemplate.opsForValue().get(itemKey);

            CartPojo cartPojo = new CartPojo();
            cartPojo.setId(id);
            cartPojo.setImages(details.getImages());
            cartPojo.setPrice(details.getPrice());
            cartPojo.setTitle(details.getTitle());
            cartPojo.setNum(num);
            list.add(cartPojo);

            //将购物车的数据放入到redis中
            redisTemplate.opsForValue().set(cartKey, list);
            return;
        }

        //没有用户登陆,往cookie中存值
        Map<Long, CartPojo> map = new HashMap<>();
        //在cookie中查询是否有临时购物车数据
        String cookieValue = CookieUtils.getCookieValueBase64(ServletUtil.getRequest(), tempCartKey);
        if (Strings.isNotEmpty(cookieValue)) {
            //如果有数据
            map = JsonUtils.jsonToMap(cookieValue, Long.class, CartPojo.class);
            //判断是否有该商品
            if (map.containsKey(id)) {
                //如果有该商品
                CartPojo cartPojo = map.get(id);
                cartPojo.setNum(cartPojo.getNum() + num);
                //将商品放入到map中，查询和修改方便，比List
                CookieUtils.doSetCookieBase64(ServletUtil.getRequest(), ServletUtil.getResponse(), tempCartKey, JsonUtils.objectToJson(map), 2592000);
                return;
            }
        }
        //如果没有，则创建，并传入值
        //因为之前的显示商品页面已经将内容存入到了redis中，直接在redis中获取商品信息
        String key = itemRedisKey + id;
        TbItemDetails details = (TbItemDetails) redisTemplate.opsForValue().get(key);

        CartPojo cartPojo = new CartPojo();
        cartPojo.setId(id);
        cartPojo.setImages(details.getImages());
        cartPojo.setPrice(details.getPrice());
        cartPojo.setTitle(details.getTitle());
        cartPojo.setNum(num);

        //将商品放入到map中，查询和修改方便，比List
        map.put(id, cartPojo);

        //将map放入到cookie中，并设置cookie时间，记得转换为json
        CookieUtils.doSetCookieBase64(ServletUtil.getRequest(), ServletUtil.getResponse(), tempCartKey, JsonUtils.objectToJson(map), 2592000);

    }

    @Override
    public List<CartPojo> showCart() {

        //判断是否有用户登录，是否是临时购物车
        TbUser tbUser = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");
        //有用户登录，直接返回购物车
        if (tbUser != null) {
            String key = cartRedisKey + tbUser.getId();
            List<CartPojo> cartPojoList = (List<CartPojo>) redisTemplate.opsForValue().get(key);
            return cartPojoList;
        }


        //如果没有用户登陆，临时购物车如下代码：
        //在cookie中查询是否有临时购物车
        String cookieValue = CookieUtils.getCookieValueBase64(ServletUtil.getRequest(), tempCartKey);
        if (Strings.isNotEmpty(cookieValue)) {
            List<CartPojo> list = new ArrayList<>();
            //如果有数据
            Map<Long, CartPojo> map = JsonUtils.jsonToMap(cookieValue, Long.class, CartPojo.class);
            for (Long i :
                    map.keySet()) {
                list.add(map.get(i));
            }
            return list;
        }
        return null;
    }

    @Override
    public EgoResult updateCartNum(Long id, int num) {

        TbUser tbUser = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");

        //有用户登陆，往redis中存值
        if (tbUser != null) {

            //用户购物车在redis中的Key,
            String cartKey = cartRedisKey + tbUser.getId();
            //判断是否有该商品
            List<CartPojo> list = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
            for (CartPojo cp :
                    list) {
                if (cp.getId().equals(id)) {
                    cp.setNum(num);
                    redisTemplate.opsForValue().set(cartKey, list);
                    return EgoResult.ok();
                }
            }

        }


        //没有用户登陆
        String cookieValue = CookieUtils.getCookieValueBase64(ServletUtil.getRequest(), tempCartKey);
        Map<Long, CartPojo> map = JsonUtils.jsonToMap(cookieValue, Long.class, CartPojo.class);
        map.get(id).setNum(num);
        CookieUtils.doSetCookieBase64(ServletUtil.getRequest(), ServletUtil.getResponse(), tempCartKey, JsonUtils.objectToJson(map), 2592000);
        return EgoResult.ok();

    }

    @Override
    public EgoResult deleteCart(Long id) {

        TbUser tbUser = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");

        //有用户登陆，往redis中存值
        if (tbUser != null) {

            //用户购物车在redis中的Key,
            String cartKey = cartRedisKey + tbUser.getId();
            //判断是否有该商品
            List<CartPojo> list = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
            for (CartPojo cp :
                    list) {
                if (cp.getId().equals(id)) {
                    list.remove(cp);
                    redisTemplate.opsForValue().set(cartKey, list);
                    return EgoResult.ok();
                }
            }

        }

        //没有用户登录，临时购物车
        String cookieValue = CookieUtils.getCookieValueBase64(ServletUtil.getRequest(), tempCartKey);
        Map<Long, CartPojo> map = JsonUtils.jsonToMap(cookieValue, Long.class, CartPojo.class);
        map.remove(id);
        CookieUtils.doSetCookieBase64(ServletUtil.getRequest(), ServletUtil.getResponse(), tempCartKey, JsonUtils.objectToJson(map), 2592000);
        return EgoResult.ok();
    }

    @Override
    public List<OrderCartPojo> showOrderCart(List<Long> ids) {
        TbUser tbUser = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");
        String cartKey = cartRedisKey + tbUser.getId();
        List<CartPojo> list = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
        List<OrderCartPojo> orderCartPojoList = new ArrayList<>();
        for (Long id :
                ids) {
            for (CartPojo cp :
                    list) {
                if (cp.getId().equals(id)){
                    OrderCartPojo orderCartPojo = new OrderCartPojo();
                    TbItem tbItem = tbItemDubboService.selectById(cp.getId());
                    BeanUtils.copyProperties(cp,orderCartPojo);
                    if (tbItem.getNum()<cp.getNum()){
                        orderCartPojo.setEnough(false);
                    }else {
                        orderCartPojo.setEnough(true);
                    }
                    orderCartPojoList.add(orderCartPojo);
                    break;
                }
            }
        }

        return orderCartPojoList;
    }

    @Override
    public int deleteCartByIds(Long userId, Long[] itemIds) {
        try {
            System.out.println("我是CartServiceImpl");
            String cartKey = cartRedisKey + userId;
            List<CartPojo> list = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
            for (Long id :
                    itemIds) {
                for (CartPojo cp :
                        list) {
                    if (cp.getId().equals(id)) {
                        list.remove(cp);
                        break;
                    }
                }
            }
            redisTemplate.opsForValue().set(cartKey,list);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
