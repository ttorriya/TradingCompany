package databaseWork;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;
 import	org.postgresql.jdbc2.optional.ConnectionPool;
import org.postgresql.jdbc3.Jdbc3ConnectionPool;
import org.postgresql.jdbc3.Jdbc3PoolingDataSource;


public final class DatabaseHandler{
    private static DatabaseHandler database;

    private Connection connection;
    private Statement statement;
    private String URL;
    private String dbUser;
    private String dbPass;

    private DatabaseHandler() throws SQLException {
        try (
                InputStream input = new FileInputStream("C:\\Users\\Admin\\Desktop\\Trading-Company\\src\\Trading-Company-server\\src\\main\\resources\\config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            dbUser = prop.getProperty("db.user");
            dbPass = prop.getProperty("db.password");
            URL = prop.getProperty("db.url");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        connection = DriverManager.getConnection(URL,dbUser, dbPass);
    }

    public static DatabaseHandler getInstance() throws SQLException {
        if (database == null) {
            database = new DatabaseHandler();
        }
        return database;
    }

    public ResultSet getResultSet(String str) throws SQLException {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(str);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultSet;

    }

    public void execute(String str) throws SQLException {
        try {
            statement = connection.createStatement();
            statement.execute(str);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
