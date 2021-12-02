package controller;

import Model.td.Address;
import Model.td.Client;
import Model.td.Contact;
import Model.td.database.DatabaseAddress;
import Model.td.database.DatabaseContact;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class InsertMorada {

    public TextField txtMorada;
    public TextField txtCode;
    public TextField txtLocal;
    public TextField txtPais;
    public static Client c;

    /**
     * create address button
     * @param actionEvent
     */
    public void finishAddress(ActionEvent actionEvent) {
        Address a = new Address();
        a.setMainAddress(false);
        a.setAddress(txtMorada.getText());
        a.setCountry(txtPais.getText());
        a.setLocation(txtLocal.getText());
        a.setNif(c.getNif());
        a.setPostalCode(txtCode.getText());

        if (DatabaseAddress.insertAddressDB(a)) {
            Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Morada inserida", "Sucesso");
            txtMorada.getScene().getWindow().hide();

        } else {
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao inserir", "Erro ao inserir a morada");
        }
    }

    /**
     * show alert method
     * @param alertType
     * @param title
     * @param header
     * @param message
     */
    private static void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();

    }

    /**
     * close window
     * @param actionEvent
     */
    public void closeWindow(ActionEvent actionEvent) {
        txtMorada.getScene().getWindow().hide();
    }
}
