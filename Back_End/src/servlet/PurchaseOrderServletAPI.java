package servlet;

import org.apache.commons.dbcp2.BasicDataSource;
import util.ResponseUtil;

import javax.json.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/orders"})
public class PurchaseOrderServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources
            PreparedStatement pstm = connection.prepareStatement("select * from orders");
            ResultSet rst = pstm.executeQuery();


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

            //create the response Object
            resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Loaded....!",allOrders.build()));


        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject JsonObject = reader.readObject();
        String oid = JsonObject.getString("oid");
        String date = JsonObject.getString("date");
        String cusId = JsonObject.getString("cusId");
        JsonArray orderDetailArray=JsonObject.getJsonArray("orderDetailArray");
        JsonArray itemArray = JsonObject.getJsonArray("itemArray");

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try {
            Connection connection = pool.getConnection();
            connection.setAutoCommit(false);

                PreparedStatement pstm = connection.prepareStatement("insert into orders values(?,?,?)");
                pstm.setObject(1, oid);
                pstm.setObject(2, date);
                pstm.setObject(3, cusId);

            if (!(pstm.executeUpdate() > 0)) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new SQLException("order cannot be insert");
            }

                for(JsonValue orderDetail:orderDetailArray) {
                    JsonObject jsonObject = orderDetail.asJsonObject();
                    PreparedStatement pstm1 = connection.prepareStatement("insert into orderdetails values(?,?,?,?)");
                        pstm1.setObject(1, jsonObject.getString("o_id"));
                        pstm1.setObject(2, jsonObject.getString("itemCode_"));
                        pstm1.setObject(3, jsonObject.getString("value_"));
                        pstm1.setObject(4, jsonObject.getString("unitPrice"));

                    if (!(pstm1.executeUpdate() > 0)) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw new SQLException("orderDetail cannot be insert");
                    }

                }

                for(JsonValue itemDetail:itemArray) {
                   JsonObject jsonObject = itemDetail.asJsonObject();
                    PreparedStatement pstm2 = connection.prepareStatement("update item set qtyOnHand=qtyOnHand-? where code=?");
                        pstm2.setObject(1,Integer.parseInt(jsonObject.getString("val_ue")));
                        pstm2.setObject(2, jsonObject.getString("item_code"));

                    if (!(pstm2.executeUpdate() > 0)) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw new SQLException("Item cannot be updated");
                    }

                }

                        connection.commit();
                        connection.setAutoCommit(true);
                        //create the response Object
                        resp.getWriter().print(ResponseUtil.getJson("OK","Order Success....!"));


        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }

    }

}
