package com.lucheng.controller;

import com.lucheng.common.R;
import com.lucheng.domain.User;
import com.lucheng.service.UserService;
import org.apache.http.HttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R<String> userLogin(@RequestBody User user, HttpSession session){
        //简单校验手机号格式
        if(ObjectUtils.isEmpty(user.getPhone())){
            return R.error("手机号为空");
        }
        if(user.getPhone().length() != 11){
            return R.error("手机号长度不符合");
        }
        return userService.loginWithPhoneNumber(user,session);
    }

    @GetMapping("/verifyCode")
    public R<String> getValidateCode(String phone, HttpServletRequest request){
        if(!StringUtils.hasText(phone)){
            return R.error("手机号错误");
        }
        return userService.getValidateCode(phone,request);
    }

    @PostMapping("/sendMsg")
    public R<String>  sendValidateCode(@RequestBody User user,HttpServletRequest request){
        //简单校验格式
        if(ObjectUtils.isEmpty(user.getPhone())){
            return R.error("手机号为空");
        }
        if(user.getPhone().length() != 11){
            return R.error("手机号长度不符合");
        }
        return userService.sendValidateCode(user,request);
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request){
        //清除掉session中user域的值
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
