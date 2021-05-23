package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity2 extends AppCompatActivity {

    //поля класса
    private String id;                              //id пользователя
    private String email;                           //email пользователя
    private String password;                        //пароль пользователь
    private String password_return;                 //проверка совпадения паролей
    private String r_name;                          //имя пользователя
    private String r_surname;                       //фамилия пользователя
    private String profit;                          //доходы пользователя
    private String expen;                           //расходы пользователя
    private String imageUri;                        //ссылка на аватарку
    private String postID;                          //id создаваемого поста
    private Post post;                              //пост
    private Date date;                              //текущая дата
    private String date1;                           //текущая дата
    public String USER_KEY = "User";                //ключ

    //Firebase
    private FirebaseAuth rAuth;
    private DatabaseReference mDataBase;

    //поля ввода данных
    private TextInputEditText r_email;              //email
    private TextInputEditText r_password;           //пароль
    private TextInputEditText r_password_return;    //подтверждение пароля


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity2);

        post = new Post("0",date1,"0","0");

        //инициализируем обьекты
        r_email = findViewById(R.id.reg_em);
        r_password = findViewById(R.id.reg_pass);
        r_password_return = findViewById(R.id.reg_pass_return);

        //инициализируем FireBase
        rAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();
        date.getTime();
        date1 =(formatter.format(date));


        //создание ошибки, если кол-во символов при вводе пароля меньше 6
        r_password.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (r_password.length() < 6) {
                r_password.setError("Короткий пароль");
            }
        });
    }

    //обработчик кнопки "регистрация"
    public void onClickSignUp(View view) {
        //считываем данные с полей ввода
        email = r_email.getEditableText().toString();
        password = r_password.getEditableText().toString();
        password_return = r_password_return.getEditableText().toString().trim();
            //проверка совпадения паролей
            if(password.equals(password_return)){
                //если верно
                rAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //добавляем пользователя в БД
                        r_name = getIntent().getExtras().getString("name");
                        r_surname = getIntent().getExtras().getString("surname");
                        id = rAuth.getCurrentUser().getUid();
                        profit = "0";
                        expen = "0";
                        imageUri = "0";
                        postID = "0";
                        User newUser = new User(id, r_name, r_surname, profit, expen,imageUri,postID,post);
                        mDataBase.push().setValue(newUser).addOnSuccessListener(aVoid -> {
                        });
                        Intent intent = new Intent(RegisterActivity2.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Snackbar.make(view, "Ошибка", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
            else {
                //если неверно
                Snackbar.make(view, "Пароли не совпадают", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }


    }


