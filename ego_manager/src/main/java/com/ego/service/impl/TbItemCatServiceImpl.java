package com.ego.service.impl;

import com.ego.commons.pojo.EasyUITree;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.pojo.TbItemCat;
import com.ego.service.TbItemCatService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TbItemCatServiceImpl implements TbItemCatService {

    @DubboReference
    private TbItemCatDubboService tbItemCatDubboService;

    @Override
    public List<EasyUITree> showTree(long pid) {
        List<TbItemCat> catList = tbItemCatDubboService.selectItemCatByPid(pid);
        List<EasyUITree> treeList = new ArrayList<>();
        for (TbItemCat cat :
                catList) {
            EasyUITree tree = new EasyUITree();
            tree.setId(cat.getId());
            tree.setText(cat.getName());
            tree.setState(cat.getIsParent()?"closed":"open");
            treeList.add(tree);
        }
        return treeList;
    }
}
