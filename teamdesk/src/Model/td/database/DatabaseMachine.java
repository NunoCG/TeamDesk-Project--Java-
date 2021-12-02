package Model.td.database;

import Model.td.Machine;
import Model.td.OperationType;
import Model.td.Operator;
import Model.td.Schedule;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseMachine {

    /**
     * Gets all the list of all created machines that exist in the data base
     *
     * @return list of machines
     */
    public static ArrayList<Machine> getAllMachineDB() throws SQLException {
        ArrayList<Machine> mch = new ArrayList<>();
        ArrayList<Schedule> listSch = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM maquina");
            while (rst.next()) {
                Machine m = new Machine();
                m.setId(rst.getString("id_maquina"));
                m.setOperationType(DatabaseOperationType.findOpTypeById(rst.getInt("Tipo_operacao")));
                m.setDescription(rst.getString("descricao"));
                m.setStatus(rst.getBoolean("estado"));
                m.setTimeWork(rst.getInt("hora_trabalho"));
                m.setInfoOpType(m.getOperationType().getInfo());
                m.setListSchedule(listSch);
                mch.add(m);
            }
            return mch;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    /**
     * Gets base information from all machines in the database and returns an array with that information
     */
    public static Machine getDatasheetMachineDB(int id_operacao) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("Select * from maquina m " +
                    "inner join operacao o " +
                    "on o.Id_maquina_operacao = m.Id_maquina " +
                    "where o.Id_operacao = ?");
            stmt.setInt(1, id_operacao);
            ResultSet rs = stmt.executeQuery();
            Machine m = new Machine();
            if (rs.next()) {
                m.setId(rs.getString("id_maquina"));
                m.setStatus(rs.getBoolean("estado"));
                m.setDescription(rs.getString("descricao"));
                m.setOperationType(DatabaseOperationType.findOpTypeById(rs.getInt("Tipo_operacao")));
                m.setTimeWork(rs.getInt("hora_trabalho"));
            }
            return m;
        } catch (SQLException e) {
            System.out.println("Não foi possível buscar produto a base de dados!");
            return null;
        }
    }

    /**
     * Insert a machine query into the data base
     */
    public static boolean insertMachineDB(Machine machine) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pstmt = con.prepareStatement("insert into maquina"
                    + "(id_maquina, tipo_operacao, descricao, estado, hora_trabalho)"
                    + "values (?,?,?,?,?)");
            pstmt.setString(1, machine.getId());
            pstmt.setInt(2, machine.getOperationType().getId());
            pstmt.setString(3, machine.getDescription());
            pstmt.setBoolean(4, machine.isStatus());
            pstmt.setDouble(5, machine.getTimeWork());
            pstmt.executeUpdate();

            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    /**
     * Inserts a machine in the database
     */
    public static void insertMachineDB() {
        insertMachineDB();
    }

    /**
     * Retorns one machine searched by it's ID
     *
     * @param idMachine
     * @return a choosed machine
     * @throws SQLException
     */
    public static Machine returnMachineById(String idMachine) throws SQLException {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement st = con.createStatement();
            ResultSet rst = st.executeQuery("SELECT * FROM maquina WHERE id_maquina LIKE '" + idMachine + "'");
            if (rst.next()) {
                Machine m = new Machine();
                m.setId(rst.getString("id_maquina"));
                m.setOperationType(DatabaseOperationType.findOpTypeById(rst.getInt("Tipo_operacao")));
                m.setDescription(rst.getString("descricao"));
                m.setStatus(rst.getBoolean("estado"));
                m.setTimeWork(rst.getInt("hora_trabalho"));
                return m;
            }
        } catch (SQLException throwables) {
            return null;
        }
        return null;
    }

    /**
     * Delets a machine from the data base
     */
    public static boolean deleteMachine(Machine m) throws SQLException {
        if (m != null) {
            try {
                Connection con = DatabaseConnect.getConnectionDB();
                PreparedStatement pst = con.prepareStatement("DELETE FROM maquina WHERE id_maquina = ?");
                pst.setString(1, m.getId());
                pst.executeUpdate();
                return true;
            } catch (SQLException throwables) {
                return false;
            }
        } else {
            System.out.println("Não existe Máquina");
        }
        return true;
    }

    /**
     * @param m
     * @return
     * @throws SQLException
     */
    public static boolean updateMachine(Machine m) throws SQLException {
        if (m != null) {
            try {
                Connection con = DatabaseConnect.getConnectionDB();
                PreparedStatement pst = con.prepareStatement("UPDATE Maquina " +
                        "SET Descricao = ?, Tipo_operacao = ?, Hora_trabalho = ?, Estado = ? " +
                        "WHERE Id_maquina like ?");
                pst.setString(1, m.getDescription());
                pst.setInt(2, m.getOperationType().getId());
                pst.setInt(3, m.getTimeWork());
                pst.setBoolean(4, m.isStatus());
                pst.setString(5, m.getId());
                pst.executeUpdate();
                return true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * @param start
     * @param end
     * @param timeC
     * @param day
     * @param idMachine
     * @return
     * @throws SQLException
     */
    public static boolean insertScheduleMachineDB(Time start, Time end, boolean timeC, int day, String idMachine) throws SQLException {

        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("insert into dia_trabalho_maquina" +
                    "(id_maquina, periodo, id_semana, hora_entrada, hora_saida) values(?,?,?,?,?)");
            stmt.setString(1, idMachine);
            stmt.setBoolean(2, timeC);
            stmt.setInt(3, day);
            stmt.setTime(4, start);
            stmt.setTime(5, end);
            stmt.executeUpdate();
            System.out.println("Inserido horario");
            return true;
        } catch (SQLException throwables) {
            System.out.println("erro");
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * @param idMaquina
     * @return
     */
    public static ArrayList<Schedule> getScheduleMachineByIdDB(String idMaquina) {
        ArrayList<Schedule> sc = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM dia_trabalho_maquina WHERE id_maquina = ?");
            stmt.setString(1, idMaquina);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setDayWeek(rs.getString("id_semana"));
                if (rs.getBoolean("periodo")) {
                    schedule.setTimeCourse1(rs.getBoolean("periodo"));
                    schedule.setStartTime1(rs.getTime("hora_entrada").toLocalTime());
                    schedule.setEndTime1(rs.getTime("hora_saida").toLocalTime());
                } else {
                    schedule.setTimeCourse2(rs.getBoolean("periodo"));
                    schedule.setStartTime2(rs.getTime("hora_entrada").toLocalTime());
                    schedule.setEndTime2(rs.getTime("hora_saida").toLocalTime());
                }
                System.out.println(schedule);
                sc.add(schedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sc;
    }

    /**
     * @param start
     * @param end
     * @param timeC
     * @param day
     * @param idMachine
     * @return
     * @throws SQLException
     */
    public static boolean updateScheduleMachine(Time start, Time end, boolean timeC, int day, String idMachine) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pst = con.prepareStatement("UPDATE dia_trabalho_maquina " +
                    "SET periodo = ?, id_semana = ?, hora_entrada = ?, hora_saida = ? " +
                    "WHERE id_maquina like ?");
            pst.setBoolean(1, timeC);
            pst.setInt(2, day);
            pst.setTime(3, start);
            pst.setTime(4, end);
            pst.setString(5, idMachine);
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     * @param s
     * @throws SQLException
     */
    public static boolean deleteScheduleMachine(Schedule s) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM dia_trabalho_maquina where id_maquina = ? AND id_semana = ?");
            stmt.setString(1, s.getIdMaquina());
            stmt.setInt(2, s.getIdSemana());
            stmt.executeUpdate();
            System.out.println("Horario eliminado");
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao eliminar horario");
            return false;
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    public static ArrayList<String> getDayWeekDB() throws SQLException {
        ArrayList<String> daysWeek = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT Dia_da_semana from Dia_Semana");
            while (rs.next()) {
                daysWeek.add(rs.getString("Dia_da_semana"));
            }
            con.close();
            System.out.println("OK");
            return daysWeek;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Erro");
            return null;
        }
    }

    /**
     * @param dayWeek
     * @return
     */
    public static int getDayWeekByDay(String dayWeek) {
        int id = 0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT ID_semana from Dia_Semana where Dia_da_Semana = ?");
            stmt.setString(1, dayWeek);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("Id_semana");
            }
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Erro");
            return id;
        }
    }

    /**
     *
     * @param idMaquina
     * @return
     */
    public static ArrayList<Schedule> getScheduleMachineOneLine(String idMaquina) {
        ArrayList<Schedule> sc = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM func_todosHorariosMaquina(?) " +
                    "WHERE hora_entrada1 IS NOT NULL OR " +
                    "hora_entrada2 IS NOT NULL");
            stmt.setString(1, idMaquina);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                schedule.setStartTime1(rs.getTime("Hora_Entrada1").toLocalTime());
                schedule.setEndTime1(rs.getTime("Hora_Saida1").toLocalTime());
                schedule.setDayWeek(rs.getString("semana"));
                schedule.setStartTime2(rs.getTime("Hora_Entrada2").toLocalTime());
                schedule.setEndTime2(rs.getTime("Hora_Saida2").toLocalTime());
                schedule.setIdMaquina(rs.getString("id"));
                schedule.setIdSemana(rs.getInt("id_semana"));
                sc.add(schedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sc;
    }
}

