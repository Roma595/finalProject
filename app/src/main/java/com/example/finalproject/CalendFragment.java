package com.example.finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendFragment extends Fragment {

    private RecyclerView recView;                       //видимый список постов
    private PostAdapter adapter;                        //адаптер для постов
    private ArrayList<Post> listData;                   //массив с постами
    private LinearLayoutManager linearLayoutManager;    //менеджер для RecyclerView

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference mDataBase;

    //конструктор
    public CalendFragment() {
    }

    public static CalendFragment newInstance() {
        return new CalendFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calend, container, false);

        //инициализируем обьекты
        recView = view.findViewById(R.id.recView);
        //Firebase
        mDataBase = FirebaseDatabase.getInstance().getReference("Post");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //работаем со списком
        listData = new ArrayList<>();
        getDataFromDB();
        adapter = new PostAdapter(listData);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recView.setAdapter(adapter);

        return view;
    }

    //метод для считывая информации из БД
    private void getDataFromDB(){

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listData.clear();
                for(DataSnapshot ds : snapshot.getChildren() ){
                    Post post = ds.getValue(Post.class);
                    listData.add(post);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDataBase.addValueEventListener(vListener);
    }

}