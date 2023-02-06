package com.lucheng.service.impl;

import com.lucheng.common.R;
import com.lucheng.service.CommonService;
import com.lucheng.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Value("${reggie.basePath}")
    private String basePath;
    @Override
    public R<String> uploadImg(MultipartFile file) {
        /**
         * 1. 获取file对象地址，了解上传照片存放的临时位置
         * 2. 生成上传图片的文件名
         *      2.1 获取图片的后缀格式
         *      2.2 使用UUID生成新的文件名，防止上传的同名图片将前面已经上传的图片覆盖掉
         * 3. 指定目录
         *      3.1 判断指定目录是否存在，如不存在先进行创建
         * 4. 存放照片
         *      4.1 将存放目录地址和文件名进行拼接，生成完整的图片路径
         *      4.2 将文件从临时位置转移到指定路径 MultipartFile.transferTo方法
         */
        log.info("文件的临时位置是: {}",file.toString());
        String originalFilename = file.getOriginalFilename();
        //获取图片的后缀格式
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成新的文件名
        String newFileName = UUID.randomUUID().toString() +suffix;
        File directory = new File(basePath);
        if(!directory.exists()){
            //目录不存在，则创建目录
            directory.mkdirs();
        }

        //存放照片(照片路径 = directory+newFileName)
        try {
            file.transferTo(new File(basePath+newFileName));
        } catch (IOException e) {
            log.info("存放照片失败，异常信息: {}",e.getMessage());
        }
        return R.success(newFileName);
    }

    @Override
    public void downloadImg(String name, HttpServletResponse response) {
        WebUtils.renderImg(basePath+name,response);
    }
}
