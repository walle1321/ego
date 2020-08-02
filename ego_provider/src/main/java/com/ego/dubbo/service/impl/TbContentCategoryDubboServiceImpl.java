package com.ego.dubbo.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.mapper.TbContentCategoryMapper;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbContentCategoryExample;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@DubboService
public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selectByPid(long pid) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(pid).andStatusEqualTo(1);
        example.setOrderByClause("sort_order asc");
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        return list;
    }

    @Override
    @Transactional
    public int insert(TbContentCategory tbContentCategory) throws DaoException {
        TbContentCategoryExample example = new TbContentCategoryExample();
        System.out.println(tbContentCategory.getName());
        example.createCriteria().andStatusEqualTo(1).andNameEqualTo(tbContentCategory.getName());
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        //有没有相同的名称
        if (list != null && list.size() == 0) {
            TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
            //如果IsParent为false改为true，正确就不用改
            if (!category.getIsParent()) {
                TbContentCategory category1 = new TbContentCategory();
                category1.setIsParent(true);
                category1.setUpdated(tbContentCategory.getCreated());
                category1.setId(tbContentCategory.getParentId());
                if (tbContentCategoryMapper.updateByPrimaryKeySelective(category1) < 0) {
                    throw new DaoException("新增失败，修改父类目的isParent失败");
                }
            }
            //进行新增操作
            if (tbContentCategoryMapper.insert(tbContentCategory) > 0) {
                return 1;
            } else {
                throw new DaoException("新增失败");
            }

        }
        throw new DaoException("已有该名称");
    }

    @Override
    public int updateNameById(TbContentCategory tbContentCategory) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andNameEqualTo(tbContentCategory.getName()).andStatusEqualTo(1);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        //有没有相同的名称
        if (list != null && list.size() == 0) {
            return tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        }
        return 0;
    }

    @Override
    @Transactional
    public int deleteById(long id) throws DaoException {

        Date date = new Date();
        TbContentCategory category = new TbContentCategory();
        category.setStatus(2);
        category.setId(id);
        category.setUpdated(date);
        tbContentCategoryMapper.updateByPrimaryKeySelective(category);
        if (tbContentCategoryMapper.selectByPrimaryKey(id).getIsParent()) {
            this.categoryIsParent(id, date);
            //判断其父节点是够还有有效地子节点
            TbContentCategory currContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
            //查询其父节点的有效子节点
            List<TbContentCategory> list = this.selectByPid(currContentCategory.getParentId());
            //如果没有其他有效子节点
            if (list!=null&&list.size()==0){

                TbContentCategory parent = new TbContentCategory();
                parent.setId(currContentCategory.getParentId());
                parent.setIsParent(false);
                parent.setUpdated(date);
                int index = tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
                if (index>0){
                    return 1;
                }
                throw  new DaoException("在更改父类的IsParent时出错");
            }
            return 1;
        }
        throw new DaoException("删除失败");
    }

    private void categoryIsParent(long id, Date date) throws DaoException {

        List<TbContentCategory> list = this.selectByPid(id);
        for (TbContentCategory tb :
                list) {
            TbContentCategory category = new TbContentCategory();
            category.setId(tb.getId());
            category.setUpdated(date);
            category.setStatus(2);
            int index = tbContentCategoryMapper.updateByPrimaryKeySelective(category);
            if (index == 1) {
                if (tb.getIsParent()) {
                    this.categoryIsParent(tb.getId(), date);
                }
            } else {
                throw new DaoException("递归删除失败");
            }
        }

    }


}
