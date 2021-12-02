package Model.td.database;

import Model.td.Address;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseAddress {

    /**
     * get all address from DB
     * @return
     * @throws SQLException
     */
    public static ArrayList<Address> getAllAddressesDB() throws SQLException {
        ArrayList<Address> listAddress = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM morada");
            while (rst.next()) {
                Address add = new Address();
                add.setId(rst.getInt("id"));
                add.setAddress(rst.getString("morada"));
                add.setPostalCode(rst.getString("codigo_postal"));
                add.setLocation(rst.getString("localidade"));
                add.setCountry(rst.getString("pais"));
                add.setMainAddress(rst.getBoolean("morada_principal"));
                add.setNif(rst.getInt("nif_cliente"));
                listAddress.add(add);
            }
            return listAddress;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     *  create an address method
     * @param add
     * @return
     */
    public static boolean insertAddressDB(Address add) {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into morada"
                    + "(morada, codigo_postal, localidade, pais, morada_principal, nif_cliente)"
                    + "values (?,?,?,?,?,?)");
            pstmt.setString(1, add.getAddress());
            pstmt.setString(2, add.getPostalCode());
            pstmt.setString(3, add.getLocation());
            pstmt.setString(4, add.getCountry());
            pstmt.setBoolean(5, add.getMainAddress());
            pstmt.setInt(6, add.getNif());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    /**
     * get all address from a client
     * @param nif
     * @return
     * @throws SQLException
     */
    public static ArrayList<Address> getAllAddressesByClient(int nif) throws SQLException {
        ArrayList<Address> listAddress = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From morada where nif_cliente = ?");
            stmt.setInt(1, nif);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Address add = new Address();
                add.setId(rs.getInt("id"));
                add.setAddress(rs.getString("morada"));
                add.setPostalCode(rs.getString("codigo_postal"));
                add.setLocation(rs.getString("localidade"));
                add.setCountry(rs.getString("pais"));
                add.setMainAddress(rs.getBoolean("morada_principal"));
                add.setNif(rs.getInt("nif_cliente"));
                listAddress.add(add);
            }
            return listAddress;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * find an address by an id
     * @param id
     * @return
     * @throws SQLException
     */
    public static Address findAddressById(int id) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From morada where id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Address address = new Address();
            if (rs.next()) {
                address.setId(rs.getInt("id"));
                address.setAddress(rs.getString("morada"));
                address.setPostalCode(rs.getString("codigo_postal"));
                address.setLocation(rs.getString("localidade"));
                address.setCountry(rs.getString("pais"));
                address.setMainAddress(rs.getBoolean("morada_principal"));
                address.setNif(rs.getInt("nif_cliente"));
            }

            return address;
        } catch (SQLException e) {
            return null;
        }
    }


    /**
     * update method to an address
     * @param add
     * @return
     * @throws SQLException
     */
    public static boolean updateAddressDB(Address add) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("UPDATE morada SET morada=?, codigo_postal=?, localidade=?, pais=?, morada_principal=? where id LIKE ?");
            pstmt.setString(1, add.getAddress());
            pstmt.setString(2, add.getPostalCode());
            pstmt.setString(3, add.getLocation());
            pstmt.setString(4, add.getCountry());
            pstmt.setBoolean(5, add.getMainAddress());
            pstmt.setInt(6, add.getId());
            pstmt.executeUpdate();
            System.out.println("Morada alterada");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar a morada");
            return false;
        }
    }
    public static Address findAddressMainById(int id, boolean main) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From morada where id = ? and morada_principal = ?");
            stmt.setInt(1, id);
            stmt.setBoolean(2, main);
            ResultSet rs = stmt.executeQuery();
            Address address = new Address();
            if (rs.next()) {
                address.setId(rs.getInt("id"));
                address.setAddress(rs.getString("morada"));
                address.setPostalCode(rs.getString("codigo_postal"));
                address.setLocation(rs.getString("localidade"));
                address.setCountry(rs.getString("pais"));
                address.setMainAddress(rs.getBoolean("morada_principal"));
                address.setNif(rs.getInt("nif_cliente"));
            }

            return address;
        } catch (SQLException e) {
            return null;

        }
    }
}
