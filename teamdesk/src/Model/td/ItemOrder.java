package Model.td;

import Model.td.helper.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static Model.td.Order.calculateTotalOrder;
import static java.lang.Math.round;

public class ItemOrder {
    private int id;
    private Product product;
    private double quantity;
    private double price;
    private double discount;
    private String line_notes;
    private int numOrder;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double sumTotal() {
        double soma = this.getQuantity()*this.getPrice();
        return (soma) - this.getDiscount();
    }

    public ItemOrder(int id, Product product, double quantity, double price, double discount, String line_notes, String unity, int numOrder) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.line_notes = line_notes;
        this.numOrder = numOrder;
    }

    public ItemOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getLine_notes() {
        return line_notes;
    }

    public void setLine_notes(String line_notes) {
        this.line_notes = line_notes;
    }

    public int getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
    }

    public static ArrayList<ItemOrder> calculateDiscount(ArrayList<ItemOrder> itens, int tipoDesconto, double desconto) {
        double totalFaturaSemDesconto = somarItensSemDesconto(itens);
       double descontoPorcento = convertePorcentagemMonetario(totalFaturaSemDesconto,desconto);
        for (ItemOrder item : itens) {
            if (tipoDesconto == 0) {
               item.setDiscount(retornaPorcentagemLinha(item,totalFaturaSemDesconto) * descontoPorcento);
            } else {
                item.setDiscount(retornaPorcentagemLinha(item,totalFaturaSemDesconto) * desconto);
            }
        }
        return itens;
    }

    public static double convertePorcentagemMonetario(double totalFaturaSemDesconto,double desconto){
        return (desconto/100) * totalFaturaSemDesconto;
    }

    public static double retornaPorcentagemLinha(ItemOrder item,double totalFaturaSemDesconto){
        return (item.getPrice() * item.getQuantity()) / totalFaturaSemDesconto;
    }

    public static double somarItensSemDesconto(ArrayList<ItemOrder> itens) {
        double soma = 0;
        for (ItemOrder it : itens) {
            soma += it.getQuantity() * it.getPrice();
        }
        return soma;
    }


    @Override
    public String toString() {
        return "ItemOrder{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", line_notes='" + line_notes + '\'' +
                '}';
    }
}
