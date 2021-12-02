package Model.td;

import Model.td.database.DatabaseAddress;
import Model.td.database.DatabaseClient;
import Model.td.database.DatabaseItemOrder;
import Model.td.database.DatabaseOrder;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 */
public class Order {

    private int num;
    private Client client;
    private Address billingAddress;
    private Address deliveryAddress;
    private LocalDate date;
    private String status;
    private boolean closed;
    private LocalDate lastModificationDate;
    private ArrayList<ItemOrder> itens;
    private double total;
    private String nameClient;

    public String getNameClient() {
           return nameClient;
    }

    public void setNameClient(String nameClient) {
        if(nameClient != null){
            this.nameClient = nameClient;
        }
    }

    /**
     *
     * @param num
     * @param client
     * @param billingAddress
     * @param deliveryAddress
     * @param date
     * @param status
     * @param closed
     * @param lastModificationDate
     */
    public Order(int num, Client client, Address billingAddress, Address deliveryAddress, LocalDate date, String status,
                 boolean closed, LocalDate lastModificationDate) {
        this.num = num;
        this.client = client;
        this.billingAddress = billingAddress;
        this.deliveryAddress = deliveryAddress;
        this.date = date;
        this.status = status;
        this.closed = closed;
        this.lastModificationDate = lastModificationDate;
    }

    /**
     *
     */
    public Order() {}

    /**
     *
     * @return
     */
    public int getNum() {
        return num;
    }

    /**
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     *
     * @return
     */
    public Client getClient() {
        return client;
    }

    /**
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     *
     * @return
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     *
     * @param billingAddress
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     *
     * @return
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     *
     * @param deliveryAddress
     */
    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     *
     * @return
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     *
     * @param closed
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     *
     * @return
     */
    public LocalDate getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     *
     * @param lastModificationDate
     */
    public void setLastModificationDate(LocalDate lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }
    /**
     *
     * @return
     */
    public ArrayList<ItemOrder> getItens() {
        return itens;
    }
    /**
     *
     * @param itens
     */
    public void setItens(ArrayList<ItemOrder> itens) {
        this.itens = itens;
    }
    /**
     *
     * @return
     */
    public double getTotal() {
        return total;
    }
    /**
     *
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    public static ArrayList<Order> getAllOrdersCompleteInformation() throws SQLException {
        ArrayList<Order> orders = DatabaseOrder.getAllOrdersDB();
        for (Order order: orders) {
            order.setClient(DatabaseClient.findClientByNif(order.client.getNif()));
            order.setNameClient(order.getClient().getName());
            order.setBillingAddress(DatabaseAddress.findAddressById(order.billingAddress.getId()));
            order.setDeliveryAddress(DatabaseAddress.findAddressById(order.deliveryAddress.getId()));
            order.setTotal(DatabaseOrder.totalOrderByOrder(order.num));
            order.setItens(DatabaseItemOrder.getAllItensOrder());
        }
        return orders;
    }

    public static double calculateTotalOrder(ArrayList<ItemOrder> itens){
       double total = 0;
           for (ItemOrder item : itens) {
               total += item.sumTotal();
           }
        return total;
    }
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Order{" +
                "num=" + num +
                ", client=" + client +
                ", billingAddress=" + billingAddress +
                ", deliveryAddress=" + deliveryAddress +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", closed=" + closed +
                ", lastModificationDate='" + lastModificationDate + '\'' +
                '}';
    }
}
