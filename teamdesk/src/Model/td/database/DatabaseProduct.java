package Model.td.database;

import Model.td.Machine;
import Model.td.Product;
import Model.td.helper.Helper;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseProduct {


    /**
     * Inserts products into the data base
     *
     * @throws SQLException
     */
    public static boolean insertDB(Product product) throws SQLException {
      boolean flag=false;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("insert into produto " +
                    "(referencia,versao,designacao,designacao_comercial,qtd_lote," +
                    "estado,unidade,quantidade,componente,materia_prima) " +
                    "values (?,?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, product.getIdProduct());
            stmt.setString(2, product.getVersionProduct());
            stmt.setString(3, product.getDesignation());
            stmt.setString(4, product.getTradeName());
            stmt.setDouble(5, product.getBatch());
            stmt.setBoolean(6, product.isStatus());
            stmt.setString(7, product.getUnity());
            stmt.setDouble(8, product.getQuantity());
            stmt.setBoolean(9, product.isComponent());
            stmt.setBoolean(10, product.isRawMaterial());
            stmt.executeUpdate();
            flag = true;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            flag = false;
        }
        return flag;
    }


    /**
     * Updates the status of a component. Searches the component by id
     */
    public static boolean updateStatusComponentById(Product product) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("UPDATE produto SET componente = ? WHERE referencia like ?");
            stmt.setBoolean(1, product.isComponent());
            stmt.setString(2, product.getIdProduct());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir na tabela produto");
            return false;
        }
    }

    /**
     * Searches products in the database by their reference and returns their id
     */
    public static int getIdProductDB(Product product) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select id FROM Produto Where referencia = ? and versao = ?");
            stmt.setString(1, product.getIdProduct());
            stmt.setString(2, product.getVersionProduct());
            ResultSet rs = stmt.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
                return id;
            }
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar id a base de dados!");
        }
        return 0;
    }

    /**
     * Gets all the products that exist and are already created in the data base
     *
     * @return list of products
     */
    public static ArrayList<Product> getAllProductsDB() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM Produto");
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setIdProduct(rs.getString("referencia"));
                p.setVersionProduct(rs.getString("versao"));
                p.setDesignation(rs.getString("designacao"));
                p.setTradeName(rs.getString("designacao_comercial"));
                p.setUnity(rs.getString("unidade"));
                p.setBatch(rs.getDouble("qtd_lote"));
                p.setQuantity(rs.getDouble("quantidade"));
                p.setStatus(rs.getBoolean("estado"));
                p.setRawMaterial(rs.getBoolean("materia_prima"));
                p.setComponent(rs.getBoolean("componente"));
                products.add(p);
            }
            return products;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produtos a base de dados!");
            return null;
        }
    }
    public static ArrayList<Product> getAllProductsDBActive() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM Produto where estado = 1");
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setIdProduct(rs.getString("referencia"));
                p.setVersionProduct(rs.getString("versao"));
                p.setDesignation(rs.getString("designacao"));
                p.setTradeName(rs.getString("designacao_comercial"));
                p.setUnity(rs.getString("unidade"));
                p.setBatch(rs.getDouble("qtd_lote"));
                p.setQuantity(rs.getDouble("quantidade"));
                p.setStatus(rs.getBoolean("estado"));
                p.setRawMaterial(rs.getBoolean("materia_prima"));
                p.setComponent(rs.getBoolean("componente"));
                products.add(p);
            }
            return products;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produtos a base de dados!");
            return null;
        }
    }

    public static ArrayList<Product> getAllActiveProductsDB() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("Select * FROM Produto WHERE estado = 1");
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setIdProduct(rs.getString("referencia"));
                p.setVersionProduct(rs.getString("versao"));
                p.setDesignation(rs.getString("designacao"));
                p.setTradeName(rs.getString("designacao_comercial"));
                p.setUnity(rs.getString("unidade"));
                p.setBatch(rs.getDouble("qtd_lote"));
                p.setQuantity(rs.getDouble("quantidade"));
                p.setStatus(rs.getBoolean("estado"));
                p.setRawMaterial(rs.getBoolean("materia_prima"));
                p.setComponent(rs.getBoolean("componente"));
                products.add(p);
            }
            return products;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produtos a base de dados!");
            return null;
        }
    }

    /**
     * Gets the base information of all products in the database and returns an array with that information
     *
     * @return
     */
    public static ArrayList<Product> getAllProductsPCODB() {
        try {
            ArrayList<Product> products = new ArrayList<>();
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select referencia,Designacao_comercial from produto p " +
                    "where id in (Select Distinct id_produto_pco " +
                    "from produto_componente_operacao pco)");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setIdProduct(rs.getString("referencia"));
                p.setTradeName(rs.getString("designacao_comercial"));
                products.add(p);
            }
            return products;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    /**
     * Gets all information of every product in the database and returns an array with that information
     */
    public static Product getDatasheetProductDB(String reference) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select DISTINCT * From Produto_componente_operacao pco " +
                    "inner join produto p on p.id = pco.Id_produto_pco " +
                    "where p.referencia like ?");
            stmt.setString(1, reference);
            ResultSet rs = stmt.executeQuery();
            Product p = new Product();
            if (rs.next()) {
                p.setIdProduct(rs.getString("referencia"));
                p.setVersionProduct(rs.getString("versao"));
                p.setDesignation(rs.getString("designacao"));
                p.setTradeName(rs.getString("designacao_comercial"));
                p.setUnity(rs.getString("unidade"));
                p.setBatch(rs.getDouble("qtd_lote"));
                p.setQuantity(rs.getDouble("quantidade"));
                p.setStatus(rs.getBoolean("estado"));
                p.setRawMaterial(rs.getBoolean("materia_prima"));
                p.setComponent(rs.getBoolean("componente"));
            }
            return p;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    /**
     * Checks if a product exists in the database. If it exists it returns true
     */
    public static boolean existsDB(Product product) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT TOP 1 referencia,versao FROM produto WHERE referencia = ? and versao = ?");
            stmt.setString(1, product.getIdProduct());
            stmt.setString(2, product.getVersionProduct());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Inserts a product in the database
     */
    public static boolean insertRelationshipDB(int id_product, int id_component, int id_alternativa, int id_operacao, int quantidade) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            String sql = "";
            if (id_component == 0) {
                sql = "insert into Produto_componente_operacao " +
                        "(id_produto_pco,id_operacao_pco,quantidade)" +
                        "values (?,?,?)";
            } else if (id_alternativa == 0) {
                sql = "insert into Produto_componente_operacao " +
                        "(id_componente_pco,id_produto_pco,id_operacao_pco,quantidade)" +
                        "values (?,?,?,?)";
            } else {
                sql = "insert into Produto_componente_operacao " +
                        "(id_componente_pco,id_produto_pco,id_alternativa_pco,id_operacao_pco,quantidade)" +
                        "values (?,?,?,?,?)";
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            if (id_component == 0) {
                stmt.setInt(1, id_product);
                stmt.setInt(2, id_operacao);
                stmt.setInt(3, quantidade);
            } else if (id_alternativa == 0) {
                stmt.setInt(1, id_component);
                stmt.setInt(2, id_product);
                stmt.setInt(3, id_operacao);
                stmt.setInt(4, quantidade);
            } else {
                stmt.setInt(1, id_component);
                stmt.setInt(2, id_product);
                stmt.setInt(3, id_alternativa);
                stmt.setInt(4, id_operacao);
                stmt.setInt(5, quantidade);
            }
            stmt.executeUpdate();
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir na tabela relação");
            return false;
        }
    }

    public static boolean updateProducts(Product product) throws SQLException {
        try {
            if (product != null) {
                Connection con = DatabaseConnect.getConnectionDB();
                PreparedStatement pst = con.prepareStatement("UPDATE Produto " +
                        "SET designacao = ?, designacao_comercial = ?, qtd_lote = ?, estado = ?, unidade = ? " +
                        ", componente = ?, materia_prima = ?" +
                        " WHERE referencia = ? and versao = ?");
                pst.setString(1, product.getDesignation());
                pst.setString(2, product.getTradeName());
                pst.setDouble(3, product.getBatch());
                pst.setBoolean(4, product.isStatus());
                pst.setString(5, product.getUnity());
                pst.setBoolean(6, product.isComponent());
                pst.setBoolean(7, product.isRawMaterial());
                pst.setString(8, product.getIdProduct());
                pst.setString(9, product.getVersionProduct());
                pst.executeUpdate();
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static Product getProductById(int id) {
        Product p = new Product();

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pst = con.prepareStatement("Select * FROM Produto where id = ?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                p.setId(rs.getInt("id"));
                p.setIdProduct(rs.getString("referencia"));
                p.setVersionProduct(rs.getString("versao"));
                p.setDesignation(rs.getString("designacao"));
                p.setTradeName(rs.getString("designacao_comercial"));
                p.setUnity(rs.getString("unidade"));
                p.setBatch(rs.getDouble("qtd_lote"));
                p.setQuantity(rs.getDouble("quantidade"));
                p.setStatus(rs.getBoolean("estado"));
                p.setRawMaterial(rs.getBoolean("materia_prima"));
                p.setComponent(rs.getBoolean("componente"));
            }
            return p;
        } catch (SQLException e) {
            System.out.println("erro ao buscar produto por id");
            return null;
        }
    }


}


