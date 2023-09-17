package servlet;

import bo.BOFactory;
import bo.custom.ItemBO;
import bo.custom.OrderBO;
import bo.custom.OrderDetailBO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
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
import java.util.ArrayList;

@WebServlet(urlPatterns = {"/pages/orders"})
public class PurchaseOrderServletAPI extends HttpServlet {

    private final OrderBO orderBO = (OrderBO) BOFactory.getBoFactory().getBo(BOFactory.BOType.ORDER);
    private final OrderDetailBO orderDetailBO = (OrderDetailBO) BOFactory.getBoFactory().getBo(BOFactory.BOType.ORDER_DETAIL);
    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBo(BOFactory.BOType.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources

            ArrayList<OrderDTO> orderDTOS = orderBO.loadAllIOrders(connection);

            JsonArrayBuilder allOrders = Json.createArrayBuilder();//create array
               for(OrderDTO odto:orderDTOS){
                JsonObjectBuilder orderObject = Json.createObjectBuilder();//create object
                orderObject.add("oid", odto.getOid());
                orderObject.add("date", odto.getDate());
                orderObject.add("cusId", odto.getCustomerID());
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

            OrderDTO orderDTO=new OrderDTO(oid,date,cusId);

            if (!(orderBO.saveOrder(connection,orderDTO))) {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
                throw new SQLException("order cannot be insert");
            }

                for(JsonValue orderDetail:orderDetailArray) {
                    JsonObject jsonObject = orderDetail.asJsonObject();

                    OrderDetailDTO orderDetailDTO=new OrderDetailDTO(jsonObject.getString("o_id"),jsonObject.getString("itemCode_"),Integer.parseInt(jsonObject.getString("value_")),Double.parseDouble(jsonObject.getString("unitPrice")));

                    if (!(orderDetailBO.saveOrderDetail(connection,orderDetailDTO))) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        connection.close();
                        throw new SQLException("orderDetail cannot be insert");
                    }

                }

                for(JsonValue itemDetail:itemArray) {
                   JsonObject jsonObject = itemDetail.asJsonObject();

                    ItemDTO itemDTO=new ItemDTO(jsonObject.getString("item_code"),Integer.parseInt(jsonObject.getString("val_ue")));

                    if (!(itemBO.editItemQty(connection,itemDTO))) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        connection.close();
                        throw new SQLException("Item cannot be updated");
                    }

                }

                        connection.commit();
                        connection.setAutoCommit(true);
                        connection.close();
                        //create the response Object
                        resp.getWriter().print(ResponseUtil.getJson("OK","Order Success....!"));


        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }

    }

}
