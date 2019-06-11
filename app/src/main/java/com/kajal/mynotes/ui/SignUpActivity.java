package com.kajal.mynotes.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.kajal.mynotes.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUserEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private Button btnSignUp;
    private Button btnToSignIn;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUserEmail = findViewById(R.id.sign_up_user_email);
        etPassword = findViewById(R.id.sign_up_user_password);
        etConfirmPassword = findViewById(R.id.sign_up_confirm_password);

        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnToSignIn = findViewById(R.id.btn_return_to_sign_in);
        btnToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignIn();
            }
        });

        progressBar = findViewById(R.id.progress_sign_up);

        mAuth = FirebaseAuth.getInstance();
    }

    private void registerUser() {
        String userEmail = etUserEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if(userEmail.isEmpty()){
            etUserEmail.setError("Email is required");
            etUserEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            etUserEmail.setError("Enter a valid email");
            etUserEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Email is required");
            etPassword.requestFocus();
            return;
        }
        if(confirmPassword.isEmpty()){
            etConfirmPassword.setError("Email is required");
            etConfirmPassword.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            etConfirmPassword.setError("does not match with password");
            etConfirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(userEmail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    Toast.makeText(getApplicationContext(),"Successfully registered",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"This email is already signed",
                                Toast.LENGTH_SHORT).show();
                        goToSignIn();
                    }
                    Toast.makeText(getApplicationContext(),"error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void goToSignIn() {
        Intent signInIntent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(signInIntent);
    }
}
