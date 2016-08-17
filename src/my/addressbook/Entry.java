/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.addressbook;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author k
 */
@Entity
@Table(name = "ENTRY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entry.findAll", query = "SELECT e FROM Entry e"),
    @NamedQuery(name = "Entry.findByIdEntry", query = "SELECT e FROM Entry e WHERE e.idEntry = :idEntry"),
    @NamedQuery(name = "Entry.findByIdPerson", query = "SELECT e FROM Entry e WHERE e.idPerson = :idPerson"),
    @NamedQuery(name = "Entry.findByIdAddress", query = "SELECT e FROM Entry e WHERE e.idAddress = :idAddress")})
public class Entry implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ENTRY")
    private Integer idEntry;
    @Basic(optional = false)
    @Column(name = "ID_PERSON")
    private int idPerson;
    @Basic(optional = false)
    @Column(name = "ID_ADDRESS")
    private int idAddress;
    
    private static String dbURL = "jdbc:derby://localhost:1527/addressbookdatabase";
    
    private Person p;
    private Address a;
    
    public Entry() {
    }
    
    public Entry(Person p, Address a) {
        this.p = p;
        this.idPerson = p.getIdPerson();
        this.a = a;
        this.idAddress = a.getIdAddress();
        this.idEntry = create();
    }
    
    public Entry(int id) {
        /*
        polacz do bazy
        pobierz dany element o podanym id
        dla tego obiektu pobiez id addr i person
        p = Person(id)
        a = addr(id)
        */
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "SELECT id_Person, id_Address FROM ENTRY WHERE id_Entry = " + id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                this.idEntry = id;
                this.p = new Person(rs.getInt(1));
                this.idPerson = p.getIdPerson();
                this.a = new Address(rs.getInt(2));
                this.idAddress = a.getIdAddress();
            } 
            s.close();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    private int create(){
        int id = -1;
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "INSERT INTO entry(id_person, id_address) VALUES("+this.idPerson+"," +this.idAddress+")";
            s.executeUpdate(sql, 1);
            ResultSet rs = s.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
        return id;
    }
    
    public void save(){
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "UPDATE entry SET person_id='"+this.idPerson+"' WHERE ID_ENTRY="+this.idEntry;
            s.execute(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    public void remove() {
        p.remove();
        a.remove();
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "DELETE FROM ENTRY WHERE ID_ENTRY="+this.idEntry;
            s.execute(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public Integer getIdEntry() {
        return idEntry;
    }

    public void setIdEntry(Integer idEntry) {
        Integer oldIdEntry = this.idEntry;
        this.idEntry = idEntry;
        changeSupport.firePropertyChange("idEntry", oldIdEntry, idEntry);
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        int oldIdPerson = this.idPerson;
        this.idPerson = idPerson;
        changeSupport.firePropertyChange("idPerson", oldIdPerson, idPerson);
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        int oldIdAddress = this.idAddress;
        this.idAddress = idAddress;
        changeSupport.firePropertyChange("idAddress", oldIdAddress, idAddress);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEntry != null ? idEntry.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Entry)) {
            return false;
        }
        Entry other = (Entry) object;
        if ((this.idEntry == null && other.idEntry != null) || (this.idEntry != null && !this.idEntry.equals(other.idEntry))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return p.getFirstName() + " " + p.getLastName() + ", " + a.getStreet() + ", " + a.getZipCode() + " " + a.getCity() + ", " + a.getCountry();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
