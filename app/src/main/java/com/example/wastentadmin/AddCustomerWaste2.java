package com.example.wastentadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class AddCustomerWaste2 extends AppCompatActivity {
    TextView tvCustomerName, tvCustomerWaste, tvStaticWasteAdded, tvStaticWasteCollected, tvNewCustomerWaste, tvStaticMoneyAchieved, tvMoneyAchieved;
    EditText etWasteAdded;
    Button btnAddWaste, btnFinish;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference dbRef, wastePerKgRef;
    int customerWasteCollected, newWasteCollected, customerMoneyAchieved, newMoneyAchieved, moneyPerKilo;
    String adminEmail, adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_waste2);

        Intent intAdminLogin = getIntent();
        adminEmail = intAdminLogin.getStringExtra("adminEmail");
        adminPassword = intAdminLogin.getStringExtra("adminPassword");

        tvCustomerName = findViewById(R.id.customer_name);
        tvCustomerWaste = findViewById(R.id.customer_wasteCollected);
        tvStaticWasteAdded = findViewById(R.id.staticWasteAdded);
        tvStaticWasteCollected = findViewById(R.id.staticNewWasteCollected);
        tvNewCustomerWaste = findViewById(R.id.customer_newWaste);
        etWasteAdded = findViewById(R.id.waste_added);
        btnAddWaste = findViewById(R.id.buttonAddWaste);
        btnFinish = findViewById(R.id.buttonFinish);
        tvStaticMoneyAchieved = findViewById(R.id.staticMoneyAchieved);
        tvMoneyAchieved = findViewById(R.id.money_achieved);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        tvStaticWasteAdded.setVisibility(View.INVISIBLE);
        tvStaticWasteCollected.setVisibility(View.INVISIBLE);
        tvNewCustomerWaste.setVisibility(View.INVISIBLE);
        tvStaticMoneyAchieved.setVisibility(View.INVISIBLE);
        tvMoneyAchieved.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.INVISIBLE);

        dbRef = mFirebaseDatabase.getReference().child("users").child(mFirebaseUser.getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String customerName = dataSnapshot.child("name").getValue().toString();
                customerWasteCollected = Integer.valueOf(dataSnapshot.child("wasteCollected").getValue().toString());
                customerMoneyAchieved = Integer.valueOf(dataSnapshot.child("moneyAchieved").getValue().toString());
                tvCustomerName.setText(customerName);
                tvCustomerWaste.setText(String.valueOf(customerWasteCollected) + " kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddCustomerWaste2.this, "The Credential you give is not a user account", Toast.LENGTH_SHORT).show();
                Intent intToBack = new Intent(AddCustomerWaste2.this, AddCustomerWaste.class);
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

        btnAddWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wasteAdded = Integer.valueOf(etWasteAdded.getText().toString());
                if (wasteAdded == 0) {
                    etWasteAdded.setError("Please Enter Waste");
                    etWasteAdded.requestFocus();
                }
                else {
                    newWasteCollected = customerWasteCollected + wasteAdded;
                    newMoneyAchieved = customerMoneyAchieved + wasteAdded * moneyPerKilo;
                    dbRef.child("wasteCollected").setValue(newWasteCollected);
                    dbRef.child("moneyAchieved").setValue(newMoneyAchieved);
                    tvStaticWasteAdded.setVisibility(View.VISIBLE);
                    tvStaticWasteCollected.setVisibility(View.VISIBLE);
                    tvNewCustomerWaste.setVisibility(View.VISIBLE);
                    tvStaticMoneyAchieved.setVisibility(View.VISIBLE);
                    tvMoneyAchieved.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.VISIBLE);
                    btnAddWaste.setVisibility(View.INVISIBLE);
                    etWasteAdded.setVisibility(View.INVISIBLE);
                    tvStaticWasteAdded.setVisibility(View.INVISIBLE);
                    tvNewCustomerWaste.setText(String.valueOf(newWasteCollected + " kg"));
                    tvMoneyAchieved.setText(String.valueOf("Rp "+ newMoneyAchieved + " ,-"));
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(AddCustomerWaste2.this, MainActivity.class);
                intToMain.putExtra("intent", "addCustomerWaste");
                intToMain.putExtra("adminEmail", adminEmail);
                intToMain.putExtra("adminPassword", adminPassword);
                startActivity(intToMain);
            }
        });
    }
}
