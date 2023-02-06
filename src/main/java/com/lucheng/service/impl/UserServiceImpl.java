package com.lucheng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucheng.common.CustomException;
import com.lucheng.common.R;
import com.lucheng.constants.SystemConstant;
import com.lucheng.domain.User;
import com.lucheng.service.UserService;
import com.lucheng.mapper.UserMapper;
import com.lucheng.utils.SMSUtils;
import com.lucheng.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
* @author lucheng
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-01-10 15:18:42
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
implements UserService{

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public R<String> loginWithPhoneNumber(User user, HttpSession session) {
//        //获取session中phone域的值
//        Object attribute = session.getAttribute(user.getPhone());
//        if(ObjectUtils.isEmpty(attribute)){
//            throw new CustomException("验证码失效");
//        }
//
//        if(!attribute.equals(user.getCode())){
//            throw new CustomException("验证码错误");
//        }

//        根据手机号查询redis
        String validateCode = (String) redisTemplate.opsForValue().get(user.getPhone());
        if(!StringUtils.hasText(validateCode)){
            throw new CustomException("验证码失效");
        }
        if(!validateCode.equals(user.getCode())){
            throw new CustomException("验证码错误");
        }
        //验证码正确，删除存储对应验证码的key
        redisTemplate.delete(user.getPhone());

        //查询user表判断该手机号是否是新用户登录,如果是新用户则写入用户表中
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, user.getPhone()));
        if(ObjectUtils.isEmpty(one)){
            user.setStatus(SystemConstant.STATUS_NORMAL);
            save(user);
            //将session域中的user值设置为userId
            session.setAttribute("user",user.getId());
        }else{
            session.setAttribute("user",one.getId());
        }
        return R.success("登录成功");
    }

    @Override
    public R<String> getValidateCode(String phone, HttpServletRequest request) {
        return null;
    }

    @Override
    public R<String> sendValidateCode(User user,HttpServletRequest request) {
        //随机生成6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
//        //将该验证码转为字符串后再存入session中
//        request.getSession().setAttribute(user.getPhone(),validateCode.toString());
        log.info("code = {}",validateCode);
        //将验证码和手机号存入redis中,有效期为5分钟
        redisTemplate.opsForValue().set(user.getPhone(),validateCode.toString(),5L, TimeUnit.MINUTES);
        return R.success("发送成功");
    }

}
