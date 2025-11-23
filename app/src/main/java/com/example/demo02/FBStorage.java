package com.example.demo02;

import static com.example.demo02.FBRef.refST;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FBStorage extends MasterClass {

    ImageView iV;

    private static final int REQUEST_PICK_IMAGE = 369;

    private String fileName ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbstorage);

        iV = findViewById(R.id.iV2);

    }

    public void download(View view)
    {
        if (fileName == null || fileName.isEmpty()) {
            Toast.makeText(this, "File name is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference refFile = refST.child("images/" + fileName);
        final long MAX_SIZE = 1024 * 1024*5; // 1MB
        refFile.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iV.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FBStorage.this, "Download Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void choose(View view)
    {
        Intent si = new Intent(Intent.ACTION_PICK, android. provider.MediaStore. Images.Media. EXTERNAL_CONTENT_URI);
        startActivityForResult(si, REQUEST_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImage(imageUri);

        }

    }

    private void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference reFfile = refST.child("images/" + fileName);
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading ... ");
            pd.show();

            reFfile.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask. TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask. TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(FBStorage.this, "Upload successful", Toast.LENGTH_SHORT) .show();
                        }
                    })
                    .addOnFailureListener (new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            pd.dismiss();
                            Toast.makeText(FBStorage. this, "Upload failed", Toast.LENGTH_SHORT) .show();
                        }

                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Uploaded " + ((int) progress) + "%");
                        }
                    });

        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }



}