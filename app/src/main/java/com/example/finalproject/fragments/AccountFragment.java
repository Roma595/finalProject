package com.example.finalproject.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.finalproject.Post;
import com.example.finalproject.Profile;
import com.example.finalproject.R;
import com.example.finalproject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    //поля класса
    private TextView acc_name;                      //имя пользователя
    private TextView dayraz;                        //бюджет на день
    private TextView Date_raz;                      //текущая дата
    private TextView day_expen;                     //расходы за день
    private CircleImageView image;                  //аватарка пользователя
    private String imageUri;                        //ссылка на аватарку
    private String date1;                           //текущая дата
    private CardView cardView;                      //карточка
    private Date date;                              //обьект класса Date
    private int raz;                                //расходы

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;



    public static AccountFragment newInstance() {
        return new AccountFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //инициализируем обьекты
        dayraz = view.findViewById(R.id.day_rz);
        Date_raz = view.findViewById(R.id.Date_raz);
        day_expen = view.findViewById(R.id.acc_expen);
        image = view.findViewById(R.id.profile_image);

        acc_name = view.findViewById(R.id.acc_name);
        cardView = view.findViewById(R.id.cardView);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        getNameDB();
        getProfit();
        getExp();
        getAvatar();

        //обработка нажатия на карточку
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });

        //получаем текущую дату
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();
        date.getTime();
        date1 = (formatter.format(date));
        Date_raz.setText(date1);

        return view;
    }
   //метод для полученися имени пользователя
    public void getNameDB() {
        User.getUserQuery(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    acc_name.setText(user.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //получение информации доходов из БД
    public void getProfit() {
        User.getUserQuery(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    String pr = user.getProfit();
                    int profit = Integer.parseInt(pr);
                    raz = (profit / 30);
                    String razhod = String.valueOf(raz);
                    dayraz.setText(razhod + " " + "руб");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getExp() {
        //ищем пост
        Post.getPostQuery(date1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    day_expen.setText(post.getExpen());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //получаем текущую аватарку пользователя и устанавливаем ее
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
                            Glide.with(AccountFragment.this).load(imageUri).into(image);
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





}