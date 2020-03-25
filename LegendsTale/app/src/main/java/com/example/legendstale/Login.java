package com.example.legendstale;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText mail,pass;
    Button login;
    private Firebase mRef;
    private static int auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mail = findViewById(R.id.mail);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);

        FirebaseApp.initializeApp(this);

        mRef = new Firebase("https://legendstale-bfc7e.firebaseio.com/");

        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = firebaseDatabase.getReference("Admin");


        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mRef.addValueEventListener(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        com.firebase.client.DataSnapshot adminData = dataSnapshot.child("Admin");
                        Iterable<com.firebase.client.DataSnapshot> admin = adminData.getChildren();

                        for(DataSnapshot ad : admin){
                            Admin a = ad.getValue(Admin.class);
                            Log.d("admin:  ",a.getEmail()+"  "+a.getPassword());
                            if(a.getEmail().matches(mail.getText().toString()) && a.getPassword().matches(pass.getText().toString())){
                                auth = 1;
                                Intent intent = new Intent(Login.this, UpdateForm.class);
                                startActivity(intent);

                            }
                            else{
                                auth = 0;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
               // Log.d("auth: ",String.valueOf(auth));
                if(auth == 0){

                  // Toast.makeText(Login.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
