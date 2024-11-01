package com.example.infosphere;

import android.widget.ImageView;

public class Pojo_History {
    private String question;
    String key;
    private  int delete;
    public Pojo_History(String question, String key) {
        this.question = question;
       this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
}
