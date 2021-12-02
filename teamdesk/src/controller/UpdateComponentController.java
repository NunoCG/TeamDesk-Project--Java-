package controller;

import Model.td.Component;
import Model.td.Product;
import Model.td.UnityType;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateComponentController implements Initializable {
    public TextField ref_product;
    public TextField designation_product;
    public TextField version_product;
    public TextField tradName_product;
    public TextField batch_product;
    public ComboBox select_unity;
    public CheckBox checkbox_product;
    public TextField quantity_product;
    public RadioButton radio_enabled;
    public RadioButton radio_disabled;
    public Button btn_update;
    public Button btn_cancel_datasheet;
    private Stage stageDialog;
    private boolean btnInsertClicked = false;
    private Component component;

    
    public TextField getRef_product() {
        return ref_product;
    }

    public void setRef_product(TextField ref_product) {
        this.ref_product = ref_product;
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

    public Component getComponent() {
        return component;
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
     * Sets the textfields with values from the component
     * @param component
     */
    public void setComponent(Component component) {
        this.component = component;
        ref_product.setText(component.getIdProduct());
        ref_product.setDisable(true);
        version_product.setText(component.getVersionProduct());
        version_product.setDisable(true);
        tradName_product.setText(component.getTradeName());
        select_unity.setValue(component.getUnity());

    }


    /**
     * Gets the values from the fields and passes them to another scene
     * @param actionEvent
     */
    public void handle_update(ActionEvent actionEvent) {
        getValuesFields();
        setBtnInsertClicked(true);
        stageDialog.close();
    }

    /**
     * Cancels the changes
     * @param actionEvent
     */
    public void cancel_datasheet(ActionEvent actionEvent) {
        stageDialog.close();
    }

    /**
     * Loads the combobox with information from an object
     */
    public void comboBoxUnityType() {
        //get values ENUM UnityType and put in comboBox
        select_unity.getItems().addAll(UnityType.values());
    }

    /**
     * Gets the values from all fields
     */
    public void getValuesFields() {
        if (verifyFieldIsEmpty()) {
            component.setTradeName(tradName_product.getText());
            component.setUnity(select_unity.getSelectionModel().getSelectedItem().toString());
            component.setStatus(radio_enabled.isSelected());
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

        if (tradName_product.getText().trim().isEmpty() || tradName_product.getText() == null) {
            errorMessage += "Designação Comercial inválida!\n";
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
