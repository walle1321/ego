package com.ego.trade.service.impl;

import com.ego.commons.pojo.DeleteCartPojo;
import com.ego.commons.pojo.MailPojo;
import com.ego.commons.pojo.OrderPojo;

import com.ego.commons.utils.ServletUtil;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbUser;
import com.ego.sender.Send;
import com.ego.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private Send send;

    @Value("${ego.rabbitmq.creatOrder.queueName}")
    private String creatOrderQueue;
    @Value("${ego.rabbitmq.cart.deleteQueueName}")
    private String deleteCartQueue;
    @Value("${ego.rabbitmq.mail.mailQueueName}")
    private String sendMailQueue;

    @Override
    public Map<String, Object> creatOrder(OrderPojo orderPojo) {
        //创建订单，队列创建，为了防止高并发时出现脏数据，有返回结果并不能实现异步请求
        String result = (String) send.convertSendAndReceive(creatOrderQueue, orderPojo);
        System.out.println("创建订单"+result);

        //异步消息，删除掉Redis中储存的购物车商品
        TbUser user = (TbUser) ServletUtil.getRequest().getSession().getAttribute("loginUser");
        List<TbOrderItem> itemList = orderPojo.getOrderItems();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < itemList.size(); i++) {
            sb.append(itemList.get(i).getItemId());
            if (i<itemList.size()-1){
                sb.append(",");
            }
        }
        DeleteCartPojo deleteCartPojo = new DeleteCartPojo();
        deleteCartPojo.setIds(sb.toString());
        deleteCartPojo.setUserId(user.getId());
        System.out.println(deleteCartPojo.toString());
        send.send(deleteCartQueue,deleteCartPojo);

        //异步消息，对用户发送邮件
        if (result!=null){
            send.send(sendMailQueue,new MailPojo(user.getEmail(),user.getId()+""));
        }

        //返回订单成功页面
        if (result!=null){
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("orderId",result);
            resultMap.put("payment",orderPojo.getPayment());
            // 当天11点之前下订单，预计当天下午送到
            // 11点到23点之前下单，预计第二天上午送到
            // 23点之后第二天下午送到。
            // 获取当前时间
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            System.out.println(hour);
            if (hour<11){
                calendar.set(Calendar.HOUR_OF_DAY,17);
            }else if(hour>=11&&hour<23){
                calendar.add(Calendar.DATE,1);
                calendar.set(Calendar.HOUR_OF_DAY,9);
            }else {
                calendar.add(Calendar.DATE,1);
                calendar.set(Calendar.HOUR_OF_DAY,17);
            }
            resultMap.put("date",calendar.getTime());
            return resultMap;
        }

        return null;
    }
}
