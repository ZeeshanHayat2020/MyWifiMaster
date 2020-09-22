package com.internet.speed.test.analyzer.wifi.key.generator.app.allRouterPassword;

public class All_Router_Model_Class {

    String brand , password , username , protocol , model;

    public All_Router_Model_Class(String brand, String password, String username, String protocol, String model) {
        this.brand = brand;
        this.password = password;
        this.username = username;
        this.protocol = protocol;
        this.model = model;
    }

    public All_Router_Model_Class() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
