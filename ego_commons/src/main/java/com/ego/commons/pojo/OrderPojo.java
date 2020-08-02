package com.ego.commons.pojo;

import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

public class OrderPojo implements Serializable {
    public static final long serialVersionUID=1L;
    private String payment;
    private Integer paymentType;
    private TbOrderShipping orderShipping;

    private List<TbOrderItem> orderItems;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "payment='" + payment + '\'' +
                ", paymentType=" + paymentType +
                ", orderShipping=" + orderShipping +
                ", orderItems=" + orderItems +
                '}';
    }
}
