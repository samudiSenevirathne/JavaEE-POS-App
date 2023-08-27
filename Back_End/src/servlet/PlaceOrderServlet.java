package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/orders"})
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from orders");
            ResultSet rst = pstm.executeQuery();
            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");



            JsonArrayBuilder allOrders = Json.createArrayBuilder();//create array
            while (rst.next()) {
                String oid = rst.getString(1);
                String date = rst.getString(2);
                String cusId = rst.getString(3);

                JsonObjectBuilder orderObject = Json.createObjectBuilder();//create object
                orderObject.add("oid", oid);
                orderObject.add("date", date);
                orderObject.add("cusId", cusId);
                allOrders.add(orderObject.build());
            }

            JsonObjectBuilder response = Json.createObjectBuilder();//create object
            response.add("state", "OK");
            response.add("message", "Successfully Loaded....!");
            response.add("data", allOrders.build());
            resp.getWriter().print(response.build());


        } catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException(e);
            JsonObjectBuilder response = Json.createObjectBuilder();//create object
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject itemJsonObject = reader.readObject();
        String oid = itemJsonObject.getString("oid");
        String date = itemJsonObject.getString("date");
        String cusId = itemJsonObject.getString("cusId");
        String itemCode = itemJsonObject.getString("itemCode");
        String qty = itemJsonObject.getString("qty");
        String unitPrice = itemJsonObject.getString("unitPrice");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            PreparedStatement pstm3 = connection.prepareStatement("insert into orders values(?,?,?)");
            pstm3.setObject(1, oid);
            pstm3.setObject(2,date);
            pstm3.setObject(3, cusId);

            PreparedStatement pstm4 = connection.prepareStatement("insert into orderdetails values(?,?,?,?)");
            pstm4.setObject(1, oid);
            pstm4.setObject(2,itemCode);
            pstm4.setObject(3, qty);
            pstm4.setObject(4, unitPrice);

            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");

            if (pstm3.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();//create object
                response.add("state", "OK");
                response.add("message", "Successfully Added....!");
                response.add("data", "");
                resp.getWriter().print(response.build());
            }


        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();//create object
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
    }
}
