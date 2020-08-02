package com.ego.portal.service;


import com.ego.commons.pojo.BigAd;
import com.ego.pojo.TbContent;

import java.util.List;

public interface TbContentService {

    /**
     * 展示大广告，先在缓存中查找
     * @return
     */
    List<BigAd> showBig();

    /**
     * 更新大广告到redis，被rabbitMQ_receive的HTTPClient调用
     * @return
     */
    List<BigAd> showBig2();

}
