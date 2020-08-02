package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.ManagerDubboService;
import com.ego.mapper.ManagerMapper;
import com.ego.pojo.Manager;
import com.ego.pojo.ManagerExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class ManagerDubboServiceImpl implements ManagerDubboService {

    @Autowired
    private ManagerMapper managerMapper;

    @Override
    public Manager selectManagerByUsername(String username) {
        ManagerExample example = new ManagerExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<Manager> list = managerMapper.selectByExample(example);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
