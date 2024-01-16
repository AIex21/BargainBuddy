package com.example.bargainbuddy;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Promotion implements Parcelable {
    private String id;
    private String title;
    private String store;
    private String promoCode;
    private String description;
    private String category;
    private float previousPrice;
    private float newPrice;
    private String expirationDate;
    private String imageURI;
    private int isCertified;
    private String website;

    public Promotion() {
    }
    public Promotion(String id, String title, String store, String promoCode, String description,
                     String category, float previousPrice, float newPrice, String expirationDate,
                     String imageURI, int isCertified, String website) {
        this.id = id;
        this.title = title;
        this.store = store;
        this.promoCode = promoCode;
        this.description = description;
        this.category = category;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
        this.expirationDate = expirationDate;
        this.imageURI = imageURI;
        this.isCertified = isCertified;
        this.website = website;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getCertified() {
        return isCertified;
    }

    public void setCertified(int certified) {
        isCertified = certified;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    // Parcelable implementation
    protected Promotion(Parcel in) {
        title = in.readString();
        store = in.readString();
        promoCode = in.readString();
        description = in.readString();
        category = in.readString();
        previousPrice = in.readFloat();
        newPrice = in.readFloat();
        expirationDate = in.readString();
        imageURI = in.readString();
        isCertified = in.readInt();;
        website = in.readString();
    }

    public static final Creator<Promotion> CREATOR = new Creator<Promotion>() {
        @Override
        public Promotion createFromParcel(Parcel in) {
            return new Promotion(in);
        }

        @Override
        public Promotion[] newArray(int size) {
            return new Promotion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(store);
        dest.writeString(promoCode);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeFloat(previousPrice);
        dest.writeFloat(newPrice);
        dest.writeString(expirationDate);
        dest.writeString(imageURI);
        dest.writeInt(isCertified);
        dest.writeString(website);
    }
}
