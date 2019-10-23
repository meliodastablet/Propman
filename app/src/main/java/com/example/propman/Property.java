package com.example.senior1;

public class Property {
    private int id;
    private String title;
    private String price;
    private String odasalon;
    private int m2;
    private String description;
    private String address;
    private String image;

    public Property(int id, String title, String price, String odasalon, int m2, String description, String address, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.odasalon = odasalon;
        this.m2 = m2;
        this.description = description;
        this.address = address;
        this.image = image;
    }

    public Property() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOdasalon() {
        return odasalon;
    }

    public void setOdasalon(String odasalon) {
        this.odasalon = odasalon;
    }

    public int getM2() {
        return m2;
    }

    public void setM2(int m2) {
        this.m2 = m2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
