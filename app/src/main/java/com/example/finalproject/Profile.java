package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {


    private TextView acc_profit;                    //доходы
    private TextView acc_raz;                       //расходы
    private TextView profile_name;                  //имя пользователя
    private TextView profile_surname;               //фамилия пользователя
    private String userID;                          //идентификатор пользователя
    private CircleImageView image;                  //аватарка пользователя
    private String imageUri;                        //ссылка на аватарку

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference mStorage;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.tollbar1);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });


        //инициализируем обьекты
        acc_profit = findViewById(R.id.acc_profit);
        acc_raz = findViewById(R.id.tv_raz);
        image = findViewById(R.id.profile_im);
        profile_name = findViewById(R.id.profile_name);
        profile_surname = findViewById(R.id.profile_surname);

        //инициализируем Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

        getProfitAndExpenDB();
        getAvatar();
        updateName();
        //обработчик нажатия на
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(Profile.this, R.style.RoundShapeTheme)
                        .setTitle("Подтвердите")
                        .setMessage("Вы действительно хотите изменить фотографию?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Profile.this, ChangeImage.class);
                                startActivity(intent);
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
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.redach){
            startActivity(new Intent(Profile.this,UpdateName.class));
        }
        return super.onOptionsItemSelected(item);
    }



    //метод для получения доходов и расходов из БД
    public void getProfitAndExpenDB() {
        User.getUserQuery(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    acc_profit.setText(user.getProfit());
                    acc_raz.setText(user.getExpen());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    //обработчик кнопки для выхода из учетной записи
    public void OnClickExit(View view) {
        new MaterialAlertDialogBuilder(Profile.this, R.style.RoundShapeTheme)
                .setTitle("ПОДТВЕРЖДЕНИЕ")
                .setMessage("Вы действительно хотите выйти из учетной записи?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(Profile.this, LoginActivity.class);
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

    public void getAvatar() {
        User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    imageUri = user.getImageUri();
                    String proverka = "0";
                    if (!imageUri.equals(proverka)) {
                        try {
                            Glide.with(Profile.this).load(imageUri).into(image);
                        } catch (Exception e) {

                        }
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateName(){
        User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    profile_name.setText(user.getName());
                    profile_surname.setText(user.getSurname());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}