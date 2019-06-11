package com.kajal.mynotes.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kajal.mynotes.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_CHOSEN = 3;
    private ImageView iv_img_chooser;
    private EditText et_name;
    private Button btn_save;
    private ProgressBar progressBar;
    private FloatingActionButton fab_addImg;
    private TextView tv_verification;

    private Uri uriProfileImg;

    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv_img_chooser = findViewById(R.id.iv_add_img);
        et_name = findViewById(R.id.et_enter_name);
        btn_save = findViewById(R.id.btn_save_info);
        progressBar = findViewById(R.id.progress_profile);
        fab_addImg = findViewById(R.id.fab_add_img);
        tv_verification = findViewById(R.id.tv_verification_status);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id = mAuth.getCurrentUser().getUid();

        fab_addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgChooser();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriProfileImg != null){
                    uploadProfileInfoToFirebase();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Users").document(user_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                String userName = task.getResult().getString("UserName");
                                String profilePicture = task.getResult().getString("profilePicture");

                                et_name.setText(userName);

                                Glide.with(ProfileActivity.this).load(profilePicture).into(iv_img_chooser);
                            }
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


        if(mAuth.getCurrentUser().isEmailVerified()){
            tv_verification.setText("Email verified");
        }else {
            tv_verification.setText("Email not verified (click to verify)");
            tv_verification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplication(),"Verification email sent",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_CHOSEN && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriProfileImg = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImg);
                iv_img_chooser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void showImgChooser(){
        Intent imgChooserIntent = new Intent();
        imgChooserIntent.setType("image/*");
        imgChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imgChooserIntent,"Select Profile Image"), IMAGE_CHOSEN);
    }

    private void uploadProfileInfoToFirebase(){
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference img_path = storageReference.child("profile_images").child(user_id+".jpg");
        final String user_name = et_name.getText().toString();

        img_path.putFile(uriProfileImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storeFirestore(img_path,user_name);
                Toast.makeText(getApplication(),"Successfully uploaded",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplication(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void storeFirestore(StorageReference img_path, String user_name) {

        progressBar.setVisibility(View.VISIBLE);

        final Map<String ,Object> userInfo = new HashMap<>();
        userInfo.put("UserName",user_name);

        if(img_path != null){

            img_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    userInfo.put("profilePicture",uri.toString());

                    firebaseFirestore.collection("Users").document(user_id)
                            .set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getApplication(),"Successfully uploaded",Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

    }

}
