package com.example.secondandroidhita.activitys.models;

public class Product {
    private int id;
    private String title;
    private String description;
    private double price;
    private String image;
    private int quantity; // New field for product quantity

    public Product() {} // Empty constructor (needed for Firebase)

    public Product(int id, String title, String description, double price, String image, int quantity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
