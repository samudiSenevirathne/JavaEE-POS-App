package lk.ijse.filter.filters;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")/*/customer*/
public class CustomerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Customer Filter init");/*Filter eka start karaganna wita deyak karaganna thiyenne */
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Before:Customer Filter do-Filter method Invoked");

        HttpServletRequest req= (HttpServletRequest) servletRequest;/*cast kara atha*/
        HttpServletResponse resp= (HttpServletResponse) servletResponse;/*cast kara atha*/

//        req.setAttribute("test","set from filter");
//
//        String name=servletRequest.getParameter("name");
//        if(name!=null && name.equals("Iman")){
//            filterChain.doFilter(servletRequest,servletResponse);/*servlet ekkat yanna kalin yawanawada nadda kiyala balanawa(can Filter)m eline eke dala thibboyh yanawa naththan yanne naa */
//            String header=resp.getHeader("testing-header");
//            System.out.println(header);
//        }else{
//            resp.setStatus(500);
//            resp.getWriter().write("User not Available");
//            System.out.println("none Authenticated User");
//        }

        req.setAttribute("Auth","username=admin,pass=admin");/*create header*/

        String username=servletRequest.getParameter("username");
        String password=servletRequest.getParameter("password");

        if(username.equals("admin")&&password.equals("admin")){
            filterChain.doFilter(servletRequest,servletResponse);/*servlet ekkat yanna kalin yawanawada nadda kiyala balanawa(can Filter)m eline eke dala thibboyh yanawa naththan yanne naa */

        }else{
            JsonObjectBuilder response = Json.createObjectBuilder();//create object
            response.add("state", "error");
            response.add("message", "Invalid User....!");
            response.add("data", "");
//            resp.setStatus(500);
        }


        System.out.println("After:Customer Filter do-Filter method Invoked");
    }

    @Override
    public void destroy() {
        System.out.println("Customer-Filter destroy");
    }
}
