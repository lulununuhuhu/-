package com.lucheng.service;

import com.lucheng.common.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface CommonService {
    public R<String> uploadImg(MultipartFile file);

    /**
     * 将图片文件传送给浏览器让浏览器渲染出来
     * @param name
     * @param response
     */
    public void downloadImg(String name, HttpServletResponse response);
}
