package com.ego.dubbo.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@DubboService
public class TbItemDubboServiceImpl implements TbItemDubboService {
    
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    
    @Override
    public List<TbItem> selectByPage(int pageNumber, int pageSize) {
        /**
         * 当前页码，每页查询的条数
         * 该插件下载第一行
         */
        PageHelper.startPage(pageNumber, pageSize);
        //Example相当于SQL中的where语句，没有条件就写入null
        //查询全部
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        return pageInfo.getList();
    }

    @Override
    public long selectCount() {
        //查询总条数
        return tbItemMapper.countByExample(null);
    }


    @Override
    //声明式事务注解，监听到异常会事务回滚，注意这个注解！！
    @Transactional
    public int updateTbItemByIds(long[] ids, int status) throws DaoException{
        int index = 0;
        for (long id :
                ids) {
            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            tbItem.setStatus((byte) status);
            tbItem.setUpdated(new Date());
            index+=tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
        if (index == ids.length){
            return 1;
        }
        throw new DaoException("批量更改商品状态失败");
    }

    @Override
    @Transactional
    public int insert(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParam) throws DaoException{

        try {
            int index = tbItemMapper.insert(tbItem);
            if (index>0){
                int index2 = tbItemDescMapper.insert(tbItemDesc);
                if (index2>0){
                    int index3 = tbItemParamItemMapper.insert(tbItemParam);
                    if (index3>0){
                        return 1;
                    }
                }
            }
        } catch (Exception e) {
        }
        throw new DaoException("新增商品失败");
    }

    @Override
    @Transactional
    public int update(TbItem tbItem, TbItemDesc tbItemDesc,TbItemParamItem tbItemParamItem) throws DaoException {
        int index = tbItemMapper.updateByPrimaryKeySelective(tbItem);
        if (index>0){
            int index2 = tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
            if (index2>0){
                int index3 = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
                if (index3>0){
                    return 1;
                }
            }
        }
        throw new DaoException("更改商品和商品描述信息失败");
    }

    @Override
    public TbItem selectById(long id) {

        return tbItemMapper.selectByPrimaryKey(id);
    }

}
