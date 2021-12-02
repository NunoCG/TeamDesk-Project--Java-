package Model.td;

import java.util.ArrayList;

/**
 * This class represents this program's Clients
 */
public class  Client extends Person {

    private int nif;
    private String notes;
    private ArrayList<Address> addresses;
    private ArrayList<Contact> contacts;

    /**
     *
     * @param name
     * @param gender
     * @param id
     * @param nif
     * @param notes
     */
    public Client(String name, String gender, String id, int nif, String notes,ArrayList<Address> addresses,ArrayList<Contact> contacts) {
        super(name, gender, id);
        this.nif = nif;
        this.notes = notes;
        this.addresses = addresses;
        this.contacts = contacts;
    }

    /**
     * Empty constructor
     */
    public Client() {
    }

    /**
     *
     * @return
     */
    public int getNif() {
        return nif;
    }

    /**
     *
     * @param nif
     */
    public void setNif(int nif) {
        this.nif = nif;
    }

    /**
     *
     * @return
     */
    public String getNotes() {
        return notes;
    }

    /**
     *
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {

        return  "Client{" +
                "nome=" + getName()+ '\'' +
                "nif='" + nif + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
