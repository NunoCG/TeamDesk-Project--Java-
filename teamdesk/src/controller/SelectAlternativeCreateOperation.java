package controller;

import Model.td.Component;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectAlternativeCreateOperation implements Initializable {
    public static Button btn_insert_operation;
    public Button btn_cancel_component;
    public TableView table_view_components;
    public TableColumn table_view_components_reference;
    public TableColumn table_view_components_version;
    public TableColumn table_view_components_designation;
    public TableColumn table_view_components_status;
    public static ObservableList<Component> listaAlternativasCreate = FXCollections.observableArrayList();


    /**
     * Cancels the changes and goes back to the previous scene
     *
     * @param actionEvent
     */
    public void cancel_component(ActionEvent actionEvent) {
        btn_cancel_component.getScene().getWindow().hide();
    }

    /**
     * Inserts an alternative into the operation
     */
    public void insert_components_operation() {
        if (table_view_components.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Por favor selecione um componente");
            alert.showAndWait();
        } else {
            Component alternativa = (Component) table_view_components.getSelectionModel().getSelectedItem();
            CreateOperationController.operacaoPublicaCreate.getComponents().get(CreateOperationController.indicePublicoCreate).setAlternative(alternativa);
            listaAlternativasCreate.set(CreateOperationController.indicePublicoCreate, alternativa);
            CreateOperationController.observableListSelectCompsCreate.remove(alternativa);
            btn_cancel_component.getScene().getWindow().hide();
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
        table_view_components.setItems(CreateOperationController.observableListSelectCompsCreate);
    }

    /**
     * When this scene is opened, this method loads the table
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadComponentsListView();
    }

}
