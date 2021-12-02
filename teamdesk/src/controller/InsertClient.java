package controller;

import Model.td.Client;
import Model.td.Operator;
import Model.td.Schedule;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseOperator;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InsertClient implements Initializable {

    public Button finish;
    public Button close;
    public TextField notas;
    public TextField name;
    public TextField nif;
    private Client client;
    private boolean clicked;

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();
    }

    /**
     * Insert Operator with javaFX
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void finishClient(ActionEvent actionEvent) throws SQLException {
        if (nif.getText().isEmpty() || name.getText().isEmpty()) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher", "Campos por preencher");
        } else {
            client.setNif(Integer.parseInt(nif.getText()));
            client.setName(name.getText());
            client.setNotes(notas.getText());
            if (nif.getText().isEmpty() || name.getText().isEmpty()) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher", "Campos por preencher");
            } else {
                if (DatabaseClient.insertClientDB(client) == true) {
                    Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Cliente inserido", "Cliente inserido com sucesso");
                    setClicked(true);
                    closeOperatorWindows();
                } else {
                    Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inserir", "Erro ao inserir o cliente");
                }
            }

        }

    }

    /**
     * Closes this window
     * @param actionEvent
     * @throws IOException
     */
    public void closeWindow(ActionEvent actionEvent) throws IOException {
        closeOperatorWindows();
    }

    /**
     * Closes Operator's Window
     */
    public void closeOperatorWindows() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }


    }




