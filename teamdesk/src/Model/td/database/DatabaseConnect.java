package Model.td.database;

import java.sql.*;

/**
 * Public class DatabaseConnect without attributes
 */
public class DatabaseConnect {

    /**
     * Connects the Java project to the databased
     * @return connection
     */
    public static Connection getConnectionDB(){
        String url= "jdbc:sqlserver://ctespbd.dei.isep.ipp.pt:1433;databaseName=LP2_G4_2021";
        String username= "LP2_G4_2021";
        String password= "LP2_2020_bd_4";
        Connection con= null;
        try {
            con = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

        return con;
    }

    /**
     * 
     * @throws SQLException
     */
    public static void name() throws SQLException {

        Connection con = getConnectionDB();
        Statement st = con.createStatement();
        String query = "Select * from a";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            System.out.println(rs.getString("Nome"));
        }
    }
}

