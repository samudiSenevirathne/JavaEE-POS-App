package lk.ijse.filter.api;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/*")
public class CustomerAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String test = (String) req.getAttribute("test");
//        System.out.println("Customer Do-Get Invoked"+test);
////        resp.addHeader("testing-header","Auther-ddwddw-wfffwfw");
//        resp.addHeader("Auth","");

        String auth = (String) req.getAttribute("Auth");
//        System.out.println("Customer Do-Get Invoked"+test);
        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

    }
}
