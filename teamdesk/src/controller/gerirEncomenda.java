package controller;

import Model.td.*;
import Model.td.database.*;
import Model.td.helper.Helper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import jdk.jshell.Snippet;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class gerirEncomenda implements Initializable {
    public TextField fieldDescont;
    public ComboBox selectTypeDiscont;
    public Button btnSelectClient;
    public Label labelNumEncomenda;
    public Label dataDocument;
    public ComboBox selectStatus;
    public Label dataModification;
    public Label labelName;
    public Label labelNif;
    public Label labelContact;
    public Label labelTotal;
    public Button btnSave;
    public Button btnCancel;
    public Button btnChangeMainAddress;
    public Button btnChangeSecondaryAddress;
    public Button btnAddItem;
    public Button btnRemoveItem;
    public Label labelDeliveryAddress;
    public Label labelMainAddress;
    public TableView<ItemOrder> tableItensOrder;
    public TableColumn<ItemOrder, String> columnReference;
    public TableColumn<ItemOrder, String> columnVersion;
    public TableColumn<ItemOrder, String> columnDescription;
    public TableColumn<ItemOrder, String> columnUnity;
    public TableColumn columnQuantity;
    public TableColumn<ItemOrder, Double> columnPrice;
    public TableColumn<ItemOrder, Double> columnTotalProduct;
    public TableColumn<ItemOrder, Double> columnDiscount;
    public ObservableList<ItemOrder> observableListItemOrder;
    private ArrayList<Product> products;
    private ArrayList<String> status;
    private int idStatus;

    public Button btnDesconto;

    private ArrayList<ItemOrder> itemOrders;
    private Stage stage;
    private boolean btnClicked;
    private Order order;
    private int tipoEncomenda;

    public int getTipoEncomenda() {
        return tipoEncomenda;
    }

    public void setTipoEncomenda(int tipoEncomenda) {
        this.tipoEncomenda = tipoEncomenda;
        if (tipoEncomenda == 0) {
            btnChangeSecondaryAddress.setDisable(true);
            btnChangeMainAddress.setDisable(true);
            btnAddItem.setDisable(true);
            btnRemoveItem.setDisable(true);
        }
        if (tipoEncomenda == 1) {
            for (ItemOrder o : itemOrders) {
                products.remove(o.getProduct());
            }
            btnChangeSecondaryAddress.setDisable(false);
            btnChangeMainAddress.setDisable(false);
        }
        if (tipoEncomenda == 2) {
            btnChangeSecondaryAddress.setDisable(true);
            btnChangeMainAddress.setDisable(true);
            btnSelectClient.setDisable(true);
            selectStatus.setDisable(true);
            selectTypeDiscont.setDisable(true);
            btnAddItem.setDisable(true);
            btnRemoveItem.setDisable(true);
            btnSave.setDisable(true);
            fieldDescont.setDisable(true);
            tableItensOrder.setEditable(false);
        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isBtnClicked() {
        return btnClicked;
    }

    public void setBtnClicked(boolean btnClicked) {
        this.btnClicked = btnClicked;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        setFieldsClient();
        setFieldsHeaderOrder();
        textFieldDesconto();
    }

    public ArrayList<ItemOrder> getItemOrders() {
        return itemOrders;
    }

    public void setItemOrders(ArrayList<ItemOrder> itemOrders) throws SQLException {
        this.itemOrders = itemOrders;
        loadOrders();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = DatabaseProduct.getAllProductsDBActive();
        status = DatabaseOrder.getAllStatus();
        comboBoxTypeDiscount();
        fillStatusOrder();
    }

    public void getStatus() {
        order.setStatus(selectStatus.getSelectionModel().getSelectedItem().toString());
    }

    public int returnIdStatus() {
        int id = 0;
        for (int i = 0; i < status.size(); i++) {
            if (order.getStatus().equals(status.get(i))) {
                id = i + 1;
            }
            if (status.get(i).equals("Finalizada")) {
                order.setClosed(true);
            } else {
                order.setClosed(false);
            }
        }
        return id;
    }

    public void btnSave(ActionEvent actionEvent) {
        getStatus();

        if (order.getClient() != null && order.getItens().size() > 0 &&
                order.getBillingAddress() != null && order.getDeliveryAddress() != null && order.getStatus() != null) {
            if (DatabaseOrder.insertOrderDB(order, returnIdStatus())) {
                for (ItemOrder io : order.getItens()) {
                    int numE = (int) DatabaseOrder.getLastNumOrder();
                  boolean inserido=  DatabaseItemOrder.insertItemOrderDB(numE, io);
                    if(inserido){
                        Helper.showAlert(Alert.AlertType.INFORMATION, "Guardar", "Encomenda Inserida",
                                "A encomenda foi inserida com sucesso");
                        setBtnClicked(true);
                        stage.close();
                    }
                }
            }
        } else {
            Helper.showAlert(Alert.AlertType.INFORMATION, "Inválido", "Campos Inválidos", "Preencha todos os campos");
        }

    }

    public void btnCancel(ActionEvent actionEvent) {
        stage.close();
    }

    public void setFieldsClient() {
        if (order.getClient() != null) {
            labelName.setText(order.getClient().getName());
            labelNif.setText(String.valueOf(order.getClient().getNif()));
            setContact();
            setAddresseBilling();
            setAddresseDelivery();
        }
    }

    public void setContact() {
        if (order.getClient().getContacts().size() > 0) {
            for (Contact c : order.getClient().getContacts()) {
                if (c.getMainContact()) {
                    labelContact.setText(c.getContact());
                }else{
                    labelContact.setText(c.getContact());
                }
            }
        }
    }

    public void setAddresseBilling() {
        if (order.getBillingAddress() != null) {
            labelMainAddress.setText(order.getBillingAddress().formatterAddress());
        }
    }

    public void setAddresseDelivery() {
        if (order.getDeliveryAddress() != null) {
            labelDeliveryAddress.setText(order.getDeliveryAddress().formatterAddress());
        }
    }

    public void setFieldsHeaderOrder() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        if (order.getNum() > 0) {
            labelNumEncomenda.setText(String.valueOf(order.getNum()));

        } else {
            int nr = (int) DatabaseOrder.getLastNumOrder() + 1;
            labelNumEncomenda.setText(String.valueOf(nr));
            order.setNum(nr);
        }

        if (order.getDate() == null) {
            dataDocument.setText(LocalDate.now().format(formatter));
            order.setDate(LocalDate.now());
        } else {
            dataDocument.setText(order.getDate().format(formatter));
        }

        if (order.getLastModificationDate() == null) {
            dataModification.setText("Sem modificação");
            order.setLastModificationDate(order.getDate());
        } else {
            dataModification.setText(order.getLastModificationDate().format(formatter));
        }

        if (selectStatus.getValue() == null) {
            for (String s : status) {
                if (s.equals("Por validar"))
                    selectStatus.setValue(s);
            }
        } else {
            selectStatus.setValue(order.getStatus());
        }


    }

    public void fillStatusOrder() {
        selectStatus.getItems().addAll(status);
    }

    public void loadOrders() throws SQLException {
        columnReference.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getProduct().getIdProduct()));
        columnVersion.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getProduct().getVersionProduct()));
        columnDescription.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getProduct().getTradeName()));
        columnUnity.setCellValueFactory(cellData ->
                new SimpleObjectProperty<String>(cellData.getValue().getProduct().getUnity()));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        columnTotalProduct.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().sumTotal()));
        observableListItemOrder = FXCollections.observableArrayList(itemOrders);
        tableItensOrder.setItems(observableListItemOrder);

        editColumnQuantity();
        editColumnPrice();
        setValueInTotal();

    }

    public void editColumnQuantity() {
        columnQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (Exception e) {
                    return 0.0;
                }
            }
        }));

        columnQuantity.setEditable(true);
        columnQuantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemOrder, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ItemOrder, Double> evt) {
                for (int i = 0; i < itemOrders.size(); i++) {
                    if (itemOrders.get(i) == tableItensOrder.getSelectionModel().getSelectedItem()) {
                        itemOrders.get(i).setQuantity(evt.getNewValue());
                        ItemOrder.calculateDiscount(itemOrders, selectTypeDiscont.getSelectionModel().getSelectedIndex()
                                , fieldDescont.getText().isEmpty() ? 0 : Double.parseDouble(fieldDescont.getText()));
                        setValueInTotal();
                        tableItensOrder.refresh();
                    }
                }
            }
        });
    }

    public void editColumnPrice() {
        columnPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (Exception e) {
                    return 0.0;
                }
            }
        }));

        columnPrice.setEditable(true);
        columnPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemOrder, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ItemOrder, Double> evt) {
                for (int i = 0; i < itemOrders.size(); i++) {
                    if (itemOrders.get(i) == tableItensOrder.getSelectionModel().getSelectedItem()) {
                        itemOrders.get(i).setPrice(evt.getNewValue());
                        setValueInTotal();
                        tableItensOrder.refresh();
                    }
                }
            }
        });
    }

    public void setValueInTotal() {
        ArrayList<ItemOrder> teste = new ArrayList<>();
        labelTotal.setText(String.format("%1$,.2f", Order.calculateTotalOrder(itemOrders)));
    }

    public void textFieldDesconto() {
        fieldDescont.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (Main.readInt(newValue)) {
                    double d = Double.parseDouble(newValue);
                    itemOrders = ItemOrder.calculateDiscount(itemOrders, selectTypeDiscont.getSelectionModel().getSelectedIndex(), d);
                    tableItensOrder.refresh();
                    setValueInTotal();
                }
            }
        });

    }

    public void comboBoxTypeDiscount() {
        ObservableList<String> options =
                FXCollections.observableArrayList("%", "€");
        selectTypeDiscont.setItems(options);
        selectTypeDiscont.setValue(options.get(0));
    }

    public void btnSelectClient(ActionEvent actionEvent) throws IOException, SQLException {
        Client cl = new Client();
        cl = controllerClient(cl);
        if (cl != null) {
            order.setClient(cl);
            order.getClient().setContacts(DatabaseContact.getAllContactByClient(cl.getNif()));
            setAddresSelectClient();
            setFieldsClient();
            btnChangeSecondaryAddress.setDisable(false);
            btnChangeMainAddress.setDisable(false);
            btnAddItem.setDisable(false);
            btnRemoveItem.setDisable(false);
        }
    }

    public void setAddresSelectClient() throws SQLException {
        ArrayList<Address> addresses = DatabaseAddress.getAllAddressesByClient(order.getClient().getNif());
        if (addresses.size() > 0) {
            for (Address ad : addresses) {
                if (ad.getMainAddress()) {
                    order.setBillingAddress(ad);
                    order.setDeliveryAddress(ad);
                    break;
                } else if (ad.getAddress() != null) {
                    order.setBillingAddress(ad);
                    order.setDeliveryAddress(ad);
                    break;
                }
            }
        }

    }

    public static Client controllerClient(Client cl) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SelectClientOrderController.class.getResource("/view/selectClientOrder.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Encomenda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        SelectClientOrderController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setClient(cl);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        if (controller.isBtnClicked()) {
            return controller.getClient();
        }
        return null;
    }

    public void btnChangeMainAddress(ActionEvent actionEvent) throws IOException, SQLException {
        Address address = new Address();
        address = controllerAddress(address, order.getClient().getNif());
        if (address != null) {
            order.setBillingAddress(address);
            labelMainAddress.setText(order.getBillingAddress().formatterAddress());
        }
    }

    public void btnChangeSecondaryAddress(ActionEvent actionEvent) throws IOException, SQLException {

        Address address = new Address();
        address = controllerAddress(address, order.getClient().getNif());
        if (address != null) {
            order.setDeliveryAddress(address);
            labelDeliveryAddress.setText(order.getDeliveryAddress().formatterAddress());
        }
    }

    public static Address controllerAddress(Address ad, int nif) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SelectAddressClientOrderController.class.getResource("/view/SelectMoradaClientOrder.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Encomenda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        SelectAddressClientOrderController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setAddress(ad);
        controller.setNif(nif);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        if (controller.isBtnClicked()) {
            return controller.getAddress();
        }
        return null;
    }

    public void btnAddItem(ActionEvent actionEvent) throws SQLException, IOException {

        Product product = new Product();
        product = controllerProduct(product, products);
        ItemOrder it = new ItemOrder();
        if (product != null) {
            products.remove(product);
            it.setProduct(product);
            it.setPrice(0);
            it.setQuantity(0);
            it.setDiscount(0);
            itemOrders.add(it);
            loadOrders();
            setValueInTotal();
        }
    }

    public void btnRemoveItem(ActionEvent actionEvent) throws SQLException {
        ItemOrder io = tableItensOrder.getSelectionModel().getSelectedItem();
        if (io != null) {
            itemOrders.remove(io);
            products.add(io.getProduct());
            loadOrders();
        } else {
            Helper.showAlert(Alert.AlertType.INFORMATION, "Item Encomenda", "Selecione",
                    "Por favor selecione um item para remover");
        }
    }

    public static Product controllerProduct(Product product, ArrayList<Product> products) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SelectProductItemOrderController.class.getResource("/view/selectProductOrder.fxml"));
        AnchorPane page = loader.load();

        // Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Encomenda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Setando o cliente no Controller.
        SelectProductItemOrderController controller = loader.getController();
        controller.setStageDialog(dialogStage);
        controller.setProduct(product);
        controller.setProducts(products);

        // Mostra o Dialog e espera até que o usuário o feche
        dialogStage.showAndWait();

        if (controller.isBtnClicked()) {
            return controller.getProduct();
        }
        return null;
    }
}
