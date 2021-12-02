package controller;

import Model.td.Component;
import Model.td.Product;
import Model.td.database.DatabaseComponent;
import Model.td.database.DatabaseProduct;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisualizarComponente implements Initializable {

    public Label labelLote;
    public Label labelEstado;
    public Label labelID;
    public Label labelVersao;
    public Label labelDesig;
    public Label labelDesigComercial;
    public Label labelUnidade;
    public TableView<Component> tabelaView;
    public TableColumn tabelaColunaNome;
    public TableColumn tabelaColunaReferencia;
    public ArrayList<Component> listProduct;
    public ObservableList<Component> observableListProduct;
    public Button alterar;
    public Button remover;
    public Label labelAlternativa;

    /**
     * When this scene is opened, this method loads the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableView();
        tabelaView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        selecionarItem(newValue);
                    }
                });
    }

    /**
     * Defines the column's cell values and sets the table items
     */
    public void loadTableView() {

        tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("tradeName"));
        tabelaColunaReferencia.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        listProduct = DatabaseComponent.getAllComponents();
        observableListProduct = FXCollections.observableArrayList(listProduct);
        tabelaView.setItems(observableListProduct);
    }

    /**
     * Sets the textfields with component's properties
     * @param component
     */
    public void selecionarItem(Component component) {
        labelID.setText(component.getIdProduct());
        labelDesigComercial.setText(component.getTradeName());
        if (component.isStatus())
            labelEstado.setText("Disponivel");
        else
            labelEstado.setText("Indisponivel");
        labelLote.setText(String.valueOf(component.getQuantity()));
        labelUnidade.setText(component.getUnity());
        labelVersao.setText(component.getVersionProduct());
    }

    /**
     * Removes a component
     * @param actionEvent
     */
    public void remover(ActionEvent actionEvent) {
    }

    /**
     * Loads Update Component scene
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void alterar(ActionEvent actionEvent) throws IOException, SQLException {
        Component component = tabelaView.getSelectionModel().getSelectedItem();
        boolean btnInsertClicked = controlletUpdateComponent(component);
        if (btnInsertClicked) {
            if (!DatabaseComponent.updateComponent(component)) {
                Helper.showAlert(Alert.AlertType.ERROR, "Alterar produto", "Erro!","Erro ao alterar produto!");
            } else {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Alterar produto", "Alterado","Produto alterado com sucesso!");
                loadTableView();
                tabelaView.getSelectionModel().select(component);
            }

        }
    }

    /**
     * Goes back to previous scene
     * @param event
     * @throws IOException
     */
    public void voltar(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Loads Update Component Scene
     * @param component
     * @return
     * @throws IOException
     */
    public static boolean controlletUpdateComponent(Component component) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateComponentController.class.getResource("/View/updateComponent.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Alterar Produto");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        UpdateComponentController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setComponent(component);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnInsertClicked();
    }
}
