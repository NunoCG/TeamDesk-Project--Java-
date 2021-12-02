package Model.td;

import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseOperator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * public class Operator with it's attributes
 */
public class Operator extends Person {

    private boolean status;
    private int time;
    static Scanner sc = new Scanner(System.in);
    private ArrayList<Schedule> listSchedule;


    /**
     * Empty constructor Operator
     */
    public Operator() {
        listSchedule = new ArrayList<>();
    }

    /**
     * call the super class
     *
     * @param status
     * @param time
     */
    public Operator(boolean status, int time, String name, String gender, String id) {
        super(name, gender, id);
        this.status = status;
        this.time = time;

    }

    public ArrayList<Schedule> getListSchedule() {
        return listSchedule;
    }

    public void setListSchedule(ArrayList<Schedule> listSchedule) {
        this.listSchedule = listSchedule;
    }

    /**
     * Get status from operator
     *
     * @return status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * set status from operator
     *
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * get time from operator
     *
     * @return time
     */
    public int getTime() {
        return time;
    }

    /**
     * set time from operator
     *
     * @param time
     */


    public void setTime(int time) {
        this.time = time;
    }


    /**
     * Return attributes from super and Operator
     *
     * @return format
     */
    @Override
    public String toString() {
        return super.toString() + ("Estado: " + this.status + "\n" +
                                    "Tempo: " + this.time + "\n");
    }

    /**
     * Create a new operator with attributes
     *
     * @return object Operator
     */
    public static Operator createOperator() {
        Operator o = new Operator();
        try {
            o.setId(Main2.readString("ID: "));
            o.setName(Main2.readString("Nome: "));
            o.setStatus(Main2.readIntTrueFalse("Estado\n0-Inativo\n1-Ativo", 1, 0));
            o.setTime(Main2.readInt("Tempo: "));
            o.setGender(Main2.readString("Sexo: "));

        } catch (Exception e) {
            System.out.println("Erro ao criar operador");
            createOperator();
        }
        return o;
    }


    /**
     * Allows the user to select a product from the list of products in the database
     * @return
     */
    public static Operator selectInList() {
        try {
            ArrayList<Operator> operators = DatabaseOperator.getAllOperatorBD();
            if(!operators.isEmpty()){
                for (int i = 0; i < operators.size(); i++) {
                    System.out.printf("%d - %s%n", i + 1, operators.get(i).getName());
                }
                return operators.get(Main2.readInt("Selecione um Operador: ") - 1);
            }else{
                return null;
            }

        } catch (Exception e) {
            System.out.println("Por favor insira um valor da lista.");
            return selectInList();
        }
    }
    /**
     * select and ask for operator
     * 
     * @return object operator
     * @throws SQLException
     */
    public static Operator selectOperator() throws SQLException {
        ArrayList<Operator> op = DatabaseOperator.getAllOperatorBD();
        for (Operator p : op) {
            System.out.println(p);
        }
        System.out.println("Qual o id do operador?");
        try {
            Operator o = new Operator();
            String id = sc.next();
            for (Operator p : op) {
                if (p.getId().equals(id)){
                    o = p;
                }
            }
            return o;
        } catch (Exception e) {
            System.out.println("Valor inválido");
            return selectOperator();
        }
    }


    /**
     * Delete operator from id in database
     *
     * @throws SQLException
     */
    public void DeleteOperator() throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Operador WHERE Id_operador =  ?");
            stmt.setString(1, this.getId());
            stmt.executeUpdate();
            System.out.println("Eliminado");
        } catch (Exception e) {
            System.err.println("Erro ao eliminar");
        }
    }

    /**
     * Verify if ID_operator repeat
     *
     * @param id
     * @return boolean
     * @throws SQLException
     */
    public static boolean VerifyDB(String id) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT TOP 1 id_operador FROM operador WHERE id_operador = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Já existe um operador com o mesmo ID");
            return true;
        }
    }

    /**
     * Verify operator ID in data base
     * @return boolean
     * @throws SQLException
     */
    public boolean VerifyDB() throws SQLException {
        return VerifyDB(this.getId());
    }


    /**
     * Creates operators and verify if already exists one with a certain ID
     * @throws SQLException
     */
    public void createOp() throws SQLException {
        boolean flag = true;
        while (flag) {
            Operator ope = Operator.createOperator();
            if (!ope.VerifyDB()) {
                DatabaseOperator.insertOperatorDB(ope);
                if (Main2.readIntTrueFalse("Deseja criar outro operador?\n0-Não\n1-Sim", 0, 1)){
                    flag = false;
                }
            } else {
                System.out.printf("Já existe um operador com o mesmo ID");
            }
        }
    }
}

