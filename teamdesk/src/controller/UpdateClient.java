package controller;

import Model.td.Address;
import Model.td.Client;
import Model.td.Contact;
import Model.td.Operator;
import Model.td.database.DatabaseAddress;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseContact;
import Model.td.database.DatabaseOperator;
import Model.td.helper.Helper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class UpdateClient implements Initializable{
    
    public TextField nameoperator;
    public Button update;
    public Button close;

    private static Stage stageDialog;
    public TextField nif;
    public TextField name;
    public TextField Notas;
    public TableView<Address> tableMoradas;
    public TableColumn<Address,String> colunaMorada;
    public TableView<Contact> tableContactos;
    public TableColumn<Contact,String> colunaContacto;
    public TextField txtMorada;
    public TextField txtCodPostal;
    public TextField txtLocal;
    public TextField txtPais;
    public TextField txtContacto;
    public TextField txtObsContacto;
    public CheckBox checkMorada;
    public CheckBox checkContacto;
    public ChoiceBox choiceTipo;
    public static Client client;
    public ArrayList<Address> listAddress;
    public ArrayList<Contact> listContact;
    public ArrayList<String> listContactType;
    public static ObservableList<Address> observableListAddress;
    public static ObservableList<Contact> observableListContact;
    public ObservableList<String> observableListContactType;

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    public Client getClient() {
        return client;
    }

    /**
     * set Client properties
     * @param client
     */
    public void setClient(Client client) {
        try {
            this.client = client;
            this.name.setText(client.getName());
            this.nif.setText(String.valueOf(client.getNif()));
            this.Notas.setText(client.getNotes());

            colunaMorada.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<String>(cellData.getValue().getAddress().toString()));
            colunaContacto.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<String>(cellData.getValue().getContact().toString()));
            listAddress = DatabaseAddress.getAllAddressesByClient(client.getNif());
            listContact = DatabaseContact.getAllContactByClient(client.getNif());
            observableListAddress = FXCollections.observableArrayList(listAddress);
            observableListContact = FXCollections.observableArrayList(listContact);
            tableMoradas.setItems(observableListAddress);
            tableContactos.setItems(observableListContact);


        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Erro", "Não existe cliente selecionado", "Selecione um cliente");

        }
    }

    public boolean isBtnUpdateOperator() {
        return btnUpdateOperator;
    }

    public void setBtnUpdateOperator(boolean btnUpdateOperator) {
        this.btnUpdateOperator = btnUpdateOperator;
    }

    private boolean btnUpdateOperator;

    /**
     * call update get values
     * @param actionEvent
     */
    @FXML
    public void updateOperator(ActionEvent actionEvent) {
        setBtnUpdateOperator(true);
        updateGetValues();
        stageDialog.close();
    }

    /**
     * set values to object
     */
    private void updateGetValues() {
        try {
            client.setNif(Integer.parseInt(nif.getText()));
            client.setName(name.getText());
            client.setNotes(Notas.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * change boolean to close window
     * @param actionEvent
     */
    @FXML
    public void cancelWindow(ActionEvent actionEvent) {
        setBtnUpdateOperator(false);
        stageDialog.close();
    }

    /**
     * change address button
     * @param actionEvent
     * @throws SQLException
     */
    public void alterarMorada(ActionEvent actionEvent) throws SQLException {

        Address a = tableMoradas.getSelectionModel().getSelectedItem();
        if ( a != null) {
            a.setAddress(txtMorada.getText());
            a.setLocation(txtLocal.getText());
            a.setCountry(txtPais.getText());
            a.setPostalCode(txtCodPostal.getText());
            if (checkMorada.isSelected())
                a.setMainAddress(true);
            else
                a.setMainAddress(false);

            if (DatabaseAddress.updateAddressDB(a)) {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Morada alterada", "Morada alterada com sucesso");
                listAddress = DatabaseAddress.getAllAddressesByClient(client.getNif());
                observableListAddress = FXCollections.observableArrayList(listAddress);
                tableMoradas.setItems(observableListAddress);
            } else {
                Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar", "Erro ao alterar a Morada");
            }
        }
        else {
            Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Morada nao está selecionada", "Selecione uma morada");}

        txtMorada.clear();
        txtPais.clear();
        txtLocal.clear();
        txtCodPostal.clear();
        checkMorada.setSelected(false);
    }

    /**
     * change contact button
     * @param actionEvent
     * @throws SQLException
     */
    public void alterarContacto(ActionEvent actionEvent) throws SQLException {
        Contact c = tableContactos.getSelectionModel().getSelectedItem();
        if ( c != null) {
            c.setContact(txtContacto.getText());
            String id_tipo = Contact.findIdfromContact((String) choiceTipo.getSelectionModel().getSelectedItem());
            c.setIdTypeContact(id_tipo);
            c.setObs(txtObsContacto.getText());
            if (checkContacto.isSelected())
                c.setMainContact(true);
            else
                c.setMainContact(false);
            if (DatabaseContact.updateContactDB(c)) {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Contacto alterado", "Contacto alterado com sucesso");
                listContact = DatabaseContact.getAllContactByClient(client.getNif());
                observableListContact = FXCollections.observableArrayList(listContact);
                tableContactos.setItems(observableListContact);
            } else {
                Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar", "Erro ao alterar o Contacto");
            }
        }else {
            Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Contacto nao está selecionado", "Selecione um contacto");
           }

        txtContacto.clear();
        txtObsContacto.clear();
        choiceTipo.getSelectionModel().clearSelection();
        checkContacto.setSelected(false);
    }

    /**
     * create address button
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void criarMorada(ActionEvent event) throws IOException, SQLException {
        InsertMorada.c = client;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/criarMorada.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(txtMorada.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
        listAddress = DatabaseAddress.getAllAddressesByClient(client.getNif());
        observableListAddress = FXCollections.observableArrayList(listAddress);
        tableMoradas.setItems(observableListAddress);

    }

    /**
     * create contact button
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void criarContacto(ActionEvent event) throws IOException, SQLException {
        InsertContact.c = client;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/criarContacto.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(txtContacto.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
        listContact = DatabaseContact.getAllContactByClient(client.getNif());
        observableListContact = FXCollections.observableArrayList(listContact);
        tableContactos.setItems(observableListContact);
    }

    /**
     * load selected object
     * @param a
     */
    public void selecionarItemA(Address a) {
        try {
            txtMorada.setText(a.getAddress());
            txtCodPostal.setText(a.getPostalCode());
            txtLocal.setText(a.getLocation());
            txtPais.setText(a.getCountry());
            if(a.getMainAddress())
                checkMorada.setSelected(true);
            else
                checkMorada.setSelected(false);


        } catch (Exception e) {
        }
    }

    /**
     * load selected object
     * @param c
     */
    public void selecionarItemC(Contact c) {
        try {
            txtContacto.setText(c.getContact());
            listContactType = DatabaseContact.getContactsTypeDB();
            observableListContactType = FXCollections.observableArrayList(listContactType);
            choiceTipo.setItems(observableListContactType);
            choiceTipo.setValue(Contact.findContactfromId(c.getIdTypeContact()));
            txtObsContacto.setText(c.getObs());
            if(c.getMainContact())
                checkContacto.setSelected(true);
            else
                checkContacto.setSelected(false);


        } catch (Exception e) {
        }
    }


    /**
     * When this scene is opened, this method adds a listener to the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableMoradas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    if (newValue != null)
                    selecionarItemA(newValue);
                });

        tableContactos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    if (newValue != null)
                        selecionarItemC(newValue);
                });
    }
}
