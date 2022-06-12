package com.itheima.reggie.web.fillter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;

import com.itheima.reggie.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author swk
 * @Date 2022/6/8 15:58
 * @Version 1.0
 */

@Slf4j
@WebFilter("/*")//表示该类是一个filter拦截所有请求
public class LoginCheckFilter implements Filter {
    //创建路径匹配器对象
    AntPathMatcher apm = new AntPathMatcher();

   //判断是否登录，并决定是否放行
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        //1.获取本次请求URI
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();
        //判断本次请求是否需要登录，才可以访问

        //定义要放行的所有请求
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/favicon.ico"
        };

        //检查是否在放行范围
        if(checkUrl(urls,requestURI)){
            //如果不需要则直接放行

            filterChain.doFilter(request,response);
            return; //后面代码不需要直接放行
        }

        //判断登录状态，如果已经登陆,直接放行
        Long employeeId = (Long) req.getSession().getAttribute("employee");
        if (employeeId != null) {
            BaseContext.setCurrentId(employeeId);
          //放行
            filterChain.doFilter(request,response);
            return; //后面代码不需要直接放行
        }
            //未登录，返回未登录的结果，跳转到登录页面
        resp.getWriter().write(JSON.toJSONString(R.fail("NOTLOGIN")));

    }

    //判断确认某个请求是否在不登录的时候就可以放行
    private boolean checkUrl(String[] urls, String requestURI) {
       boolean matchResult = false;
        for (String url : urls) {
             matchResult = apm.match(url, requestURI);
            if (matchResult){
               return true;
            }
        }
        log.info("本次请求url为：{}，是否需要放行{}", requestURI, matchResult);

        return false;
    }
}
