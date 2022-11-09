package com.mianbrothersbooksellerandstationers.android.models;

public class ProductModel {

    String category, desc, image, key, name, timestamp;
    boolean inStock;
    int price, stock;

    public ProductModel() {
    }

    public ProductModel(String category, String desc, String image, String name, String selectedCategory, String key, String timestamp, boolean inStock, int price, int stock) {
        this.category = category;
        this.desc = desc;
        this.image = image;
        this.name = name;
        this.key = key;
        this.timestamp = timestamp;
        this.inStock = inStock;
        this.price = price;
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
