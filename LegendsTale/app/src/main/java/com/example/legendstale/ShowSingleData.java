package com.example.legendstale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowSingleData extends AppCompatActivity {

    TextView name,des;
    Button tw,web,phn, text;
    ImageView imageView;
    DatabaseHelper myDb;
    String  n,d,t,w,c;

    private UserViewHolder mAdaptor;
    DatabaseReference dbRef;

    private List<Update> mUpload;
    private List<Update> mUploadnew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.showsingle);
        name = findViewById(R.id.name);
        des = findViewById(R.id.des);
        tw = findViewById(R.id.twt);
        web = findViewById(R.id.web);
        imageView = findViewById(R.id.img);
        mUpload = new ArrayList<>();
        phn = findViewById(R.id.phn);
        text = findViewById(R.id.text_no);

        tw.setBackground(ContextCompat.getDrawable(this, R.drawable.twitter1));


        myDb = new DatabaseHelper(this);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("message").trim();
        Log.d("ShowSingleData.this",message);


        dbRef = FirebaseDatabase.getInstance().getReference("Legends");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mUpload.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Update up = postSnapshot.getValue(Update.class);
                    //mUpload.add(up);
                    if(up.getName().toLowerCase().trim().contains(message.toLowerCase().trim())) {
                        name.setText(up.getName());
                        des.setText(up.getDescription());
                        //tw.setMovementMethod(LinkMovementMethod.getInstance());
                        //tw.setText(up.getTwiter());
                        t = up.getTwiter();
                        w = up.getWeb();
                        c = up.getContact();
                        //web.setMovementMethod(LinkMovementMethod.getInstance());
                        //web.setText(up.getWeb());
                        Picasso.with(ShowSingleData.this).load(up.getImgUrl()).into(imageView);
                        break;
                    }
                }
                mAdaptor = new UserViewHolder(ShowSingleData.this,mUpload);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShowSingleData.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse1 = new Intent(Intent.ACTION_VIEW,Uri.parse(t.toString()));
                startActivity(browse1);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browse = new Intent(Intent.ACTION_VIEW,Uri.parse(w.toString()));
                startActivity(browse);
            }
        });
        phn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+c.toString()));//change the number
                startActivity(callIntent);

            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", c.toString(), null)));

            }
        });




    }


}

