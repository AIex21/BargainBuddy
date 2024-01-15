package com.example.bargainbuddy;

import java.util.List;

public class Favourite {
    private String uid;
    private List<String> promotionsID;

    public Favourite(String uid, List<String> promotionsID) {
        this.uid = uid;
        this.promotionsID = promotionsID;
    }

    public Favourite() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getPromotionsID() {
        return promotionsID;
    }

    public void setPromotionsID(List<String> promotionsID) {
        this.promotionsID = promotionsID;
    }
}
