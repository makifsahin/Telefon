package com.example.telefon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MesajActivity extends AppCompatActivity {
    RadioButton rb1,rb2,rb3;
    Button btn;
    EditText coklu;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        btn = (Button) findViewById(R.id.button);
        coklu = (EditText) findViewById(R.id.editTextTextMultiLine);

        rb1.setChecked(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coklu.getText().toString().trim().isEmpty()){

                    Database db = new Database(getApplicationContext());
                    String grupId = "1";
                    if(rb2.isChecked()){
                        grupId ="2";
                    }else if(rb3.isChecked()){
                        grupId= "3";
                    }
                    String numaralar = db.numaralarGrup(grupId);
                    if(numaralar != ""){
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + Uri.encode(numaralar)));
                        intent.putExtra("sms_body", coklu.getText().toString().trim());
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "Grupda numara yok!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Mesaj boş bırakılamaz", Toast.LENGTH_SHORT).show();
                }




            }
        });
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });
        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });
        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                }
            }
        });


    }
}
