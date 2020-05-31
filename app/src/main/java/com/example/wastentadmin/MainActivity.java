package com.example.wastentadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView tvAdminName;
    Button btnAddWaste, btnTransferMoney, btnLogOut;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference dbRef;

    String intent, adminEmail, adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intAdminLogin = getIntent();
        intent = intAdminLogin.getStringExtra("intent");
        adminEmail = intAdminLogin.getStringExtra("adminEmail");
        adminPassword = intAdminLogin.getStringExtra("adminPassword");

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (intent != "login") {
            mFirebaseAuth.signInWithEmailAndPassword(adminEmail, adminPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!(task.isSuccessful())) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Intent intError = new Intent(MainActivity.this, Login.class);
                        startActivity(intError);
                    }
                    else {
                        mainFunction();
                    }
                }
            });
        }
        else {
            mainFunction();
        }
    }

    public void mainFunction() {
        tvAdminName = findViewById(R.id.admin_name);
        btnAddWaste = findViewById(R.id.buttonAddWaste);
        btnTransferMoney = findViewById(R.id.buttonTransferMoney);
        btnLogOut = findViewById(R.id.buttonLogOut);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        dbRef = mFirebaseDatabase.getReference().child("admin").child(mFirebaseUser.getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String adminName = dataSnapshot.child("name").getValue().toString();
                tvAdminName.setText(adminName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "You are not an admin", Toast.LENGTH_SHORT).show();
                Intent intToLoginFailed = new Intent(MainActivity.this, Login.class);
                startActivity(intToLoginFailed);
            }
        });

        btnAddWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToAddWaste = new Intent(MainActivity.this, AddCustomerWaste.class);
                intToAddWaste.putExtra("adminEmail", adminEmail);
                intToAddWaste.putExtra("adminPassword", adminPassword);
                startActivity(intToAddWaste);
            }
        });

        btnTransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToTransferMoney = new Intent(MainActivity.this, TransferMoney.class);
                intToTransferMoney.putExtra("adminEmail", adminEmail);
                intToTransferMoney.putExtra("adminPassword", adminPassword);
                startActivity(intToTransferMoney);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToLogin = new Intent(MainActivity.this, Login.class);
                startActivity(intToLogin);
            }
        });
    }
}
