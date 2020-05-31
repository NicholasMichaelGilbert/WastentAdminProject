package com.example.wastentadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TransferMoney extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnNext;
    FirebaseAuth mFirebaseAuth;
    String adminEmail, adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        Intent intAdminLogin = getIntent();
        adminEmail = intAdminLogin.getStringExtra("adminEmail");
        adminPassword = intAdminLogin.getStringExtra("adminPassword");

        etEmail = findViewById(R.id.transferMoney_email);
        etPassword = findViewById(R.id.transferMoney_password);
        btnNext = findViewById(R.id.buttonNextTransfer);
        mFirebaseAuth = FirebaseAuth.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                if (email.isEmpty()) {
                    etEmail.setError("Please Enter Email");
                    etEmail.requestFocus();
                }
                else if (password.isEmpty()) {
                    etPassword.setError("Please Enter Password");
                    etPassword.requestFocus();
                }
                else if (email.isEmpty() && password.isEmpty()) {
                    etEmail.setError("Please Enter Email");
                    etEmail.requestFocus();
                    etPassword.setError("Please Enter Password");
                    etPassword.requestFocus();
                    Toast.makeText(TransferMoney.this, "Email and Password Fields are EMPTY", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(TransferMoney.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!(task.isSuccessful())) {
                                Toast.makeText(TransferMoney.this, "Wrong Credential, Please add the right Email and Password", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intToNext = new Intent(TransferMoney.this, TransferMoney2.class);
                                intToNext.putExtra("adminEmail", adminEmail);
                                intToNext.putExtra("adminPassword", adminPassword);
                                startActivity(intToNext);
                            }
                        }
                    });
                }
            }
        });
    }
}
