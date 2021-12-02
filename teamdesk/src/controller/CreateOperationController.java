package controller;

import Model.td.Component;
import Model.td.Operation;
import Model.td.OperationType;
import Model.td.Product;
import Model.td.database.DatabaseComponent;
import Model.td.database.DatabaseOperation;
import Model.td.database.DatabaseProduct;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateOperationController implements Initializable {
    public TableColumn referenciaColuna;
    public TableColumn versaoColuna;
    public TableColumn designacaoColuna;
    public TableColumn estadoColuna;
    public TextArea instrucoesBtn;
    public TextArea descricaoBtn;
    public TextField nPecasbtn;
    public TextField qtdOpsBtn;
    public TextField tempoProdBtn;
    public ChoiceBox tipoOpBox;
    public Button addComponentHandler;
    public Button removeComponent;
    public Button confirmarBtn;
    public Button cancelarBtn;
    public ComboBox tipoOpBtn;
    @FXML
    public TableView tabela;
    public Button addAlternativeHandler;
    public TableColumn alternativaColuna;
    public Button btnRemoveAlternative;
    public TableView alternativeTable;
    @FXML
    public TableColumn<Component, String> quantidadeColuna;
    private Stage stageDialog;
    private boolean btnInsertClicked = false;
    private Operation operation;
    ArrayList<OperationType> opT;
    private ArrayList<Component> components = new ArrayList<>();
    private ObservableList<Component> observableListComponents;
    public static Component componentePublicoCreate;
    public static ObservableList<Component> observableListSelectCompsCreate;
    public static Operation operacaoPublicaCreate;
    public static int indicePublicoCreate;
    public static ArrayList<Component> arrayComponents = new ArrayList<>();

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

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     * After opening this scene, this method sets all columns to not sortable and gets all products from the database
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        referenciaColuna.setSortable(false);
        versaoColuna.setSortable(false);
        designacaoColuna.setSortable(false);
        estadoColuna.setSortable(false);
        quantidadeColuna.setSortable(false);
        alternativaColuna.setSortable(false);
        operacaoPublicaCreate = new Operation();
        operacaoPublicaCreate.setComponents(new ArrayList<Component>());
        comboBoxTypeOperation();
        insertTableView();
        addAlternativeHandler.setTooltip(new Tooltip("Botão para adicionar um componente que sirva de alternativa para outro componente"));
        for (Product prod : DatabaseProduct.getAllProductsDB()) {
            if (prod != null) {
                components.add(Component.convertProductInComponent(prod));
            }
        }
        observableListSelectCompsCreate = FXCollections.observableArrayList(components);
        tabela.setEditable(true);
        quantidadeColuna.setCellFactory(TextFieldTableCell.forTableColumn());
        quantidadeColuna.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Component, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Component, String> evt) {
                for (int i = 0; i < operation.getComponents().size(); i++) {
                    if (operation.getComponents().get(i) == tabela.getSelectionModel().getSelectedItem()) {
                        operation.getComponents().get(i).setQuantity(Double.parseDouble(evt.getNewValue()));
                        tabela.refresh();
                    }
                }
            }
        });
    }

    /**
     * Defines the table's cellValues
     */
    public void insertTableView() {
        referenciaColuna.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        versaoColuna.setCellValueFactory(new PropertyValueFactory<>("versionProduct"));
        designacaoColuna.setCellValueFactory(new PropertyValueFactory<>("designation"));
        estadoColuna.setCellValueFactory(new PropertyValueFactory<>("status"));
        quantidadeColuna.setCellValueFactory(cellData -> new SimpleObjectProperty<String>(String.valueOf(cellData.getValue().getQuantity())));
        alternativaColuna.setCellValueFactory(new PropertyValueFactory<>("designation"));
        tabela.setEditable(true);
    }

    /**
     * Gets all operation types from the dataase and fills the combobox with them
     */
    public void comboBoxTypeOperation() {
        try {
            opT = OperationType.getOperationTypesDB();
            for (OperationType ot : opT) {
                tipoOpBtn.getItems().add(ot.getInfo());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Adds a component to the operation
     */
    public void addComponentHandler(ActionEvent actionEvent) throws IOException {
        operacaoPublicaCreate = operation;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/SelectComponents_fx.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
            }
        });
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(confirmarBtn.getScene().getWindow());
        stage.setResizable(false);
        stage.showAndWait();
        if (SelectComponentsFx.component2Create != null) {
            operation = operacaoPublicaCreate;
            observableListComponents = FXCollections.observableArrayList(operation.getComponents());
            tabela.setItems(observableListComponents);
            SelectAlternativeCreateOperation.listaAlternativasCreate.add(null);
            alternativeTable.getItems().add(null);
            componentePublicoCreate = null;
        }
    }

    /**
     * Removes a component from the operation
     */
    public void removeCompHandler(ActionEvent actionEvent) {
        if (tabela.getSelectionModel().getSelectedItem() != null) {
            int index = tabela.getSelectionModel().getSelectedIndex();
            Component comp = (Component) tabela.getSelectionModel().getSelectedItem();
            comp.setAlternative(null);
            observableListSelectCompsCreate.add(comp);
            tabela.getItems().remove(index);
            if (alternativeTable.getItems().get(index) != null) {
                observableListSelectCompsCreate.add((Component) alternativeTable.getItems().get(index));
            }
            alternativeTable.getItems().remove(index);
            operation.getComponents().remove(index);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText("Selecione um componente para remover.");
            alert.showAndWait();
        }
    }

    /**
     * Creates the operation
     */
    public void confirmarHandler(ActionEvent actionEvent) {
        if (emptyVerifier() == true && integerVerifier() == true) {
            int indexOpType = tipoOpBtn.getSelectionModel().getSelectedIndex();
            OperationType opType = opT.get(indexOpType);
            operation.setType(opType);
            operation.setPartsPerHour(Integer.parseInt(nPecasbtn.getText()));
            operation.setOperatorsQt(Integer.parseInt(qtdOpsBtn.getText()));
            operation.setProductionTime(Integer.parseInt(tempoProdBtn.getText()));
            operation.setTecInstructions(instrucoesBtn.getText());
            operation.setDescription(descricaoBtn.getText());
            SelectAlternativeCreateOperation.listaAlternativasCreate = FXCollections.observableArrayList();
            btnInsertClicked = true;
            stageDialog.close();
        }
    }

    /**
     * Cancels the creation of the operation
     */
    public void cancelarHandler(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText("Pretende sair sem guardar?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {

            SelectAlternativeCreateOperation.listaAlternativasCreate.removeAll();
            addComponentHandler.getScene().getWindow().hide();
        }
    }

    /**
     * Verifies if fields are integer
     */
    public boolean integerVerifier() {
        try {
            Integer.parseInt(nPecasbtn.getText());
            Integer.parseInt(qtdOpsBtn.getText());
            Integer.parseInt(tempoProdBtn.getText());
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos inválidos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor verifique se inseriu números nos campos de números inteiros");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Verifies if fields are empty
     */
    public boolean emptyVerifier() {
        if (nPecasbtn.getText().isEmpty() || qtdOpsBtn.getText().isEmpty() || tempoProdBtn.getText().isEmpty() ||
                tipoOpBtn.getSelectionModel().isEmpty() || instrucoesBtn.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos inválidos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor preencha todos os campos obrigatórios.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Adds an alternative to a component
     */
    public void addAlternative(ActionEvent actionEvent) throws IOException {
        operacaoPublicaCreate = operation;
        if (tabela.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Por favor selecione um componente");
            alert.showAndWait();
        } else {
            indicePublicoCreate = tabela.getSelectionModel().getSelectedIndex();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/SelectAlternativeCreateOperation.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                }
            });
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(confirmarBtn.getScene().getWindow());
            stage.setResizable(false);
            stage.showAndWait();
            alternativeTable.setItems(SelectAlternativeCreateOperation.listaAlternativasCreate);
            operation = operacaoPublicaCreate;
        }
    }

    /**
     * Removes an alternative from a component
     */
    public void removeAlternative(ActionEvent actionEvent) {
        if (alternativeTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Por favor selecione uma alternativa");
            alert.showAndWait();
        } else {
            int index = alternativeTable.getSelectionModel().getSelectedIndex();
            Component comp = (Component) alternativeTable.getSelectionModel().getSelectedItem();
            observableListSelectCompsCreate.add(comp);
            operation.getComponents().get(index).setAlternative(null);
            SelectAlternativeCreateOperation.listaAlternativasCreate.set(index, null);
            observableListSelectCompsCreate.add(comp);
            alternativeTable.setItems(SelectAlternativeCreateOperation.listaAlternativasCreate);
        }
    }
}
