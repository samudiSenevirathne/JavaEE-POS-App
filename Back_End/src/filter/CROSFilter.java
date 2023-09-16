package filter;

import util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",filterName = "A")
public class CROSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Auth Filter DO Filter Invoked");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String auth = req.getHeader("Auth");

        if(auth != null  &&  auth.equals("user=samuDi,pass=samu@123")){
             filterChain.doFilter(servletRequest,servletResponse);
        }else{
            resp.addHeader("Content-Type","application/json");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().print(ResponseUtil.getJson("Auth-Error", "You are not Authenticated to use this Service.!"));
        }
    }

    @Override
    public void destroy() {

    }
}
