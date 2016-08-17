package my.addressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AddressBook {
    
    private static String dbURL = "jdbc:derby://localhost:1527/addressbookdatabase";
    
    private static AddressBook instance = null;
    
    private int entryListSize = 0;
    private ArrayList<Entry> entryList = new ArrayList<Entry>();
    
    private AddressBook() {
        /*
        polacz do bazy
            pobierz ID wszystkich entry 
            dla kazdego id: Entry(id)
            dodaj do entryList
        */
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "SELECT id_Entry FROM ENTRY";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                Entry e = new Entry(id);
                entryList.add(e);
            } 
            s .close();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    public synchronized static AddressBook getInstance() {
        if (instance == null) instance = new AddressBook();
        return instance;
    }
    
    public ArrayList<Entry> getEntryList() {
        return entryList;
    } 
    
    public int getEntryListSize() {
        return entryList.size();
    }
}
