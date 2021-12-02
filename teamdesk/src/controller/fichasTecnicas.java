package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class fichasTecnicas {
    public Button gestaoDeClientes;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;
    public Button btnVisFichas;
    public Button btnPreencherFichas;
    public Button btnApagarFichas;

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

    public void visualizar(ActionEvent event) {

    }

    public void preencher(ActionEvent event) {

    }


    public void apagar(ActionEvent event) {

    }
}
