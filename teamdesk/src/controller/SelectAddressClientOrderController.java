package controller;

import Model.td.Address;
import Model.td.Client;
import Model.td.database.DatabaseAddress;
import Model.td.database.DatabaseClient;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class SelectAddressClientOrderController  implements Initializable {
    public TextField txtMirada;
    public TableView tableAddress;
    public TableColumn columnMorada;
    public TableColumn columnCodigoPostal;
    public TableColumn columnCidade;
    private ArrayList<Address> addresses;
    private ObservableList<Address> observableArrayList;
    private Address address;
    private int nif;
    private boolean btnClicked;
    private Stage stageDialog;


    public int getNif() {
        return nif;
    }

    public void setNif(int nif) throws SQLException {
        this.nif = nif;
        loadAddress();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
        address = (Address) tableAddress.getSelectionModel().getSelectedItem();
        if(getAddress() == null){
            Helper.showAlert(Alert.AlertType.INFORMATION,"Atenção","Selecionar Morada",
                    "Por favor selecione uma morada para a encomenda.");
        }else{
            setBtnClicked(true);
            stageDialog.close();
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        stageDialog.close();
    }
    public void loadAddress() throws SQLException {
        columnMorada.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnCodigoPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnCidade.setCellValueFactory(new PropertyValueFactory<>("location"));
        addresses = DatabaseAddress.getAllAddressesByClient(nif);
        observableArrayList = FXCollections.observableArrayList(addresses);
        tableAddress.setItems(observableArrayList);
        filteredSearchList();

    }
    public void filteredSearchList() throws SQLException {
        FilteredList<Address> filteredList = new FilteredList<>(observableArrayList, b -> true);
        txtMirada.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredList.setPredicate(address -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);
                if (address.getAddress().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (address.getPostalCode().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (address.getLocation().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                }else {
                    return false;
                }
            });
        });
        SortedList<Address> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableAddress.comparatorProperty());
        tableAddress.setItems(sortedList);
    }
}
