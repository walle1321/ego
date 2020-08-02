package com.ego.commons.pojo;

import java.io.Serializable;

public class DeleteCartPojo implements Serializable {
    public static final long serialVersionUID=1L;

    @Override
    public String toString() {
        return "DeleteCartPojo{" +
                "userId=" + userId +
                ", ids='" + ids + '\'' +
                '}';
    }

    private Long userId;
    private String ids;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
