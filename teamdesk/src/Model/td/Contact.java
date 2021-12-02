package Model.td;

import Model.td.database.DatabaseContact;

import java.util.ArrayList;

/**
 *
 */
public class Contact {

    private int id;
    private String contact;
    private boolean mainContact;
    private String obs;
    private String idTypeContact;
    private int nif_client;

    /**
     *
     * @param id
     * @param contact
     * @param mainContact
     * @param obs
     * @param idTypeContact
     * @param nif_client
     */
    public Contact(int id, String contact, boolean mainContact, String obs, String idTypeContact, int nif_client) {
        this.id = id;
        this.contact = contact;
        this.mainContact = mainContact;
        this.obs = obs;
        this.idTypeContact = idTypeContact;
        this.nif_client = nif_client;
    }

    /**
     *
     */
    public Contact() {}

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getContact() {
        return contact;
    }

    /**
     *
     * @param contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     *
     * @return
     */
    public boolean getMainContact() {
        return mainContact;
    }

    /**
     *
     * @param mainContact
     */
    public void setMainContact(boolean mainContact) {
        this.mainContact = mainContact;
    }

    /**
     *
     * @return
     */
    public String getObs() {
        return obs;
    }

    /**
     *
     * @param obs
     */
    public void setObs(String obs) {
        this.obs = obs;
    }

    /**
     *
     * @return
     */
    public String getIdTypeContact() {
        return idTypeContact;
    }

    /**
     *
     * @param idTypeContact
     */
    public void setIdTypeContact(String idTypeContact) {
        this.idTypeContact = idTypeContact;
    }

    /**
     *
     * @return
     */
    public int getNif() {
        return nif_client;
    }

    /**
     *
     * @param nif
     */
    public void setNif(int nif) {
        this.nif_client = nif;
    }

    public static String findIdfromContact(String tipo) {
        ArrayList<String> l = DatabaseContact.getContactsTypeDB();
        int b=0;
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).equals(tipo) ){
                b = i + 1 ;
            }
        }
        return String.valueOf(b);
    }

    public static String findContactfromId(String id) {
        ArrayList<String> l = DatabaseContact.getContactsTypeDB();
        int idt= Integer.parseInt(id);
        String nome = "null";
        for (int i = 0; i < l.size(); i++) {
            if (idt ==  i +1){
                nome = l.get(i) ;
            }
        }
        return nome;
    }


    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contact='" + contact + '\'' +
                ", mainContact='" + mainContact + '\'' +
                ", obs='" + obs + '\'' +
                ", idTypeContact='" + idTypeContact + '\'' +
                ", nif=" + nif_client +
                '}';
    }
}
