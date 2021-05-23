package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //поля класса
    private String email;                       //email пользователя
    private String password;                    //пароль пользователя

    private ProgressDialog mProgressDialog;     //диалоговое окно при нажатии кнопки "авторизация"
    TextInputEditText l_email;                  //поле ввода email
    TextInputEditText l_password;               //поле ввода пароля
    //Firebase
    private FirebaseAuth lAuth;
    private FirebaseUser cUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        //инициализируем поля ввода
        l_email = findViewById(R.id.login_em);
        l_password = findViewById(R.id.login_pass);

        //инициализируем диалоговое окно
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Авторизация...");

        //инициализируем FireBase
        lAuth = FirebaseAuth.getInstance();

    }

    //метод для проверки авторизованности пользователя
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = lAuth.getCurrentUser();
        if (cUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show();
        }
    }

    //обработчик кнопки для регистрации пользователя
    public void onClickToSingUp(View view) {
        Intent inte = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(inte);

    }

    //обработчик кнопки для авторизации пользователя
    public void onClickSingIn(View view) {
        //получаем данные из полей ввода
        email = l_email.getEditableText().toString().trim();
        password = l_password.getEditableText().toString().trim();

        //проверка, не пустые ли поля ввода
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgressDialog.show();
            //добавляем нового пользователя
            lAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //mProgressDialog.dismiss();
                    if (task.isSuccessful()) {
                        // Войти в аккаунт удалось
                        cUser = lAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "Успешно", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Не получилось войти в аккаунт
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            // Неправильно введена почта
                            Toast.makeText(getApplicationContext(), "Email", Toast.LENGTH_LONG).show();
                            l_email.setError("Email");
                            l_email.setFocusable(true);
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // Неправильно введен пароль
                            Toast.makeText(getApplicationContext(), "password", Toast.LENGTH_LONG).show();
                            l_password.setError("password");
                            l_password.setFocusable(true);
                        }
                    }
                }

            });
        } else {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
        }

    }

}
