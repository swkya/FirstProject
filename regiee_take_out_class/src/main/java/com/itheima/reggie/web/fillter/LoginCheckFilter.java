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
    // 2.2 创建Spring提供的路径匹配器对象
    AntPathMatcher apm = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        //1. 获取本次请求的URI
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        log.info("请求路径为：{}", requestURI);

        //2. 判断本次请求, 是否需要登录, 才可以访问

        // 2.1 定义无需要登录就要放行的资源
        String[] urls = {
                "/front/**",
                "/backend/**",
                "/favicon.ico",
                "/employee/logout",
                "/employee/login",
                // 前台登录相关请求
                "/user/sendMsg",
                "/user/login"
        };

        if (chechkUrl(urls, requestURI)) {

            //3. 如果不需要，则直接放行
            filterChain.doFilter(request, response);
            return;
        }
        // 后台员工登录状态检查
        Long employeeId = (Long) req.getSession().getAttribute("employee");
        if (employeeId != null) {
            //4. 判断登录状态，如果已登录，则直接放行

            // 4.1 存用户id到ThreadLocal
            BaseContext.setCurrentId(employeeId);

            // 放行
            filterChain.doFilter(request, response);

            return;
        }

        // 前台用户登录状态检查
        Long userId = (Long) req.getSession().getAttribute("user");
        System.out.println("用户id是"+userId);
        if (userId != null) {
            //4. 判断登录状态，如果已登录，则直接放行

            // 4.1 存用户id到ThreadLocal
            BaseContext.setCurrentId(userId);

            // 放行
            filterChain.doFilter(request, response);

            return;
        }

        //5. 如果未登录, 则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.fail("NOTLOGIN")));

    }

    // 2.3 使用Spring提供的路径匹配器判断是否匹配
    private boolean chechkUrl(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = apm.match(url, requestURI);
            if (match) {
                log.info("请求url是{}, 是否直接放行{}", requestURI, true);
                return true;
            }
        }
        log.info("请求url是{}, 是否直接放行{}", requestURI, false);
        return false;
    }
}
