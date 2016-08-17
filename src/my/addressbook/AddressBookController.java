package my.addressbook;

import java.util.ArrayList;

public class AddressBookController {   
    AddressBook addressBook = AddressBook.getInstance();
    
    public Person createNewPerson(String firstName, String lastName) {
        Person p = new Person(firstName, lastName);
        return p;
    }
    
    public Address createNewAddress(String street, String zipCode, String city, String country) {
        Address a = new Address(street, zipCode, city, country);
        return a;
    }
    
    public Entry createNewEntry(Person p, Address a) {
        Entry e = new Entry(p, a);
        return e;
    }
    
    public ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries = addressBook.getEntryList();
        return entries;
    }
    
    public int getEntryListSize() {
        return addressBook.getEntryListSize();
    }
    
    public void removeEntry(Entry entry) {
        entry.remove();
    }
}
