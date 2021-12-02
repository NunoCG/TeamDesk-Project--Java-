package controller;

import Model.td.Operator;
import Model.td.Schedule;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;


public class VisualizarOperador implements Initializable {
    public Label labelID;
    public Button alterar;
    public Button remover;
    public TableColumn colunaNome;
    public TableView<Operator> tabelaView;
    public TableColumn colunaID;
    public ArrayList<Operator> listOperator;
    public ObservableList<Operator> observableListOperator;
    public Label labelNome;
    public Label labelEstado;
    public Label labelHorario;
    public Label labelSexo;
    public Button voltarMenu;
    public Button viewSchedule;
    public TextField txtSearch;

    private Stage stageDialog;

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    static Alert a = new Alert(Alert.AlertType.NONE);

    /**
     * When this scene is opened, this method loads the table
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTableView();
            tabelaView.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> selecionarItem(newValue));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Gets select operator and loads Update Operator Scene
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void alterarOperador(ActionEvent actionEvent) throws IOException, SQLException {
        boolean flag = false;
        Operator operator = tabelaView.getSelectionModel().getSelectedItem();
        if (operator != null) {
            boolean btnUpdateClicked = UpdateOperatorController(operator);
            if (btnUpdateClicked) {
                if (DatabaseOperator.updateOperatorDB(operator)) {
                    Helper.showAlert(Alert.AlertType.INFORMATION, "Informação", "Operador alterado", "Operador alterado com sucesso");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao alterar", "Erro ao alterar o operador");
                }
                loadTableView();
            }
        }
    }

    /**
     * Removes the selected Operator
     *
     * @param actionEvent
     * @throws SQLException
     */
    @FXML
    public void removerOperador(ActionEvent actionEvent) throws SQLException {

        Operator operator = tabelaView.getSelectionModel().getSelectedItem();
        a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja eliminar?", ButtonType.YES, ButtonType.CANCEL);
        try {
            if (operator == null) {
                showAlert(Alert.AlertType.WARNING, "Erro", "Não existe operador selecionado", "Selecione um operador");
            } else if (a.showAndWait().get() == ButtonType.YES) {
                DatabaseOperator.DeleteOperator(operator);
                DatabaseOperator.DeleteOperatorSchedule(operator);
                loadTableView();
                showAlert(Alert.AlertType.INFORMATION, "Informação", "Operador removido", "Operador removido com sucesso");
            }
        } catch (SQLException throwables) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao eliminar", "Erro ao eliminar o operador");
        }
    }

    /**
     * Opens Operator Creation menu
     *
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void criarOperador(ActionEvent actionEvent) throws IOException, SQLException {
        if (insertOperatorController()) {
            loadTableView();
        }
    }

    /**
     * Goes back to previous scene
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void voltar(ActionEvent actionEvent) throws IOException {

        Parent op = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene operator = new Scene(op);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(operator);
        window.show();
    }


    /**
     * Defines column's cell values and populates the table
     *
     * @throws SQLException
     */
    public void loadTableView() throws SQLException {

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colunaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        listOperator = DatabaseOperator.getAllOperatorBD();
        observableListOperator = FXCollections.observableArrayList(listOperator);
        tabelaView.setItems(observableListOperator);
        filteredSerachList();
    }

    public void filteredSerachList() throws SQLException {
        FilteredList<Operator> filteredList = new FilteredList<>(observableListOperator, b -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(operator -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);
                if (operator.getId().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (operator.getName().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Operator> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tabelaView.comparatorProperty());
        tabelaView.setItems(sortedList);
    }

    /**
     * Gets the values from the textfields and sets them into the operator
     *
     * @param op
     */
    public void selecionarItem(Operator op) {
        try {
            labelID.setText(op.getId());
            labelNome.setText(op.getName());
            if (op.isStatus())
                labelEstado.setText("Ativo");
            else
                labelEstado.setText("Inativo");
            labelHorario.setText(String.valueOf(op.getTime()));
            labelSexo.setText(op.getGender());
        } catch (Exception e) {
        }
    }

    /**
     * Shows an alert
     *
     * @param alertType
     * @param title
     * @param header
     * @param message
     */
    private static void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Opens Update Operator scene
     *
     * @param operator
     * @return
     * @throws IOException
     */
    public boolean UpdateOperatorController(Operator operator) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateOperator.class.getResource("/View/UpdateOperator.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Alterar operador");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        UpdateOperator controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setOperator(operator);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnUpdateOperator();

    }


    /**
     * Opens Insert Operator scene
     *
     * @return
     * @throws IOException
     */
    public boolean insertOperatorController() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UpdateOperator.class.getResource("/View/Operator_FX.fxml"));
        AnchorPane page = loader.load();
        Scene sceneoperator = new Scene(page);
        Stage stage = new Stage();
        stage.setTitle("Criar operador");
        stage.setScene(sceneoperator);

        InsertOperator controller = loader.getController();
        controller.setStageDialog(stageDialog);
        stage.showAndWait();
        return controller.isClicked();
    }


    public void viewAllSchedule(ActionEvent actionEvent) throws IOException, SQLException {
        if (!tabelaView.getSelectionModel().isEmpty()) {
            viewSchedulePane();
        } else {
            Helper.showAlert(Alert.AlertType.WARNING, "Aviso", "Selecionar operador", "Selecionar operador na tabela");
        }
    }


    public boolean viewSchedulePane() throws IOException, SQLException {
        Operator operator = tabelaView.getSelectionModel().getSelectedItem();
        System.out.println(operator);
        operator = (ScheduleManagementView.getSchedule(operator));

        ScheduleManagementView.operator = operator;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleManagementView.class.getResource("/View/scheduleManagementView.fxml"));
        System.out.println("aqui4444");
        AnchorPane page = loader.load();
        Scene sceneSchedule = new Scene(page);
        Stage stage = new Stage();
        stage.setTitle("Ver horário");
        stage.setScene(sceneSchedule);

        ScheduleManagementView controller = loader.getController();
        controller.setStageDialog(stage);
        stage.showAndWait();
        return controller.isClicked();
    }
}

