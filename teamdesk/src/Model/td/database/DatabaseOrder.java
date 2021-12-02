package Model.td.database;

import Model.td.Order;
import Model.td.helper.Helper;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 */
public class DatabaseOrder {

    /**
     *
     * @return
     * @throws SQLException
     */
    public static ArrayList<Order> getAllOrdersDB() throws SQLException {
        ArrayList<Order> listOrders = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT e.*,ee.estado_encomenda " +
                    "FROM Encomenda e inner join estado_encomenda ee " +
                    "on ee.id = e.id_estado");
            while (rst.next()) {
                Order o = new Order();
                o.setNum(rst.getInt("nr_encomenda"));
                o.setClient(DatabaseClient.findClientByNif(rst.getInt("nif_cliente")));
                o.setBillingAddress(DatabaseAddress.findAddressById(rst.getInt("id_morada_fat")));
                o.setDeliveryAddress(DatabaseAddress.findAddressById(rst.getInt("id_morada_ent")));
                o.setDate(rst.getDate("data_doc").toLocalDate());
                o.setStatus(rst.getString("estado_encomenda"));
                o.setClosed(rst.getBoolean("fechado"));
                o.setLastModificationDate(rst.getDate("data_ultima_mod").toLocalDate());
                listOrders.add(o);
            }
            return listOrders;
        } catch (SQLException throwables) {
            return null;
        }
    }


    /**
     *
     * @param o
     * @return
     */
    public static boolean insertOrderDB(Order o,int status) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into Encomenda"
                    + "(nif_cliente, id_morada_fat, id_morada_ent, data_doc, id_estado, fechado, data_ultima_mod)"
                    + "values (?,?,?,?,?,?,?)");

            pstmt.setInt(1, o.getClient().getNif());
            pstmt.setInt(2, o.getBillingAddress().getId());
            pstmt.setInt(3, o.getDeliveryAddress().getId());
            pstmt.setDate(4, Date.valueOf(o.getDate()));
            pstmt.setInt(5, status);
            pstmt.setBoolean(6, o.isClosed());
            pstmt.setDate(7, Date.valueOf(o.getLastModificationDate()));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            Helper.showAlert(Alert.AlertType.INFORMATION,"Erro","Erro ao inserir","Erro ao inserir a encomenda");
            return false;
        }
    }

    public static double totalOrderByOrder(int numOrder){
        double nr=0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("select * from totalEncomenda where nr_encomenda = ?");
            stmt.setInt(1, numOrder);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                nr =rs.getDouble("total");
            }
            return nr;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return 0;
        }

    }
    public static double getLastNumOrder(){
        int nr=0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT TOP 1 * FROM encomenda ORDER BY nr_encomenda DESC");
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                nr = rs.getInt("nr_encomenda");
            }
            return nr;

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return 0;
        }

    }

    public static ArrayList<String> getAllStatus(){
        ArrayList<String> status= new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM estado_encomenda");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                status.add(rs.getString("estado_encomenda"));
            }
            return status;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            return null;
        }
    }


}
