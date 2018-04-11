package com.highgate.highgate.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MH on 7/7/2017.
 */


public class Product {
    private int id;
    private String name;
    private String sku;
    private int group;
    private float price;
    private float special_price;
    private float customer_price;
    private boolean availability;
    private int on_hand;
    private String category_name;
    private String search_keyword;
    private List<String> images = new ArrayList<>();
    private int number;
    private int currentImageIndex;

    public Product() {
        this.id = 1;
        this.name = "";
        this.sku = "";
        this.group = 1;
        this.price = 0.0f;
        this.special_price = 0.0f;
        this.customer_price = 0.0f;
        this.availability = false;
        this.on_hand = 0;
        this.category_name = "";
        this.search_keyword = "";
        this.images = new ArrayList<>();
        this.number = 1;
        this.currentImageIndex = 0;
    }

    public Product(int id, String name, String sku, int group, float price, float special_price, float customer_price, boolean availability, int on_hand, String category_name, String search_keyword, List<String> images, int number, int currentImageIndex) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.group = group;
        this.price = price;
        this.special_price = special_price;
        this.customer_price = customer_price;
        this.availability = availability;
        this.on_hand = on_hand;
        this.category_name = category_name;
        this.search_keyword = search_keyword;
        this.images = images;
        this.number = number;
        this.currentImageIndex =currentImageIndex;
    }

    public Product(Product clone){
        this.id = clone.getId();
        this.name = clone.getName();
        this.sku = clone.getSku();
        this.group = clone.getGroup();
        this.price = clone.getPrice();
        this.special_price = clone.getSpecial_price();
        this.customer_price = clone.getCustomer_price();
        this.availability = clone.isAvailability();
        this.on_hand = clone.getOn_hand();
        this.category_name = clone.getCategory_name();
        this.search_keyword = clone.getSearch_keyword();
        this.images = clone.getImages();
        this.number = clone.getNumber();
        this.currentImageIndex = clone.getCurrentImageIndex();
    }

    public void setProperty(JsonObject oneJsonObject){
        JsonElement nameJson = oneJsonObject.get("name");
        if(!nameJson.isJsonNull())
            this.setName(nameJson.getAsString());

        JsonElement skuJson = oneJsonObject.get("sku");
        if(!skuJson.isJsonNull())
            this.setSku(skuJson.getAsString());

        JsonElement priceJson = oneJsonObject.get("price");
        if(!priceJson.isJsonNull())
            this.setPrice(priceJson.getAsFloat());

        JsonElement special_priceJson = oneJsonObject.get("special_price");
        if(!special_priceJson.isJsonNull())
            this.setSpecial_price(special_priceJson.getAsFloat());

        JsonElement customer_priceJson = oneJsonObject.get("customer_price");
        if(!customer_priceJson.isJsonNull())
            this.setCustomer_price(customer_priceJson.getAsFloat());

        if (Math.abs(this.getSpecial_price() - 0.0f) > 0.0001f)
            this.setCustomer_price(this.getSpecial_price());

        if (Math.abs(this.getCustomer_price() - 0.0f) < 0.0001f)
            this.setCustomer_price(this.getPrice());

        JsonElement availabilityJson = oneJsonObject.get("availability");
        if(!availabilityJson.isJsonNull())
            this.setAvailability(availabilityJson.getAsBoolean());



        JsonElement on_handJson = oneJsonObject.get("on_hand");
        if(!on_handJson.isJsonNull())
            this.setOn_hand(on_handJson.getAsInt());

        JsonElement search_keywordJson = oneJsonObject.get("search_keyword");
        if(!search_keywordJson.isJsonNull())
            this.setSearch_keyword(search_keywordJson.getAsString());

        JsonElement imagesJson = oneJsonObject.get("images");
        if(!imagesJson.isJsonNull()) {
            JsonArray imagesJsonArray = imagesJson.getAsJsonArray();
            for (int i = 0; i < imagesJsonArray.size(); i++) {
                String imageUrl = imagesJsonArray.get(i).getAsString();
                if(!imageUrl.equals("")) this.images.add(imageUrl);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(float special_price) {
        this.special_price = special_price;
    }

    public float getCustomer_price() {
        return customer_price;
    }

    public void setCustomer_price(float customer_price) {
        this.customer_price = customer_price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getOn_hand() {
        return on_hand;
    }

    public void setOn_hand(int on_hand) {
        this.on_hand = on_hand;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSearch_keyword() {
        return search_keyword;
    }

    public void setSearch_keyword(String search_keyword) {
        this.search_keyword = search_keyword;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void setCurrentImageIndex(int currentImageIndex) {
        this.currentImageIndex = currentImageIndex;
    }

    public int increaseImageIndex() {
        if (this.images.size() == 0) return 0;
        this.currentImageIndex = (this.currentImageIndex + 1) % this.images.size();
        return currentImageIndex;
    }

    public int decreaseImageIndex() {
        if (this.images.size() == 0) return 0;
        if (this.currentImageIndex == 0)
            this.currentImageIndex = this.images.size() - 1;
        else
            this.currentImageIndex--;
        return this.currentImageIndex;
    }

}


