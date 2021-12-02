package controller;

import Model.td.Order;
import Model.td.Product;
import Model.td.database.DatabaseItemOrder;
import Model.td.database.DatabaseOrder;
import Model.td.database.DatabaseProduct;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.jar.JarOutputStream;

public class Encomenda implements Initializable {
    public TableView<Order> listaEncomendas;
    public TableColumn colunaNumEncomenda;
    public TableColumn colunaValor;
    public TableColumn colunaEstado;
    public Button btnCriarEncomenda;
    public Button btnEditarEncomenda;
    public Button btnDetalheEncomenda;
    public TableColumn colunaCliente;
    private ArrayList<Order> orders;
    private ObservableList<Order> observableList;

    private Order order = new Order();

    /**
     * Loads orders when this scene opens
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadOrders();
            listaEncomendas.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            order =  newValue;
                        }
                    }
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Creates an order
     * @param actionEvent
     * @throws IOException
     */
    public void btnCriarEncomenda(ActionEvent actionEvent) throws IOException, SQLException {
        Order o = new Order();
        o.setItens(new ArrayList<>());
        boolean btnClicked = controlletOrder(o,0);
        if(btnClicked){
            loadOrders();
        }
    }

    /**
     * Edits the selected order
     * @param actionEvent
     * @throws IOException
     */
    public void btnEditarEncomenda(ActionEvent actionEvent) throws IOException {
        if(order.getNum() > 0){
            try{
                order.setItens(DatabaseItemOrder.getAllItensOrderByOrder(order.getNum()));
                boolean btnClicked = controlletOrder(order,1);
            }catch(Exception e){
            }
        }else{
            Helper.showAlert(Alert.AlertType.INFORMATION,"Atenção","Selecionar Encomenda","Por favor selecione uma encomenda para poder editar.");
        }

    }

    /**
     * Deletes the selected order
     * @param actionEvent
     */
    public void btnDetalheEncomenda(ActionEvent actionEvent) {
        if(order.getNum() > 0){
            try{
                order.setItens(DatabaseItemOrder.getAllItensOrderByOrder(order.getNum()));
                boolean btnClicked = controlletOrder(order,2);

            }catch(Exception e){
            }
        }else{
            Helper.showAlert(Alert.AlertType.INFORMATION,"Atenção","Selecionar Encomenda","Por favor selecione uma encomenda para poder editar.");
        }
    }

    public static boolean controlletOrder(Order order,int tipoEncomenda) throws IOException, SQLException {
    /**
     * Opens Order Management menu
     * @param order
     * @return
     * @throws IOException
     */

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(gerirEncomenda.class.getResource("/view/gerirEncomenda.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Encomenda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        gerirEncomenda controller = loader.getController();
        controller.setStage(dialogStage);
        controller.setOrder(order);
        controller.setItemOrders(order.getItens());
        controller.setTipoEncomenda(tipoEncomenda);
        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isBtnClicked();
    }

    /**
     * Sets the table cell values
     * @throws SQLException
     */
    public void loadOrders() throws SQLException {
        colunaNumEncomenda.setCellValueFactory(new PropertyValueFactory<>("num"));
        colunaCliente.setCellValueFactory(new PropertyValueFactory<>("nameClient"));
        colunaEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        orders = Order.getAllOrdersCompleteInformation();
        observableList = FXCollections.observableArrayList(orders);
        listaEncomendas.setItems(observableList);
    }


    public void btnBack(ActionEvent actionEvent) throws IOException {
        Parent op = FXMLLoader.load(getClass().getClassLoader().getResource("View/menu.fxml"));
        Scene operator = new Scene(op);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(operator);
        window.show();
    }
}
