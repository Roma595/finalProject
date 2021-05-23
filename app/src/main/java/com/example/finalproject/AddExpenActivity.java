package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddExpenActivity extends AppCompatActivity {


    //поля класса
    private String prof;                //начальный доход пользователя
    private String profit;              //конечный расход после добавления
    private String expen;               //вводимый расход
    private String ex;                  //начальный расход пользователя
    private String exp;                 //конечный расход, после добавления
    private String post_expen;          //начальный расход за день
    private String post_exp;            //конечный расход за день
    private String key;                 //идентификатор поста
    private String userID;              //идентифакатор пользователя

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;

    //поле ввода значений
    private TextInputEditText ed_expen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expen);

        //инициализируем поле ввода
        ed_expen = findViewById(R.id.ed_expen);

        //инициализируем БД
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

    }

    //метод для обновления расходов
    public void updateExpen(){
        //получаем текст
        expen =(ed_expen.getEditableText().toString().trim());
        //ищем пользователя
        User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    userID = ds.getKey();
                    User user = ds.getValue(User.class);
                    prof = user.getProfit();
                    ex = user.getExpen();
                    profit = String.valueOf(((Integer.valueOf(prof))-(Integer.valueOf(expen))));
                    exp = String.valueOf(((Integer.valueOf(ex))+(Integer.valueOf(expen))));
                    //обновляем данные
                    HashMap map = new HashMap();
                    map.put("profit",profit);
                    map.put("expen",exp);
                    database.getReference("User").child(String.valueOf(userID))
                            .updateChildren(map);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //получаем текущую дату
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        date.getTime();
        String date1 =(formatter.format(date));

        //ищем пост
        Post.getPostQuery(date1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    key = ds.getKey();
                    Post post = ds.getValue(Post.class);
                    post_expen = post.getExpen();
                    post_exp = String.valueOf((Integer.valueOf(post_expen)+Integer.valueOf(expen)));

                    //обновляем данные
                    HashMap map1 = new HashMap();
                    map1.put("expen",post_exp);
                    database.getReference("Post").child(String.valueOf(key)).updateChildren(map1);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //обработчик кнопки сохранения добавления расходов
    public void onSaveExpen(View view){
        updateExpen();
        Intent intent = new Intent(AddExpenActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}