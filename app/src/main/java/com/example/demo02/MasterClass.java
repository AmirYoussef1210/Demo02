package com.example.demo02;


import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MasterClass extends AppCompatActivity
{
    private static ChargerHelper chhelp;
    private static boolean isRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (chhelp == null) {
            chhelp = new ChargerHelper();
        }
    }

    public void startreceiver() {
        if (!isRegistered) {
            IntentFilter chargerFilter = new IntentFilter();
            chargerFilter.addAction(Intent.ACTION_POWER_CONNECTED);
            chargerFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

            getApplicationContext().registerReceiver(chhelp, chargerFilter);
            isRegistered = true;
        }
    }

    public void stopreceiver() {
        if (chhelp != null && isRegistered) {
            getApplicationContext().unregisterReceiver(chhelp);
            isRegistered = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ChargerAct) {
            Intent intent = new Intent(this, ChargerActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.notifAct) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.gemini_act) {
            Intent intent = new Intent(this, PicToGemini_activity.class);
            startActivity(intent);
        }
        else if(id == R.id.tempAct) {
            Intent intent = new Intent(this, ChangeRTDBActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.AuthAct) {
            Intent intent = new Intent(this, FBauthActivity.class);
            startActivity(intent);

        } else if (id == R.id.listAct){
            Intent intent = new Intent(this, RTDBList.class);
            startActivity(intent);
        } else if (id == R.id.StorageAct) {
            Intent intent = new Intent(this, FBStorage.class);
            startActivity(intent);

        }
        else if(id == R.id.FBStorage2ACt){
            Intent intent = new Intent(this, FBStorage2.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
