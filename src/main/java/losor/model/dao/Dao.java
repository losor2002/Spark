package losor.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import losor.model.bean.Bean;

public interface Dao<T extends Bean>
{
    Optional<T> get(T t) throws SQLException;
    
    List<T> getAll(T t) throws SQLException;
    
    void save(T t) throws SQLException;

    void update(T t, T newId) throws SQLException;
    
    void delete(T t) throws SQLException;

    boolean exists(T t) throws SQLException;
}