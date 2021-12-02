package controller;

import Model.td.Client;
import Model.td.Product;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseProduct;
import Model.td.helper.Helper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

public class SelectProductItemOrderController {
    public TableView tableProducts;
    public TableColumn columnProduto;
    public TableColumn columnVersao;
    public TextField txtProduto;
    private Product product;
    private boolean btnClicked;
    private Stage stageDialog;
    private ObservableList  observableArrayList;
    private ArrayList<Product> products;

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) throws SQLException {
        this.products = products;
        loadProducts();

    }

    public boolean isBtnClicked() {
        return btnClicked;
    }

    public void setBtnClicked(boolean btnClicked) {
        this.btnClicked = btnClicked;
    }

    public Stage getStageDialog() {
        return stageDialog;
    }

    public void setStageDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) throws SQLException {
        this.product = product;

    }

    public void btnSelect(ActionEvent actionEvent) {
        product = (Product) tableProducts.getSelectionModel().getSelectedItem();
        if(getProduct() == null){
            Helper.showAlert(Alert.AlertType.INFORMATION,"Atenção","Selecionar Produto",
                    "Por favor selecione uma produto para a encomenda.");
        }else{
            setBtnClicked(true);
            stageDialog.close();
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        stageDialog.close();
    }


    public void loadProducts() throws SQLException {
        columnProduto.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        columnVersao.setCellValueFactory(new PropertyValueFactory<>("versionProduct"));

        observableArrayList = FXCollections.observableArrayList(getProducts());
        tableProducts.setItems(observableArrayList);
        filteredSearchList();

    }
    public void filteredSearchList() throws SQLException {
        FilteredList<Product> filteredList = new FilteredList<>(observableArrayList, b -> true);
        txtProduto.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase(Locale.ROOT);
                if (product.getIdProduct().toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(product.getVersionProduct()).toLowerCase(Locale.ROOT).indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Product> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableProducts.comparatorProperty());
        tableProducts.setItems(sortedList);
    }
}
