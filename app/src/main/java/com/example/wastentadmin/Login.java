package com.example.wastentadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText etEmailId, etPassId;
    Button btnLogin;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailId = findViewById(R.id.emailInput);
        etPassId = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmailId.getText().toString();
                final String password = etPassId.getText().toString();

                if (email.isEmpty()) {
                    etEmailId.setError("Please Enter Email");
                    etEmailId.requestFocus();
                }
                else if (password.isEmpty()) {
                    etPassId.setError("Please Enter Password");
                    etPassId.requestFocus();
                }
                else if (email.isEmpty() && password.isEmpty()) {
                    etEmailId.setError("Please Enter Email");
                    etEmailId.requestFocus();
                    etPassId.setError("Please Enter Password");
                    etPassId.requestFocus();
                    Toast.makeText(Login.this, "Email and Password Fields are EMPTY", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!(task.isSuccessful())) {
                                Toast.makeText(Login.this, "Login Failed, Please Enter the Right Email or Password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intToMain = new Intent(Login.this, MainActivity.class);
                                intToMain.putExtra("intent", "login");
                                intToMain.putExtra("adminEmail", email);
                                intToMain.putExtra("adminPassword", password);
                                startActivity(intToMain);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Login.this, "Error Occurred, Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
