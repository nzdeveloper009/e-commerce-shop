package com.mianbrothersbooksellerandstationers.android.models;

import android.widget.ImageView;

public class CartModel {
    String productname,productcategory,customerId,cartID;
    String image;
    int totalPrice,orignalPrice,productOrderQuantity,producttotalquantity;

    public CartModel() {
    }

    public CartModel(String productname, String productcategory, String customerId, String cartID, String image, int totalPrice, int orignalPrice, int productOrderQuantity, int producttotalquantity) {
        this.productname = productname;
        this.productcategory = productcategory;
        this.customerId = customerId;
        this.cartID = cartID;
        this.image = image;
        this.totalPrice = totalPrice;
        this.orignalPrice = orignalPrice;
        this.productOrderQuantity = productOrderQuantity;
        this.producttotalquantity = producttotalquantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrignalPrice() {
        return orignalPrice;
    }

    public void setOrignalPrice(int orignalPrice) {
        this.orignalPrice = orignalPrice;
    }

    public int getProductOrderQuantity() {
        return productOrderQuantity;
    }

    public void setProductOrderQuantity(int productOrderQuantity) {
        this.productOrderQuantity = productOrderQuantity;
    }

    public int getProducttotalquantity() {
        return producttotalquantity;
    }

    public void setProducttotalquantity(int producttotalquantity) {
        this.producttotalquantity = producttotalquantity;
    }
}
