package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{

     boolean save(Connection connection, T dto) throws SQLException;
     boolean delete(Connection connection,String id)throws SQLException;
     boolean update(Connection connection,T dto)throws SQLException;
     ArrayList<T> loadAll(Connection connection)throws SQLException;

}
