package com.xcr.orange.oa.core.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/9/5
 */
public class SimpleCORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //HttpServletRequest req = (HttpServletRequest)servletRequest;
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,OPTIONS,HEAD,PUT,DELETE");
        response.setHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept,Authorization");
        response.setHeader("Access-Control-Max-Age","3600");

        //response.setHeader("Access-Control-Allow-Headers","Content-Type,Authorization");
        //response.setHeader("X-Frame-Options","ALLOW-FROM");
        //response.setHeader("Access-Control-Allow-Credentials","true");
        //response.setHeader("Content-Security-Policy","frame-ancestors *;connect-src *");
        //response.setHeader("Set-Cookie","JSESSIONID="+req.getSession().getId()+";SameSite=None;Secure");
        //if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) servletRequest).getMethod())) {
        //    response.setStatus(HttpServletResponse.SC_OK);
        //}else {
        //}
        filterChain.doFilter(servletRequest,servletResponse);

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
