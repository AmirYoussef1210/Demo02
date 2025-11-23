package com.example.demo02;

import static com.example.demo02.FBRef.refBase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RTDBList extends MasterClass {

    EditText eTId1;
    EditText eTText;
    ListView lV;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rtdblist);

        eTId1 = findViewById(R.id.eTId1);
        eTText = findViewById(R.id.eTText);
        lV = findViewById(R.id.lV);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        lV.setAdapter(arrayAdapter);


        readData();



    }

    public void readData() {
        refBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dS)
            {
                arrayList.clear();
                for (DataSnapshot Base : dS.getChildren()) {
                    String entry = Base.getKey() + " : " + Base.getValue();
                    arrayList.add(entry);
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError dbError) {
                Log.e("Screen_2/readData", "Error reading data: "+dbError);
            }
        });
    }

    public void send(View view) {
        String id = eTId1.getText().toString();
        String content = eTText.getText().toString();

        if(id.isEmpty() || content.isEmpty()){
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
        }
        else {
            refBase.child(id).setValue(content);
            readData();
        }
    }


}