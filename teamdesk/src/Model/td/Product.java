package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseMachine;
import Model.td.database.DatabaseProduct;

import java.sql.*;
import java.util.ArrayList;

/**
 * public class Product with it's atributtes
 */
public class Product {

    private int id;
    private String idProduct;
    private String versionProduct;
    private String designation;
    private String tradeName;
    private String unity;
    private double batch;
    private double quantity;
    private boolean status;
    private boolean rawMaterial;
    private boolean component;
    private ArrayList<Operation> operations;
    private ArrayList<Component> components;


    /**
     * Constructor Empty
     */
    public Product() {
        this.components = new ArrayList<>();
        this.operations = new ArrayList<>();
    }


    /**
     * Constructor complete
     *
     * @param idProduct      String for reference product
     * @param versionProduct String for version product
     * @param designation    String for designation product
     * @param tradeName      String for trade name product
     * @param unity          String for unity product
     * @param batch          Integer for batch product
     * @param status         Boolean for Product status (true:enable/false:desable)
     * @param rawMaterial    Boolean if product is rawMaterial (true:yes/false:no)
     * @param operations     ArrayList type Operation for Operations in Product
     * @param components     ArrayList type Product for Components in Product
     */
    public Product(String idProduct, String versionProduct, String designation, String tradeName,
                   String unity, double batch, double quantity, boolean status, boolean rawMaterial, boolean component,
                   ArrayList<Operation> operations, ArrayList<Component> components) {

        this.idProduct = idProduct;
        this.versionProduct = versionProduct;
        this.designation = designation;
        this.tradeName = tradeName;
        this.unity = unity;
        this.batch = batch;
        this.quantity = quantity;
        this.status = status;
        this.rawMaterial = rawMaterial;
        this.component = component;
        this.operations = operations;
        this.components = components;
    }


    /**
     * Get id product
     *
     * @return id product
     */
    public String getIdProduct() {

        return idProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set id product
     *
     * @param idProduct
     */
    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * Get name product
     *
     * @return name product
     */

    /**
     * Set name product
     *
     * @param nameProduct
     */

    /**
     * Get version product
     *
     * @return version product
     */
    public String getVersionProduct() {
        return versionProduct;
    }

    /**
     * Set version product
     *
     * @param versionProduct
     */
    public void setVersionProduct(String versionProduct) {
        this.versionProduct = versionProduct;
    }

    /**
     * Get designation product
     *
     * @return designation
     */
    public String getDesignation() {
        return designation;
    }

    /**
     * Set designation product
     *
     * @param designation
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /**
     * Get trade name product
     *
     * @return trade name product
     */
    public String getTradeName() {
        return tradeName;
    }

    /**
     * Set trade name product
     *
     * @param tradeName
     */
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    /**
     * Get unity product
     *
     * @return unity product
     */
    public String getUnity() {
        return unity;
    }

    /**
     * Ser untiy product
     *
     * @param unity
     */
    public void setUnity(String unity) {
        this.unity = unity;
    }

    /**
     * Get bach product
     *
     * @return batch product
     */
    public double getBatch() {
        return batch;
    }

    /**
     * Set batch product
     *
     * @param batch
     */
    public void setBatch(double batch) {
        this.batch = batch;
    }

    /**
     * Get quantity product
     *
     * @return
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Set quantity product
     *
     * @param quantity
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Get state product
     *
     * @return status product  true or false
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Set state product
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Get true or false if product is rawMaterial
     *
     * @return rawMaterial product true or false
     */
    public boolean isRawMaterial() {
        return rawMaterial;
    }

    /**
     * Set rawMaterial product
     *
     * @param rawMaterial
     * @return
     */
    public void setRawMaterial(boolean rawMaterial) {
        this.rawMaterial = rawMaterial;

    }

    /**
     * returns boolean (if it's component or not)
     */
    public boolean isComponent() {
        return component;
    }

    /**
     * set Component
     *
     * @param component
     */
    public void setComponent(boolean component) {
        this.component = component;
    }

    /**
     * Get ArrayList of Operation in product
     *
     * @return ArrayList Operation of product
     */
    public ArrayList<Operation> getOperations() {
        return operations;
    }

    /**
     * Set ArrayList of Operation in product
     *
     * @param operations
     */
    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    /**
     * Return Arraylis of Component in product
     *
     * @return
     */
    public ArrayList<Component> getComponents() {
        return components;
    }


    /**
     * Set ArrayList of Component in product
     *
     * @param components
     */
    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    /**
     * set object component
     *
     * @param component
     */
    public void setComponents(Component component) {
        this.components.add(component);
    }

    /**
     * Gets all base information needed for an Operation's datasheet (operation - machine - components)
     */
    public static void getFullDatasheet(Product pr) {
        pr.setOperations(Operation.getDatasheetOperationsDB(pr.getId()));
        for (Operation operation : pr.getOperations()) {
            operation.setComponents(Component.getDatasheetComponentDB(operation.getId()));
            for (Component c : operation.getComponents()) {
                c.setAlternative(Component.getDatasheetAlternativaDB(DatabaseProduct.getIdProductDB(c), DatabaseProduct.getIdProductDB(pr)));
            }
        }
    }

    /**
     * Inserts a product in the database
     */
    public boolean saveProduct() throws SQLException {
        boolean flag = false;
            flag = DatabaseProduct.insertDB(this);
            if (this.getOperations().size() > 0) {
              flag =   Operation.insertOperationsInProducts(this.getOperations(), DatabaseProduct.getIdProductDB(this));
            }
        return flag;
    }

    /**
     * Prints a Product's datasheet
     *
     * @param produto
     */
    public static void generateDatasheet(Product produto) {
        System.out.println("\n----------ARTIGO----------");
        System.out.println("Referência: " + produto.getIdProduct() + "\n" +
                "Versão: " + produto.getVersionProduct() + "\n" +
                "Designação: " + produto.getDesignation() + "\n" +
                "Designação Comercial: " + produto.getTradeName() + "\n" +
                "Qtd Lote: " + produto.getBatch() + "\n" +
                "Estado: " + produto.isStatus() + "\n" +
                "Unidade: " + produto.getUnity());
        if (produto.getOperations().isEmpty()) {
            System.out.println("");
        } else {
            for (Operation op : produto.getOperations()) {
                System.out.println("\t------OPERAÇÕES------");
                Operation.generateDatasheet(op);
            }
        }
    }

    /**
     * toString of all items products
     *
     * @return string product
     */
    @Override
    public String toString() {
        String tipo = "Produto";
        if (this.rawMaterial) {
            tipo = "Matéria Prima";
        }
        return String.format("----%s----\nID: %s\nVersão: %s\nDesignação: %s\nDesignação Comercial: %s" +
                        "\nUnidade: %s\nLote: %f\nStado: %s\n", tipo,
                this.getIdProduct(), this.getVersionProduct(), this.getDesignation(), this.getTradeName(),
                this.getUnity(), this.getBatch(), (this.isStatus() == true ? "Ativo" : "Inativo"));
    }
}
