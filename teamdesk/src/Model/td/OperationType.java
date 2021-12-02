package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseOperationType;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OperationType {

    private int id;
    private String info;

    public OperationType() {
    }

    public OperationType(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static OperationType createOperationType(int id, String info) {
        OperationType ot = new OperationType();
        ot.setId(id);
        ot.setInfo(info);
        return ot;
    }

    /**
     * Gets all the OperationTypes from the database and returns an arraylist with them
     */
    public static ArrayList<OperationType> getOperationTypesDB() throws SQLException {
        ArrayList<OperationType> types = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM tipo_operacao");
            while (rst.next()) {
                OperationType op = new OperationType();
                op.setId(rst.getInt("id_tipo_operacao"));
                op.setInfo(rst.getString("descricao"));
                types.add(op);
            }
            return types;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Searches OperationTypes by id and returns the one that matches the criteria
     */
    public static OperationType returnOperationTypeById(String idType) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM tipo_operacao WHERE id_tipo_operacao LIKE '" + idType + "'");
            if (rst.next()) {
                OperationType op = new OperationType();
                op.setId(rst.getInt("id_tipo_operacao"));
                op.setInfo(rst.getString("descricao"));
                return op;
            }
        } catch (SQLException throwables) {
            return null;
        }
        return null;
    }

    public static OperationType returnOperationTypeByInfo(String info) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM tipo_operacao WHERE descricao LIKE '" + info + "'");
            if (rst.next()) {
                OperationType op = new OperationType();
                op.setId(rst.getInt("id_tipo_operacao"));
                op.setInfo(rst.getString("descricao"));
                return op;
            }
        } catch (SQLException throwables) {
            return null;
        }
        return null;
    }

    @Override
    public String toString() {
        return "OperationType{" +
                "id=" + id +
                ", info='" + info + '\'' +
                '}';
    }
}
