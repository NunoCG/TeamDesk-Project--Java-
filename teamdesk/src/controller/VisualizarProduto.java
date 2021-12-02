package controller;

import Model.td.Operation;
import Model.td.Product;
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
import java.util.ResourceBundle;

public class VisualizarProduto implements Initializable {

    public Label labelLote;
    public Label labelEstado;
    public Label labelID;
    public Label labelVersao;
    public Label labelDesig;
    public Label labelDesigComercial;
    public Label labelUnidade;
    public TableView<Product> tabelaView;
    public TableColumn tabelaColunaNome;
    public TableColumn tabelaColunaReferencia;
    public ArrayList<Product> listProduct;
    public ObservableList<Product> observableListProduct;
    public Button alterar;
    public Button remover;

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
                }
        );
    }

    /**
     * Defines the columns's cell values and populates the table
     */
    public void loadTableView() {
        tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("designation"));
        tabelaColunaReferencia.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        listProduct = DatabaseProduct.getAllProductsDB();
        observableListProduct = FXCollections.observableArrayList(listProduct);
        tabelaView.setItems(observableListProduct);
    }

    /**
     * Gets the values from the textfields and inserts them into the product
     * @param p
     */
    public void selecionarItem(Product p) {
        labelDesig.setText(p.getDesignation());
        labelDesigComercial.setText(p.getTradeName());
        if (p.isStatus())
            labelEstado.setText("Disponivel");
        else
            labelEstado.setText("Indisponivel");
        labelID.setText(p.getIdProduct());
        labelLote.setText(String.valueOf(p.getBatch()));
        labelUnidade.setText(p.getUnity());
        labelVersao.setText(p.getVersionProduct());

    }

    public void remover(ActionEvent actionEvent) {
    }

    /**
     * Opens Update Product scene
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void alterar(ActionEvent actionEvent) throws IOException, SQLException {
        Product product = tabelaView.getSelectionModel().getSelectedItem();
        boolean btnInsertClicked = controlletUpdateProduct(product);
        if (btnInsertClicked) {
            if (!DatabaseProduct.updateProducts(product)) {
                Helper.showAlert(Alert.AlertType.ERROR, "Alterar produto", "Erro!","Erro ao alterar produto!");
            } else {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Alterar produto", "Alterado","Produto alterado com sucesso!");
                loadTableView();
                tabelaView.getSelectionModel().select(product);
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
     * Opens Update Product Scene
     * @param product
     * @return
     * @throws IOException
     */
    public static boolean controlletUpdateProduct(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateProductController.class.getResource("/View/updateProduct.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Alterar Produto");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        UpdateProductController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setProduct(product);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnInsertClicked();
    }

}