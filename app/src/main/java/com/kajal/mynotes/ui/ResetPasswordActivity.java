package com.kajal.mynotes.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kajal.mynotes.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText et_email;
    private Button btn_reset;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_email = findViewById(R.id.et_verified_email);
        btn_reset = findViewById(R.id.btn_send_reset_email);

        mAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = et_email.getText().toString();
                if(TextUtils.isEmpty(email)){
                    et_email.setError("You will have to enter your email here");
                    et_email.requestFocus();
                }else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplication(),"Reset password link sent to your email,Check inbox",Toast.LENGTH_SHORT).show();
                                Intent signInIntent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                                startActivity(signInIntent);;
                            }else {
                                Toast.makeText(getApplication(),"error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
