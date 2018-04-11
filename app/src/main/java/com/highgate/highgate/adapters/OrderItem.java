package com.highgate.highgate.adapters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */

public class OrderItem {
    private int id;
    private String date;
    private double orderTotal;
    private double subTotal;
    private int status;
    private String customerOrderNumber;
    private String dispatchMethod;
    private String coneNoteN;
    private String href;

    private List<OrderItemDetail> orderItemDetails;

    public OrderItem(){
        this.id = 0;
        this.date = "";
        this.orderTotal = 0.0;
        this.subTotal = 0.0;
        this.status = 0;
        this.customerOrderNumber = "";
        this.dispatchMethod = "";
        this.coneNoteN = "";
        this.href = "";
        orderItemDetails = new ArrayList<>();
    }

    public OrderItem(int id, String date, double orderTotal, double subTotal, int status, String stockCode, String description, int quantity, double unitPrice, double lineTotal, String customerOrderNumber, String dispatchMethod, String coneNoteN, String href, List<OrderItemDetail> orderItemDetails) {
        this.id = id;
        this.date = date;
        this.orderTotal = orderTotal;
        this.subTotal = subTotal;
        this.status = status;
        this.customerOrderNumber = customerOrderNumber;
        this.dispatchMethod = dispatchMethod;
        this.coneNoteN = coneNoteN;
        this.href = href;
        this.orderItemDetails = orderItemDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCustomerOrderNumber() {
        return customerOrderNumber;
    }

    public void setCustomerOrderNumber(String customerOrderNumber) {
        this.customerOrderNumber = customerOrderNumber;
    }

    public String getDispatchMethod() {
        return dispatchMethod;
    }

    public void setDispatchMethod(String dispatchMethod) {
        this.dispatchMethod = dispatchMethod;
    }

    public String getConeNoteN() {
        return coneNoteN;
    }

    public void setConeNoteN(String coneNoteN) {
        this.coneNoteN = coneNoteN;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<OrderItemDetail> getOrderItemDetails() {
        return orderItemDetails;
    }

    public void setOrderItemDetails(List<OrderItemDetail> orderItemDetails) {
        this.orderItemDetails = orderItemDetails;
    }

    public void addListItem(OrderItemDetail orderItemDetail){
        this.orderItemDetails.add(orderItemDetail);
    }
}
