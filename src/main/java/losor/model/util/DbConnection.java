package losor.model.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DbConnection
{
	private static final DataSource instance = getDataSource();

	private DbConnection() {}

    private static DataSource getDataSource()
    {
        try
        {
		    Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            return (DataSource) envCtx.lookup("jdbc/SparkDB");
        }
        catch(NamingException e)
        {
            throw new RuntimeException("Errore nel prendere il DataSource da JNDI");
        }
    }

	public static Connection getConnection() throws SQLException
    {
		return instance.getConnection();
	}
}