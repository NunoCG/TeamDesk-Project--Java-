package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GestaoDeEncomendas {
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;
    public Button gestaoDeClientes;
    public Button gestaoDeEncomendas;

    public void switchToGestaoDeClientes(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeClientes.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void gestaoDeEncomendas(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeEncomendas.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void fichasTecnicas(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/fichasTecnicas.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void configuracoes(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    public void sair(ActionEvent event) {

        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }
}
