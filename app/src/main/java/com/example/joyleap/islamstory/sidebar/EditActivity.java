package com.example.joyleap.islamstory.sidebar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.joyleap.islamstory.MainActivity;
import com.example.joyleap.islamstory.R;
import com.example.joyleap.islamstory.helper.Config;
import com.example.joyleap.islamstory.helper.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {

    ArchiveActivity Arch = new ArchiveActivity();

    RadioGroup radio_kategori;
    RadioButton radio_sejarah,radio_kisah, radio_sedekah;
    EditText edit_judul, edit_deskripsi, edit_isi;
    Button btn_simpan;
    RippleView rip_simpan;

    String kategori;

    Cursor cursor;

    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        radio_kategori = findViewById(R.id.radio_kategori);

        radio_sejarah = findViewById(R.id.radio_sejarah);
        radio_kisah = findViewById(R.id.radio_kisah);
        radio_sedekah = findViewById(R.id.radio_sedekah);

        edit_judul = findViewById(R.id.edit_judul);
        edit_deskripsi = findViewById(R.id.edit_deskripsi);
        edit_isi = findViewById(R.id.edit_isi);

        btn_simpan = findViewById(R.id.btn_simpan);

        rip_simpan = findViewById(R.id.rip_simpan);

        _editMysql();

        getSupportActionBar().setTitle("Edit Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void _editMysql(){

        kategori = Arch.kategori;
        switch (kategori){
            case "SEJARAH":radio_sedekah.setChecked(true);
                break;
            case "SEDEKAH":radio_sedekah.setChecked(true);
                break;
            case "KISAH INSPIRATIF":radio_kisah.setChecked(true);
                break;
        }

        radio_kategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_sejarah:kategori = "SEJARAH";
                        break;
                    case R.id.radio_kisah:kategori = "KISAH INSPIRATIF";
                        break;
                    case R.id.radio_sedekah:kategori = "SEDEKAH";
                        break;
                }
            }
        });


        //story_id : 0, kategory : 1, judul : 2, deskripsi : 3, isi : 4
        edit_judul.setText(Arch.judul);
        edit_deskripsi.setText(Arch.deskripsi);
        edit_isi.setText(Arch.isi);


        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(kategori.equals("")||edit_judul.getText().toString().equals("")||edit_deskripsi.getText().toString().equals("")||edit_isi.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar!!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    AndroidNetworking.post(Config.HOST + "update.php")
                            .addBodyParameter("story_id", Arch.story_id)
                            .addBodyParameter("kategori", kategori)
                            .addBodyParameter("judul", edit_judul.getText().toString())
                            .addBodyParameter("deskripsi", edit_deskripsi.getText().toString())
                            .addBodyParameter("isi", edit_isi.getText().toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response

                                    try{
                                        if(response.getString("response").equals("success")){
                                            Toast.makeText(getApplicationContext(), "Tulisan berhasil diupdate!",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                            Toast.makeText(getApplicationContext(), response.getString("response"),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                }
                            });

                }
            }
        });
    }

    private void _editSQLite(){
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM story WHERE story_id='" + ArchiveActivity.story_id + "'",
                null);
        cursor.moveToFirst();
        kategori = cursor.getString(1);
        switch (kategori){
            case "SEJARAH":radio_sedekah.setChecked(true);
                break;
            case "SEDEKAH":radio_sedekah.setChecked(true);
                break;
            case "KISAH INSPIRATIF":radio_kisah.setChecked(true);
                break;
        }

        radio_kategori.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_sejarah:kategori = "SEJARAH";
                        break;
                    case R.id.radio_kisah:kategori = "KISAH INSPIRATIF";
                        break;
                    case R.id.radio_sedekah:kategori = "SEDEKAH";
                        break;
                }
            }
        });


        //story_id : 0, kategory : 1, judul : 2, deskripsi : 3, isi : 4
        edit_judul.setText(cursor.getString(2));
        edit_deskripsi.setText(cursor.getString(3));
        edit_isi.setText(cursor.getString(4));

        getSupportActionBar().setTitle("Edit Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(kategori.equals("")||edit_judul.getText().toString().equals("")||edit_deskripsi.getText().toString().equals("")||edit_isi.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar!!",
                            Toast.LENGTH_SHORT).show();
                }else {
                    SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                    database.execSQL("UPDATE story SET kategori='" + kategori + "', judul='" + edit_judul.getText().toString() +
                            "', deskripsi='" + edit_deskripsi.getText().toString() +"', isi='" + edit_isi.getText().toString() +
                            "' WHERE story_id='" + ArchiveActivity.story_id +"' ");

                    Toast.makeText(getApplicationContext(), "Data berhasil disimpan!",
                            Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
