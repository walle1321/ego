package com.ego.dubbo.service;

import com.ego.pojo.TbUser;

public interface TbUserDubboService {

    /**
     * 通过用户名，邮箱，手机号检查用户是否存在
     * 通过用户名，邮箱，手机号查询用户
     * @param tbUser
     * @return
     */
    TbUser selectByUser(TbUser tbUser);

    /**
     * 新增
     * @param tbUser
     * @return
     */
    int insert(TbUser tbUser);

    /**
     * 通过用户名密码查询
     * @param tbUser
     * @return
     */
    TbUser selectByNameAndPwd(TbUser tbUser);
}
