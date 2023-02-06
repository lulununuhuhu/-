package com.lucheng.utils;

import com.alibaba.fastjson.JSON;
import com.lucheng.common.CustomException;
import com.lucheng.common.R;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class WebUtils {

    /**
     * 向前端返回字符串内容的json数据
     * @param response
     * @param string 返回内容字符串
     */
    public static void renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(R.error(string)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 向浏览器返回图片文件的字节流数据
     * @param imgPath
     * @param response
     */
    public static void renderImg(String imgPath, HttpServletResponse response) {
        FileInputStream fis = null;//开启imgPath文件的字节输入流
        ServletOutputStream os = null;//开启响应的输入字节流
        try {
            fis = new FileInputStream(new File(imgPath));
            os = response.getOutputStream();
            int len = 0;
            byte[] cbuf = new byte[1024];
            response.setContentType("image/jpeg");
            while((len = fis.read(cbuf)) != -1){
                os.write(cbuf,0,len);
            }
        } catch (IOException e) {
            log.info("传输图片异常: {}",e.getMessage());
            throw new CustomException("传输图片失败");
        }finally {
            //关闭流
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
