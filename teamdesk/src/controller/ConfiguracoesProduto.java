package controller;

import Model.td.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConfiguracoesProduto /*implements Initializable*/ {

    public Button gestaoDeClientes;
    public Button gestaoDeEncomendas;
    public Button btnFichas;
    public Button btnConfig;
    public Button btnSair;
    public ListView mListView;
    public Button btnMostrarTodos;
    public TableColumn tableID;
    public TableColumn tableNome;
    public TableColumn tableDescri;
    public TextField searchBox;
    public Button btnSearch;
    public TableView table;

    public void switchToGestaoDeClientes(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeClientes.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();

    }

    public void gestaoDeEncomendas(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/gestaoDeClientes.fxml"));
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

    public void sair(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnSair.getScene().getWindow();
        stage.close();
    }

    public void handleItemsClicks() {
        mListView.setOnMouseClicked(mouseEvent -> {
            String selectedItem = mListView.getSelectionModel().getSelectedItem().toString();
            Dialog d = new Alert(Alert.AlertType.INFORMATION,selectedItem);
            d.show();
        });
    }

    public void showAllProducts(ActionEvent event) throws IOException {

        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configuracoesProduto.fxml"));
        Scene scene = new Scene(new Group(gestaoDeClientes));
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        //initialize();
        handleItemsClicks();
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(mListView);
        vbox.setAlignment(Pos.CENTER_RIGHT);

        Group group = ((Group) scene.getRoot());
        group.getChildren().addAll(vbox);
        group.setLayoutX(100);
        window.setScene(scene);
        window.show();
    }

    /*
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Product> list = Model.td.Product.getAllProductsDB();

        for (Product products : list) {
            tableID.setCellFactory(products.getIdProduct());
        }
    }*/
}
