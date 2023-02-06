package com.lucheng.filter;

import com.lucheng.constants.SystemConstant;
import com.lucheng.utils.PathMatcherUtils;
import com.lucheng.utils.ThreadLocalUtils;
import com.lucheng.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器：判断请求url能否放行
 */
@WebFilter(filterName = "authenticationFilter",urlPatterns = "/*")//使用WebFilter注解来确定过滤器匹配url的范围
@Slf4j
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("当前线程id是:"+Thread.currentThread().getId());
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        log.info("拦截到请求 {}",requestURI);
        //自定义一个请求接口url白名单包括登录url、静态资源文件
        String[] whiteUrl = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/frontend/**",
                "/common/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg",
                "/user/verifyCode"
        };

        //判断本次请求是否需要处理
        boolean check = PathMatcherUtils.isPathMatch(requestURI, whiteUrl);
        if(check){
            log.info("放行请求url:{}",requestURI);
            filterChain.doFilter(request,response);//放行
            return;
        }
        //判断pc后台是否处于登录态
        Object attribute = request.getSession().getAttribute("employee");
        if(!ObjectUtils.isEmpty(attribute)){
            log.info("用户已登录，登录id是 {}",request.getSession().getAttribute("employee"));
            //将登录id使用ThreadLocal存入线程局部变量中
            ThreadLocalUtils.setCurrentUser(request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);//放行
            return;
        }

        //判断移动后台是否处于登录状态
        Object attribute2 = request.getSession().getAttribute("user");
        if(!ObjectUtils.isEmpty(attribute2)){
            log.info("用户已登录，用户电话号是 {}",request.getSession().getAttribute("user"));
            //将登录电话号使用ThreadLocal存入线程局部变量中
            ThreadLocalUtils.setCurrentUser(request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);//放行
            return;
        }

        log.info("用户未登录");
        //处于未登录状态，返回前端未登录结果
        WebUtils.renderString(response, SystemConstant.USER_NOT_LOGIN);
    }
}
