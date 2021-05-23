package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText ed_name;                  //поле ввода имени пользователя
    private TextInputEditText ed_surname;               //поле ввода фамилии пользователя
    private String name;                                //имя пользователя
    private String surname;                             //фамилия пользователя

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        //инициализируем обьекты
        ed_name = findViewById(R.id.ed_profit);
        ed_surname = findViewById(R.id.ed_surname);

    }
    //обработчик кнопки продолжения регистрации
    public void onClickContinue(View view){
        //получаем данные
        name = ed_name.getText().toString().trim();
        surname = ed_surname.getText().toString().trim();
        //проверка наличия в полях ввода текста
        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(surname)){
            //добавляем нового пользователя в БД

            //переходим на другую активность
            Intent intent = new Intent(RegisterActivity.this, RegisterActivity2.class);
            intent.putExtra("name",name);
            intent.putExtra("surname",surname);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Введите Имя и Фамилию",Toast.LENGTH_SHORT).show();
        }
    }
}

