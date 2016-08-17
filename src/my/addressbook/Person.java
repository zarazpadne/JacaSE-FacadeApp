/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.addressbook;

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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "PERSON")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findByIdPerson", query = "SELECT p FROM Person p WHERE p.idPerson = :idPerson"),
    @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.lastName = :lastName")})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_PERSON")
    private Integer idPerson;
    @Basic(optional = false)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "LAST_NAME")
    private String lastName;
    private static String dbURL = "jdbc:derby://localhost:1527/addressbookdatabase";
    
    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idPerson = this.create();
    }
    
    public Person(int id) {
        /*
        lacz do bazy
        pobiera person o danym id
        idPerson = id
        analogicznie w address
        */
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "SELECT first_name, last_name FROM PERSON WHERE id_person = " + id;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                this.firstName = rs.getString(1);
                this.lastName = rs.getString(2);
                this.idPerson = id;
            } 
            s .close();
        } catch (Exception except) {
            except.printStackTrace();
        }
    }

    private int create(){
        int idPerson = -1;
        try {
            Connection connection = DriverManager.getConnection(dbURL); 
            Statement s = connection.createStatement();
            String sql = "INSERT INTO person(first_name, last_name) VALUES('"+this.firstName+"', '" + this.lastName +"')";
            s.executeUpdate(sql, 1);
            ResultSet rs = s.getGeneratedKeys();
            if (rs.next()) {
                idPerson = rs.getInt(1);
            }
        } catch (Exception except) {
            except.printStackTrace();
        }
        return idPerson;
    }
    
    
    public void remove() {
        try {
            Connection connection = DriverManager.getConnection(dbURL,"APP","APP"); 
            Statement s = connection.createStatement();
            String sql = "DELETE FROM PERSON WHERE ID_PERSON="+this.idPerson;
            s.execute(sql);
        } catch (Exception except) {
            except.printStackTrace();
        }
    }
    
    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPerson != null ? idPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.idPerson == null && other.idPerson != null) || (this.idPerson != null && !this.idPerson.equals(other.idPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "my.addressbook.Person[ idPerson=" + idPerson + " ]";
    }
    
}
