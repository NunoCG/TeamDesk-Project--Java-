package controller;

import Model.td.Operation;
import Model.td.OperationType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisualizarFichaTecnica implements Initializable {

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
    public Button remover;
    public ChoiceBox<String> boxOp;
    public TextField txtOp;
    public AnchorPane anchor;
    public Label tituloOp;

    /**
     * When this scene opens, this method loads the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableView();
        tabelaView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItem(newValue));
    }

    /**
     * Defines the column's cell values
     */
    public void loadTableView() {
        tabelaColunaNome.setCellValueFactory(new PropertyValueFactory<>("designation"));
        tabelaColunaReferencia.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        listProduct =DatabaseProduct.getAllProductsDB();
        observableListProduct = FXCollections.observableArrayList(listProduct);
        tabelaView.setItems(observableListProduct);
    }

    /**
     * Gets the textfields values and sets them into the product
     * @param pro
     */
    public void selecionarItem(Product pro) {
        Product.getFullDatasheet(pro);
        labelDesig.setText(pro.getDesignation());
        labelDesigComercial.setText(pro.getTradeName());
        if(pro.isStatus())
            labelEstado.setText("Disponivel");
        else
            labelEstado.setText("Indisponivel");
        labelID.setText(pro.getIdProduct());
        labelLote.setText(String.valueOf(pro.getBatch()));
        labelUnidade.setText(pro.getUnity());
        labelVersao.setText(pro.getVersionProduct());

        anchor.getChildren().clear();
        Accordion ac = new Accordion();

        for (int i = 0; i < pro.getOperations().size(); i++) {
            TitledPane pane = new TitledPane(pro.getOperations().get(i).getDescription() , new Label(pro.getOperations().get(i).toString()));
            ac.getPanes().add(pane);
        }

        if (pro.getOperations().size() > 0 ) {
            tituloOp.setText("Operações");
        }
        anchor.getChildren().add(ac);
    }

    /**
     * Removes
     * @param actionEvent
     */
    public void remover(ActionEvent actionEvent) {
    }

    public void btnInserir(ActionEvent actionEvent) throws IOException {
        Product product = new Product();
        product.setOperations(new ArrayList<>());
        if(controllerDatasheet(product)){
            loadTableView();
        }
    }

    /**
     * Goes back to previous scene
     * @param event
     * @throws IOException
     */
    public void voltar(ActionEvent event) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/menu.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }

    /**
     * Opens Insert Datasheet scene
     * @param product
     * @return
     * @throws IOException
     */
    public static boolean controllerDatasheet(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InsertDatasheetController.class.getResource("/view/insertDataSheet.fxml"));
        AnchorPane page = loader.load();
        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Inserir Operação");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        InsertDatasheetController controller = loader.getController();
        controller.setProduct(product);
        controller.setStageDialog(dialogStage);

        dialogStage.showAndWait();
        return controller.isBtnCliecked();
    }
}
