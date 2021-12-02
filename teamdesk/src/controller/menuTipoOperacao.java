package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class menuTipoOperacao {
    public Button gestaoDeClientes;
    public Button btnSair;
    public Button btnConfig;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Label titulo;
    public Button btnCriar;
    public Button btnApagar;
    public Button btnVoltar;

    public void switchToGestaoDeClientes(ActionEvent actionEvent) {
    }

    public void gestaoDeEncomendas(ActionEvent actionEvent) {
    }

    public void fichasTecnicas(ActionEvent actionEvent) {
    }

    public void sair(ActionEvent actionEvent) {
    }

    public void configuracoes(ActionEvent actionEvent) {
    }

    public void menuCriar(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/insertOperationType.fxml"));
        Scene operatorScene = new Scene(operator);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(operatorScene);
        window.show();
    }

    public void menuApagar(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/deleteOperationType.fxml"));
        Scene operatorScene = new Scene(operator);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(operatorScene);
        window.show();
    }

    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }
}
