package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProfitActivity extends AppCompatActivity {

    //поля класса
    private String prof;                    //вводимое значение
    private String profi;                   //начальные данные пользователя
    private String profit;                  //конечные данные пользователя после ввода доходов
    private String userID;                  //идентификатор пользователя

    //Firebase
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;

    //поле ввода значений
    private TextInputEditText ed_profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profit);

        //инициализируем БД
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        //инициализируем поле ввода
        ed_profit = findViewById(R.id.ed_profit);
    }


    //метод обновления доходов
    public void updateProfit() {
        //получаем значение из поля ввода
        prof = ((ed_profit.getEditableText().toString().trim()));
        //ищем пользователя
        User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    userID = ds.getKey();
                    User user = ds.getValue(User.class);
                    profi = user.getProfit();
                    profit = String.valueOf(((Integer.valueOf(profi))+(Integer.valueOf(prof))));

                    //обновляем данные
                    HashMap map = new HashMap();
                    map.put("profit",profit);
                    database.getReference("User").child(String.valueOf(userID))
                            .updateChildren(map);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    //обработчик кнопки сохранения добавленных доходов
    public void onSaveProfit(View view){
        updateProfit();
        Intent intent = new Intent(AddProfitActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}