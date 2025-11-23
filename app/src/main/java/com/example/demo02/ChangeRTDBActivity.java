package com.example.demo02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeRTDBActivity extends MasterClass {

    TextView tempText;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rtdbactivity);



        tempText = findViewById(R.id.textView4);

        ref = FirebaseDatabase.getInstance().getReference("app/counter");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer value = snapshot.getValue(Integer.class);
                if (value != null) {
                    tempText.setText("Temp: " + value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tempText.setText("Error.");
            }
        });

    }
}
