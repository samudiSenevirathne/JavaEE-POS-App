package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",filterName = "Z")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Cross Filter DO Filter Invoked");

        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        //we can check what is the HTTP method
        String method = req.getMethod();

        //forward every request to requested servlet
        filterChain.doFilter(servletRequest,servletResponse);

        if(method.equals("OPTIONS")){
            resp.setStatus(200);
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "PUT,DELETE");
            resp.addHeader("Access-Control-Allow-Headers", "content-type,auth");
        }else{
            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");
        }
    }

    @Override
    public void destroy() {

    }

    /*Application eka kotas walata kadeema nisa needs this policy(CROS Policy).*/
}
