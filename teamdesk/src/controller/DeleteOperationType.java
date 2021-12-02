package controller;

import Model.td.OperationType;
import Model.td.database.DatabaseOperationType;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DeleteOperationType implements Initializable {

    public Button btnVoltar;
    public TextField to;
    public Button a;
    ObservableList<Object> list = FXCollections.observableArrayList();
    public Button apagartipo;
    public Label titulo;
    public Label idtipo;
    public ChoiceBox tiposOperacao;


    /**
     * Deletes an operation type
     *
     * @param actionEvent
     */
    public void apagartipo(ActionEvent actionEvent) {
        Object ot = tiposOperacao.getValue();
        String info = ot.toString();
        try {
            if (DatabaseOperationType.deleteOperationType(info)) {
                showAlert(Alert.AlertType.INFORMATION, "Informação", "Tipo de Operacao removido", "Removido com sucesso");
                list.remove(ot);
                tiposOperacao.getItems().setAll(list);
                to.setText("");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover", "Não pode eliminar tipos de operação de máquinas existentes");
                DatabaseOperationType.deleteOperationType(info);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Loads the combobox with items from the database
     *
     * @throws SQLException
     */
    private void loadData() throws SQLException {
        list.removeAll(list);
        ArrayList<OperationType> ot = DatabaseOperationType.getAllOperationTypesBD();
        for (OperationType o : ot) {
            list.add(o.getId());
        }
        tiposOperacao.getItems().addAll(list);
    }

    /**
     * Loads all information when this scene is opened
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Goes back the previous scene
     *
     * @param actionEvent
     * @throws IOException
     */
    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/menuTipoOperacao.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Shows an alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param actionEvent
     */
    public void tometodo(ActionEvent actionEvent) {
    }

    /**
     * Sets text values with information from the database
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void am(ActionEvent actionEvent) throws SQLException {
        Object ot = tiposOperacao.getValue();
        int id = Integer.parseInt(ot.toString());
        ArrayList<OperationType> list = DatabaseOperationType.getAllOperationTypesBD();
        for (OperationType o : list) {
            if (o.getId() == id)
                to.setText(o.getInfo());
        }
    }
}
