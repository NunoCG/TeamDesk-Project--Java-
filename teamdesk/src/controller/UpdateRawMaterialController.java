package controller;

import Model.td.Product;
import Model.td.UnityType;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateRawMaterialController implements Initializable {
    public TextField ref_product;
    public TextField designation_product;
    public TextField version_product;
    public TextField tradName_product;
    public TextField batch_product;
    public ComboBox select_unity;
    public CheckBox checkbox_product;
    public TextField quantity_product;
    public RadioButton radio_enabled;
    public RadioButton radio_desabled;
    public Button btn_update;
    public Button btn_cancel_datasheet;

    private Stage stageDialog;
    private boolean btnInsertClicked = false;
    private Product product;
    @FXML
    private ToggleGroup gender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxUnityType();
    }

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    public boolean isBtnInsertClicked() {
        return btnInsertClicked;
    }

    public void setBtnInsertClicked(boolean btnInsertClicked) {
        this.btnInsertClicked = btnInsertClicked;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * Sets the textfield values with the product's properties
     * @param product
     */
    public void setProduct(Product product) {
        this.product = product;
        System.out.println(product);
        ref_product.setText(product.getIdProduct());
        ref_product.setDisable(true);
        version_product.setText(product.getVersionProduct());
        version_product.setDisable(true);
        tradName_product.setText(product.getTradeName());
        batch_product.setText(String.valueOf(product.getBatch()));
        designation_product.setText(product.getDesignation());
        select_unity.setValue(product.getUnity());

    }

    /**
     * Gets the values from the textfields and passes them to another scene
     * @param actionEvent
     */
    public void handle_update(ActionEvent actionEvent) {
        getValuesFields();
        btnInsertClicked = true;
        stageDialog.close();
    }

    /**
     * Cancels the changes and closes the scene
     * @param actionEvent
     */
    public void cancel_datasheet(ActionEvent actionEvent) {
        stageDialog.close();
    }


    /**
     * Sets the combobox values
     */
    public void comboBoxUnityType() {
        //get values ENUM UnityType and put in comboBox
        select_unity.getItems().addAll(UnityType.values());
    }

    /**
     * Gets the values from all fields and sets them in the product
     */
    public void getValuesFields() {
        if (verifyFieldIsEmpty()) {
            product.setDesignation(designation_product.getText());
            product.setTradeName(tradName_product.getText());
            product.setBatch(Double.parseDouble(batch_product.getText()));
            product.setUnity(select_unity.getSelectionModel().getSelectedItem().toString());
            product.setStatus(radio_enabled.isSelected());
        }
    }

    /**
     * Verifies if fields are empty
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


        if (select_unity.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione unidade inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Helper.showAlert(Alert.AlertType.WARNING, "Inválido","Campos inválidos!", errorMessage);
            return false;
        }
    }
}
