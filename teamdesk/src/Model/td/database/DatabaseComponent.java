package Model.td.database;

import Model.td.Component;
import Model.td.Product;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseComponent {

    /**
     * Gets all components from the database and returns an arraylist with them
     */
    public static ArrayList<Component> getAllComponents() {
        ArrayList<Component> components = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Produto where componente = 1");
            while (rs.next()) {
                Component c = new Component();
                c.setIdProduct(rs.getString("referencia"));
                c.setVersionProduct(rs.getString("versao"));
                c.setDesignation(rs.getString("designacao"));
                c.setTradeName(rs.getString("designacao_comercial"));
                c.setUnity(rs.getString("unidade"));
                c.setBatch(rs.getDouble("qtd_lote"));
                c.setQuantity(rs.getDouble("quantidade"));
                c.setStatus(rs.getBoolean("estado"));
                c.setRawMaterial(rs.getBoolean("materia_prima"));
                c.setComponent(rs.getBoolean("componente"));
                components.add(c);
            }
            return components;
        } catch (SQLException e) {
            System.out.println("Não foi possível encontrar componentes na base de dados!");
            return null;
        }
    }

    public static ArrayList<Component> getAllActiveComponents() {
        ArrayList<Component> components = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Produto where componente = 1 AND estado = 1");
            while (rs.next()) {
                Component c = new Component();
                c.setIdProduct(rs.getString("referencia"));
                c.setVersionProduct(rs.getString("versao"));
                c.setDesignation(rs.getString("designacao"));
                c.setTradeName(rs.getString("designacao_comercial"));
                c.setUnity(rs.getString("unidade"));
                c.setBatch(rs.getDouble("qtd_lote"));
                c.setQuantity(rs.getDouble("quantidade"));
                c.setStatus(rs.getBoolean("estado"));
                c.setRawMaterial(rs.getBoolean("materia_prima"));
                c.setComponent(rs.getBoolean("componente"));
                components.add(c);
            }
            return components;
        } catch (SQLException e) {
            System.out.println("Não foi possível encontrar componentes na base de dados!");
            return null;
        }
    }


    /**
     * Searches Components by Operation ID in the database and returns the Components that match the criteria
     *
     * @return ArrayList of Components that match the criteria
     */
    public static ArrayList<Component> getComponentsByOperationID(String idOperation) {
        ArrayList<Component> components = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Produto INNER JOIN Produto_componente_operacao " +
                    "ON Produto.Id = Produto_componente_operacao.Id_componente_pco " +
                    "WHERE Id_operacao_pco LIKE '" + idOperation + "'");
            while (rs.next()) {
                Component c = new Component();
                c.setIdProduct(rs.getString("referencia"));
                c.setVersionProduct(rs.getString("versao"));
                c.setDesignation(rs.getString("designacao"));
                c.setTradeName(rs.getString("designacao_comercial"));
                c.setUnity(rs.getString("unidade"));
                c.setBatch(rs.getDouble("qtd_lote"));
                c.setQuantity(rs.getDouble("quantidade"));
                c.setStatus(rs.getBoolean("estado"));
                c.setRawMaterial(rs.getBoolean("materia_prima"));
                c.setComponent(rs.getBoolean("componente"));
                components.add(c);
            }
            return components;
        } catch (SQLException e) {
            System.out.println("Não foi possível encontrar componentes na base de dados!");
            return null;
        }
    }

    /**
     * Gets the base information of all components in the database and returns an array with the information
     */
    public static ArrayList<Component> getDatasheetComponentDB(int id_operacao) {
        ArrayList<Component> components = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From Produto_componente_operacao pco " +
                    "inner join produto p on p.id = pco.id_componente_pco " +
                    "where pco.id_operacao_pco = ?");
            stmt.setInt(1, id_operacao);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Component component = new Component();
                component.setIdProduct(rs.getString("referencia"));
                components.add(component);
            }
            return components;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    /**
     * Gets the base information of all component's alternative in the database and returns an array with the information
     */
    public static Component getDatasheetAlternativaDB(int id_component, int id_produto) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * From Produto_componente_operacao pco " +
                    "inner join produto p on p.id = pco.id_alternativa_pco " +
                    "where pco.id_componente_pco = ? and pco.id_produto_pco = ?");
            stmt.setInt(1, id_component);
            stmt.setInt(2, id_produto);
            ResultSet rs = stmt.executeQuery();
            Component alternative = new Component();
            if (rs.next()) {
                alternative.setIdProduct(rs.getString("referencia"));
            }
            return alternative;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    public static boolean updateComponent(Component component) throws SQLException {
        try {
            System.out.println(component);
            if (component != null) {
                Connection con = DatabaseConnect.getConnectionDB();
                PreparedStatement pst = con.prepareStatement("UPDATE Produto " +
                        "SET designacao_comercial = ?, estado = ?, unidade = ? " +
                        " WHERE referencia = ? and versao = ?");
                pst.setString(1, component.getTradeName());
                pst.setBoolean(2, component.isStatus());
                pst.setString(3, component.getUnity());
                pst.setString(4, component.getIdProduct());
                pst.setString(5, component.getVersionProduct());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


}

