package servlet;


import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;
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

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServletAPI extends HttpServlet {

    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBo(BOFactory.BOType.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support
        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()){  //used try-resources

            ArrayList<CustomerDTO> customerDTOS = customerBO.loadAllCustomers(connection);

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();//create array
           for(CustomerDTO cdto:customerDTOS){
                JsonObjectBuilder customerObject = Json.createObjectBuilder();//create object
                customerObject.add("id", cdto.getId());
                customerObject.add("name", cdto.getName());
                customerObject.add("address", cdto.getAddress());
                customerObject.add("salary", cdto.getSalary());
                allCustomers.add(customerObject.build());
            }

            //create the response Object
             resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Loaded....!",allCustomers.build()));


        } catch (SQLException e) {
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

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) { //used try-resources

            CustomerDTO customerDTO=new CustomerDTO(cusID,cusName,cusAddress,cusSalary);


                if (customerBO.saveCustomer(connection,customerDTO)) {
                    //create the response Object
                    resp.getWriter().print(ResponseUtil.getJson("OK","Successfully Added....!"));
                }

            } catch(SQLException e){
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

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try(Connection connection = pool.getConnection()) { //used try-resources

            CustomerDTO customerDTO=new CustomerDTO(cusID,cusName,cusAddress,cusSalary);

                    if (customerBO.editCustomer(connection,customerDTO)) {
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //QueryString Support,Formdata NotSupport,Json Support
        String id = req.getParameter("id");

        ServletContext servletContext = getServletContext();
        BasicDataSource pool = (BasicDataSource) servletContext.getAttribute("dbcp");
        try (Connection connection = pool.getConnection()){ //used try-resources

                    if (customerBO.deleteCustomer(connection,id)) {
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
