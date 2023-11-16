package com.gadgetguru.scrapper.controllers;

public class Product {
    private String name;
    private String price;

    // Construtor
    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}