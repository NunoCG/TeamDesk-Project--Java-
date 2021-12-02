package controller;

import Model.td.Client;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseItemOrder;
import Model.td.helper.Helper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class SelectClientOrderController implements Initializable {
    public TableView tableClients;
    public TableColumn columnNome;
    public TableColumn columnNif;
    public TextField txtCliente;
    public Button btnSelect;
    public Button btnCancel;
    private ArrayList<Client> clients;
    private ObservableList<Client> observableArrayList;
    private Client client;
    private boolean btnClicked;
    private Stage stageDialog;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) throws SQLException {
        this.client = client;
        loadOrders();
    }

    public boolean isBtnClicked() {
        return btnClicked;
    }

    public void setBtnClicked(boolean btnClicked) {
        this.btnClicked = btnClicked;
    }

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void btnSelect(ActionEvent actionEvent) {
        client = (Client) tableClients.getSelectionModel().getSelectedItem();
        if(getClient() == null){
            Helper.showAlert(Alert.AlertType.INFORMATION,"Atenção","Selecionar Cliente",
                    "Por favor selecione uma encomenda para poder editar.");
        }else{
            setBtnClicked(true);
            stageDialog.close();
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        stageDialog.close();
    }


    public void loadOrders() throws SQLException {
        columnNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
        clients= DatabaseClient.getAllClientsDB();
        observableArrayList = FXCollections.observableArrayList(clients);
        tableClients.setItems(observableArrayList);
        filteredSearchList();

    }
    public void filteredSearchList() throws SQLException {
        FilteredList<Client> filteredList = new FilteredList<>(observableArrayList, b -> true);
        txtCliente.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredList.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);
                if (client.getName().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(client.getNif()).toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Client> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableClients.comparatorProperty());
        tableClients.setItems(sortedList);
    }



}
