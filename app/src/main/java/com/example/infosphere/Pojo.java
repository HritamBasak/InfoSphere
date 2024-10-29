package com.example.infosphere;

public class Pojo
{
    String title;
    String description;
    String urlToImage;

    public Pojo(String title, String description, String urlToImage) {
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
    }

    public Pojo() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

}
