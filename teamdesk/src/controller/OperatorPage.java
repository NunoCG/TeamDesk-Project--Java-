package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class OperatorPage {
    public Button CreateOperator;
    public Button gestaoDeClients;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;

    public void CreateOperatorFX(ActionEvent actionEvent) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/Operator_FX.fxml"));
        Scene op = new Scene(operator);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(op);
        window.show();
    }

    public void switchToGestaoDeClientes(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeClientes.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void gestaoDeEncomendas(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeEncomendas.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void fichasTecnicas(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/fichasTecnicas.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void configuracoes(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void sair(ActionEvent event) {

        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }
}