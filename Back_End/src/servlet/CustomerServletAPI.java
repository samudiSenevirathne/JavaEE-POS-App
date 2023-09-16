package servlet;


import util.ResponseUtil;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet rst = pstm.executeQuery();
               resp.addHeader("Content-Type","application/json");/**/
               resp.addHeader("Access-Control-Allow-Origin","*");



            JsonArrayBuilder allCustomers = Json.createArrayBuilder();//create array
            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);

                JsonObjectBuilder customerObject = Json.createObjectBuilder();//create object
                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);
                customerObject.add("salary", salary);
                allCustomers.add(customerObject.build());
            }

            //create the response Object
             resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Loaded....!",allCustomers.build()));


        } catch (ClassNotFoundException | SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata Support,Json Support
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("cusSalary");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

                PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
                pstm.setObject(1, cusID);
                pstm.setObject(2, cusName);
                pstm.setObject(3, cusAddress);
                pstm.setObject(4, cusSalary);
                resp.addHeader("Content-Type", "application/json");/*if you want to alert in json you have to use it again(my point) !!!*/
                resp.addHeader("Access-Control-Allow-Origin","*");

                if (pstm.executeUpdate() > 0) {
                    //create the response Object
                    resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Added....!"));
                }

            } catch(ClassNotFoundException | SQLException e){
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
            }
        }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  //QueryString Support,Formdata NotSupport,Json Support
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customerJsonObject = reader.readObject();
        String cusID =customerJsonObject.getString("id");
        String cusName = customerJsonObject.getString("name");
        String cusAddress =customerJsonObject.getString("address");
        String cusSalary =customerJsonObject.getString("salary");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("update Customer set name=?,address=?,salary=? where id=?");
                    pstm.setObject(4, cusID);
                    pstm.setObject(1, cusName);
                    pstm.setObject(2, cusAddress);
                    pstm.setObject(3, cusSalary);
                    resp.addHeader("Content-Type","application/json");/*if you want to alert in json you have to use it again(my point) !!!*/
            resp.addHeader("Access-Control-Allow-Origin","*");
            resp.addHeader("Access-Control-Allow-Headers","content-type");


                    if (pstm.executeUpdate() > 0) {
                        //create the response Object
                        resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Updated....!"));
                    }

        } catch (ClassNotFoundException | SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support
        String id = req.getParameter("id");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("delete from Customer where id=?");
                    pstm.setObject(1, id);
                    resp.addHeader("Content-Type","application/json");/*if you want to alert in json you have to use it again(my point) !!!*/
                    resp.addHeader("Access-Control-Allow-Origin","*");


                    if (pstm.executeUpdate() > 0) {
                        //create the response Object
                        resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Deleted....!"));
                    }

        } catch (ClassNotFoundException | SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "PUT,DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }
}
