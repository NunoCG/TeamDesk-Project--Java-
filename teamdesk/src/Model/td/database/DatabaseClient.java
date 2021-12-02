package Model.td.database;

import Model.td.Client;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseClient {

    /**
     *
     * @return
     * @throws SQLException
     */
    public static ArrayList<Client> getAllClientsDB() throws SQLException {
        ArrayList<Client> listClients = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM cliente");
            while (rst.next()) {
                Client c = new Client();
                c.setName(rst.getString("nome"));
                c.setNif(rst.getInt("nif"));
                c.setNotes(rst.getString("notas"));
                c.setContacts(DatabaseContact.getAllContactByClient(c.getNif()));
                c.setAddresses(DatabaseAddress.getAllAddressesByClient(c.getNif()));
                listClients.add(c);
            }
            return listClients;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean insertClientDB(Client c) {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into cliente"
                    + "(nome, nif, notas)"
                    + "values (?,?,?)");
            pstmt.setString(1, c.getName());
            pstmt.setInt(2, c.getNif());
            pstmt.setString(3, c.getNotes());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    /**
     *
     * @param nif
     * @return
     * @throws SQLException
     */
    public static Client findClientByNif(int nif) throws SQLException {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From cliente where nif = ?");
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery();
            Client client = new Client();
            if (rs.next()) {
                client.setName(rs.getString("nome"));
                client.setNif(rs.getInt("nif"));
                client.setNotes(rs.getString("notas"));
                client.setAddresses(DatabaseAddress.getAllAddressesByClient(nif));
                client.setContacts(DatabaseContact.getAllContactByClient(nif));
            }
            return client;
        } catch (SQLException e) {
            return null;
        }

    }

    public static void DeleteClient(Client client) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM cliente WHERE nif = ?");
            stmt.setString(1, String.valueOf(client.getNif()));
            stmt.executeUpdate();
            System.out.println("Eliminado");
        } catch (Exception e) {
            System.err.println("Erro ao eliminar");
        }
    }

    public static boolean updateClientDB(Client c) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("UPDATE cliente " +
                    "SET nif=?, nome=?, notas=? " +
                    "where nif LIKE ?");

            stmt.setString(1, String.valueOf(c.getNif()));
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getNotes());
            stmt.setString(4, String.valueOf(c.getNif()));
            stmt.executeUpdate();
            System.out.println("Cliente alterado");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar o cliente");
            return false;
        }
    }
}
