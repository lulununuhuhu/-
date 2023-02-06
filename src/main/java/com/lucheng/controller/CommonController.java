package com.lucheng.controller;

import com.lucheng.common.R;
import com.lucheng.service.CommonService;
import com.lucheng.service.impl.CommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件上传和下载相关接口
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Resource
    private CommonService commonService;
    /**
     * 上传图片到本地指定目录下生成图片
     * @param file 参数名必须是file，因为前端传来的表单数据其中name属性名就是file
     * @return
     */
    @PostMapping("/upload")
    public R<String> uploadImg(MultipartFile file){
        return commonService.uploadImg(file);
    }

    /**
     * 向浏览器回显已下载的文件
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void showImg(String name, HttpServletResponse response){
        commonService.downloadImg(name,response);
    }
}
