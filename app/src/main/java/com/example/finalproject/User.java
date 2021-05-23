package com.example.finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class User {

    //поля класса
    public String id;           //id
    public String name;         //имя пользователя
    public String surname;      //фамилия пользователя
    public String profit;       //доход
    public String expen;        //расход
    public String imageUri;     //ссылка на аватарку
    public String postID;
    public Post post;

    //конструктор
    public User() {
    }

    //конструктор
    public User(String id, String name, String surname, String profit, String expen, String imageUri, String postID, Post post) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.profit = profit;
        this.expen = expen;
        this.imageUri = imageUri;
        this.postID = postID;
        this.post = post;

    }
    //метод для получение объекта Query для конкретного пользователя по ID
    public static Query getUserQuery(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User");
        Query query = reference.orderByChild("id").equalTo(id);
        return query;
    }

    //геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
