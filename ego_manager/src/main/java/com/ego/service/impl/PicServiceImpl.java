package com.ego.service.impl;

import com.ego.commons.utils.FastDFSClient;
import com.ego.service.PicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class PicServiceImpl implements PicService {

    @Value("${ego.fastdfs.nginx}")
    private String nginxPort;

    @Override
    public Map<String, Object> uploadPic(MultipartFile uploadfile) {

        Map<String,Object> map = new HashMap<>();

        System.out.println(nginxPort);

        System.out.println(uploadfile.getOriginalFilename());

        /**
         * 第一个参数：图片文件
         * 第二个参数：上传的文件的原名称 主要为了获取文件后缀
         */
        try {
            String[] result = FastDFSClient.uploadFile(uploadfile.getInputStream(), uploadfile.getOriginalFilename());
            /**
             * 第一个参数：卷名--group1
             * 第二个参数：文件位置名--
             */
            System.out.println(result[0]+"--------"+result[1]);

            map.put("error",0);
            map.put("url",nginxPort+result[0]+"/"+result[1]);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("error","1");
            map.put("message","上传失败");
        }

        return map;
    }
}
