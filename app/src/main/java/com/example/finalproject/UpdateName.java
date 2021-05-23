package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateName extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText surname;
    FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mActionBarToolbar);

        mActionBarToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateName.this, Profile.class));
            }
        });

        name = findViewById(R.id.update_n);
        surname = findViewById(R.id.update_s);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

    }

    public void onClickSave(View view){
        update();
        startActivity(new Intent(UpdateName.this,Profile.class));
    }

    public void update(){
        String update_name = name.getEditableText().toString().trim();
        String update_surname = surname.getEditableText().toString().trim();

        if(!TextUtils.isEmpty(update_name)&&!TextUtils.isEmpty(update_surname)){
            User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String key = ds.getKey();
                        User user = ds.getValue(User.class);

                        HashMap map = new HashMap();
                        map.put("name",update_name);
                        map.put("surname",update_surname);
                        database.getReference("User").child(String.valueOf(key)).updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }



}