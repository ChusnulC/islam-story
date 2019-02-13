package com.example.joyleap.islamstory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.joyleap.islamstory.helper.Config;
import com.example.joyleap.islamstory.sidebar.ArchiveActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Detail extends AppCompatActivity {

    ListView list_news;
    ArrayList<HashMap<String, String>> arusStory;
    Button btnFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        list_news = findViewById(R.id.list_news);
        arusStory = new ArrayList<>();

//        btnFull = (Button)findViewById(R.id.btnFull);
//        btnFull.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"sfsdfsdfsdf",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

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
        _readMysql2();
    }

    private void _readMysql(){

        AndroidNetworking.post(Config.HOST+"detail.php")
                .addBodyParameter("story_id", MainActivity.story_id)
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

    private void _readMysql2(){

        AndroidNetworking.post(Config.HOST+"detail_news.php")
                .addBodyParameter("story_id", MainActivity.story_id)
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

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arusStory, R.layout.list_news,//input to layout
                new String[] {"story_id", "kategori", "judul", "isi"},
                new int[] {R.id.text_story_id, R.id.category, R.id.title, R.id.text_isi});
        list_news.setAdapter(simpleAdapter);

//
//        btnFull = (Button)findViewById(R.id.btnFull);
//        btnFull.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"sfsdfsdfsdf",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }
}
