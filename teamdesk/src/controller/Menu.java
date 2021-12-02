package controller;

import Model.td.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Menu implements Initializable {
    public Button gestaoDeClientes;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;
    public ImageView imageView;

    /**
     * Switches to the Client Management  submenu
     */
    public void switchToGestaoDeClientes(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeClientes.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();

    }

    /**
     * Switches to the Orders Management submenu
     */
    public void gestaoDeEncomendas(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/encomenda.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Switches to the datasheets submenu
     */
    public void fichasTecnicas(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/visualizarFichaTecnica.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Switches to the configurations submenu
     */
    public void configuracoes(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Exits the program
     *
     * @param event
     * @throws IOException
     */
    public void sair(ActionEvent event) throws IOException {

        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image (getClass().getResourceAsStream("/styles/logo1.png"));
        imageView.setImage(image);
    }

    /*private void loadPage(String page) throws IOException {

        Parent root = null;
        root = FXMLLoader.load(getClass().getResource(page+".fxml"));
        bp.setCenter(root);
    }*/
}
