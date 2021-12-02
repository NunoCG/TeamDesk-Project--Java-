package controller;

import Model.td.Machine;
import Model.td.OperationType;
import Model.td.Schedule;
import Model.td.database.DatabaseConnect;
import Model.td.database.DatabaseMachine;
import Model.td.database.DatabaseOperationType;
import Model.td.database.DatabaseOperator;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 */
public class MachineConfig implements Initializable {

    public Button btnExit;
    public AnchorPane sidePain;
    public AnchorPane changePain;
    public TableColumn<Machine, String> colId;
    public TableColumn<Machine, String> colDescription;
    public TableColumn<Machine, Integer> colOpType;
    public TableColumn<Machine, Boolean> colStatus;
    public TableView<Machine> tableMachine;
    public TextField txtId;
    public TextField txtDescription;
    public RadioButton choiceAtivo;
    public ToggleGroup status;
    public RadioButton choiceInativo;
    public ComboBox comboBox;
    public Button btnScheduleMachine;
    public TableView<Schedule> tableSchedule;
    public TableColumn clmnIdWeekSchedule;
    public TableColumn clmnHourInSchedule1;
    public TableColumn clmnHourOutSchedule1;
    public TableColumn clmnHourInSchedule2;
    public TableColumn clmnHourOutSchedule2;
    public TextField txtSearch;
    public Button btnClearFields;
    public Button btnRemoveSchedule;
    private Machine machine = new Machine();
    ObservableList<Machine> obMach;
    ObservableList<Schedule> obSche;
    ArrayList<OperationType> opT;
    ArrayList<Machine> listMachines;
    ArrayList<Schedule> listSchedule = new ArrayList<>();
    int index = -1;

    /**
     * Creates a machine and inserts both the machine and the it's schedule into the database
     * @throws IOException
     * @throws SQLException
     */
    public void create() throws IOException, SQLException {

        if (txtId.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                comboBox.getItems().isEmpty() || (choiceInativo.getText().isEmpty() &&
                choiceAtivo.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher",
                    "Campos por preencher");
        } else {
            machine.setId(txtId.getText());
            machine.setDescription(txtDescription.getText());
            opT = OperationType.getOperationTypesDB();
            int indexOpType = comboBox.getSelectionModel().getSelectedIndex();
            OperationType opType = opT.get(indexOpType);
            machine.setOperationType(DatabaseOperationType.findOpTypeById(opType.getId()));

            if (choiceAtivo.isSelected()) {
                choiceInativo.setSelected(false);
                machine.setStatus(true);
            }

            if (choiceInativo.isSelected()) {
                choiceAtivo.setSelected(false);
                machine.setStatus(false);
            }
            if (DatabaseMachine.insertMachineDB(machine) && insertScheduleMachine()) {
                showAlert(Alert.AlertType.CONFIRMATION, "Confirmação", "Sucesso",
                        "Máquina e Horários inseridos com sucesso");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao inserir máquina",
                        "Algo de errado aconteceu ao inserir a máquina");
            }
            showMachineTable();
        }
    }

    /**
     * method that inserts machine schedules into the database
     */
    public boolean insertScheduleMachine() {
        try {
            for (Schedule s : machine.getListSchedule()) {
                if (s.isTimeCourse1() == false) {
                    DatabaseMachine.insertScheduleMachineDB(Time.valueOf(s.getStartTime1()),
                            Time.valueOf(s.getEndTime1()),
                            s.isTimeCourse1(),
                            DatabaseMachine.getDayWeekByDay(s.getDayWeek()), machine.getId());
                }
                if (s.isTimeCourse2() == true) {
                    DatabaseMachine.insertScheduleMachineDB(Time.valueOf(s.getStartTime2()),
                            Time.valueOf(s.getEndTime2()),
                            s.isTimeCourse2(),
                            DatabaseOperator.getDayWeekByDay(s.getDayWeek()), machine.getId());
                }
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    public boolean updateScheduleMachine() {
        try {
            for (Schedule s : machine.getListSchedule()) {
                if (s.isTimeCourse1() == false) {
                    System.out.println("if false");
                    DatabaseMachine.updateScheduleMachine(Time.valueOf(s.getStartTime1()),
                            Time.valueOf(s.getEndTime1()),
                            s.isTimeCourse1(),
                            DatabaseMachine.getDayWeekByDay(s.getDayWeek()), machine.getId());
                }
                if (s.isTimeCourse2() == true) {
                    System.out.println("if true");
                    DatabaseMachine.updateScheduleMachine(Time.valueOf(s.getStartTime2()),
                            Time.valueOf(s.getEndTime2()),
                            s.isTimeCourse2(),
                            DatabaseMachine.getDayWeekByDay(s.getDayWeek()), machine.getId());
                }
            }
            System.out.println("Return true");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("return false");
            return false;
        }
    }

    /**
     * @throws IOException
     */
    /*public void delete() throws IOException, SQLException {
        if (txtId.getText().isEmpty() || txtDescription.getText().isEmpty() ||
                comboBox.getItems().isEmpty() || (choiceInativo.getText().isEmpty()
                && choiceAtivo.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Aviso", "Campos por preencher",
                    "Campos por preencher");
        } else {
            machine.setId(txtId.getText());
            machine.setDescription(txtDescription.getText());
            opT = OperationType.getOperationTypesDB();
            int indexOpType = comboBox.getSelectionModel().getSelectedIndex();
            OperationType opType = opT.get(indexOpType);
            machine.setOperationType(DatabaseOperationType.findOpTypeById(opType.getId()));
            if (DatabaseMachine.deleteMachine(machine)) {
                showAlert(Alert.AlertType.CONFIRMATION, "Confirmação", "Sucesso!",
                        "A máquina foi eliminada com sucesso!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao eliminar",
                        "Algo de errado aconteceu ao eliminar a máquina");
            }
            showMachineTable();
        }
    }*/

    /**
     * @throws SQLException
     */
    public void update() throws SQLException {
        index = tableMachine.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        if (txtId.getText().isEmpty() && txtDescription.getText().isEmpty()
                && comboBox.getItems().isEmpty() && (choiceInativo.getText().isEmpty()
                && choiceAtivo.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Aviso",
                    "Campos por preencher", "Campos por preencher");
        } else {
            String id = txtId.getText();
            machine.setId(id);
            String desc = txtDescription.getText();
            machine.setDescription(desc);
            opT = OperationType.getOperationTypesDB();
            int indexOpType = comboBox.getSelectionModel().getSelectedIndex();
            OperationType opType = opT.get(indexOpType);
            machine.setOperationType(DatabaseOperationType.findOpTypeById(opType.getId()));
            if (choiceAtivo.isSelected()) {
                machine.setStatus(true);
            } else if (choiceInativo.isSelected()) {
                machine.setStatus(false);
            }

            if (!id.equals(colId.getCellData(index))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO!");
                alert.setHeaderText("O ID da máquina não pode ser alterado!");
                alert.showAndWait();
            } else {

                if (DatabaseMachine.updateMachine(machine)) {
                    showAlert(Alert.AlertType.CONFIRMATION, "Confirmação",
                            "Sucesso!", "Máquina alterada com sucesso");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro",
                            "Erro ao alterar",
                            "Algo de errado aconteceu ao alterar a máquina");
                }
            }
            showMachineTable();
        }
    }

    /**
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void scheduleMachine(ActionEvent actionEvent) throws IOException, SQLException {
        ArrayList<Schedule> sch = new ArrayList<>();
        boolean flag = createScheduleController(sch);

        if (flag) {
            machine.setListSchedule(sch);
            System.out.println(machine.getListSchedule());
        }
    }

    /**
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

    /**
     * Removes only the schedules
     * @param actionEvent
     * @throws SQLException
     */
    public void removeSchedule(ActionEvent actionEvent) throws SQLException {
        Schedule schedule = tableSchedule.getSelectionModel().getSelectedItem();

        if (tableSchedule.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover horário", "Tem de selecionar um horário da tabela");
        } else {
            tableSchedule.getItems().removeAll(schedule);
            if (DatabaseMachine.deleteScheduleMachine(schedule)) {
                showAlert(Alert.AlertType.CONFIRMATION, "Confirmação",
                        "Sucesso!", "Horário removido com sucesso");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao remover",
                        "Algo de errado aconteceu ao remover o horário da máquina");
            }
        }
        showMachineScheduleTable();
    }

    /**
     * @param event
     * @throws IOException
     */
    public void exit(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void clearFields() {
        btnScheduleMachine.setDisable(false);
        txtId.clear();
        txtSearch.clear();
        txtDescription.clear();
        comboBox.getSelectionModel().clearSelection();
        tableSchedule.getItems().clear();
        choiceAtivo.setSelected(false);
        choiceInativo.setSelected(false);
    }

    /**
     * @throws SQLException
     */
    private void loadDataComboBox() throws SQLException {
        try {
            opT = OperationType.getOperationTypesDB();
            for (OperationType ot : opT) {
                comboBox.getItems().add(ot.getInfo());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @throws SQLException
     */
    public void filteredSearchList() throws SQLException {
        FilteredList<Machine> filteredList = new FilteredList<>(obMach, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(machine -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);
                if (machine.getId().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (machine.getDescription().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Machine> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableMachine.comparatorProperty());
        tableMachine.setItems(sortedList);
    }

    /**
     * shows alert message
     * @param alertType
     * @param title
     * @param header
     * @param message
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * @param mouseEvent
     */
    public void getSelectedMachines(javafx.scene.input.MouseEvent mouseEvent) {
        index = tableMachine.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        btnScheduleMachine.setDisable(true);
        txtId.setText(colId.getCellData(index));
        txtDescription.setText(colDescription.getCellData(index));
        comboBox.getSelectionModel().select(index);
        if (colStatus.getCellData(index)) {
            choiceAtivo.setSelected(true);
        } else {
            choiceInativo.setSelected(true);
        }
        machine = tableMachine.getItems().get(index);
        showMachineScheduleTable();
    }

    /**
     * @throws SQLException
     */
    public void showMachineTable() throws SQLException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOpType.setCellValueFactory(new PropertyValueFactory<>("infoOpType"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        try {
            listMachines = DatabaseMachine.getAllMachineDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        obMach = FXCollections.observableArrayList(listMachines);
        tableMachine.setItems(obMach);
        filteredSearchList();
    }

    /**
     *
     */
    public void showMachineScheduleTable() {
        clmnIdWeekSchedule.setCellValueFactory(new PropertyValueFactory<>("dayWeek"));
        clmnHourInSchedule1.setCellValueFactory(new PropertyValueFactory<>("startTime1"));
        clmnHourOutSchedule1.setCellValueFactory(new PropertyValueFactory<>("endTime1"));
        clmnHourInSchedule2.setCellValueFactory(new PropertyValueFactory<>("startTime2"));
        clmnHourOutSchedule2.setCellValueFactory(new PropertyValueFactory<>("endTime2"));
        listSchedule = DatabaseMachine.getScheduleMachineOneLine(machine.getId());
        obSche = FXCollections.observableArrayList(listSchedule);
        tableSchedule.setItems(obSche);
        tableSchedule.refresh();
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showMachineTable();
            showMachineScheduleTable();
            loadDataComboBox();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
