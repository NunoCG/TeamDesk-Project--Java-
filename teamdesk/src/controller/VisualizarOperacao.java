package controller;

import Model.td.Component;
import Model.td.Operation;
import Model.td.OperationType;
import Model.td.Product;
import Model.td.database.DatabaseOperation;
import Model.td.database.DatabaseProduct;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class VisualizarOperacao implements Initializable {
    public Button btnVoltar;
    public Button btnAlterar;
    public TableView tabela;
    public TableColumn idColuna;
    public TableColumn tipoOperacaoColuna;
    public TableColumn qtdTotalColuna;
    public TableColumn ordemColuna;
    public TableColumn tempoProducaoColuna;
    public TableColumn qtdOperadoresColuna;
    public TableColumn descricaoColuna;
    public TableColumn instrucoesColuna;
    public ArrayList<Operation> operationList;
    public ObservableList<Operation> observableListOperation;


    /**
     * When this scene is opened, this method loads the table
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        insertTableView();
    }


    /**
     * Gets the selected operation and opens Update Operation Scene
     *
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void alterar(ActionEvent actionEvent) throws IOException {
        try {
            Operation operation = (Operation) tabela.getSelectionModel().getSelectedItem();
            boolean btnInsertClicked = alterarOperacao(operation);
            if (btnInsertClicked) {
                insertTableView();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Por favor selecione uma operação");
            alert.showAndWait();
        }
    }

    /**
     * Defines column's cell values and populates table
     */
    public void insertTableView() {
        idColuna.setCellValueFactory(new PropertyValueFactory<>("id"));
        tipoOperacaoColuna.setCellValueFactory(new PropertyValueFactory<>("typeInfo"));
        qtdTotalColuna.setCellValueFactory(new PropertyValueFactory<>("partsPerHour"));
        ordemColuna.setCellValueFactory(new PropertyValueFactory<>("order"));
        tempoProducaoColuna.setCellValueFactory(new PropertyValueFactory<>("productionTime"));
        qtdOperadoresColuna.setCellValueFactory(new PropertyValueFactory<>("operatorsQt"));
        descricaoColuna.setCellValueFactory(new PropertyValueFactory<>("description"));
        instrucoesColuna.setCellValueFactory(new PropertyValueFactory<>("tecInstructions"));
        try {
            operationList = DatabaseOperation.getOperationsDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        observableListOperation = FXCollections.observableArrayList(operationList);
        tabela.setItems(observableListOperation);
    }

    /**
     * Gets the selected operation and opens Update Operation Scene
     *
     * @param operation
     * @return
     * @throws IOException
     */
    public boolean alterarOperacao(Operation operation) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AlterarOperacao.class.getResource("/view/AlterarOperacao.fxml"));
        AnchorPane page = loader.load();
        Stage dialogStage = new Stage();
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        AlterarOperacao controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setOperation(operation);

        dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(btnAlterar.getScene().getWindow());
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        if (AlterarOperacao.operacaoUpdate != null) {
            DatabaseOperation.updateOperationRelationDB(AlterarOperacao.operacaoUpdate);
        }
        insertTableView();
        return controller.isBtnInsertClicked();
    }


    /**
     * Goes back to the previous scene
     *
     * @param actionEvent
     */
    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent gestaoDeClientes = FXMLLoader.load(getClass().getClassLoader().getResource("View/configurations.fxml"));
        Scene gestaoDeClientesScene = new Scene(gestaoDeClientes);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(gestaoDeClientesScene);
        window.show();
    }
}
