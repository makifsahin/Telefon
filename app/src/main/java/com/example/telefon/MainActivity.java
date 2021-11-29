package com.example.telefon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    EditText etKadi,etOkulNo;
    final Handler handler = new Handler();
    Timer timer = new Timer(false);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etKadi = (EditText) findViewById(R.id.editTextIsim);
        etOkulNo = (EditText) findViewById(R.id.editTextOkulNo);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_CONTACTS)
                                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED) {
                            if (!etKadi.getText().toString().isEmpty() && etOkulNo.getText().toString().length() == 12) {
                                timer.cancel();
                                Intent intent = new Intent(getApplicationContext(), TelefonActivity.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "İzinleri kabul etmeniz gerekli!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS},
                    1);
        }

    }

}
