package Model.td.database;

import Model.td.OperationType;
import Model.td.Operator;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseOperationType {

    public static ArrayList<OperationType> getAllOperationTypesBD() throws SQLException {
        ArrayList<OperationType> otype = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM Tipo_operacao");
            while (rs.next()) {
                OperationType ot = new OperationType();
                ot.setId(rs.getInt("id_tipo_operacao"));
                ot.setInfo(rs.getString("descricao"));
                otype.add(ot);
            }
            con.close();
        } catch (Exception e) {
            System.err.println("Erro");
        }
        return otype;
    }

    public static boolean insertOperationTypeDB(OperationType ot) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("insert into Tipo_operacao" +
                    " (id_tipo_operacao,descricao) values (?,?)");
            stmt.setInt(1, ot.getId());
            stmt.setString(2, ot.getInfo());
            stmt.executeUpdate();
            //System.out.println("Tipo de operacao inserida");
            return true;
        } catch (Exception e) {
            //System.out.println("Erro ao inserir o Tipo de operacao");
            return false;
        }
    }

    public static boolean deleteOperationType(String ot) throws SQLException {
        System.out.println(Integer.parseInt(ot));
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Tipo_operacao WHERE id_tipo_operacao = ?");
            stmt.setInt(1, Integer.parseInt(ot));
            stmt.executeUpdate();
            //System.out.println("Eliminado");
            return true;
        } catch (Exception e) {
            //System.err.println("Erro ao eliminar");
            return false;
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public static OperationType findOpTypeById(int id) throws SQLException {

        ArrayList<OperationType> listOp = DatabaseOperationType.getAllOperationTypesBD();
        OperationType ot = new OperationType();
        ot.setId(id);
        for (OperationType operationType: listOp) {
            if (operationType.getId() == id) {
                ot.setInfo(operationType.getInfo());
            }
        }
        return ot;
    }

}
