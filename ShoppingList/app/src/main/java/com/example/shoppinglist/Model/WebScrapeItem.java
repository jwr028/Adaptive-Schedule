package com.example.shoppinglist.Model;

public class WebScrapeItem{
    private String itemName;
    private String imageURL;


    public WebScrapeItem(String itemName, String imageURL) {
        this.itemName = itemName;
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
