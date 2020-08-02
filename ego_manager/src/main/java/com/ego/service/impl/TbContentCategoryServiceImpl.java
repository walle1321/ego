package com.ego.service.impl;

import com.ego.commons.exception.DaoException;
import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.pojo.TbContentCategory;
import com.ego.service.TbContentCategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {

    @DubboReference
    private TbContentCategoryDubboService tbContentCategoryDubboService;

    @Override
    public List<EasyUITree> selectByPid(long id) {
        List<TbContentCategory> list = tbContentCategoryDubboService.selectByPid(id);
        List<EasyUITree> listTree = new ArrayList<>();

        for (TbContentCategory tb :
                list) {
            EasyUITree tree = new EasyUITree();
            tree.setId(tb.getId());
            tree.setState(tb.getIsParent()?"closed":"open");
            tree.setText(tb.getName());
            listTree.add(tree);
        }

        return listTree;
    }

    @Override
    public EgoResult insert(TbContentCategory tbContentCategory) {

        Date date = new Date();
        tbContentCategory.setId(IDUtils.genItemId());
        tbContentCategory.setCreated(date);
        tbContentCategory.setUpdated(date);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        try {
            int insert = tbContentCategoryDubboService.insert(tbContentCategory);
            if (insert>0){
                return EgoResult.ok(tbContentCategory);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("新增失败");
    }

    @Override
    public EgoResult updateNameById(TbContentCategory tbContentCategory) {

        tbContentCategory.setUpdated(new Date());
        int index = tbContentCategoryDubboService.updateNameById(tbContentCategory);
        if (index>0){
            return EgoResult.ok();
        }
        return EgoResult.error("更改名字失败");
    }

    @Override
    public EgoResult delete(long id) {
        int index = 0;
        try {
            index = tbContentCategoryDubboService.deleteById(id);
            if (index>0){
                   return EgoResult.ok();
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return EgoResult.error("删除失败");
    }


}
