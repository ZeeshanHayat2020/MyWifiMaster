package com.internet.speed.test.analyzer.wifi.key.generator.app.models;

public class ModelMain {

    private int imgId;
    private String title;

    public ModelMain(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }


    public int getImgId() {
        return imgId;
    }

    public String getTitle() {
        return title;
    }
}
