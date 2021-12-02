package controller;

import Model.td.Product;
import Model.td.UnityType;
import Model.td.database.DatabaseProduct;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.ls.LSOutput;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Product_fx implements Initializable {

    public TextField ref_product;
    public TextField version_product;
    public TextField designation_product;
    public TextField tradName_product;
    public TextField batch_product;
    public CheckBox checkbox_product;
    public TextField quantity_product;
    public RadioButton radio_enabled;
    public ToggleGroup gender;
    public ComboBox select_unity;
    public Button btn_save_product;
    public RadioButton radio_desabled;
    public Button btn_insert_operation;
    public Button btn_cancel_datasheet;

    /**
     * When this scene is opened, this method loads the combobox
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        select_unity.getItems().addAll(UnityType.values());
    }


    /**
     * Sets the products properties
     * @param actionEvent
     */
    @FXML
    public void save_product(ActionEvent actionEvent) {
        Product product = new Product();
        if (verifyFieldIsEmpty()) {
            try {
                product.setIdProduct(ref_product.getText());
                product.setVersionProduct(version_product.getText());
                product.setDesignation(designation_product.getText());
                product.setTradeName(tradName_product.getText());
                product.setBatch(Double.parseDouble(batch_product.getText()));
                product.setUnity(select_unity.getSelectionModel().getSelectedItem().toString());
                RadioButton chk = (RadioButton) gender.getSelectedToggle(); // Cast object to radio button
                product.setStatus(chk.getText().equals("Ativo"));

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
    }

    /**
     * Cancels the changes
     * @param actionEvent
     */
    @FXML
    public void cancel_datasheet(ActionEvent actionEvent) {
    }

    /**
     * Inserts operation into the product
     * @param actionEvent
     */
    @FXML
    public void insert_operation_product(ActionEvent actionEvent) {
    }

    @FXML
    public void desabled_radio(ActionEvent actionEvent) {
    }

    @FXML
    public void enabled_radio(ActionEvent actionEvent) {
    }

    /**
     * Shows an alert
     * @param alertType
     * @param title
     * @param message
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Checkbox handler
     * @param actionEvent
     */


    /**
     * Verifies if fields are empty
     * @return
     */
    public boolean verifyFieldIsEmpty() {
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

        if (quantity_product.getText().trim().isEmpty() || quantity_product.getText() == null) {
            errorMessage += "Quantidade inválida!\n";
        } else {
            try {
                Double.parseDouble(quantity_product.getText());
            } catch (Exception e) {
                errorMessage += "Quantidade precisa ser número!\n";
            }
        }
        if (select_unity.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione unidade inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showAlert(Alert.AlertType.WARNING, "Campos inválidos!", errorMessage);
            return false;
        }
    }
}
