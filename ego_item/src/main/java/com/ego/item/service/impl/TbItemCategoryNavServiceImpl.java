package com.ego.item.service.impl;

import com.ego.commons.pojo.TbItemDetails;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.item.pojo.ItemCategoryNav;
import com.ego.item.pojo.ItemCategoryNode;
import com.ego.item.pojo.ParamItem;
import com.ego.item.service.TbItemCategoryNavService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TbItemCategoryNavServiceImpl implements TbItemCategoryNavService {

    @DubboReference
    private TbItemCatDubboService tbItemCatDubboService;
    @DubboReference
    private TbItemDubboService tbItemDubboService;
    @DubboReference
    private TbItemDescDubboService tbItemDescDubboService;
    @DubboReference
    private TbItemParamItemDubboService tbItemParamItemDubboService;

    @Override
    @Cacheable(cacheNames = "com.ego.item",key = "'showItemCat'")
    public ItemCategoryNav showCategory() {
        ItemCategoryNav nav = new ItemCategoryNav();
        nav.setData(selectAllByPid(0));
        return nav;
    }

    @Override
    @Cacheable(cacheNames = "com.ego.item",key = "'details:'+#id")
    public TbItemDetails showItem(Long id) {

        System.out.println("我被执行了");

        TbItem tbItem = tbItemDubboService.selectById(id);
        TbItemDetails tbItemDetails = new TbItemDetails();
        tbItemDetails.setId(tbItem.getId());
        tbItemDetails.setPrice(tbItem.getPrice());
        tbItemDetails.setSellPoint(tbItem.getSellPoint());
        tbItemDetails.setTitle(tbItem.getTitle());
        String image = tbItem.getImage();
        tbItemDetails.setImages(image!=null&&!image.equals("")?image.split(","):new String[1]);

        return tbItemDetails;

    }

    @Override
    public String showDesc(Long itemId) {
        TbItemDesc desc = tbItemDescDubboService.selectTbItemDescById(itemId);
        return desc.getItemDesc();
    }

    @Override
    public String showParamItem(long id) {
        TbItemParamItem tbItemParamItem = tbItemParamItemDubboService.selectByItemId(id);
        String json = tbItemParamItem.getParamData();
        List<ParamItem> list = JsonUtils.jsonToList(json, ParamItem.class);
        StringBuffer sf = new StringBuffer();
        for(ParamItem paramItem : list){
            // 把一个ParamItem当作一个表格看待
            sf.append("<table style='color:gray;' width='100%' cellpadding='5'>");
            for(int i = 0 ;i<paramItem.getParams().size();i++){
                sf.append("<tr>");
                if(i==0){// 说明是第一行，第一行要显示分组信息
                    sf.append("<td style='width:100px;text-align:right;'>"+paramItem.getGroup()+"</td>");
                }else{
                    // html列是空最好给个空格
                    sf.append("<td> </td>");// 除了第一行以外其他行第一列都是空。
                }
                sf.append("<td style='width:200px;text-align:right;'>"+paramItem.getParams().get(i).getK()+"</td>");
                sf.append("<td>"+paramItem.getParams().get(i).getV()+"</td>");
                sf.append("</tr>");
            }
            sf.append("</table>");
            sf.append("<hr style='color:gray;'/>");
        }
        return sf.toString();
    }

    private List<Object> selectAllByPid(long id){
        List<Object> listResult = new ArrayList<>();
        List<TbItemCat> itemCats = tbItemCatDubboService.selectItemCatByPid(id);
        for (TbItemCat tb :
                itemCats) {
            if (tb.getIsParent()) {
                ItemCategoryNode node = new ItemCategoryNode();
                node.setU("/products/"+tb.getId()+".html");
                node.setN( "<a href='/products/"+tb.getId()+".html'>"+tb.getName()+"</a>");
                listResult.add(node);
                node.setI(selectAllByPid(tb.getId()));
            }else {
                listResult.add("/products/"+tb.getId()+".html|"+tb.getName());
            }
            }
        return listResult;
    }
}
