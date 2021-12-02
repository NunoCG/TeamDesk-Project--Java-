package Model.td.helper;

import javafx.scene.control.Alert;

import java.text.DecimalFormat;

public class Helper {
    public static DecimalFormat df = new DecimalFormat("0.00");



    public static void showAlert(Alert.AlertType alertType, String title,String header, String message) {
        //show Alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static double formatDouble(double d){
        return Double.parseDouble(df.format(d));
    }
}
