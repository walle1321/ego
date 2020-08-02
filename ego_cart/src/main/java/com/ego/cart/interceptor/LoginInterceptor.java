package com.ego.cart.interceptor;

import com.ego.pojo.TbUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${ego.passport.loginUrl}")
    private String loginUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断用户是否登录
        TbUser tbUser = (TbUser) request.getSession().getAttribute("loginUser");
        if(tbUser!=null){
            System.out.println("已经登录");
            return true;
        }
        response.sendRedirect(loginUrl);
        return false;
    }
}
