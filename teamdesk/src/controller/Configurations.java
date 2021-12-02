package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Configurations {
    public Button gestaoDeClientes;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;
    public Button btnProduto;
    public Button btnComponente;
    public Button btnMateriaPrima;
    public Button btnMaquina;
    public Button btnOperador;
    public Button btnTipoOperacao;


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
     */
    public void sair(ActionEvent event) {
        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }

    /**
     * Switches to the Products menu
     */
    public void menuProduto(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/visualizarProduto.fxml"));
        Scene op = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(op);
        window.show();
    }

    /**
     * Switches to the Components menu
     */
    public void menuComponente(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/visualizarComponente.fxml"));
        Scene op = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(op);
        window.show();
    }

    /**
     * Switches to the Raw Material menu
     */
    public void menuMateriaPrima(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/visualizarMateriaPrima.fxml"));
        Scene op = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(op);
        window.show();
    }

    /**
     * Switches to the Machines menu
     */
    public void menuMaquina(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("View/machineConfig.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Switches to the Operators menu
     */
    public void menuOperador(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/visualizarOperador.fxml"));
        Scene op = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(op);
        window.show();

        /*
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateOperator.class.getResource("/view/visualizarOperador.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Visualizar operador");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();
*/
    }

    /**
     * Switches to the Operation Type menu
     */
    public void menuTipoOperacao(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/menuTipoOperacao.fxml"));
        Scene operatorScene = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(operatorScene);
        window.show();
    }

    public void menuOperacoes(ActionEvent event) throws IOException {
        Parent operator = FXMLLoader.load(getClass().getClassLoader().getResource("View/VIsualizarOperacao.fxml"));
        Scene operatorScene = new Scene(operator);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(operatorScene);
        window.show();
    }
}


