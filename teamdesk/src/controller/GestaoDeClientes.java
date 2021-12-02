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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GestaoDeClientes implements Initializable {


    public Label labelNome;
    public Label labelNIF;
    public Label labelNotas;
    public Button alterar;
    public Button remover;
    public Button voltarMenu;
    public Button criar;
    public TableView tabelaView;
    public TableColumn colunaNome;
    public TableColumn colunaNIF;
    public ArrayList<Client> listClient;
    public ArrayList<Address> listAddress;
    public ArrayList<Contact> listContact;
    public ObservableList<Client> observableListClient;
    static Alert a = new Alert(Alert.AlertType.NONE);
    public TableView<Contact> tableContacto;
    public TableView<Address> tableMorada;
    public TableColumn<Contact, String> colunaContacto;
    public TableColumn<Address, String> colunaMorada;
    public ObservableList<Address> observableListAddress;
    public ObservableList<Contact> observableListContact;

    /**
     * Change info from client
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void alterarCliente(ActionEvent actionEvent) throws SQLException, IOException {
        Client client = (Client) tabelaView.getSelectionModel().getSelectedItem();
        if (client != null) {
            boolean btnUpdateClicked = UpdateClientController(client);
            if (btnUpdateClicked) {
                if (DatabaseClient.updateClientDB(client)) {
                    Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Cliente alterado", "Cliente alterado com sucesso");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar", "Erro ao alterar o Cliente");
                }
                loadTableView();
            }
        }
    }

    /**
     * Call Update Controller
     *
     * @param client
     * @return
     * @throws IOException
     */
    public boolean UpdateClientController(Client client) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateOperator.class.getResource("/View/UpdateClient.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Alterar cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o clieot all things are profitable.nte no Controller.
        UpdateClient controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setClient(client);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnUpdateOperator();

    }

    /**
    * Removes a client
    */
    /*public void removerCliente(ActionEvent actionEvent) {
        Client client = (Client) tabelaView.getSelectionModel().getSelectedItem();
        a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja eliminar?", ButtonType.YES, ButtonType.CANCEL);
        try {
            if (client == null) {
                showAlert(Alert.AlertType.WARNING, "Erro", "Não existe cliente selecionado", "Selecione um cliente");
            } else if (a.showAndWait().get() == ButtonType.YES) {
                DatabaseClient.DeleteClient(client);
                loadTableView();
                showAlert(Alert.AlertType.INFORMATION, "Informação", "Cliente removido", "Cliente removido com sucesso");
            }
        } catch (SQLException throwables) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao eliminar", "Erro ao eliminar o cliente");
        }
    }*/

    /**
     * Back to previous scene
     *
     * @param actionEvent
     * @throws IOException
     */
    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent op = FXMLLoader.load(getClass().getClassLoader().getResource("View/menu.fxml"));
        Scene operator = new Scene(op);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(operator);
        window.show();
    }

    /**
     * Call insert client scene
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void criarCliente(ActionEvent actionEvent) throws IOException, SQLException {
        if (insertClientController()) {
            loadTableView();
        }
    }

    /**
     * load table view with object properties
     *
     * @throws SQLException
     */
    public void loadTableView() throws SQLException {

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colunaNIF.setCellValueFactory(new PropertyValueFactory<>("nif"));
        listClient = DatabaseClient.getAllClientsDB();
        observableListClient = FXCollections.observableArrayList(listClient);
        tabelaView.setItems(observableListClient);
    }

    /**
     * load addresses and contacts
     *
     * @param c - Client
     * @throws SQLException
     */
    public void loadTableAddressContact(Client c) throws SQLException {

        colunaMorada.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getAddress().toString()));
        colunaContacto.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getContact().toString()));
        listAddress = DatabaseAddress.getAllAddressesByClient(c.getNif());
        listContact = DatabaseContact.getAllContactByClient(c.getNif());
        observableListAddress = FXCollections.observableArrayList(listAddress);
        observableListContact = FXCollections.observableArrayList(listContact);
        tableMorada.setItems(observableListAddress);
        tableContacto.setItems(observableListContact);
    }

    /**
     * load table from selected item
     *
     * @param c
     */
    public void selecionarItem(Client c) {
        try {
            labelNome.setText(c.getName());
            labelNIF.setText(String.valueOf(c.getNif()));
            labelNotas.setText(c.getNotes());
            loadTableAddressContact(c);


        } catch (Exception e) {
        }
    }

    /**
     * When this scene is opened, this method loads the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTableView();
            tabelaView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> selecionarItem((Client) newValue));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Call insert Client Controller
     *
     * @return
     * @throws IOException
     */
    public boolean insertClientController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateClient.class.getResource("/View/Client_FX.fxml"));
        AnchorPane page = loader.load();
        Scene sceneclient = new Scene(page);
        Stage stage = new Stage();
        stage.setTitle("Criar cliente");
        stage.setScene(sceneclient);
        InsertClient controller = loader.getController();
        stage.showAndWait();
        return controller.isClicked();
    }

    /**
     * show Alert method
     *
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
}
