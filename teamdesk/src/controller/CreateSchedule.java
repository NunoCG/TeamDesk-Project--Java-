package controller;

import Model.td.Schedule;
import Model.td.database.DatabaseOperator;
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
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class CreateSchedule implements Initializable {
    public TextField timeoperator;
    public TextField scheduleStart1;
    public ChoiceBox scrollDays;
    public TextField scheduleEnd1;
    public TextField scheduleStart2;
    public TextField scheduleEnd2;
    public Button insert;
    public Button back;
    public TableColumn daysWeek;
    public TableColumn start1;
    public TableColumn end1;
    public TableColumn start2;
    public TableColumn end2;
    public Button remove;
    public Button save;
    public ArrayList<Schedule> listSchedule = new ArrayList<>();
    public ObservableList<Schedule> observableListSchedule;
    public TableView<Schedule> tableSchedule;
    private Schedule sc;
    private boolean flag = true;
    private Stage stageDialog;
    private boolean btnInsertClicked;

    public TextField getTimeoperator() {
        return timeoperator;
    }

    public void setTimeoperator(TextField timeoperator) {
        this.timeoperator = timeoperator;
    }

    public ArrayList<Schedule> getListSchedule() {
        return listSchedule;
    }

    /**
     * Populates the table with the arraylist passed by parameter
     * @param listSchedule
     * @throws SQLException
     */
    public void setListSchedule(ArrayList<Schedule> listSchedule) throws SQLException {
        if (listSchedule != null) {
            this.listSchedule = listSchedule;
            loadTableView();
        }
    }

    public boolean isBtnInsertClicked() {
        return btnInsertClicked;
    }

    public void setBtnInsertClicked(boolean btnInsertClicked) {
        this.btnInsertClicked = btnInsertClicked;
    }


    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }


    /**
     * Sets all textfields and column values to the blank values
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sc = new Schedule();
        choiceBoxDays();
        try {
            tableSchedule.refresh();
            loadTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scrollDays.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue.intValue() > -1) {

                        cleanFields();
                    }

                });
    }

    /**
     * Fills all the column values
     * @param newValue
     */
    public void preencherFields(Number newValue) {


    }

    /**
     * Insert the schedule calling the methods
     *
     * @param actionEvent
     * @throws SQLException
     */
    public void insertSchedule(ActionEvent actionEvent) throws Exception {
        allSchedule();
        loadTableView();
        tableSchedule.refresh();
        cleanFields();
        cleanDays();
        sc = new Schedule();
    }

    /**
     * Clears all fields
     */
    public void cleanFields() {
        scheduleStart1.setText("");
        scheduleStart2.setText("");
        scheduleEnd1.setText("");
        scheduleEnd2.setText("");
    }

    public void cleanDays() {
        scrollDays.getSelectionModel().clearSelection();
    }

    /**
     * back to the previous scene
     *
     * @param actionEvent
     * @throws IOException
     */
    public void backSchedule(ActionEvent actionEvent) throws IOException {
        stageDialog.close();
    }

    /**
     * remove selected schedule
     *
     * @param actionEvent
     */
    public void removeSchedule(ActionEvent actionEvent) {
        Alert a;
        Schedule schedule = tableSchedule.getSelectionModel().getSelectedItem();
        a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja eliminar?", ButtonType.YES, ButtonType.CANCEL);
        try {
            if (schedule == null) {
                Helper.showAlert(Alert.AlertType.WARNING, "Erro", "Não existe um horário selecionado", "Selecione um horário");
            } else if (a.showAndWait().get() == ButtonType.YES) {
                getListSchedule().remove(schedule);
                loadTableView();
                Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Horário removido", "Horário removido com sucesso");
            }
        } catch (SQLException throwables) {
            Helper.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao eliminar", "Erro ao eliminar o horário");
        }
    }

    /**
     * Saves the schedule
     * @param actionEvent
     */
    public void saveSchedule(ActionEvent actionEvent) {
        setBtnInsertClicked(true);
        stageDialog.close();
    }

    /**
     * choose the day in choiceBox
     */
    public void choiceBoxDays() {
        try {
            scrollDays.getItems().addAll(DatabaseOperator.getDayWeekDB());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * get all the values in the time
     */
    public void allSchedule() throws Exception {
        Schedule sc = populatingObject();
        if (sc != null) {
            if (getListSchedule().size() != 0) {
                Schedule delete = deleteRepeatDay(sc);
                if (delete != null) {
                    for (int i = 0; i < getListSchedule().size(); i++) {
                        if (getListSchedule().get(i).getDayWeek().equals(sc.getDayWeek())) {
                            System.out.println(i + " " + getListSchedule().get(i));
                            getListSchedule().remove(i);
                            getListSchedule().add(sc);
                            break;
                        }
                    }
                }else{
                    getListSchedule().add(sc);
                }
            }else{
                getListSchedule().add(sc);

            }
        }

    }

    public boolean fillPropertysTime1(boolean isDay) {
        try {
            if (isDay == true && verificationEmptySchedule(scheduleStart1, scheduleEnd1) == true) {
                sc.setStartTime1(LocalTime.parse(scheduleStart1.getText()));
                sc.setEndTime1(LocalTime.parse(scheduleEnd1.getText()));
                if (verificationScheduleHours(sc.getStartTime1(), sc.getEndTime1())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos inválidos",
                    "Preencha corretamente os campos");

        }

        return false;
    }

    public boolean fillPropertysTime2(boolean isDay) {
        try {
            if (isDay == true && verificationEmptySchedule(scheduleStart2, scheduleEnd2) == true) {
                sc.setStartTime2(LocalTime.parse(scheduleStart2.getText()));
                sc.setEndTime2(LocalTime.parse(scheduleEnd2.getText()));
                if (verificationScheduleHours(sc.getStartTime2(), sc.getEndTime2())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos inválidos",
                    "Preencha corretamente os campos");

        }
        return false;
    }


    public Schedule deleteRepeatDay(Schedule sc) throws SQLException {
        for (Schedule s : getListSchedule()) {
            if (s.getDayWeek().equals(sc.getDayWeek())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Sets object's properties
     * @return
     * @throws Exception
     */
    public Schedule populatingObject() throws Exception {
        boolean isDay = verificationDay(scrollDays);
        int count = 0;
        boolean flag = false;
        if (isDay == true && scheduleStart1.getText().isEmpty() && scheduleStart2.getText().isEmpty()
                && scheduleEnd1.getText().isEmpty() && scheduleEnd2.getText().isEmpty()) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher",
                    "Horários por preencher");
        } else {
            if (fillPropertysTime1(isDay)) {
                count += 1;
                flag = true;
            } else {
                if (scheduleStart1.getText().isEmpty() && scheduleEnd1.getText().isEmpty()) {
                    flag = true;
                }
            }

            if (fillPropertysTime2(isDay) && flag) {
                count += 1;
            } else {
                if (scheduleStart2.getText().isEmpty() && scheduleEnd2.getText().isEmpty()) {
                    flag = true;
                } else{
                    flag = false;
                }

            }
            if (flag == false) {
                return null;
            }

            if (count == 2) {

                if (!verificationScheduleHours(sc.getEndTime1(), sc.getStartTime2())) {
                    count = 0;
                }
                if (verificationScheduleHours(sc.getEndTime2(), LocalTime.parse("23:59:59")) &&
                        verificationScheduleHours(sc.getStartTime1(), sc.getEndTime2())) {

                } else {
                    count = 0;
                }
            }

            if ((count > 0) && (isDay == true)) {
                sc.setDayWeek(scrollDays.getValue().toString());
                return sc;
            }
        }
        return null;
    }


    /**
     * Loads the schedule in table
     * @throws SQLException
     */
    public void loadTableView() throws SQLException {
        daysWeek.setCellValueFactory(new PropertyValueFactory<>("dayWeek"));
        start1.setCellValueFactory(new PropertyValueFactory<>("startTime1"));
        end1.setCellValueFactory(new PropertyValueFactory<>("endTime1"));
        start2.setCellValueFactory(new PropertyValueFactory<>("startTime2"));
        end2.setCellValueFactory(new PropertyValueFactory<>("endTime2"));
        if (getListSchedule() != null) {
            observableListSchedule = FXCollections.observableArrayList(getListSchedule());
            tableSchedule.setItems(observableListSchedule);
        }
    }


    /**
     * Verifies if values are empty #1
     */
    private boolean verificationEmptySchedule(TextField start, TextField end) {
        if (!start.getText().isEmpty() && !end.getText().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Verifies if values are empty #2
     */
    private boolean verificationSchedule(TextField start, TextField end) {
        if ((!start.getText().isEmpty() && end.getText().isEmpty()) ||
                (start.getText().isEmpty() && !end.getText().isEmpty())) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher",
                    "Horários por preencher");
            return false;
        }
        return true;
    }

    /**
     * Verifies if values are valid #1
     */
    private boolean verificationDay(ChoiceBox day) {
        if (day.getValue() == null) {
            Helper.showAlert(Alert.AlertType.ERROR, "Aviso", "Dia da semana",
                    "Selecione um dia da semana");
            return false;
        }
        return true;
    }
    /**
     * Verifies if values are valid #2
     */
    private boolean verificationScheduleAfterBefore(TextField start, TextField end) {
        LocalTime time1 = LocalTime.of(23, 59, 59);
        try {
            if (!start.getText().isEmpty() && !end.getText().isEmpty()) {
                if (LocalTime.parse(start.getText()).isAfter(LocalTime.parse(end.getText()))) {
                    if ((LocalTime.parse(start.getText()).isAfter(time1)) &&
                            (LocalTime.parse(end.getText())).isAfter(time1)) {
                        Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                                "Corrigir os valores inseridos");
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                    "Campo inválido");
            return false;
        }
    }

    /**
     * Verifies if values are valid #3
     */
    private boolean verificationStartEndBefore(TextField start, TextField end) {
        try {
            if (LocalTime.parse(end.getText()).isBefore(LocalTime.parse(start.getText()))) {
                return false;
            }
        } catch (Exception e) {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                    "Corrigir os valores inseridos");
        }
        return true;
    }

    /*
        private boolean verificationRepeatHourTextfield(TextField start, TextField end) {
            try {
                if (start.getText().equals(end.getText())) {
                    Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                            "Corrigir as horas");
                    return false;
                }
                if (start.getText().equals(start.getText())) {
                    Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                            "Corrigir as horas");
                    return false;
                }
                if (end.getText().equals(end.getText())) {
                    Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                            "Corrigir as horas");
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return true;
        }
    private boolean verificationRepeatHour() {
        try {
            if (sc.getStartTime1().equals(sc.getEndTime1())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais ");
                return false;
            }
            if (scheduleStart1.getText().equals(scheduleStart2.getText())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais");
                return false;
            }
            if (scheduleStart1.getText().equals(scheduleEnd2.getText())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais");
                return false;
            }
            if (scheduleStart2.getText().equals(scheduleEnd2.getText())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais");
                return false;
            }
            if (scheduleEnd1.getText().equals(scheduleEnd2.getText())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais");
                return false;
            }
            if (scheduleEnd1.getText().equals(scheduleStart2.getText())) {
                Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                        "Horas com valores iguais");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
        */

    private boolean comparetoSch(LocalTime t1, LocalTime t2) {
        boolean flag = false;
        int s = t1.compareTo(t2);
        if (s == 0) {
            flag = false;
        }
        if (s > 0) {
            flag = false;
        }
        if (s == -1) {
            flag = true;
        }
        return flag;
    }

    private boolean verificationScheduleHours(LocalTime t1, LocalTime t2) {
        if (comparetoSch(t1, t2)) {
            return true;
        } else {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Corrigir os valores",
                    "valores");
        }
        return false;
    }
}