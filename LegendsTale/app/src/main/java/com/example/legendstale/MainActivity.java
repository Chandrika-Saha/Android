package com.example.legendstale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MainActivity extends AppCompatActivity implements UserViewHolder.OnItemClickListener {

    RecyclerView re;
    TextView name;
    SearchView searchView;

    private UserViewHolder mAdaptor;
    DatabaseReference dbRef;

    private List<Update> mUpload;
    private List<Update> mUploadnew;
    ArrayList<String> namelist = new ArrayList<>();
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseApp.initializeApp(MainActivity.this);

        namelist = new ArrayList<>();

        re = findViewById(R.id.listview);
        name = findViewById(R.id.name);
        searchView = findViewById(R.id.simpleSearchView);
        re.setHasFixedSize(true);
        re.setLayoutManager(new LinearLayoutManager(this));

        mUpload = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Legends");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUpload.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Update up = postSnapshot.getValue(Update.class);
                    mUpload.add(up);
                    namelist.add(up.getName());
                }
                mAdaptor = new UserViewHolder(MainActivity.this,mUpload);
                mAdaptor.setOnItemClickListener(MainActivity.this);
                re.setAdapter(mAdaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        //searchFirebase();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFirebase(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFirebase(newText);
                return false;
            }
        });

        re.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowSingleData.class);
                int pos = re.getChildLayoutPosition(v);
                intent.putExtra("message", namelist.get(pos));
                startActivityForResult(intent, 1);
            }
        });

        }

    private void searchFirebase(String s) {

        final String news = s;
        mUploadnew = new ArrayList<>();
        namelist = new ArrayList<>();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploadnew.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Update up = postSnapshot.getValue(Update.class);
                    if(up.getName().toLowerCase().trim().contains(news.toLowerCase().trim())) {
                        mUploadnew.add(up);
                        namelist.add(up.getName());
                    }
                }
                UserViewHolder mAdaptornew = new UserViewHolder(MainActivity.this,mUploadnew);
                mAdaptornew.setOnItemClickListener(MainActivity.this);
                re.setAdapter(mAdaptornew);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, ShowSingleData.class);
        //int pos = re.getChildLayoutPosition(v);
        intent.putExtra("message", namelist.get(position));
        startActivityForResult(intent, 1);
    }
}




