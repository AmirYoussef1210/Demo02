package com.example.demo02;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBRef
{
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();

    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference refBase = FBDB.getReference("Base");

    public static FirebaseStorage FBST = FirebaseStorage.getInstance();
    public static StorageReference refST = FBST.getReference();



}

