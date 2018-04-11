package com.highgate.highgate.adapters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */

public class InvoiceItem {
    private int id;
    private String date;
    private double amount;
    private double subTotal;
    private String type;
    private String invoiceN;
    private String orderN;
    private String customerOrderNumber;
    private String dispatchMethod;
    private String coneNoteN;
    private String status;
    private String href;
    private List<InvoiceItemDetail> invoiceItemDetails;

    public InvoiceItem() {
        this.id = 0;
        this.date = "";
        this.amount = 0.0;
        this.type = "";
        this.invoiceN = "";
        this.orderN = "";
        this.customerOrderNumber = "";
        this.dispatchMethod = "";
        this.coneNoteN = "";
        this.subTotal = 0.0;
        this.status = "";
        this.href = "";
        this.invoiceItemDetails = new ArrayList<>();
    }

    public InvoiceItem(int id, String date, double amount, String type, String stockCode, String description, int quantity, double unitPrice, double lineTotal, String invoiceN, String orderN, String customerOrderNumber, String dispatchMethod, String coneNoteN, double subTotal, double grandTotal, String status, String href, List<InvoiceItemDetail> invoiceItemDetails) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.invoiceN = invoiceN;
        this.orderN = orderN;
        this.customerOrderNumber = customerOrderNumber;
        this.dispatchMethod = dispatchMethod;
        this.coneNoteN = coneNoteN;
        this.subTotal = subTotal;
        this.status = status;
        this.href = href;
        this.invoiceItemDetails = invoiceItemDetails;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvoiceN() {
        return invoiceN;
    }

    public void setInvoiceN(String invoiceN) {
        this.invoiceN = invoiceN;
    }

    public String getOrderN() {
        return orderN;
    }

    public void setOrderN(String orderN) {
        this.orderN = orderN;
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

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<InvoiceItemDetail> getInvoiceItemDetails() {
        return invoiceItemDetails;
    }

    public void setInvoiceItemDetails(List<InvoiceItemDetail> invoiceItemDetails) {
        this.invoiceItemDetails = invoiceItemDetails;
    }

    public void addListItem(InvoiceItemDetail invoiceItemDetail){
        this.invoiceItemDetails.add(invoiceItemDetail);
    }
}
