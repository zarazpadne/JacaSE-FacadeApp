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
@Table(name = "ADDRESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findByIdAddress", query = "SELECT a FROM Address a WHERE a.idAddress = :idAddress"),
    @NamedQuery(name = "Address.findByStreet", query = "SELECT a FROM Address a WHERE a.street = :street"),
    @NamedQuery(name = "Address.findByZipCode", query = "SELECT a FROM Address a WHERE a.zipCode = :zipCode"),
    @NamedQuery(name = "Address.findByCity", query = "SELECT a FROM Address a WHERE a.city = :city"),
    @NamedQuery(name = "Address.findByCountry", query = "SELECT a FROM Address a WHERE a.country = :country")})
public class Address implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ADDRESS")
    private Integer idAddress;
    @Basic(optional = false)
    @Column(name = "STREET")
    private String street;
    @Basic(optional = false)
    @Column(name = "ZIP_CODE")
    private String zipCode;
    @Basic(optional = false)
    @Column(name = "CITY")
    private String city;
    @Basic(optional = false)
    @Column(name = "COUNTRY")
    private String country;
    private static String dbURL = "jdbc:derby://localhost:1527/addressbookdatabase";
    
    public Address() {
    }
    
    public Address(String street, String zipCode, String city, String country) {
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.idAddress = create();
    }
    
    public Address(int id) {
        /*
        lacz do bazy
        pobiera person o danym id
        idPerson = id
        analogicznie w address
        */
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "SELECT street, zip_code, city, country FROM ADDRESS WHERE id_address = " + id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                this.idAddress = id;
                this.street = rs.getString(1);
                this.zipCode = rs.getString(2);
                this.city = rs.getString(3);
                this.country = rs.getString(4);
            } 
            s .close();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    private int create(){
        int id = -1;
        try {
            Connection connection = DriverManager.getConnection(dbURL); 
            Statement s = connection.createStatement();
            String sql = "INSERT INTO address(street, zip_code, city, country) VALUES('"+this.street+"', '" + this.zipCode + "','" + this.city + "','" + this.country+"')";
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
            Connection connection = DriverManager.getConnection(dbURL); 
            Statement s = connection.createStatement();
            String sql = "UPDATE person SET street'"+this.street+ "', zip_code = '" +this.zipCode + "', '" + this.city + "', '" + this.country + "' WHERE ID_ADDRESS="+this.idAddress;
            s.execute(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    public void remove() {
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "DELETE FROM ADDRESS WHERE ID_ADDRESS="+this.idAddress;
            s.execute(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    public Address(Integer idAddress) {
        this.idAddress = idAddress;
        try{
            Connection connection = DriverManager.getConnection(dbURL); 
            Statement s = connection.createStatement();
            String sql = "SELECT street, zip_code, city, country FROM Person WHERE ID_ADDRESS="+idAddress;
            s.execute(sql);
            ResultSet rs = s.getResultSet();

            while(rs.next())
                this.street = rs.getString("street");
                this.zipCode = rs.getString("zip_code");
                this.city = rs.getString("city");
                this.country = rs.getString("country");
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    public Integer getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Integer idAddress) {
        Integer oldIdAddress = this.idAddress;
        this.idAddress = idAddress;
        changeSupport.firePropertyChange("idAddress", oldIdAddress, idAddress);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        String oldStreet = this.street;
        this.street = street;
        changeSupport.firePropertyChange("street", oldStreet, street);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        String oldZipCode = this.zipCode;
        this.zipCode = zipCode;
        changeSupport.firePropertyChange("zipCode", oldZipCode, zipCode);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        String oldCity = this.city;
        this.city = city;
        changeSupport.firePropertyChange("city", oldCity, city);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        String oldCountry = this.country;
        this.country = country;
        changeSupport.firePropertyChange("country", oldCountry, country);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAddress != null ? idAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.idAddress == null && other.idAddress != null) || (this.idAddress != null && !this.idAddress.equals(other.idAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "my.addressbook.Address[ idAddress=" + idAddress + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
}
