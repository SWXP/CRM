package com.yjxxt.crm.interceptors;

import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 拦截器
 * @author ZHAI
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User temp = userService.selectByPrimaryKey(userId);
        if(userId==0|| temp==null){
            throw  new NoLoginException();
        }
        return true;
    }
}
