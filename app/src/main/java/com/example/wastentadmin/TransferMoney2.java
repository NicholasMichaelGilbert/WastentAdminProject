package com.example.wastentadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransferMoney2 extends AppCompatActivity {
    TextView tvCustomerName, tvCustomerMoneyOwned, tvStaticMoneyWithdrawn, tvStaticNewBalanceData, tvStaticNewBalance, tvCustomerNewMoneyOwned;
    EditText etMoneyWithdrawn;
    Button btnWithdrawn, btnFinishTransfer;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference dbRef, wastePerKgRef;
    int customerMoney, moneyPerKilo, newCustomerMoney;
    String adminEmail, adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money2);

        Intent intAdminLogin = getIntent();
        adminEmail = intAdminLogin.getStringExtra("adminEmail");
        adminPassword = intAdminLogin.getStringExtra("adminPassword");

        tvCustomerName = findViewById(R.id.customer_name_transfer);
        tvCustomerMoneyOwned = findViewById(R.id.money_owned_transfer);
        tvStaticMoneyWithdrawn = findViewById(R.id.staticMoneyWithdrawn);
        tvStaticNewBalanceData = findViewById(R.id.staticNewBalanceData);
        tvStaticNewBalance = findViewById(R.id.staticNewBalance);
        tvCustomerNewMoneyOwned = findViewById(R.id.customer_money_transfer);
        etMoneyWithdrawn = findViewById(R.id.money_withdrawn_transfer);
        btnWithdrawn = findViewById(R.id.buttonWithdrawn);
        btnFinishTransfer = findViewById(R.id.buttonFinish_transfer);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        tvStaticNewBalanceData.setVisibility(View.INVISIBLE);
        tvStaticNewBalance.setVisibility(View.INVISIBLE);
        tvCustomerNewMoneyOwned.setVisibility(View.INVISIBLE);
        btnFinishTransfer.setVisibility(View.INVISIBLE);

        dbRef = mFirebaseDatabase.getReference().child("users").child(mFirebaseUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String customerName = dataSnapshot.child("name").getValue().toString();
                customerMoney = Integer.valueOf(dataSnapshot.child("moneyAchieved").getValue().toString());
                tvCustomerName.setText(customerName);
                tvCustomerMoneyOwned.setText("Rp " + String.valueOf(customerMoney) + " ,-");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransferMoney2.this, "The Credential you give is not a user account", Toast.LENGTH_SHORT).show();
                Intent intToBack = new Intent(TransferMoney2.this, TransferMoney.class);
                startActivity(intToBack);
            }
        });

        wastePerKgRef = mFirebaseDatabase.getReference().child("wastePerKg");

        wastePerKgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moneyPerKilo = Integer.valueOf(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnWithdrawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int moneyWithdrawn = Integer.valueOf(etMoneyWithdrawn.getText().toString());
                if (moneyWithdrawn == 0) {
                    etMoneyWithdrawn.setError("Please enter money withdrawn");
                    etMoneyWithdrawn.requestFocus();
                }
                else if (moneyWithdrawn > customerMoney) {
                    etMoneyWithdrawn.setError("Money Withdrawn exceed your current Money Achieved");
                    etMoneyWithdrawn.requestFocus();
                }
                else {
                    newCustomerMoney = customerMoney - moneyWithdrawn;
                    dbRef.child("moneyAchieved").setValue(newCustomerMoney);
                    tvCustomerNewMoneyOwned.setText(String.valueOf("Rp "+ newCustomerMoney+ " ,-"));
                    tvStaticNewBalanceData.setVisibility(View.VISIBLE);
                    tvStaticNewBalance.setVisibility(View.VISIBLE);
                    tvCustomerNewMoneyOwned.setVisibility(View.VISIBLE);
                    btnFinishTransfer.setVisibility(View.VISIBLE);
                    tvStaticMoneyWithdrawn.setVisibility(View.INVISIBLE);
                    etMoneyWithdrawn.setVisibility(View.INVISIBLE);
                    btnWithdrawn.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnFinishTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(TransferMoney2.this, MainActivity.class);
                intToMain.putExtra("intent", "transferMoney");
                intToMain.putExtra("adminEmail", adminEmail);
                intToMain.putExtra("adminPassword", adminPassword);
                startActivity(intToMain);
            }
        });
    }
}
