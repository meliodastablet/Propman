package com.example.propman;

public class Property {
    private String title;
    private String price;
    private String rooms;
    private String area;
    private String description;
    private String address;
    private String uid;
    private String coordinate;
    private String uniquepropertyid;
    private String imagefilepath;
    private String garage;
    private String garden;
    private String security;
    private String pool;
    private String time;
    private String dues;
    private String filepath_count;

    public String getFilepath_count() {
        return filepath_count;
    }

    public void setFilepath_count(String filepath_count) {
        this.filepath_count = filepath_count;
    }

    public Property(String title, String price, String rooms, String area, String description, String address, String uid, String filepath, String uniquepropertyid, String coordinate, String garage, String garden, String security, String pool, String dues, String filepath_count, String time) {
        this.title = title;
        this.price = price;
        this.rooms = rooms;
        this.area = area;
        this.description = description;
        this.address = address;
        this.uid=uid;
        this.uniquepropertyid = uniquepropertyid;
        this.imagefilepath=filepath;
        this.coordinate = coordinate;
        this.garage=garage;
        this.garden = garden;
        this.security=security;
        this.pool = pool;
        this.dues = dues;
        this.filepath_count = filepath_count;
        this.time = time;
    }


    public String getDues() {
        return dues;
    }

    public void setDues(String dues) {
        this.dues = dues;
    }

    public Property() {
    }

    public String getGarage() {
        return garage;
    }

    public void setGarage(String garage) {
        this.garage = garage;
    }

    public String getGarden() {
        return garden;
    }

    public void setGarden(String garden) {
        this.garden = garden;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
