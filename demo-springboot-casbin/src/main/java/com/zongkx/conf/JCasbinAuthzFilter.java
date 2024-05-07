package com.zongkx.conf;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.security.auth.message.AuthException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : yx-0176
 * @description
 * @date : 2021/1/20
 */
@WebFilter(urlPatterns = "/*" , filterName = "JCasbinAuthzFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)//执行顺序，最高级别最先执行，int从小到大
public class JCasbinAuthzFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(JCasbinAuthzFilter.class);

    private EnforcerFactory enforcer;

    @Autowired
    public JCasbinAuthzFilter(EnforcerFactory enforcer){
        this.enforcer = enforcer;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("过滤器初始化");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String user = request.getParameter("username");
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (!enforcer.enforce(user, path, method)) {
            log.info("无权访问");
            throw new AuthException("无权访问");
        }
        chain.doFilter(request, response);


    }

    @Override
    public void destroy() {

    }

}
