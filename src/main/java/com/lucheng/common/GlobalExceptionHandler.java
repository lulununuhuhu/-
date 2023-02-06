package com.lucheng.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类
 */
@ResponseBody
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class, Service.class})
public class GlobalExceptionHandler {


    /**
     * 专门针对处理SQLIntegrityConstraintViolationException异常的方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        String resMessage = null;
        log.error(message);
        //在异常信息中获取到重复值
        if(message.contains("Duplicate entry")){
            String[] s = message.split(" ");
            resMessage = s[2]+"已存在";
        }
        return R.error(resMessage);
    }

    /**
     * 专门针对处理业务异常信息的方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex){
        log.info("捕获到业务异常信息: {}",ex.getMessage());
        return R.error(ex.getMessage());
    }
}
