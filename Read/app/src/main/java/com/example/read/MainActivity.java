package com.example.read;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    Button snapImg, detectText, detectFace;
    ImageView cameraImg;
    TextView showText;
    Bitmap bitmap;
    String text;
    private TextToSpeech mtts;
    int txtC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snapImg = findViewById(R.id.snap);
        detectText = findViewById(R.id.detecttext);
        cameraImg = findViewById(R.id.imageCamera);
        showText = findViewById(R.id.textview);
        detectFace = findViewById(R.id.detectface);

        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = mtts.setLanguage(Locale.ENGLISH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("MTTP", "Language Not Supported");
                    }else {
                        detectFace.setEnabled(true);
                        detectText.setEnabled(true);
                    }

                }else {
                    Log.e("MTTP", "Initialization failed");
                }
            }
        });


        snapImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextDetect();
            }
        });

        detectFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceDetect();
            }
        });

    }
    @Override
    protected void onDestroy() {
        if(mtts != null){
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }

    public void FaceDetect() {
        if(bitmap == null)
        {
            Toast.makeText(this,"No image found",Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder().build();

            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector();
            detector.detectInImage(firebaseVisionImage)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                            Process_Face(firebaseVisionFaces);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void Process_Face(List<FirebaseVisionFace> firebaseVisionFaces) {
        int count = 0;
        for(FirebaseVisionFace face: firebaseVisionFaces){
            count++;
        }
        showText.setTextSize(20);
        String faces;
        if(count <= 1)
        {
            faces = count + " face found.";
        }
        else {
            faces = count + " faces found.";
        }
        showText.setText(faces);
        Speech(faces);
    }

    private void Speech(String text) {
        mtts.setPitch(1);
        mtts.setSpeechRate(1);
        mtts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void TextDetect() {
        if(bitmap == null)
        {
            Toast.makeText(this,"No image found",Toast.LENGTH_SHORT).show();
        }
        else{
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
            firebaseVisionTextDetector.detectInImage(firebaseVisionImage)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            Process_Text(firebaseVisionText);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    
                }
            });
        }
    }

    private void Process_Text(FirebaseVisionText firebaseVisionText) {
       /* List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
        if(blocks.size()==0){
            Toast.makeText(this,"No text Deteted",Toast.LENGTH_SHORT).show();
        }
        else{
            for(FirebaseVisionText.Block block: firebaseVisionText.getBlocks()){
                String text = block.getText();
                showText.setTextSize(20);
                showText.setText(text);
            }
        }*/
       txtC = 0;
        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(this,"No text Deteted",Toast.LENGTH_SHORT).show();
            txtC = 1;
           // return;
        }
        text = "";
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    text = text + " " + elements.get(k).getText();
                }
                text = text + "\n";
            }
        }
        showText.setTextSize(20);
        showText.setText(text);
        if(txtC == 1)
        {
            text = "No text found.";
        }
        Speech(text);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = (Bitmap) data.getExtras().get("data");
        cameraImg.setImageBitmap(bitmap);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
