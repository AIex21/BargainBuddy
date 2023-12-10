package com.example.bargainbuddy;

import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Promotion {
    private String title;
    private String store;
    private String promoCode;
    private String description;
    private String category;
    private float previousPrice;
    private float newPrice;
    private String expirationDate;
    private String imageURI;

    public Promotion() {
    }
    public Promotion(String title, String store, String promoCode, String description, String category, float previousPrice, float newPrice, String expirationDate, String imageURI) {
        this.title = title;
        this.store = store;
        this.promoCode = promoCode;
        this.description = description;
        this.category = category;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.expirationDate = expirationDate;
        this.imageURI = imageURI;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(float previousPrice) {
        this.previousPrice = previousPrice;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
