package controller;

import Model.td.Operator;
import Model.td.Schedule;
import Model.td.database.DatabaseOperator;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


public class UpdateOperator {
    public TextField idoperator;
    public TextField nameoperator;
    public TextField timeoperator;
    public Button update;
    public RadioButton activeOperator;
    public ToggleGroup Status;
    public RadioButton inactiveOperator;
    public RadioButton masculineOperator;
    public ToggleGroup Gender;
    public RadioButton feminineOperator;
    public Button close;

    private static Stage stageDialog;
    public Button viewSchedule;
    private Operator operator;

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    public Operator getOperator() {
        return operator;
    }

    /**
     * Sets the text field values with operation's properties
     *
     * @param operator
     */
    public void setOperator(Operator operator) {
        try {
            this.operator = operator;
            this.idoperator.setText(operator.getId());
            this.idoperator.setDisable(true);
            this.nameoperator.setText(operator.getName());
            if (operator.isStatus() == true) {
                activeOperator.setSelected(true);
            } else {
                inactiveOperator.setSelected(true);
            }
            if (operator.getGender().equals("Masculino")) {
                masculineOperator.setSelected(true);
            } else {
                feminineOperator.setSelected(true);
            }
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Erro", "Não existe operador selecionado", "Selecione um operador");

        }
    }

    public boolean isBtnUpdateOperator() {
        return btnUpdateOperator;
    }

    public void setBtnUpdateOperator(boolean btnUpdateOperator) {
        this.btnUpdateOperator = btnUpdateOperator;
    }

    private boolean btnUpdateOperator;

    /**
     * Gets values from textfields and passes them to another scene
     *
     * @param actionEvent
     */
    @FXML
    public void updateOperator(ActionEvent actionEvent) {
        setBtnUpdateOperator(true);
        updateGetValues();
        stageDialog.close();
    }

    /**
     * Sets the operator with values from the textfields
     */
    private void updateGetValues() {
        boolean status;
        String gender;
        try {
            operator.setId((idoperator.getText()));
            operator.setName(nameoperator.getText());
            if (activeOperator.isSelected()) {
                status = true;
            } else {
                status = false;
            }
            operator.setStatus(status);
            if (masculineOperator.isSelected()) {
                gender = "Masculino";
            } else {
                gender = "Feminino";
            }
            operator.setGender(gender);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cancels the changes and goes back to the previous window
     *
     * @param actionEvent
     */
    @FXML
    public void cancelWindow(ActionEvent actionEvent) {
        setBtnUpdateOperator(false);
        stageDialog.close();
    }


    public void viewScheduleBt(ActionEvent actionEvent) throws IOException, SQLException {
        //viewSchedule.setDisable(true);
         ArrayList<Schedule> schedules = DatabaseOperator.getScheduleBDByIDOperator(operator.getId());
        //System.out.println(operator.setListSchedule(schedules));
        createScheduleController(operator.getListSchedule());

        }


    public boolean createScheduleController(ArrayList<Schedule> schedules) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CreateSchedule.class.getResource("/view/createSchedule.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Inserir horário");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        CreateSchedule controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setListSchedule(schedules);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnInsertClicked();
    }
}
