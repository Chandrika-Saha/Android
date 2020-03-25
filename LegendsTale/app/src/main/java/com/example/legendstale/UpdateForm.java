package com.example.legendstale;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UpdateForm extends AppCompatActivity
{

    Button out,up,img_button;
    ProgressBar bar;
    EditText name,description, twt,web,contact;
    Uri img_uri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference mDbref;
    private StorageReference mStorRef;
    private StorageTask mUptask;
    FirebaseAuth mAuth;
    //Firebase mDbref;
    //FirebaseStorage mStorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateform);
        out = findViewById(R.id.out);
        up = findViewById(R.id.update);
        name = findViewById(R.id.nm);
        description = findViewById(R.id.ds);
        twt = findViewById(R.id.tt);
        web = findViewById(R.id.wb);
        img_button = findViewById(R.id.img);
        bar = findViewById(R.id.pro);
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
        contact = findViewById(R.id.con);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("UpdateForm.this", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("UpdateForm.this", "signInAnonymously:failure", task.getException());
                            Toast.makeText(UpdateForm.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

        mStorRef = FirebaseStorage.getInstance().getReference("Legends");
        //mStorRef = new FirebaseStorage("gs://legendstale-bfc7e.appspot.com/Legends");
        mDbref = FirebaseDatabase.getInstance().getReference("Legends");



        out.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UpdateForm.this, MainActivity.class);
                startActivity(intent);
            }
        });

        up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mUptask!=null && mUptask.isInProgress()){
                    Toast.makeText(UpdateForm.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }
                UploadAll();
            }
        });

        img_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                OpenFileChooser();
            }
        });

        }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void UploadAll()
    {
        if(img_uri!=null){
            final StorageReference fileReference = mStorRef.child(System.currentTimeMillis()
            +"."+getFileExtension(img_uri));
            fileReference.putFile(img_uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateForm.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    bar.setProgress((int) progress);

                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(@NonNull Uri downloadUri) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(0);
                        }
                    }, 250);
                    Update update = new Update(name.getText().toString().trim(),
                            description.getText().toString().trim(),
                            twt.getText().toString().trim(),
                            web.getText().toString().trim(),
                            downloadUri.toString(),
                            contact.getText().toString().trim());
                    String upId = mDbref.push().getKey();
                    mDbref.child(upId).setValue(update);

                }
            });

        } else {
            Toast.makeText(this, "No file Selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void OpenFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
        && data!=null && data.getData()!=null){
            img_uri = data.getData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }
}
