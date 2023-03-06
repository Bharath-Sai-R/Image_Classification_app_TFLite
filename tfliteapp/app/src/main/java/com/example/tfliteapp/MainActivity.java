package com.example.tfliteapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfliteapp.ml.BirdsModel;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);

        picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    public void classifyImage(Bitmap img)
         {
             try {
                 BirdsModel model = BirdsModel.newInstance(getApplicationContext());

                 // Creates inputs for reference.
                 TensorImage image = TensorImage.fromBitmap(img);

                 // Runs model inference and gets result.
                 BirdsModel.Outputs outputs = model.process(image);
                 List<Category> probability = outputs.getProbabilityAsCategoryList();

                 // Releases model resources if no longer used.
                 model.close();
             } catch (IOException e) {
                 // TODO Handle the exception
             }
         }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
             Bitmap image=(Bitmap) data.getExtras().get("data");
             int dimension= Math.min(image.getWidth(),image.getHeight());
             image=ThumbnailUtils.extractThumbnail(image,dimension,dimension);
             imageView.setImageBitmap(image);

             image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
             classifyImage(image);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}