package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO{

    public boolean save(Connection connection, T dto)throws SQLException;
    public boolean delete(Connection connection,String id)throws SQLException;
    public boolean update(Connection connection,T dto)throws SQLException;
    public ArrayList<T> loadAll(Connection connection)throws SQLException;

}
