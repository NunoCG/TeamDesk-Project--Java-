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

public class AlterarOperacao implements Initializable {
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
    public static Component componentePublico;
    public static ObservableList<Component> observableListSelectComps;
    public static Component alternativa;
    public static ArrayList<Component> componentesPublico = new ArrayList<>();
    public static Operation operacaoPublica;
    public static int indicePublico;
    public static Operation operacaoUpdate = new Operation();

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

    /**
     * After opening this scene, this method adds all operation components and alternatives
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        referenciaColuna.setSortable(false);
        versaoColuna.setSortable(false);
        designacaoColuna.setSortable(false);
        estadoColuna.setSortable(false);
        quantidadeColuna.setSortable(false);
        alternativaColuna.setSortable(false);
        addAlternativeHandler.setTooltip(new Tooltip("Botão para adicionar um componente que sirva de alternativa para outro componente"));
        ArrayList<Component> components = new ArrayList<>();
        for (Product prod : DatabaseProduct.getAllProductsDB()) {
            if (prod != null) {
                components.add(Component.convertProductInComponent(prod));
            }
        }
        observableListSelectComps = FXCollections.observableArrayList(components);
        tabela.setEditable(true);
        quantidadeColuna.setCellFactory(TextFieldTableCell.forTableColumn());
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
     * Sets all textfield and column values to the operation's values
     *
     * @param operation
     */
    public void setOperation(Operation operation) {
        ArrayList<Component> components54 = new ArrayList<>();
        this.operation = operation;
        instrucoesBtn.setText(operation.getTecInstructions());
        descricaoBtn.setText(operation.getDescription());
        nPecasbtn.setText(String.valueOf(operation.getPartsPerHour()));
        qtdOpsBtn.setText(String.valueOf(operation.getOperatorsQt()));
        tempoProdBtn.setText(String.valueOf(operation.getProductionTime()));
        comboBoxTypeOperation();
        tipoOpBtn.setValue(operation.getType().getInfo());
        referenciaColuna.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        versaoColuna.setCellValueFactory(new PropertyValueFactory<>("versionProduct"));
        designacaoColuna.setCellValueFactory(new PropertyValueFactory<>("designation"));
        estadoColuna.setCellValueFactory(new PropertyValueFactory<>("status"));
        quantidadeColuna.setCellValueFactory(cellData -> new SimpleObjectProperty<String>(String.valueOf(cellData.getValue().getQuantity())));
        alternativaColuna.setCellValueFactory(new PropertyValueFactory<>("designation"));
        tabela.setEditable(true);
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
        if (operation.getComponents() != null) {
            observableListComponents = FXCollections.observableArrayList(operation.getComponents());
            tabela.getItems().addAll(observableListComponents);
            for (Component comp : operation.getComponents()) {
                if (comp.getAlternative() != null) {
                    components54.add(comp.getAlternative());
                } else {
                    components54.add(null);
                }
            }
        }
        ObservableList<Component> observableAlternativas = FXCollections.observableArrayList(components54);
        SelectAlternative.listaAlternativas = observableAlternativas;
        alternativeTable.setItems(observableAlternativas);
    }


    /**
     * Adds a component to the operation
     */
    public void addComponentHandler(ActionEvent actionEvent) throws IOException {
        operacaoPublica = operation;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/SelectComponentUpdateOperation.fxml"));
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
        List<Component> components = new ArrayList<>();
        if (SelectComponentUpdateOperationController.component2 != null) {
            operation = operacaoPublica;
            observableListComponents = FXCollections.observableArrayList(operation.getComponents());
            tabela.setItems(observableListComponents);
            alternativeTable.getItems().add(null);
            componentePublico = null;
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
            observableListSelectComps.add(comp);
            tabela.getItems().remove(index);
            if (alternativeTable.getItems().get(index) != null) {
                observableListComponents.add((Component) alternativeTable.getItems().get(index));
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
     * Confirms the changes made in the operation
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
            SelectAlternative.listaAlternativas = FXCollections.observableArrayList();
            operacaoUpdate = operation;
            btnInsertClicked = true;
            stageDialog.close();
        }
    }

    /**
     * Cancels the changes made in the operation
     */
    public void cancelarHandler(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);
        alert.setContentText("Pretende sair sem guardar?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            operacaoUpdate = null;
            SelectAlternative.listaAlternativas.removeAll();
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
        operacaoPublica = operation;
        if (tabela.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Por favor selecione um componente");
            alert.showAndWait();
        } else {
            indicePublico = tabela.getSelectionModel().getSelectedIndex();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("View/SelecionarAlternativa.fxml"));
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
            alternativeTable.setItems(SelectAlternative.listaAlternativas);
            operation = operacaoPublica;
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
            observableListSelectComps.add(comp);
            operation.getComponents().get(index).setAlternative(null);
            SelectAlternative.listaAlternativas.set(index, null);
            observableListSelectComps.add(comp);
            alternativeTable.setItems(SelectAlternative.listaAlternativas);


        }
    }
}
