package controller;

import Model.td.OperationType;
import Model.td.database.DatabaseOperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class InsertOperationType {

    public TextField inputtipo;
    public Button criartipo;
    public Label tipooperacao;
    public Label titulo;
    public Label idtipo;
    public TextField inputid;
    public Button btnVoltar;

    public void inputtipo(ActionEvent actionEvent) throws SQLException {


    }

    /**
     * Creates an Operation Type
     *
     * @param actionEvent
     */
    public void criartipo(ActionEvent actionEvent) {
        int id = 0;
        if (inputid.getText().isEmpty() || inputtipo.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher", "Campos por preencher");
        } else {
            try {
                id = Integer.parseInt(inputid.getText());

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Informação", "Tipo de Operacao ID", "O Id da operação só é permitido numeros");
            }
            if (id != 0) {
                String info = inputtipo.getText();
                OperationType ot = OperationType.createOperationType(id, info);

                try {
                    if (DatabaseOperationType.insertOperationTypeDB(ot))
                        showAlert(Alert.AlertType.INFORMATION, "Informação", "Tipo de Operacao inserido", "Inserido com sucesso");
                    else
                        showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inserir", "Provavelmente ja existe");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public void inputid(ActionEvent actionEvent) {
    }

    /**
     * Goes back to the previous submenu
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
     *
     * @param alertType
     * @param title
     * @param header
     * @param message
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
