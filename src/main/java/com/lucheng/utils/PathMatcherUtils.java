package com.lucheng.utils;

import org.springframework.util.AntPathMatcher;

/**
 * 请求路径校验的一个工具类
 */
public class PathMatcherUtils {

    /**
     * 判断某个请求路径是否符合在路径组中的一个
     * @param requestUrl
     * @param urls
     * @return
     */
    public static boolean isPathMatch(String requestUrl,String[] urls){
        AntPathMatcher matcher = new AntPathMatcher();
        for (String url : urls) {
            boolean res = matcher.match(url, requestUrl);
            if(res == true)
                return true;
        }
        return false;
    }
}
