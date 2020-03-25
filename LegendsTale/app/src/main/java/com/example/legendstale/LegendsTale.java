package com.example.legendstale;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LegendsTale extends Application {

   // FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

       // mAuth = FirebaseAuth.getInstance();
       // if (user != null) {
            // do your stuff
       // } else {
            //signInAnonymously();
        //}

    }



}
