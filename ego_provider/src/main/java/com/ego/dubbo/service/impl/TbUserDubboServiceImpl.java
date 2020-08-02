package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbUserDubboService;
import com.ego.mapper.TbUserMapper;
import com.ego.pojo.TbUser;
import com.ego.pojo.TbUserExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class TbUserDubboServiceImpl implements TbUserDubboService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser selectByUser(TbUser tbUser) {

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        if (tbUser.getUsername()!=null){
            criteria.andUsernameEqualTo(tbUser.getUsername());
        }else if (tbUser.getEmail()!=null){
            criteria.andEmailEqualTo(tbUser.getEmail());
        }else if (tbUser.getPhone()!=null){
            criteria.andPhoneEqualTo(tbUser.getPhone());
        }

        List<TbUser> users = tbUserMapper.selectByExample(example);

        if (users!=null&&users.size()>0){
            return users.get(0);
        }

        return null;
    }

    @Override
    public int insert(TbUser tbUser) {
        return tbUserMapper.insert(tbUser);
    }

    @Override
    public TbUser selectByNameAndPwd(TbUser tbUser) {
        TbUserExample example = new TbUserExample();
        example.createCriteria().andUsernameEqualTo(tbUser.getUsername()).andPasswordEqualTo(tbUser.getPassword());
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
