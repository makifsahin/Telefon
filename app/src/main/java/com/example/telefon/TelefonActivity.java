package com.example.telefon;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TelefonActivity extends AppCompatActivity {
    RadioButton rb1,rb2,rb3;
    Button btn;
    ListView rehber_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefon);
        rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MesajActivity.class);
                startActivity(intent);
            }
        });
        rb1.setChecked(true);

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
        rehber_list=findViewById(R.id.rehber_list);

            ArrayList<Kisiler> kisiler = new ArrayList<Kisiler>();

            Cursor telefonun_rehberi = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,  null, null);
            while (telefonun_rehberi.moveToNext())
            {
                @SuppressLint("Range") String isim = telefonun_rehberi.getString(telefonun_rehberi.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                @SuppressLint("Range") String numara = telefonun_rehberi.getString(telefonun_rehberi.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                @SuppressLint("Range") String contactID=telefonun_rehberi.getString(telefonun_rehberi.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                Kisiler r_nesnesi = new Kisiler();
                r_nesnesi.set_isim(isim);
                r_nesnesi.set_numara(numara);
                r_nesnesi.setResim(ContactPhoto(contactID));
                kisiler.add(r_nesnesi);
            }

            telefonun_rehberi.close();
            KisilerAdapter kisilerAdapter = new KisilerAdapter(this, kisiler);
            if (rehber_list != null) {
                rehber_list.setAdapter(kisilerAdapter);
                rehber_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {
                        // DB'de kayıt varmı diye kontrol edilir yoksa eklenir
                        // DB'de kayıt varsa GRUP update edilir
                        Database dbs = new Database(getApplicationContext());
                        dbs.numaraSil(kisiler.get(index).get_numara());

                        if(rb1.isChecked()){
                            //GRUP_1'e ekle
                            Database db = new Database(getApplicationContext());
                            String numara = kisiler.get(index).get_numara();
                            Boolean varMi = db.numaraVarmi(numara);
                            if(varMi){
                                db.numaraSil(kisiler.get(index).get_numara());
                            }
                            Boolean numarakEKle = db.numaraEkle(kisiler.get(index).get_numara(),kisiler.get(index).get_isim(),"1");
                            if(numarakEKle){
                                Toast.makeText(getApplicationContext(), "GRUP 1'e Eklendi", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Ekleme işleminde hata!", Toast.LENGTH_SHORT).show();
                            }
                        }else if(rb2.isChecked()){
                            //GRUP_2'e ekle
                            Database db = new Database(getApplicationContext());
                            String numara = kisiler.get(index).get_numara();
                            Boolean varMi = db.numaraVarmi(numara);
                            if(varMi){
                                db.numaraSil(kisiler.get(index).get_numara());
                            }
                            Boolean numarakEKle = db.numaraEkle(kisiler.get(index).get_numara(),kisiler.get(index).get_isim(),"2");
                            if(numarakEKle){
                                Toast.makeText(getApplicationContext(), "GRUP 2'ye Eklendi", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Ekleme işleminde hata!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //GRUP_3'e ekle
                            Database db = new Database(getApplicationContext());
                            String numara = kisiler.get(index).get_numara();
                            Boolean varMi = db.numaraVarmi(numara);
                            if(varMi){
                                db.numaraSil(kisiler.get(index).get_numara());
                            }
                            Boolean numarakEKle = db.numaraEkle(kisiler.get(index).get_numara(),kisiler.get(index).get_isim(),"3");
                            if(numarakEKle){
                                Toast.makeText(getApplicationContext(), "GRUP 3'e Eklendi", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Ekleme işleminde hata!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        return false;
                    }
                });

            }
        }





    public Bitmap ContactPhoto(String contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId));
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            byte[] data = cursor.getBlob(0);
            if (data != null)
                return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
            else
                return null;
        }
        cursor.close();
        return null;
    }

}
