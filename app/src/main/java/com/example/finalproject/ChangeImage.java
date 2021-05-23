package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeImage extends AppCompatActivity {

    private String userID;                          //идентификатор пользователя
    private CircleImageView image1;                 //аватарка пользователя
    private Uri uploadUri;                          //ссылка на аватарку пользователя

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference mStorage;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        //создаем Toolbar и добавляем кнопку возврата
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeImage.this, Profile.class));
            }
        });

        //инициализируем изоборажение
        image1 = findViewById(R.id.profile_im1);


        //инициализируем Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && data != null && data.getData() != null){
            if(resultCode==RESULT_OK){
                Log.d("MyLog","ImageUru" + data.getData());
                image1.setImageURI(data.getData());

            }
        }
    }

    //метод, загружающий изображение в облачное хранилище FirebaseStorage
    private void uploadImage(){
        Bitmap bitmap = ((BitmapDrawable) image1.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte [] byteArray = baos.toByteArray();
        final StorageReference mRef = mStorage.child(System.currentTimeMillis() + "myImage");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();

                User.getUserQuery(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            userID = ds.getKey();

                            HashMap map = new HashMap();
                            map.put("imageUri",uploadUri.toString());
                            database.getReference("User").child(String.valueOf(userID))
                                    .updateChildren(map);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    //метод получения изображения из хранилища телефона
    private void getImage(){
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);
    }

    //обработчик кнопки для выбора фотографии из хранилища телефона
    public void getImag(View view){
        getImage();
    }


    //обработчик кнопки сохранения изменений
    public void Save(View view){
        uploadImage();
        startActivity(new Intent(ChangeImage.this, Profile.class));
    }
}