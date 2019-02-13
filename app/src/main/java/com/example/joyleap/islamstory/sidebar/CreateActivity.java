package com.example.joyleap.islamstory.sidebar;

import android.database.sqlite.SQLiteDatabase;
import android.renderscript.RenderScript;
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
import com.example.joyleap.islamstory.R;
import com.example.joyleap.islamstory.helper.Config;
import com.example.joyleap.islamstory.helper.SqliteHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateActivity extends AppCompatActivity {
    RadioGroup radio_kategori;
    RadioButton radio_sejarah,radio_kisah, radio_sedekah;
    EditText edit_judul, edit_deskripsi, edit_isi;
    Button btn_simpan;
    RippleView rip_simpan;

    String kategori;

    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        kategori ="";

        sqliteHelper = new SqliteHelper(this);

        radio_kategori = findViewById(R.id.radio_kategori);

        radio_sejarah = findViewById(R.id.radio_sejarah);
        radio_kisah = findViewById(R.id.radio_kisah);
        radio_sedekah = findViewById(R.id.radio_sedekah);

        edit_judul = findViewById(R.id.edit_judul);
        edit_deskripsi = findViewById(R.id.edit_deskripsi);
        edit_isi = findViewById(R.id.edit_isi);

        btn_simpan = findViewById(R.id.btn_simpan);

        rip_simpan = findViewById(R.id.rip_simpan);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code ya
            }
        });

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

        rip_simpan.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if(kategori.equals("")||edit_judul.getText().toString().equals("")||edit_deskripsi.getText().toString().equals("")||edit_isi.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar!!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    _createMysql();
                }

            }
        });

        getSupportActionBar().setTitle("Tulis Berita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void _createMysql(){

        AndroidNetworking.post(Config.HOST + "create.php")
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
                                Toast.makeText(getApplicationContext(), "Tulisan berhasil disimpan!",
                                        Toast.LENGTH_LONG).show();
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
