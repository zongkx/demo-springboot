package com.zongkx;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author zongkxc
 */
@RequiredArgsConstructor
public class MyAuthFilter extends OncePerRequestFilter {
    private final List<String> whiteList;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(whiteList.stream().anyMatch(request.getRequestURI()::startsWith)){
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        MutableHttpServletRequest req = new MutableHttpServletRequest(request);
        if(StringUtils.hasLength(token)){ // 模拟 token check
            req.setAttribute("test", token);// do something
            filterChain.doFilter(req, response);
        }else{
            response.setHeader("Content-Type", "application/json");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                writer.write("no auth");
                writer.flush();
            } catch (IOException ignored) {

            }
        }
    }


    // 可重复使用的HttpServletRequest
    public static class MutableHttpServletRequest extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;
        public MutableHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }
        public void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }
        @Override
        public String getHeader(String name) {
            return this.customHeaders.get(name);
        }
        public Map<String, String> getCustomHeaders() {
            return this.customHeaders;
        }
        @Override
        public Enumeration<String> getHeaders(String name) {
            if (null != name) {
                Vector<String> vector = new Vector<>();
                vector.add(this.customHeaders.get(name));
                return vector.elements();
            }
            return super.getHeaders(null);
        }
    }
}
