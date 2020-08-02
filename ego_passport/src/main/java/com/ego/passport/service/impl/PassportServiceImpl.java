package com.ego.passport.service.impl;

import com.ego.commons.pojo.CartPojo;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.commons.utils.ServletUtil;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.passport.service.PassportService;
import com.ego.pojo.TbUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;

@Service
public class PassportServiceImpl implements PassportService {

    @DubboReference
    private TbUserDubboService tbUserDubboService;
    @Value("${ego.cookie.tempCart}")
    private String tempCartKey;
    @Value("${ego.redis.cartKey}")
    private String cartRedisKey;


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public EgoResult check(TbUser tbUser) {
        TbUser user = tbUserDubboService.selectByUser(tbUser);
        if (user==null){
            return EgoResult.ok();
        }
        return EgoResult.error("用户已存在");
    }

    @Override
    public EgoResult insert(TbUser tbUser) {

        tbUser.setId(IDUtils.genItemId());
        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        String pwd = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(pwd);

        int insert = tbUserDubboService.insert(tbUser);
        if (insert>0){
            return EgoResult.ok();
        }
        return EgoResult.error("注册失败");
    }

    @Override
    public EgoResult login(TbUser tbUser) {
        String pwd = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(pwd);
        TbUser user = tbUserDubboService.selectByNameAndPwd(tbUser);
        if (user!=null){
            //判断出来有该用户，将临时购物车和用户购物车合并
            //在cookie中取出临时购物车数据，添加到redis中用户购物车，注意要合并商品数量
            //在cookie中查询是否有临时购物车数据，没有直接返回EgoResult，有的话合并
            String cookieValue = CookieUtils.getCookieValueBase64(ServletUtil.getRequest(), tempCartKey);
            if (Strings.isNotEmpty(cookieValue)) {
                //cookie中有数据，取出来临时购物车数据
                Map<Long, CartPojo> map = JsonUtils.jsonToMap(cookieValue, Long.class, CartPojo.class);
                //redis中取出用户购物车数据
                String cartKey = cartRedisKey + user.getId();
                List<CartPojo> cartPojoList = (List<CartPojo>) redisTemplate.opsForValue().get(cartKey);
                //如果redis中用户购物车有数据，则和临时购物车比较，否则直接添加到购物车
                boolean flag = false;
                if (cartPojoList!=null&&cartPojoList.size()>0){
                    flag = true;
                }else {
                    //如果不新建，回报空指针错误
                    cartPojoList = new ArrayList<CartPojo>();
                }
                //循环遍历添加到用户购物车
                for (Long key :
                        map.keySet()) {
                    boolean isEists = false;
                    if (flag){
                        for (CartPojo cp :
                                cartPojoList) {
                            if (cp.getId().equals(map.get(key).getId())){
                                cp.setNum(cp.getNum()+map.get(key).getNum());
                                isEists = true;
                                break;
                            }
                        }
                    }
                    //如果原始数据没有重复的，则直接添加，如果有重复的，不用再次添加
                    if (!isEists){
                        cartPojoList.add(map.get(key));
                    }
                }

                //将购物车的数据放入到redis中
                redisTemplate.opsForValue().set(cartKey, cartPojoList);
                // 删除临时购物车
                CookieUtils.deleteCookie(ServletUtil.getRequest(),ServletUtil.getResponse(),tempCartKey);
            }

            //把查询出来的用户信息放入到EgoResult中，放到作用域中
            return EgoResult.ok(user);
        }
        return EgoResult.error("用户名或密码错误");
    }
}


