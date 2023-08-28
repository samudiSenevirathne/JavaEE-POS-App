package servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Item");
            ResultSet rst = pstm.executeQuery();
            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");


            JsonArrayBuilder allItems = Json.createArrayBuilder();//create array
            while (rst.next()) {
                String code = rst.getString(1);
                String name = rst.getString(2);
                int qtyOnHand = rst.getInt(3);
                double unitPrice = rst.getDouble(4);

                JsonObjectBuilder itemObject = Json.createObjectBuilder();//create Object
                    itemObject.add("code",code);
                    itemObject.add("description",name);
                    itemObject.add("qty",qtyOnHand);
                    itemObject.add("unitPrice",unitPrice);
                    allItems.add(itemObject.build());
            }

            JsonObjectBuilder response = Json.createObjectBuilder();//create Object
            response.add("state", "OK");
            response.add("message", "Successfully Loaded....!");
            response.add("data", allItems.build());
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
        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

                    PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qty);
                    pstm.setObject(4, unitPrice);
                    resp.addHeader("Content-Type", "application/json");
                    resp.addHeader("Access-Control-Allow-Origin","*");

                    if (pstm.executeUpdate() > 0) {
                        JsonObjectBuilder response =  Json.createObjectBuilder();//create object
                        response.add("state", "OK");
                        response.add("message", "Successfully Added....!");
                        response.add("data", "");
                        resp.getWriter().print(response.build());
                    }


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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject itemJsonObject = reader.readObject();
        String code = itemJsonObject.getString("code");
        String itemName = itemJsonObject.getString("description");
        String qty = itemJsonObject.getString("qty");
        String unitPrice = itemJsonObject.getString("unitPrice");
        String value=itemJsonObject.getString("value");
         if(qty.equals(value)) {

         }else{
             int q = Integer.parseInt(qty);
             int v = Integer.parseInt(value);
             int diff = q - v;
             qty = String.valueOf(diff);
         }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            PreparedStatement pstm3 = connection.prepareStatement("update Item set description=?,qtyOnHand=?,unitPrice=? where code=?");
            pstm3.setObject(1, itemName);
            pstm3.setObject(2, qty);
            pstm3.setObject(3, unitPrice);
            pstm3.setObject(4, code);
            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");
            resp.addHeader("Access-Control-Allow-Headers","content-type");

            if (pstm3.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();//create object
                response.add("state", "OK");
                response.add("message", "Successfully Updated....!");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "1234");

            PreparedStatement pstm2 = connection.prepareStatement("delete from Item where code=?");
            pstm2.setObject(1, code);
            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");


            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder response = Json.createObjectBuilder();//create object
                response.add("state", "OK");
                response.add("message", "Successfully Deleted....!");
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
        resp.addHeader("Access-Control-Allow-Methods", "PUT,DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "content-type");
    }
}
