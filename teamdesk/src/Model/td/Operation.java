package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseMachine;
import Model.td.database.DatabaseProduct;
import javafx.beans.property.StringProperty;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;

/**
 * This class represents operations like welding and such.
 */
public class Operation {
    private OperationType type;
    private String description;
    private int id;
    private int productionTime;
    private int operatorsQt; //Quantity of operators needed for the operation
    private String tecInstructions; //Technical instructions
    private ArrayList<Component> components;
    private int partsPerHour;
    private int order;
    private String typeInfo;
    private int typeID;

    private String typeOperation;


    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String getInfo) {
        this.typeOperation = getInfo;
    }



    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(String typeInfo) {
        this.typeInfo = typeInfo;
    }


    /**
     * Empty constructor
     */
    public Operation() {
    }

    /**
     * Class constructor with all parameters
     */
    public Operation(OperationType type, String description, int id,
                     int productionTime, int operatorsQt, String tecInstructions,
                     ArrayList<Component> components, int partsPerHour, int order) {
        this.type = type;
        this.description = description;
        this.id = id;
        this.productionTime = productionTime;
        this.operatorsQt = operatorsQt;
        this.tecInstructions = tecInstructions;
        this.components = components;
        this.partsPerHour = partsPerHour;
        this.order = order;
    }

    /**
     * All getters and setters
     */
    public OperationType getType() {
        return type;
    }

    public String getOperationInfo() {
        return type.getInfo();
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }

    public int getOperatorsQt() {
        return operatorsQt;
    }

    public void setOperatorsQt(int operatorsQt) {
        this.operatorsQt = operatorsQt;
    }

    public String getTecInstructions() {
        return tecInstructions;
    }

    public void setTecInstructions(String tecInstructions) {
        this.tecInstructions = tecInstructions;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public int getPartsPerHour() {
        return partsPerHour;
    }

    public void setPartsPerHour(int partsPerHour) {
        this.partsPerHour = partsPerHour;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Creates and operation with attributes given by the user
     */
    public static Operation createOperation() {
        Operation o = new Operation();
        ArrayList<Component> components = new ArrayList<>();
        try {
            o.setOrder(Main2.readInt("Digite a ordem da operação:")); // verificar se exite ordem repetida no arraylist
            o.setDescription(Main2.readString("Digite a descrição da operação: "));
            o.setTecInstructions(Main2.readString("Digite as instruções técnicas da operação:"));
            o.setProductionTime(Main2.readInt("Digite o tempo(em segundos) da operação:"));
            o.setOperatorsQt(Main2.readInt("Digite a quantidade de operadores da operação:"));
            o.setPartsPerHour(Main2.readInt("Digite o número de peças que são produzidas por hora:"));
        } catch (Exception e) {
            System.out.println("Erro! Valores inválidos.");
        }
        return o;
    }

    /**
     * Inserts an Operation in the database
     */
    public boolean insertOperationDB() {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into operacao"
                    + "( ordem, qtd_mao_obra, tempo, quantidade, instrucao, id_tipo_operacao,descricao)"
                    + "values (?,?,?,?,?,?,?)");
            pstmt.setInt(1, this.getOrder()); //ordem
            pstmt.setInt(2, this.getOperatorsQt()); //qtd_mao_obra
            pstmt.setInt(3, this.getProductionTime()); //tempo
            pstmt.setInt(4, this.getPartsPerHour()); //quantidade
            pstmt.setString(5, this.getTecInstructions()); //instrucao
            pstmt.setInt(6, this.getType().getId()); //id_maquina_operacao
            pstmt.setString(7, this.getDescription()); //descricao
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possivel inserir na base de dados!");
            return false;
        }
    }


    /**
     * Gets all the operations from the database and returns an array with them
     *
     * @return Array of operations in the database
     */
    public static ArrayList<Operation> getOperationsDB() throws SQLException {
        ArrayList<Operation> operations = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM operacao");
            while (rst.next()) {
                Operation op = new Operation();
                op.setId(rst.getInt("id_operacao"));
                op.setOrder(rst.getInt("ordem"));
                op.setOperatorsQt(rst.getInt("qtd_mao_obra"));
                op.setProductionTime(rst.getInt("tempo"));
                op.setPartsPerHour(rst.getInt("quantidade"));
                op.setTecInstructions(rst.getString("instrucao"));
                op.setDescription(rst.getString("descricao"));
                op.setType(OperationType.returnOperationTypeById(rst.getString("id_tipo_operacao")));
                op.setComponents(Component.getComponentsByOperationID(String.valueOf(op.getType().getId())));
                operations.add(op);
            }
            return operations;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Gets base information of all Operations from the database and returns an array with that information
     */
    public static ArrayList<Operation> getDatasheetOperationsDB(int id_produto) {
        ArrayList<Operation> operations = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * from operacao o " +
                    "where Id_operacao in (Select Distinct id_operacao_pco " +
                    "from produto_componente_operacao pco " +
                    "where id_produto_pco = ?)");
            stmt.setInt(1, id_produto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Operation op = new Operation();
                op.setId(rs.getInt("id_operacao"));
                op.setOrder(rs.getInt("ordem"));
                op.setDescription(rs.getString("descricao"));
                op.setTecInstructions(rs.getString("instrucao"));
                op.setProductionTime(rs.getInt("tempo"));
                op.setOperatorsQt(rs.getInt("qtd_mao_obra"));
                op.setPartsPerHour(rs.getInt("quantidade"));
                OperationType ot = new OperationType();
                int id = rs.getInt("id_Tipo_Operacao");
                ot.setId(id);
                ot.setInfo("");
                op.setType(ot);

                operations.add(op);
            }
            return operations;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    /**
     * Gets the id of the last operation that was inserted in the database and returns it
     */
    public static int getLastOperation() {
        int id = 0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT TOP 1 id_operacao FROM operacao order by Id_operacao DESC");
            if (rs.next()) {
                id = rs.getInt("id_operacao");
            }
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

    /**
     * Inserts the operation inside the parameter in the database
     *
     * @param operation
     */
    public static void insertOperationDB(Operation operation) {
        operation.insertOperationDB();
    }

    public static boolean insertOperationsInProducts(ArrayList<Operation> operations, int id_product) throws SQLException {
        boolean flag = false;
        for (Operation operation : operations) {
            operation.insertOperationDB();
            if (operation.getComponents().size() > 0) {
                flag = Component.insertComponentsInOperations(operation.getComponents(), id_product, operation.getPartsPerHour());
            } else {
                flag = DatabaseProduct.insertRelationshipDB(id_product, 0, 0, Operation.getLastOperation(), operation.getPartsPerHour());
            }
        }
        return flag;
    }

    /**
     * Lists all operations from the database and deletes one chosen by the user
     */
    public static void deleteOperationDB() throws SQLException {
        int id;
        ArrayList<Operation> operations = new ArrayList<>(getOperationsDB());
        try {
            for (Operation op : operations) {
                System.out.println(op + "\n-----------------------------------------------------\n");
            }
            id = Main2.readInt("\nDigite o ID da operação que quer eliminar");
            for (Operation op : operations) {
                if (op.getId() == id) {
                    System.out.println(op + "\n\n");
                    if (Main2.readIntTrueFalse("Tem a certeza que quer eliminar esta operação?", 1, 0)) {
                        Connection con = DatabaseConnect.getConnectionDB();
                        PreparedStatement stmt = con.prepareStatement("DELETE FROM Operacao WHERE Id_operacao = ?");
                        stmt.setInt(1, id);
                        stmt.executeUpdate();
                        System.out.println("Operação eliminada com sucesso!");
                    } else {
                        System.out.println("A operação não foi eliminada.");
                    }
                } else {
                    System.out.println("Operação não encontrada.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Erro");
        }
    }

    /**
     * Prints an Operation's datasheet
     */
    public static void generateDatasheet(Operation operation) {
        System.out.println("\tOrdem: " + operation.getOrder() + "\n" +
                "\tIdentificação da Operação: " + operation.getDescription() + "\n" +
                "\tID da Operação: " + operation.getType().getId() + "\n" +
                "\tMão de Obra: " + operation.getOperatorsQt() + "\n" +
                "\tTempo: " + operation.getProductionTime() + "\n" +
                "\tQuantidade Hora:" + operation.getPartsPerHour() + "\n" +
                "\tInstruções Técnicas: " + operation.getTecInstructions());
        if (operation.getComponents() == null) {
            System.out.println("");
        } else {
            for (Component comp : operation.getComponents()) {
                System.out.println("\t\t---COMPONENTES---");
                Component.generateDatasheet(comp);
            }
        }
    }

    /**
     * Asks the user if he wants to make another operation. If the user answers yes, another operation is created and added
     * to the list of operations.In the end, this method returns the list of Operations
     */
    public static ArrayList<Operation> createListOperations() {
        ArrayList<Operation> listOperations = new ArrayList<>();
        listOperations.add(createOperation());
        while (Main2.readIntTrueFalse("Deseja adicionar mais uma operação?\n1: Sim\n0: Não", 1, 0)) {
            listOperations.add(createOperation());
        }
        return listOperations;

    }



    /**
     * Turns the class variables into an understandable string
     *
     * @return String
     */
    @Override // Arranjar toString na parte dos components
    public String toString() {
        String componentes = "";
        if (this.components != null) {
            for (Component comp : this.components) {
                componentes = comp.getIdProduct() + "," + componentes;
            }
        }
        return "----- OPERATION " + this.id + " -----" + "\n" +
                "* Type: " + this.type.getInfo() + "\n" +
                "* Description: " + this.description + "\n" +
                "* ID: " + this.id + "\n" +
                "* Production Time: " + this.productionTime + "\n" +
                "* Operators Quantity: " + this.operatorsQt + "\n" +
                "* Technical instructions: " + this.tecInstructions + "\n" +
                "* Components: " + componentes + "\n" +
                "* Parts Per Hour: " + this.partsPerHour + "\n" +
                "* Order: " + this.order + "\n\n";


    }
}


