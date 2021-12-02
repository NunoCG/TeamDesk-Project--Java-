package Model.td.database;

import Model.td.Address;
import Model.td.Contact;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseContact {

    /**
     * get all contacts from DB
     *
     * @return
     * @throws SQLException
     */
    public static ArrayList<Contact> getAllContactsDB() throws SQLException {
        ArrayList<Contact> listContacts = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM contactos");
            while (rst.next()) {
                Contact c = new Contact();
                c.setId(rst.getInt("id"));
                c.setContact(rst.getString("contacto"));
                c.setObs(rst.getString("observacoes"));
                c.setMainContact(rst.getBoolean("contacto_principal"));
                c.setIdTypeContact(rst.getString("id_tipo"));
                c.setNif(rst.getInt("nif_cliente"));
                listContacts.add(c);
            }
            return listContacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * create a contact method
     *
     * @param c
     * @return
     */
    public static boolean insertContactsDB(Contact c) {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into contacto (contacto, observacoes, contacto_principal, id_tipo, nif_cliente) values (?,?,?,?,?)");
            pstmt.setString(1, c.getContact());
            pstmt.setString(2, c.getObs());
            pstmt.setBoolean(3, c.getMainContact());
            pstmt.setString(4, c.getIdTypeContact());
            pstmt.setInt(5, c.getNif());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    /**
     * get all contacts from a client
     *
     * @param nif
     * @return
     * @throws SQLException
     */
    public static ArrayList<Contact> getAllContactByClient(int nif) throws SQLException {
        ArrayList<Contact> listContacts = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select c.*, tc.tipo_contacto From contacto c " +
                    "inner join tipo_contacto tc on tc.id = c.id_tipo where nif_cliente = ?");
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setContact(rs.getString("contacto"));
                contact.setIdTypeContact(String.valueOf(rs.getInt("id_tipo")));
                contact.setMainContact(rs.getBoolean("contacto_principal"));
                contact.setNif(rs.getInt("nif_cliente"));
                contact.setObs(rs.getString("observacoes"));
                listContacts.add(contact);
            }
            return listContacts;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * update a contact method
     *
     * @param c
     * @return
     * @throws SQLException
     */
    public static boolean updateContactDB(Contact c) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("UPDATE contacto SET contacto=?, id_tipo=?, observacoes=?, contacto_principal=?  where id LIKE ?");
            pstmt.setString(1, c.getContact());
            pstmt.setString(2, c.getIdTypeContact());
            pstmt.setString(3, c.getObs());
            pstmt.setBoolean(4, c.getMainContact());
            pstmt.setInt(5, c.getId());
            pstmt.executeUpdate();
            System.out.println("Contacto alterado");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Erro ao alterar o contacto");
            return false;
        }
    }

    /**
     * get contact types
     *
     * @return
     */
    public static ArrayList<String> getContactsTypeDB() {
        ArrayList<String> list = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select tipo_contacto From tipo_contacto");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String s;
                s = rs.getString("tipo_contacto");
                list.add(s);
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Contact getPrincipalContactByClient(int nif) throws SQLException {
        Contact contact = new Contact();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select c.*, tc.tipo_contacto From contacto c " +
                    "inner join tipo_contacto tc on tc.id = c.id_tipo where nif_cliente = ? and contacto_principal = 1");
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                contact.setId(rs.getInt("id"));
                contact.setContact(rs.getString("contacto"));
                contact.setIdTypeContact(rs.getString("tipo_contacto"));
                contact.setMainContact(rs.getBoolean("contacto_principal"));
                contact.setNif(rs.getInt("nif_cliente"));
                contact.setObs(rs.getString("observacoes"));
            }
            return contact;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
