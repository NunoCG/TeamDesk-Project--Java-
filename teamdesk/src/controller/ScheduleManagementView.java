package controller;

import Model.td.Operator;
import Model.td.Schedule;
import Model.td.database.DatabaseOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScheduleManagementView implements Initializable {
    public TableColumn daysWeek;
    public TableColumn start1;
    public TableColumn end1;
    public TableColumn start2;
    public TableColumn end2;
    public Button back;
    public TableView<Schedule> tableView;
    public ObservableList<Schedule> observableListSchedule;
    private boolean clicked;
    public Stage stageDialog;
    static public Operator operator;
    public ArrayList<Schedule> sc = operator.getListSchedule();

    private ArrayList<Operator> op = new ArrayList<>();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(operator);
        System.out.println(operator.getListSchedule());
        try {
            loadTableView();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void backScene(ActionEvent actionEvent) {
        stageDialog.close();
    }

    public static Operator getSchedule(Operator operator) throws SQLException {
        ArrayList<Schedule> sc =  DatabaseOperator.getScheduleBDOp(operator.getId());

        for (Schedule s : sc) {
            s.setDayWeek(DatabaseOperator.getDayFromID(s.getDayWeek()));
        }
        operator.setListSchedule(sc);

        return operator;

    }

    public void loadTableView() throws SQLException {
        daysWeek.setCellValueFactory(new PropertyValueFactory<>("dayWeek"));
        start1.setCellValueFactory(new PropertyValueFactory<>("startTime1"));
        end1.setCellValueFactory(new PropertyValueFactory<>("endTime1"));
        start2.setCellValueFactory(new PropertyValueFactory<>("startTime2"));
        end2.setCellValueFactory(new PropertyValueFactory<>("endTime2"));
        if (sc != null) {
            observableListSchedule = FXCollections.observableArrayList(sc);
            tableView.setItems(observableListSchedule);
        }
    }
}

