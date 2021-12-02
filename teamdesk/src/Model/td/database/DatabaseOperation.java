package Model.td.database;

import Model.td.Component;
import Model.td.Operation;
import Model.td.OperationType;
import Model.td.Product;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseOperation {
    /**
     * Inserts an Operation in the database
     */
    public void insertOperationDB(Operation operation) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into operacao"
                    + "( ordem, qtd_mao_obra, tempo, quantidade, instrucao, id_maquina_operacao,descricao)"
                    + "values (?,?,?,?,?,?,?)");
            pstmt.setInt(1, operation.getOrder()); //ordem
            pstmt.setInt(2, operation.getOperatorsQt()); //qtd_mao_obra
            pstmt.setInt(3, operation.getProductionTime()); //tempo
            pstmt.setInt(4, operation.getPartsPerHour()); //quantidade
            pstmt.setString(5, operation.getTecInstructions()); //instrucao
            pstmt.setString(6, String.valueOf(operation.getType().getId())); //id_tipo_operacao
            pstmt.setString(7, operation.getDescription()); //descricao
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possivel inserir na base de dados!");
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
                op.setComponents(DatabaseComponent.getComponentsByOperationID(String.valueOf(op.getId())));
                op.setTypeInfo(op.getType().getInfo());
                op.setTypeID(op.getType().getId());
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
                operations.add(op);
            }
            return operations;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    public static void updateOperationRelationDB(Operation operation) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("UPDATE Operacao SET Ordem = ?, Qtd_mao_obra = ?, Tempo = ?," +
                    " Quantidade = ?,Instrucao = ?,Descricao = ?, " +
                    "id_Tipo_Operacao = ? WHERE Id_operacao =?");
            pstmt.setInt(1, operation.getOrder()); //ordem
            pstmt.setInt(2, operation.getOperatorsQt()); //qtd_mao_obra
            pstmt.setInt(3, operation.getProductionTime()); //tempo
            pstmt.setInt(4, operation.getPartsPerHour()); //quantidade
            pstmt.setString(5, operation.getTecInstructions()); //instrucao
            pstmt.setString(6, operation.getDescription()); //descricao
            pstmt.setInt(7, operation.getType().getId()); //id_tipo_operacao
            pstmt.setInt(8, operation.getId()); //id_tipo_operacao
            pstmt.executeUpdate();
            pstmt.close();
            int idProduto = returnProductIDFromOperation(operation);
            deleteRelationPCO(operation);
            if (operation.getComponents() != null) {
                for (Component comp : operation.getComponents()) {
                    if (comp != null) {
                        insertRelationPCO(operation, idProduto, comp);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível atualizar a operação!");
        }
    }

    public static void insertRelationPCO(Operation operation, int idProduto, Component comp) {
        int idAlternativa = 0;
        int idComp = DatabaseProduct.getIdProductDB(comp);
        if (comp.getAlternative() != null) {
            idAlternativa = DatabaseProduct.getIdProductDB(comp.getAlternative());
        }
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstst = con.prepareStatement("INSERT INTO Produto_componente_operacao " +
                    "(Id_componente_pco, Id_produto_pco, Id_alternativa_pco, Id_operacao_pco, Quantidade) " +
                    "VALUES (?,?,?,?,?)");
            pstst.setInt(1, idComp);
            pstst.setInt(2, idProduto);
            if (idAlternativa == 0) {
                pstst.setString(3, null);
            } else {
                pstst.setInt(3, idAlternativa);
            }
            pstst.setInt(4, operation.getId());
            pstst.setDouble(5, comp.getQuantity());
            pstst.execute();
            pstst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível atualizar a relação!");
        }
    }

    public static int returnProductIDFromOperation(Operation operation) {
        int idProduto = 0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement st = con.prepareStatement("SELECT DISTINCT Id_produto_pco FROM Produto_componente_operacao WHERE Id_operacao_pco = ?");
            st.setInt(1, operation.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                idProduto = rs.getInt("id_produto_pco");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível encontrar o ID!");
        }
        return idProduto;
    }

    public static void deleteRelationPCO(Operation operation) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement st = con.prepareStatement("DELETE FROM Produto_componente_operacao WHERE Id_operacao_pco = ?");
            st.setInt(1, operation.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível eliminar a relação!");
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

    public static void updateOperationDB(Operation operation) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("UPDATE Operacao SET Ordem = ?, Qtd_mao_obra = ?, Tempo = ?," +
                    " Quantidade = ?,Instrucao = ?,Descricao = ?, " +
                    "id_Tipo_Operacao = ? WHERE Id_operacao =?");
            pstmt.setInt(1, operation.getOrder()); //ordem
            pstmt.setInt(2, operation.getOperatorsQt()); //qtd_mao_obra
            pstmt.setInt(3, operation.getProductionTime()); //tempo
            pstmt.setInt(4, operation.getPartsPerHour()); //quantidade
            pstmt.setString(5, operation.getTecInstructions()); //instrucao
            pstmt.setString(6, operation.getDescription()); //descricao
            pstmt.setInt(7, operation.getType().getId()); //id_tipo_operacao
            pstmt.setInt(8, operation.getId()); //id_tipo_operacao
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível atualizar a operação!");
        }
    }

    //ACERTAR ALTERNATIVAS E ADICIONAR NA CLASSE DE ALTERAR OPERAÇAO
    /*public static void updateOperationRelationTable(Operation operation, Product product) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM Produto_componente_operacao " +
                    "WHERE Id_operacao =" + operation.getId() + " AND Id_produto_pco =" + product.getIdProduct());
            for (Component comp : operation.getComponents()) {
                PreparedStatement pstmt2 = con.prepareStatement("INSERT INTO Produto_componente_operacao " +
                        "(Id_componente_pco, id_produto, id_alternativa_pco,id_operacao_pco, Quantidade) " +
                        "VALUES (" + comp.getId() + ", " + product.getId() + ", "
                        + alternativa + ", " + operation.getId() + ", " + operation.getPartsPerHour() + ");");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Não foi possível atualizar a tabela!");
        }
    }*/
}
