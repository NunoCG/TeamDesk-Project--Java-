package Model.td.database;

import Model.td.ItemOrder;
import Model.td.Order;
import Model.td.helper.Helper;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseItemOrder {
    public static ArrayList<ItemOrder> getAllItensOrder() {
        ArrayList<ItemOrder> items = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM linha_encomenda");
            while (rs.next()) {
                ItemOrder io = new ItemOrder();
                io.setId(rs.getInt("id"));
                io.setQuantity(rs.getDouble("quantidade"));
                io.setDiscount(rs.getDouble("desconto"));
                io.setPrice(rs.getDouble("preco"));
                io.setLine_notes(rs.getString("preco"));
                io.setProduct(DatabaseProduct.getProductById(rs.getInt("id_produto")));
                items.add(io);
            }
            return items;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produtos a base de dados!");
            return null;
        }
    }
    public static ArrayList<ItemOrder> getAllItensOrderByOrder(int nrEncomenda) {
        ArrayList<ItemOrder> items = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * FROM linha_encomenda where nr_encomenda = ?");
            stmt.setInt(1, nrEncomenda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItemOrder io = new ItemOrder();
                io.setId(rs.getInt("id"));
                io.setQuantity(rs.getDouble("quantidade"));
                io.setDiscount(rs.getDouble("desconto"));
                io.setPrice(rs.getDouble("preco"));
                io.setLine_notes(rs.getString("preco"));
                io.setProduct(DatabaseProduct.getProductById(rs.getInt("id_produto")));
                items.add(io);
            }
            return items;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produtos a base de dados!");
            return null;
        }
    }
    public static boolean insertItemOrderDB(int nrEncomenda,ItemOrder io) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into linha_encomenda"
                    + "(nr_encomenda,id_produto,quantidade,unidade,preco,desconto,nota_linha)"
                    + "values (?,?,?,?,?,?,?)");

            pstmt.setInt(1, nrEncomenda);
            pstmt.setInt(2, io.getProduct().getId());
            pstmt.setDouble(3, io.getQuantity());
            pstmt.setString(4, io.getProduct().getUnity());
            pstmt.setDouble(5, io.getPrice());
            pstmt.setDouble(6, io.getDiscount());
            pstmt.setString(7, io.getLine_notes());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            Helper.showAlert(Alert.AlertType.INFORMATION,"Erro","Erro ao inserir","Erro ao inserir a linha encomenda");
            return false;
        }
    }
}
