package com.ego.commons.pojo;

import java.util.Arrays;

/**
 * 购物车实体类
 */
public class CartPojo {
    private Long id;
    private String title;
    private Long price;
    private String [] images;
    private Integer num;

    @Override
    public String toString() {
        return "CartPojo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", images=" + Arrays.toString(images) +
                ", num=" + num +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
