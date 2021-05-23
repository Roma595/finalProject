package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import com.example.finalproject.fragments.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Date date;                              //текущая дата
    private String date1;                           //текущая дата
    private String key;                             //идентификатор пользователя
    private String profit;                          //доходы
    private String expen;                           //расходы
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDataBase;
    private FirebaseDatabase database;

    //массив кнопок для диалогового окна
    String[] items = new String[] {"Доходы", "Расходы"};

    //Навигационная панель в главной активности
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.bottom_main:
                        loadFragment(AccountFragment.newInstance());
                        return true;
                    case R.id.bottom_raz:
                        loadFragment(CalendFragment.newInstance());
                        return true;

                }
                return false;
            };


    //метод загрузки фрагментов
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        

        fab.setOnClickListener((View view) -> {
            new MaterialAlertDialogBuilder(MainActivity.this, R.style.RoundShapeTheme)
                    .setTitle("Выберите категорию:")
                    .setItems(items, (dialog, position) -> {
                        switch (position) {
                            case 0:
                                Intent intent = new Intent(MainActivity.this, AddProfitActivity.class);
                                startActivity(intent);

                            case 1:
                                startActivity(new Intent(MainActivity.this, AddExpenActivity.class));
                        }
                    })
                    .show();
        });




        mDataBase = FirebaseDatabase.getInstance().getReference().child("Post");
        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();
        date.getTime();
        date1 =(formatter.format(date));

        //проверка наличия поста
        //если поста нет, вызывается метод createNewPost();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Post")
                .orderByChild("date")
                .equalTo(date1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {

                }
                else{
                    createNewPost();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //устанавливаем окно начальным
        loadFragment(AccountFragment.newInstance());
    }


    //обработчик кнопки для выхода из учетной записи
    public void OnClickExit(View view){
        new MaterialAlertDialogBuilder(MainActivity.this, R.style.RoundShapeTheme)
                .setTitle("ПОДТВЕРЖДЕНИЕ")
                .setMessage("Вы действительно хотите выйти из учетной записи?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //метод добавления новых постов Расходов
    public void createNewPost() {

        key = mDataBase.push().getKey();
        profit = "0";
        expen = "0";

        Post newPost = new Post(key, date1, profit, expen);

        mDataBase.push().setValue(newPost);

        User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String UserKey = ds.getKey();
                    User user = ds.getValue(User.class);

                    HashMap map = new HashMap();
                    map.put("postID",key);
                    database.getReference("User").child(String.valueOf(UserKey)).updateChildren(map);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}