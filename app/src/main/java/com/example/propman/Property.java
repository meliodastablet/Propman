package com.example.propman;

public class Property {
    private String title;
    private String price;
    private String rooms;
    private String area;
    private String description;
    private String address;
    private String uid;
    private String uniquepropertyid;
    private String imagefilepath;

    public Property( String title, String price, String rooms, String area, String description, String address,String uid,String filepath,String uniquepropertyid) {
        this.title = title;
        this.price = price;
        this.rooms = rooms;
        this.area = area;
        this.description = description;
        this.address = address;
        this.uid=uid;
        this.uniquepropertyid = uniquepropertyid;
        this.imagefilepath=filepath;
    }

    public Property() {
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

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getarea() {
        return area;
    }

    public void setarea(String area) {
        this.area = area;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImagefilepath() {
        return imagefilepath;
    }

    public void setImagefilepath(String filepath) {
        this.imagefilepath = filepath;
    }

    public String getUniquepropertyid() {
        return uniquepropertyid;
    }

    public void setUniquepropertyid(String uniquepropertyid) {
        this.uniquepropertyid = uniquepropertyid;
    }
}
