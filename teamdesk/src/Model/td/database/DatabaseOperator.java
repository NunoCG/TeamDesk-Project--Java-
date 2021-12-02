package Model.td.database;

import Model.td.Operator;
import Model.td.Schedule;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class DatabaseOperator {
    /**
     * insert operator in database
     *
     * @param op
     * @throws SQLException
     */
    public static boolean insertOperatorDB(Operator op) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("insert into Operador" +
                    " (Id_operador, Nome, Estado,sexo) values (?,?,?,?)");
            stmt.setString(1, op.getId());
            stmt.setString(2, op.getName());
            stmt.setBoolean(3, op.isStatus());
            stmt.setString(4, op.getGender());
            stmt.executeUpdate();
            System.out.println("Operador inserido");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao inserir o operador");
            return false;
        }
    }

    /**
     * Show all Operators in database
     *
     * @return list of operators
     * @throws SQLException
     */
    public static ArrayList<Operator> getAllOperatorBD() throws SQLException {
        ArrayList<Operator> op = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            Statement stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM Operador");
            while (rs.next()) {
                Operator operator = new Operator();
                operator.setId(rs.getString("Id_operador"));
                operator.setName(rs.getString("Nome"));
                operator.setStatus(rs.getBoolean("Estado"));
                operator.setGender(rs.getString("Sexo"));
                op.add(operator);
            }
            con.close();
        } catch (Exception e) {
            System.err.println("Erro");
        }
        return op;
    }

    /**
     * Delete operator from id in database
     *
     * @throws SQLException
     */
    public static void DeleteOperator(Operator operator) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Operador WHERE Id_operador =  ?");
            stmt.setString(1, operator.getId());
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
            PreparedStatement stmt = con.prepareStatement("SELECT TOP 1 id_operador" +
                    " FROM operador WHERE id_operador = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Verify operator ID in data base
     *
     * @return boolean
     * @throws SQLException
     */
    public boolean VerifyDB(Operator operator) throws SQLException {
        return VerifyDB(operator.getId());
    }

    public static boolean updateOperatorDB(Operator op) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("UPDATE Operador " +
                    "SET id_operador=?, nome=?, estado=?, sexo=? " +
                    "where id_operador LIKE ?");

            stmt.setString(1, op.getId());
            stmt.setString(2, op.getName());
            stmt.setBoolean(3, op.isStatus());
            stmt.setString(4, op.getGender());
            stmt.setString(5, op.getId());
            stmt.executeUpdate();
            System.out.println("Operador alterado");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erro ao alterar o operador");
            return false;
        }
    }

    public static boolean insertScheduleOperatorDB(Time start, Time end, boolean timeC, int day, String idOp) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("insert into Dia_trabalho_operador" +
                    "(hora_entrada,hora_saida,periodo,id_operador, id_semana) values(?,?,?,?,?)");
            stmt.setTime(1, start);
            stmt.setTime(2, end);
            stmt.setBoolean(3, timeC);
            stmt.setString(4, idOp);
            stmt.setInt(5, day);
            stmt.execute();
            System.out.println("Inserido horario");
            return true;
        } catch (SQLException throwables) {
            System.out.println("erro");
            throwables.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Schedule> getScheduleBDByIDOperator(String id) {
        ArrayList<Schedule> sc = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Dia_trabalho_operador where id_operador = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                if (rs.getBoolean("Periodo")) {
                    schedule.setTimeCourse1(true);
                    schedule.setStartTime1(rs.getTime("Hora_entrada").toLocalTime());
                    schedule.setEndTime1(rs.getTime("Hora_saida").toLocalTime());
                    schedule.setDayWeek(rs.getString("id_semana"));
                } else {
                    schedule.setTimeCourse2(false);
                    schedule.setStartTime2(rs.getTime("Hora_entrada").toLocalTime());
                    schedule.setEndTime2(rs.getTime("Hora_saida").toLocalTime());
                    schedule.setDayWeek(rs.getString("id_semana"));
                }
                sc.add(schedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sc;
    }

    public static String getDayFromID(String day) {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT Dia_da_Semana from Dia_Semana where ID_semana = ?");
            stmt.setString(1, day);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                day = rs.getString("Dia_da_Semana");
            }
            return day;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Erro");
            return day;
        }
    }

    public static int getDayWeekByDay(String dayWeek) {
        int id = 0;
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("SELECT ID_semana from Dia_Semana where Dia_da_Semana = ?");
            stmt.setString(1, dayWeek);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID_semana");
            }
            return id;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Erro");
            return id;
        }
    }

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

    public static void DeleteOperatorSchedule(Operator operator) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("DELETE FROM Dia_trabalho_operador where id_operador = ?");
            stmt.setString(1, operator.getId());
            stmt.executeUpdate();
            System.out.println("Eliminado horario");
        } catch (Exception e) {
            System.err.println("Erro ao eliminar horario");
        }
    }

    public static ArrayList<Schedule> getScheduleBDOp(String id) {
        ArrayList<Schedule> sc = new ArrayList<>();
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement stmt = con.prepareStatement("select * from func_todosHorariosOperador( ? ) " +
                    "where hora_entrada1 is not null  or hora_entrada2 is not null");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule();
                if(rs.getTime("Hora_entrada1") != null){
                    schedule.setStartTime1(rs.getTime("Hora_entrada1").toLocalTime());
                }
                if(rs.getTime("Hora_saida1") != null) {
                    schedule.setEndTime1(rs.getTime("Hora_saida1").toLocalTime());
                }
                schedule.setDayWeek(rs.getString("id_semana"));
                if(rs.getTime("Hora_entrada2") != null){
                    schedule.setStartTime2(rs.getTime("Hora_entrada2").toLocalTime());
                }
                if(rs.getTime("Hora_saida2") != null) {
                    schedule.setEndTime2(rs.getTime("Hora_saida2").toLocalTime());
                }
                    sc.add(schedule);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sc;
    }

    /*
    public static boolean updateScheduleOperator(boolean timeC, int day, LocalTime start, LocalTime end, String idop) throws SQLException {
        try {
            Connection con = DatabaseConnect.getConnectionDB();
            PreparedStatement pst = con.prepareStatement("UPDATE dia_trabalho_operador " +
                    "SET periodo = ?, id_semana = ?, hora_entrada = ?, hora_saida = ? " +
                    "WHERE id_operador like ?");
            pst.setBoolean(1, timeC);
            pst.setInt(2, day);
            pst.setTime(3, Time.valueOf(start));
            pst.setTime(4, Time.valueOf(end));
            pst.setString(5, idop);
            pst.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
*/
}
