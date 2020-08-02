package com.ego.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PicService {
    /**
     * 文件上传
     * @param uploadfile
     * @return
     */
    Map<String,Object> uploadPic(MultipartFile uploadfile);
}
