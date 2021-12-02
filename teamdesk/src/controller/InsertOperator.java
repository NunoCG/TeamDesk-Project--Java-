package controller;

import Model.td.Operator;
import Model.td.Schedule;
import Model.td.database.DatabaseOperator;
import Model.td.helper.Helper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InsertOperator implements Initializable {

    public TextField idoperator;
    public TextField nameoperator;
    public TextField timeoperator;
    public Button finish;
    public RadioButton activeOperator;
    public ToggleGroup Status;
    public RadioButton inactiveOperator;
    public RadioButton masculineOperator;
    public ToggleGroup Gender;
    public RadioButton feminineOperator;
    public Button close;
    public Button insertSchedule;
    public Schedule schedules;
    private Operator operator;
    private boolean clicked;
    public Stage stageDialog;

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }


    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * When this scene is opened, this method instances new objects
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        operator = new Operator();
        schedules = new Schedule();
    }

    /**
     * Insert Operator with javaFX
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void finishOperator(ActionEvent actionEvent) throws SQLException {
        String gender;
        boolean status;
        if (idoperator.getText().isEmpty() || nameoperator.getText().isEmpty()) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher", "Campos por preencher");
        } else {
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
                if (idoperator.getText().isEmpty() || nameoperator.getText().isEmpty()) {
                    Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher", "Campos por preencher");
                } else {
                    if (DatabaseOperator.insertOperatorDB(operator) == true) {
                        insertScheduleOp();
                        setClicked(true);
                        closeOperatorWindows();
                        Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Operador inserido", "Operador inserido com sucesso");

                    } else {
                        Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inserir", "Erro ao inserir o operador");
                    }
                }
                System.out.println(operator);
                System.out.println(operator.getListSchedule().toString());
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * Inserts the schedule in the database
     */
    public void insertScheduleOp() {
        try {
            for (Schedule s : operator.getListSchedule()) {
                if (s.isTimeCourse1() == false) {
                    if(s.getStartTime1() != null && s.getEndTime1() != null){
                        DatabaseOperator.insertScheduleOperatorDB(Time.valueOf(s.getStartTime1()),
                                Time.valueOf(s.getEndTime1()),
                                s.isTimeCourse1(),
                                DatabaseOperator.getDayWeekByDay(s.getDayWeek()), operator.getId());
                    }

                }
                if(s.isTimeCourse2() == true){
                    if(s.getStartTime2() != null && s.getEndTime2() != null){
                    DatabaseOperator.insertScheduleOperatorDB(Time.valueOf(s.getStartTime2()),
                            Time.valueOf(s.getEndTime2()),
                            s.isTimeCourse2(),
                            DatabaseOperator.getDayWeekByDay(s.getDayWeek()), operator.getId());
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Goes back to the previous scene
     *
     * @param actionEvent
     * @throws IOException
     */
    public void closeWindow(ActionEvent actionEvent) throws IOException {
        closeOperatorWindows();
    }

    /**
     * Closes operators window
     */
    public void closeOperatorWindows() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    /**
     * Inserts the schedule into the operator
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void insertScheduleOperator(ActionEvent actionEvent) throws IOException, SQLException {
        ArrayList<Schedule> sch = new ArrayList<>();
        boolean flag = createScheduleController(sch);
        if (flag) {
            operator.setListSchedule(sch);
            System.out.println(operator.getListSchedule());
        }
    }


    /**
     * Opens Create Schedule scene
     *
     * @param schedules
     * @return
     * @throws IOException
     * @throws SQLException
     */

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