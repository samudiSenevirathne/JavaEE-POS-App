package servlet;

import bo.BOFactory;
import bo.custom.ItemBO;
import dto.ItemDTO;
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

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServletAPI extends HttpServlet {

    private final ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBo(BOFactory.BOType.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources

            ArrayList<ItemDTO> itemDTOS = itemBO.loadAllItems(connection);

            JsonArrayBuilder allItems = Json.createArrayBuilder();//create array
            for(ItemDTO idto:itemDTOS) {
                JsonObjectBuilder itemObject = Json.createObjectBuilder();//create Object
                    itemObject.add("code",idto.getCode());
                    itemObject.add("description",idto.getDescription());
                    itemObject.add("qty",idto.getQtyOnHand());
                    itemObject.add("unitPrice",idto.getUnitPrice());
                    allItems.add(itemObject.build());
            }

            //create the response Object
            resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Loaded....!",allItems.build()));


        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources

            ItemDTO itemDTO=new ItemDTO(code,itemName,Integer.parseInt(qty),Double.parseDouble(unitPrice));

                    if (itemBO.saveItem(connection,itemDTO)) {
                        //create the response Object
                        resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Added....!"));
                    }


        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
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

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources

            ItemDTO itemDTO=new ItemDTO(code,itemName,Integer.parseInt(qty),Double.parseDouble(unitPrice));

            if (itemBO.editItem(connection,itemDTO)) {
                //create the response Object
                resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Updated....!"));
            }

        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) {  //used try-resources

            if (itemBO.deleteItem(connection,code)) {
                //create the response Object
                resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Deleted....!"));
            }

        } catch (SQLException e) {
            //create the response Object
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.getJson("Error",e.getMessage()));
        }
    }

}
