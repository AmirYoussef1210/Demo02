package com.example.demo02;

import static com.example.demo02.FBRef.refST;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FBStorage2 extends MasterClass {
    ImageView iV;
    private String fileName ;
    private static final int REQUEST_CAMERA_PERMISSION = 3699;
    private static final int REQUEST_IMAGE_CAPTURE = 367;
    private Uri capturedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbstorage2);

        iV = findViewById(R.id.iV2);
    }

    public void takePic(View view)
    {
        String filename = "img_" + System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imgFile = File.createTempFile(filename,".jpg",storageDir);

            capturedImageUri = FileProvider.getUriForFile(this, "com.example.demo02", imgFile);
            Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);

            if (takePicIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
            }
        } catch (IOException e) {
            Toast.makeText(FBStorage2.this,"Failed to create ",Toast.LENGTH_LONG).show();
        }
    }

    public void download1(View view)
    {
        if (fileName == null || fileName.isEmpty()) {
            Toast.makeText(this, "File name is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference refFile = refST.child("images/" + fileName);
        final long MAX_SIZE = 1024 * 1024 * 5; // 1MB
        refFile.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadImage(Uri imageUri) {
        fileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference refFile = refST.child("images/" + fileName);

        ProgressDialog pD = new ProgressDialog(this);
        pD.setTitle("Uploading...");
        pD.show();

        refFile.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pD.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pD.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        pD.setMessage("Uploaded " + (int) progress + " %");
                    }
                });
    }
}