package controller;

import Model.td.Component;
import Model.td.Product;
import Model.td.UnityType;
import Model.td.database.DatabaseComponent;
import Model.td.database.DatabaseProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectComponentUpdateOperationController implements Initializable {

    public Button btn_insert_operation;
    public Button btn_cancel_component;
    public TableView table_view_components;
    public TableColumn table_view_components_designation;
    public TableColumn table_view_components_status;
    public TableColumn table_view_components_reference;
    public TableColumn table_view_components_version;
    private List<Component> components;
    private ObservableList<Component> observableListProducts;
    public static Component component2;
    public ObservableList<Component> observableListComponentsHolder;
    private boolean flag = true;


    /**
     * When this scene is opened, this method loads the table
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadComponentsListView();
    }

    /**
     * Cancels the changes and goes back to the previous scene
     *
     * @param actionEvent
     */
    @FXML
    public void cancel_component(ActionEvent actionEvent) {
        component2 = null;
        btn_cancel_component.getScene().getWindow().hide();
    }

    /**
     * Inserts a component into the operation
     */
    @FXML
    public void insert_components_operation(ActionEvent actionEvent) {
        if (table_view_components.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Por favor selecione um componente");
            alert.showAndWait();
        } else {
            component2 = (Component) table_view_components.getSelectionModel().getSelectedItem();
            //AlterarOperacao.componentePublico = component2;
            AlterarOperacao.operacaoPublica.getComponents().add(component2);
            AlterarOperacao.observableListSelectComps.remove(component2);
            btn_insert_operation.getScene().getWindow().hide();
        }
    }

    /**
     * Sets the column's cell values
     */
    public void loadComponentsListView() {
        table_view_components_reference.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        table_view_components_version.setCellValueFactory(new PropertyValueFactory<>("versionProduct"));
        table_view_components_designation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        table_view_components_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        table_view_components.setItems(AlterarOperacao.observableListSelectComps);
    }
}


