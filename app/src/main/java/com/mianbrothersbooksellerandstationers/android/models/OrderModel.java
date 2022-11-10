package com.mianbrothersbooksellerandstationers.android.models;

public class OrderModel {
    String productname,productcategory,productid,producttotalquantity,productOrderQuantity,orderBy,buyerAddress,buyerPhoneNo,buyerID,price,orderID;
    String salesTime;
    public OrderModel() {

    }

    public OrderModel(String productname, String productcategory, String productid, String producttotalquantity, String productOrderQuantity, String orderBy, String buyerAddress, String buyerPhoneNo, String buyerID,String price,String orderID) {
        this.productname = productname;
        this.productcategory = productcategory;
        this.productid = productid;
        this.producttotalquantity = producttotalquantity;
        this.productOrderQuantity = productOrderQuantity;
        this.orderBy = orderBy;
        this.buyerAddress = buyerAddress;
        this.buyerPhoneNo = buyerPhoneNo;
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
    }

    public OrderModel(String productname, String productcategory, String productid, String producttotalquantity, String productOrderQuantity, String orderBy, String buyerAddress, String buyerPhoneNo, String buyerNIC, String buyerID, String price, String orderID, String salesTime) {
        this.productname = productname;
        this.productcategory = productcategory;
        this.productid = productid;
        this.producttotalquantity = producttotalquantity;
        this.productOrderQuantity = productOrderQuantity;
        this.orderBy = orderBy;
        this.buyerAddress = buyerAddress;
        this.buyerPhoneNo = buyerPhoneNo;
        this.buyerID = buyerID;
        this.price = price;
        this.orderID = orderID;
        this.salesTime = salesTime;
    }

    public String getSalesTime() {
        return salesTime;
    }

    public void setSalesTime(String salesTime) {
        this.salesTime = salesTime;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProducttotalquantity() {
        return producttotalquantity;
    }

    public void setProducttotalquantity(String producttotalquantity) {
        this.producttotalquantity = producttotalquantity;
    }

    public String getProductOrderQuantity() {
        return productOrderQuantity;
    }

    public void setProductOrderQuantity(String productOrderQuantity) {
        this.productOrderQuantity = productOrderQuantity;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerPhoneNo() {
        return buyerPhoneNo;
    }

    public void setBuyerPhoneNo(String buyerPhoneNo) {
        this.buyerPhoneNo = buyerPhoneNo;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }
}
