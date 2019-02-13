package com.example.joyleap.islamstory.sidebar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ArchiveActivity extends AppCompatActivity {

    SqliteHelper sqliteHelper;
    Cursor cursor;

    SwipeRefreshLayout swipe_refres;
    ListView list_story;
    ArrayList<HashMap<String, String>> arusStory;

    public static String story_id;

    //untuk edit
    public static String kategori, judul, deskripsi, isi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        list_story = findViewById(R.id.list_story);
        arusStory = new ArrayList<>();
        swipe_refres = findViewById(R.id.swipe_refres);

        swipe_refres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                _readAdapter();
                //storyAdapter();
            }
        });

        getSupportActionBar().setTitle("Daftar Tulisan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        //storyAdapter();
        _readMysql();
    }

    private void _readMysql(){

        arusStory.clear();
        list_story.setAdapter(null);
        AndroidNetworking.post(Config.HOST+"read.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        try{

                            JSONArray jsonArray = response.getJSONArray("hasil");
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                HashMap<String, String> map = new HashMap<>();
                                map.put("story_id", jsonObject.getString("story_id"));
                                map.put("kategori", jsonObject.getString("kategori"));
                                map.put("judul", jsonObject.getString("judul"));
                                map.put("deskripsi", jsonObject.getString("deskripsi"));
                                map.put("isi", jsonObject.getString("isi"));
                                arusStory.add(map);
                            }
                            _readAdapter();

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

    private void _readAdapter(){

        swipe_refres.setRefreshing(false);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arusStory, R.layout.list_story,//input to layout
                new String[] {"story_id", "kategori", "judul", "deskripsi", "isi"},
                new int[] {R.id.text_story_id, R.id.category, R.id.text_judul, R.id.text_deskripsi, R.id.text_isi});
        list_story.setAdapter(simpleAdapter);
        list_story.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                story_id = ((TextView)view.findViewById(R.id.text_story_id)).getText().toString();
                kategori = ((TextView)view.findViewById(R.id.category)).getText().toString();
                isi = ((TextView)view.findViewById(R.id.text_isi)).getText().toString();
                judul = ((TextView)view.findViewById(R.id.text_judul)).getText().toString();
                deskripsi = ((TextView)view.findViewById(R.id.text_deskripsi)).getText().toString();

                ListMenu();
            }
        });

    }

    private void _delMysql(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Yakin untuk menghapus data ini ?");
        builder.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        AndroidNetworking.post(Config.HOST + "delete.php")
                                .addBodyParameter("story_id", story_id)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // do anything with response

                                        try{
                                            if(response.getString("response").equals("success")){
                                                Toast.makeText(getApplicationContext(), "Data berhasil dihapus",
                                                        Toast.LENGTH_LONG).show();
                                                _readMysql();
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
                });
        builder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void ListMenu(){
        final Dialog dialog = new Dialog(ArchiveActivity.this);
        dialog.setContentView(R.layout.list_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        RippleView rip_hapus = dialog.findViewById(R.id.rip_hapus);
        RippleView rip_edit = dialog.findViewById(R.id.rip_edit);

        rip_edit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                startActivity(new Intent(ArchiveActivity.this, EditActivity.class));
            }
        });

        rip_hapus.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                dialog.dismiss();
                _delMysql();
                //hapus();
            }
        });

        dialog.show();
    }
}
