package com.ego.dubbo.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public List<TbItemParam> selectTbItemParam(int page, int rows) {
        PageHelper.startPage(page,rows);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(null);
        return list;

    }

    @Override
    public int selectCount() {
        return (int) tbItemParamMapper.countByExample(null);
    }

    @Override
    public TbItemParam selectTbItemById(long id) {
        TbItemParamExample tbItemParamExample = new TbItemParamExample();
        tbItemParamExample.createCriteria().andItemCatIdEqualTo(id);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(tbItemParamExample);
        if (tbItemParams.size()>0&&tbItemParams!=null){
            return tbItemParams.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public int insert(TbItemParam tbItemParam) throws DaoException {
        try {
            int index = tbItemParamMapper.insert(tbItemParam);
            if (index>0){
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new DaoException("新增商品类目失败");
    }

    @Override
    @Transactional
    public int delete(long[] ids) throws DaoException {
        int index = 0;
        for (long id :
              ids  ) {
            index += tbItemParamMapper.deleteByPrimaryKey(id);
        }
        try {
            if (index==ids.length){
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw  new DaoException("批量删除失败");
    }

}
