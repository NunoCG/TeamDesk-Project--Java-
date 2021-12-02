package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseProduct;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;

/**
 * Class Component inherited from Product
 */
public class Component extends Product {

    private ArrayList<Operation> operations;
    private Component alternative;
    private TextField quantityTextField;

    public TextField getQuantityTextField() {
        return quantityTextField;
    }

    public void setQuantityTextField(TextField quantityTextField) {
        this.quantityTextField = quantityTextField;
    }

    /**
     * Empty constructor Component
     */
    public Component() {
    }

    /**
     * Constructor Component with attributes as parameters
     * Sets the object product like an alternative component
     */
    public Component(String idComponent, String versionComponent, String designation, String tradeName,
                     String unity, double batch, double quantity, boolean status, boolean rawMaterial, boolean component,
                     ArrayList<Operation> operations, ArrayList<Component> components, Component alternative) {

        super(idComponent, versionComponent, designation, tradeName,
                unity, batch, quantity, status, rawMaterial, component,
                operations, components);

        this.alternative = alternative;
    }

    /**
     * Get object product alternative
     *
     * @return object product
     */
    public Component getAlternative() {
        return alternative;
    }

    /**
     * Set object product
     *
     * @param alternative of component
     */
    public void setAlternative(Component alternative) {
        this.alternative = alternative;
    }

    /**
     * Gives the option to select a product/component or to create one
     *
     * @param products
     * @return object Product
     */
    public static Product selectInList(ArrayList<Product> products) {

        try {
            int selected = 0;
            if (!products.isEmpty()) {
                for (int i = 0; i < products.size(); i++) {
                    System.out.printf("%d - %s%n", i + 1, products.get(i).getTradeName());
                }
                selected = Main2.readInt("Selecione um Produto/Componente ou 0 para criar: ");
                if (selected == 0) {
                    return null;
                }
                return products.get(selected - 1);
            } else {
                System.out.println("Não existe componente. Deve criar um produto ou matéria prima");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Por favor insira um valor da lista.");
            return selectInList(products);
        }
    }

    /**
     * Converts product into component
     *
     * @param product
     * @return object Component
     */
    public static Component convertProductInComponent(Product product) {
        try {
            if (!product.isComponent()) {
                product.setComponent(true);
            }
            return new Component(
                    product.getIdProduct(), product.getVersionProduct(), product.getDesignation(),
                    product.getTradeName(), product.getUnity(), product.getBatch(), product.getQuantity(), product.isStatus(),
                    product.isRawMaterial(), product.isComponent(), product.getOperations(), product.getComponents(), null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lets user choose if he wants to create an alternative for a component
     *
     * @param products
     * @return object component
     */
    public Component ifAlternative(ArrayList<Product> products) {
        Product prod = new Product();
        for (Product p : products) {
            if (p.getIdProduct().equals(this.getIdProduct())) {
                prod = p;
            }
        }
        try {
            if (!products.isEmpty() && products.size() > 1) {
                products.remove(prod);
                Product product = selectInList(products);
                return convertProductInComponent(product);
            }
        } catch (Exception e) {
            return ifAlternative(products);
        }
        return null;
    }

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
                //System.out.println(rs.getString("referencia"));
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

    /**
     * Prints a Component's datasheet
     */
    public static void generateDatasheet(Component component) {
        System.out.println("\t\tReferência: " + component.getIdProduct() + "\n" +
                "\t\tDesignação Comercial: " + component.getTradeName() + "\n" +
                "\t\tVersão: " + component.getVersionProduct() + "\n" +
                "\t\tQuantidade: " + component.getQuantity() + "\n" +
                "\t\tUnidade: " + component.getUnity() + "\n" +
                "\t\tAlternativa: " + component.getAlternative().getIdProduct() + "\n\n");
    }

    /**
     * Insert in data base override method
     */
    public static boolean insertComponentsInOperations(ArrayList<Component> components, int id_product, int getPartsPerHour) throws SQLException {
        boolean flag = false;
        for (Component component : components) {
                component.setComponent(true);
                DatabaseProduct.updateStatusComponentById(component);
                flag = DatabaseProduct.insertRelationshipDB(id_product, DatabaseProduct.getIdProductDB(component),
                        component.getAlternative() != null ? DatabaseProduct.getIdProductDB(component.getAlternative()) : 0,
                        Operation.getLastOperation(), getPartsPerHour);
        }
        return flag;
    }

    /**
     * Prints all components from the database and lets the user choose one. Returns the component chosen by the user
     */
    public static Component selectInListComponent() {
        try {
            ArrayList<Component> components = getAllComponents();
            if (!components.isEmpty()) {
                for (int i = 0; i < components.size(); i++) {
                    System.out.printf("%d - %s%n", i + 1, components.get(i).getTradeName());
                }
                return components.get(Main2.readInt("Selecione um Produto: ") - 1);
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("Por favor insira um valor da lista.");
            return null;
        }
    }

    /**
     * String of component and alternative component
     *
     * @return Component with alternative or not
     */
    @Override
    public String toString() {
        return String.format("-----Componente-----\nID: %s\nDesignação comercial: %s\nVersão: %s" +
                        "\nQuantidade:%f\nUnidade:%s\nAlternativa:%s\n",
                this.getIdProduct(), this.getTradeName(), this.getVersionProduct(), this.getQuantity(), this.getUnity(),
                (this.getAlternative() == null) ? "ND" : this.getAlternative().getIdProduct());
    }
}
