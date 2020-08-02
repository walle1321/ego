package com.ego.sender.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {
    @Value("${ego.rabbitmq.content.queueName}")
    private String contentQueue;
    @Value("${ego.rabbitmq.item.insert}")
    private String itemQueue;
    @Value("${ego.rabbitmq.item.delete}")
    private String deleteItemQueue;
    @Value("${ego.rabbitmq.creatOrder.queueName}")
    private String creatOrderQueue;
    @Value("${ego.rabbitmq.cart.deleteQueueName}")
    private String deleteCartQueue;
    @Value("${ego.rabbitmq.mail.mailQueueName}")
    private String sendMailQueue;



    /**
     * 如果没有队列，帮助创建队列。
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(contentQueue);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("amq.direct");
    }

    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding(Queue queue, DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).withQueueName();
    }


    //下面的方式是为了消息队列没有该该队列，进行创建
    @Bean
    public Queue itemQueue() {
        return new Queue(itemQueue);
    }
    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding2(Queue itemQueue, DirectExchange directExchange){
        return BindingBuilder.bind(itemQueue).to(directExchange).withQueueName();
    }




    //下面的方式是为了消息队列没有该该队列，进行创建
    @Bean
    public Queue deleteItemQueue() {
        return new Queue(deleteItemQueue);
    }
    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding3(Queue deleteItemQueue, DirectExchange directExchange){
        return BindingBuilder.bind(deleteItemQueue).to(directExchange).withQueueName();
    }


    //下面的方式是为了消息队列没有该该队列，进行创建,创建订单的队列
    @Bean
    public Queue creatOrderQueue() {
        return new Queue(creatOrderQueue);
    }
    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding4(Queue creatOrderQueue, DirectExchange directExchange){
        return BindingBuilder.bind(creatOrderQueue).to(directExchange).withQueueName();
    }




    //下面的方式是为了消息队列没有该该队列，进行创建,
    //删除Redis中的购物车商品
    @Bean
    public Queue deleteCartQueue() {
        return new Queue(deleteCartQueue);
    }
    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding5(Queue deleteCartQueue, DirectExchange directExchange){
        return BindingBuilder.bind(deleteCartQueue).to(directExchange).withQueueName();
    }



    //下面的方式是为了消息队列没有该该队列，进行创建,
    //创建完订单进行发送邮件
    @Bean
    public Queue sendMailQueue() {
        return new Queue(sendMailQueue);
    }
    @Bean
    // 参数名和方法名一直就是从spring容器中获取对应方法的返回值,将队列和交换器绑定在一起
    public Binding binding6(Queue sendMailQueue, DirectExchange directExchange){
        return BindingBuilder.bind(sendMailQueue).to(directExchange).withQueueName();
    }


}
