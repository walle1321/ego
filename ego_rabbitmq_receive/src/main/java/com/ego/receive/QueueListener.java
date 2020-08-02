package com.ego.receive;

import com.ego.commons.pojo.DeleteCartPojo;
import com.ego.commons.pojo.MailPojo;
import com.ego.commons.pojo.OrderPojo;
import com.ego.commons.pojo.TbItemDetails;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.mail.MyMailSender;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueueListener {

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${ego.rabbitmq.item.host}")
    private String itemHost;
    @Value("${ego.rabbitmq.content.host}")
    private String contentHost;
    @Value("${ego.rabbitmq.item.deletehost}")
    private String deleteItemHost;
    @DubboReference
    private TbItemDubboService tbItemDubboService;
    @DubboReference
    private TbOrderDubboService tbOrderDubboService;
    @Autowired
    private MyMailSender myMailSender;


    // 最简单的写法，但是如果当没有发送消息时，队列没有创建，启动当前项目就会报错。
    // @RabbitListener(queues = "content")
// 即使没有队列，也可以启动 receive，自动帮助创建对垒
    @RabbitListener(bindings =
            {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.content.queueName}"),
                    exchange = @Exchange(name = "amq.direct"))})
    public void content(Object obj) {
        System.out.println("我接收到了：" + obj);
        //需要做的时数据同步到redis
        //1.mysql取数据,通过dubbo的consumer调用
        //2.数据缓存到redis
        // 直接调用 ego_port
        HttpClientUtil.doGet(contentHost);
    }

    //接受到消息，发给search，redis进行新增
    @RabbitListener(bindings =
            {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.item.insert}"),
                    exchange = @Exchange(name = "amq.direct"))})
    public void itemInsert(String id) {
        /**
         * 对solr进行新增
         */
        System.out.println("我接收到了：" + id);
        Map<String, String> map = new HashMap<>();
        map.put("ids", id);
        String s = HttpClientUtil.doGet(itemHost, map);
        System.out.println(s);


        /**
         *对redis进行新增（redis和solr没有修改，修改就是新增代码，solr只能删除和新增）
         *
         */

        String[] ids = id.split(",");
        for (String myId :
                ids) {
            System.out.println("redis姐搜到了" + myId);
            TbItem tbItem = tbItemDubboService.selectById(Long.parseLong(myId));
            TbItemDetails tbItemDetails = new TbItemDetails();

            tbItemDetails.setId(tbItem.getId());
            tbItemDetails.setPrice(tbItem.getPrice());
            tbItemDetails.setSellPoint(tbItem.getSellPoint());
            tbItemDetails.setTitle(tbItem.getTitle());
            String image = tbItem.getImage();
            tbItemDetails.setImages(image != null && !image.equals("") ? image.split(",") : new String[1]);
            String key = "com.ego.item::details:" + myId;
            System.out.println(tbItem.getImage());
            System.out.println(tbItemDetails.toString());
            redisTemplate.opsForValue().set(key, tbItemDetails);

        }

    }


    //接受到消息，发给search，redis进行删除
    @RabbitListener(bindings =
            {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.item.delete}"),
                    exchange = @Exchange(name = "amq.direct"))})
    public void itemDelete(String ids) {
        /**
         * solr中删除数据
         */
        System.out.println("我接收到了：" + ids);
        Map<String, String> map = new HashMap<>();
        map.put("ids", ids);
        String s = HttpClientUtil.doGet(deleteItemHost, map);
        System.out.println(s);

        /**
         * redis中删除
         */
        String[] id = ids.split(",");
        for (String myId :
                id) {
            String key = "com.ego.item::details:" + myId;
            redisTemplate.delete(key);

        }
    }

    /**
     * 创建订单队列
     * <p>
     * 当接收消息带有返回值时，表示需要返回给发送方一个状态。发送方必须使用可接收状态的方法
     *
     * @param msg
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.creatOrder.queueName}"), exchange = @Exchange(name = "amq.direct"))})
    public String orderCreate(Message msg) {
        // 反序列化
        try {
            //1.先去接受到消息
            byte[] body = msg.getBody();
            InputStream is = new ByteArrayInputStream(body);
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(is);
            OrderPojo orderPojo = (OrderPojo) objectInputStream.readObject();
            System.out.println(orderPojo);
            //2.判断库存是否足够
            List<TbOrderItem> itemList = orderPojo.getOrderItems();
            for (TbOrderItem toi :
                    itemList) {
                if (toi.getNum()>tbItemDubboService.selectById(Long.parseLong(toi.getItemId())).getNum()){
                    return null;
                }

            }
            //3.如果足够，对TbOrder,TbOrderItem,TbOrderItemShipping等订单表新增
            // tborder
            TbOrder tbOrder = new TbOrder();
            tbOrder.setPayment(orderPojo.getPayment());
            tbOrder.setPaymentType(orderPojo.getPaymentType());
            // 订单id
            String id = "" + IDUtils.genItemId();
            tbOrder.setOrderId(id);
            //  时间
            Date date = new Date();
            tbOrder.setCreateTime(date);
            tbOrder.setUpdateTime(date);
            // tborderitem
            for (TbOrderItem tbOrderItem : itemList) {
                tbOrderItem.setId(IDUtils.genItemId() + "");
                tbOrderItem.setOrderId(id);
            }
            // tbordershipping
            TbOrderShipping orderShipping = orderPojo.getOrderShipping();
            orderShipping.setOrderId(id);
            orderShipping.setCreated(date);
            orderShipping.setUpdated(date);

            // 插入数据
            int index = tbOrderDubboService.insertTbOrder(tbOrder, itemList, orderShipping);
            if (index == 1) {
                return id;
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对Redis中的购物车商品删除
     * @param msg
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.cart.deleteQueueName}"), exchange = @Exchange(name = "amq.direct"))})
    public void deleteCart(Message msg) {
        // 反序列化
        try {
            //1.先去接受到消息
            byte[] body = msg.getBody();
            InputStream is = new ByteArrayInputStream(body);
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(is);
            DeleteCartPojo deleteCartPojo = (DeleteCartPojo) objectInputStream.readObject();
            System.out.println("deleteCart"+deleteCartPojo);
            //2.采用跨域请求来对Redis中的购物车商品删除，去ego_cart模块
            Map<String,String> map = new HashMap<>();
            map.put("userId",""+deleteCartPojo.getUserId());
            map.put("ids",deleteCartPojo.getIds());
            System.out.println("userId"+deleteCartPojo.getUserId());
            System.out.println("ids"+deleteCartPojo.getIds());
            String s = HttpClientUtil.doGet("http://localhost:8085/cart/deleteByIds",map);
            System.out.println("deleteCartResult"+s);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 订单创建成功对用户发送邮件
     * @param msg
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(name = "${ego.rabbitmq.mail.mailQueueName}"), exchange = @Exchange(name = "amq.direct"))})
    public void sendMail(Message msg) {
        try {
            //1.先去接受到消息
            byte[] body = msg.getBody();
            InputStream is = new ByteArrayInputStream(body);
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(is);
            MailPojo mailPojo = (MailPojo) objectInputStream.readObject();
            System.out.println("MailPojo"+mailPojo);
            myMailSender.send(mailPojo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



}
