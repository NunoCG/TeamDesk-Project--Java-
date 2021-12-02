package controller;

import Model.td.*;
import Model.td.database.DatabaseProduct;
import Model.td.helper.Helper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class InsertDatasheetController implements Initializable {

    public TextField ref_product;
    public TextField version_product;
    public TextField designation_product;
    public TextField tradName_product;
    public TextField batch_product;
    public CheckBox checkbox_product;
    public RadioButton radio_enabled;
    public ToggleGroup gender;
    public ComboBox select_unity;
    public Button btn_save_product;
    public RadioButton radio_desabled;
    public Button btn_insert_operation;
    public Button btn_cancel_datasheet;
    public TableView<Operation> tableOperations;
    public TableColumn<Operation, Integer> columnOrder;
    public TableColumn<Operation, String> columnTypeOperation;
    public TableColumn<Operation, String> columnDescription;
    private Stage StageDialog;
    private ObservableList<Operation> observableListOperations;

    //Atributos
    private Product product;
    private boolean btnCliecked;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isBtnCliecked() {
        return btnCliecked;
    }

    public void setBtnCliecked(boolean btnCliecked) {
        this.btnCliecked = btnCliecked;
    }

    public Stage getStageDialog() {
        return StageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        StageDialog = stageDialog;
    }

    /**
     * When this scene is opened, this method loads the combobox
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxUnityType();
    }

    /**
     * Inserts an operation into the product
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void insert_operation_product(ActionEvent actionEvent) throws IOException {
        Operation operation = new Operation();
        operation.setComponents(new ArrayList<>());
        operation.setType(new OperationType());
        boolean btnInsertClicked = CreateOperationController(operation);
        if (btnInsertClicked) {
            operation.setOrder(product.getOperations().size() + 1);
            product.getOperations().add(operation);
            loadTableOperations();
        }
    }

    /**
     * Saves the product
     * @param actionEvent
     * @throws SQLException
     */
    @FXML
    public void save_product(ActionEvent actionEvent) throws SQLException {
        if (verifyFieldIsEmpty()) {
            populateDatasheet();
            if (product.getOperations().isEmpty()) {
                product.setRawMaterial(true);
            } else {
                product.setRawMaterial(false);
            }

            if (product.saveProduct()) {
                Helper.showAlert(Alert.AlertType.INFORMATION, "Inserir", "Ficha Técnica", "Ficha Técnica foi inserida com sucesso!");
            } else {
                Helper.showAlert(Alert.AlertType.WARNING, "Inserir", "Erro",
                        "Erro ao inserir a Ficha Técnica");
            }
            setBtnCliecked(true);
            getStageDialog().close();
        }

    }

    /**
     * Loads all textfield values
     */
    public void populateDatasheet() {
        product.setIdProduct(ref_product.getText());
        product.setVersionProduct(version_product.getText());
        product.setDesignation(designation_product.getText());
        product.setTradeName(tradName_product.getText());
        product.setBatch(Double.parseDouble(batch_product.getText()));
        product.setUnity(select_unity.getSelectionModel().getSelectedItem().toString());
        RadioButton chk = (RadioButton) gender.getSelectedToggle(); // Cast object to radio button
        product.setStatus(chk.getText().equals("Ativo"));
    }

    /**
     * Cancels the datasheet changes
     * @param actionEvent
     */
    @FXML
    public void cancel_datasheet(ActionEvent actionEvent) {
        getStageDialog().close();
    }

    /**
     * Gets values from object and loads combobox with them
     */
    public void comboBoxUnityType() {
        //get values ENUM UnityType and put in comboBox
        select_unity.getItems().addAll(UnityType.values());
    }

    /**
     * Opens Create Operation Scene
     * @param operation
     * @return
     * @throws IOException
     */
    public boolean CreateOperationController(Operation operation) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CreateOperationController.class.getResource("/view/CreateOperation.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Inserir Operação");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        CreateOperationController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setOperation(operation);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnInsertClicked();
    }

    /**
     * Defines the column's cell values
     */
    public void loadTableOperations() {
        columnOrder.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getOrder()));
        columnTypeOperation.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getType().getInfo()));
        columnDescription.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDescription()));
        observableListOperations = FXCollections.observableArrayList(product.getOperations());
        tableOperations.setItems(observableListOperations);
    }

    /**
     * Verifies if the fields are empty
     * @return
     */
    public boolean verifyFieldIsEmpty() {
        //Verify if fields are empty
        String errorMessage = "";
        if (ref_product.getText().trim().isEmpty() || ref_product.getText() == null) {
            errorMessage += "Referencia inválida!\n";
        }
        if (version_product.getText().trim().isEmpty() || version_product.getText() == null) {
            errorMessage += "Versão inválida!\n";
        }
        if (designation_product.getText().trim().isEmpty() || designation_product.getText() == null) {
            errorMessage += "Designação inválida!\n";
        }
        if (tradName_product.getText().trim().isEmpty() || tradName_product.getText() == null) {
            errorMessage += "Designação Comercial inválida!\n";
        }
        if (batch_product.getText().trim().isEmpty() || batch_product.getText() == null) {
            errorMessage += "Lote inválido!\n";
        } else {
            try {
                Double.parseDouble(batch_product.getText());
            } catch (Exception e) {
                errorMessage += "Lote precisa ser número!\n";
            }
        }
        if (select_unity.getSelectionModel().
                getSelectedItem() == null) {
            errorMessage += "Selecione unidade inválida!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Helper.showAlert(Alert.AlertType.WARNING, "Inválido", "Campos inválidos!", errorMessage);
            return false;
        }

    }

    /**
     * Moves the selected operation to 1 index above
     * @param actionEvent
     */
    public void btnUp(ActionEvent actionEvent) {
        int index = tableOperations.getSelectionModel().getSelectedIndex();
        if (index != 0 && product.getOperations().size() > 1 && index != -1) {
            product.getOperations().get(index - 1).setOrder(index + 1);
            product.getOperations().get(index).setOrder(index);
            sortingOperations();
            loadTableOperations();
        }
    }

    /**
     * Moves the selected operation to 1 index below
     * @param actionEvent
     */
    public void btnDown(ActionEvent actionEvent) {
        int index = tableOperations.getSelectionModel().getSelectedIndex();
        if (index != (product.getOperations().size() - 1) && product.getOperations().size() > 1 && index != -1) {

            product.getOperations().get(index + 1).setOrder(index + 1);
            product.getOperations().get(index).setOrder(index + 2);
            sortingOperations();
            loadTableOperations();
        }
    }

    /**
     * Sorts operations
     */
    public void sortingOperations() {
        Collections.sort(product.getOperations(), (o1, o2) -> {
            int order = String.valueOf(o1.getOrder()).compareTo(String.valueOf(o2.getOrder()));
            return order;
        });
    }

    /**
     * Removes selected operation
     * @param actionEvent
     */
    public void btnRemove(ActionEvent actionEvent) {
        Operation op = tableOperations.getSelectionModel().getSelectedItem();
        if (op != null) {
            product.getOperations().remove(op);
            for (int i = 0; i < product.getOperations().size(); i++) {
                product.getOperations().get(i).setOrder(i + 1);
            }
            loadTableOperations();
        }
    }
}
