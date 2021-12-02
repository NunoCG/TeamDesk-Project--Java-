package controller;

import Model.td.Client;
import Model.td.Contact;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseContact;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

    public class InsertContact implements Initializable {

    public TextField txtcontact;
    public TextField txtObs;
    public ChoiceBox choiceTipo;
    public static Client c;
    public ObservableList<String> observableListContactType;
    public ArrayList<String> listContactType;

        /**
         * create contact button
         * @param actionEvent
         */
    public void finishContact(ActionEvent actionEvent) {
        Contact contact = new Contact();
        contact.setMainContact(false);
        contact.setContact(txtcontact.getText());
        contact.setObs(txtObs.getText());
        contact.setNif(c.getNif());
        contact.setIdTypeContact(Contact.findIdfromContact((String) choiceTipo.getSelectionModel().getSelectedItem()));


        if (DatabaseContact.insertContactsDB(contact)) {
            Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Contacto inserido", "Sucesso");
            txtcontact.getScene().getWindow().hide();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erro!", "Erro ao inserir", "Erro ao inserir o contacto");
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
        txtcontact.getScene().getWindow().hide();

    }

        /**
         * When this scene is opened, this method loads the contacts from the database
         * @param url
         * @param resourceBundle
         */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listContactType = DatabaseContact.getContactsTypeDB();
        observableListContactType = FXCollections.observableArrayList(listContactType);
        choiceTipo.setItems(observableListContactType);

    }
}
