package com.example.finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Post {

    //поля класса
    public String id;       //id
    public String date;     //дата
    public String profit;   //бюджет на день
    public String expen;    //расход за день

    //конструктор
    public Post() {
    }

    //конструктор
    public Post(String id, String date, String profit, String expen) {
        this.id = id;
        this.date = date;
        this.profit = profit;
        this.expen = expen;

    }
    //метод для получение объекта Query для конкретного поста по ID
    public static Query getPostQuery(String date) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Post");
        Query query = reference.orderByChild("date").equalTo(date);
        return query;
    }

    //геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getExpen() {
        return expen;
    }

    public void setExpen(String expen) {
        this.expen = expen;
    }


}
