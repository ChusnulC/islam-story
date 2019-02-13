package com.example.joyleap.islamstory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.joyleap.islamstory.helper.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Full extends AppCompatActivity {

    ListView list_full_news;
    ArrayList<HashMap<String, String>> arusStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_full);
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

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arusStory, R.layout.list_full_news,//input to layout
                new String[] {"story_id", "kategori", "judul", "isi"},
                new int[] {R.id.text_story_id, R.id.category, R.id.title, R.id.text_isi});
        list_full_news.setAdapter(simpleAdapter);

    }
}
