package com.lucheng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lucheng.common.R;
import com.lucheng.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
* @author lucheng
* @description 针对表【user(用户信息)】的数据库操作Service
* @createDate 2023-01-10 15:18:42
*/
public interface UserService extends IService<User> {

    /**
     * 验证手机验证码登录
     * @param user
     * @return
     */
    public R<String> loginWithPhoneNumber(User user, HttpSession session);

    /**
     * 给手机号phone提供验证码
     * @param phone
     * @return
     */
    public R<String> getValidateCode(String phone, HttpServletRequest request);

    /**
     * 发送验证码
     * @param user
     * @return
     */
    public R<String> sendValidateCode(User user,HttpServletRequest request);
}
