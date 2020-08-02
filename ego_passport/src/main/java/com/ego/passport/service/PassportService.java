package com.ego.passport.service;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

public interface PassportService {

    /**
     * 通过用户名，邮箱，手机号检查用户是否存在
     * @param tbUser
     * @return
     */
    EgoResult check(TbUser tbUser);

    /**
     * 注册
     * @param tbUser
     * @return
     */
    EgoResult insert(TbUser tbUser);

    /**
     * 登陆
     * @param tbUser
     * @return
     */
    EgoResult login(TbUser tbUser);
}
