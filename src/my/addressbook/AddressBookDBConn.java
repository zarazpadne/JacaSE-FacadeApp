package my.addressbook;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.security.rsa.RSACore;

public class AddressBookDBConn {
    private AddressBookDBConn() {
    }
    
    private static AddressBookDBConn instance = null;
    
    private static String dbURL = "jdbc:derby://localhost:1527/addressbookdatabase";
    private static String username = "APP";
    private static String pass = "APP";
    private static Connection connection = null;
    private static Statement statement = null;
    
    public static Connection openConnection() {
        try {
            connection = DriverManager.getConnection(dbURL, username, pass); 
        } catch (Exception except) {
            except.printStackTrace();
        }
        return connection;
    }
    
    public static ResultSet executeSql(String sql) {
        ResultSet rs = null;
        try {
            Connection connection = AddressBookDBConn.openConnection();
            Statement s = connection.createStatement();
            rs = s.executeQuery(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
        AddressBookDBConn.closeConnection();
        return rs;
    }
    
    private static void closeConnection() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }           
        } catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    }
    
    public synchronized static AddressBookDBConn getInstance() {
        if (instance == null) instance = new AddressBookDBConn();
        return instance;
    }
}
