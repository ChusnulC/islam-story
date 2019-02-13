package com.example.joyleap.islamstory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.joyleap.islamstory.helper.Config;
import com.example.joyleap.islamstory.helper.SqliteHelper;
import com.example.joyleap.islamstory.sidebar.ArchiveActivity;
import com.example.joyleap.islamstory.sidebar.CreateActivity;
import com.example.joyleap.islamstory.sidebar.EditActivity;
import com.example.joyleap.islamstory.sidebar.ProfileActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MaterialSearchView searchView;
    List<Integer> picture;
    List<String> category;
    List<String> title;

    ViewPager viewPager;
    TabLayout indicator;

    ListView list_static_news;
    ArrayList<HashMap<String, String>> arusStory;

    public static String story_id;

    //untuk edit
    public static String kategori, judul, deskripsi, isi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_static_news = findViewById(R.id.list_statis_news);
        arusStory = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Toast.makeText(getApplicationContext(), query ,
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        picture = new ArrayList<>();
        picture.add(Color.TRANSPARENT);
        picture.add(Color.TRANSPARENT);
        picture.add(Color.TRANSPARENT);

        category = new ArrayList<>();
        category.add("Top Story");
        category.add("Top Story");
        category.add("Top Story");

        title = new ArrayList<>();
        title.add("Kisah Unik Loper Koran Naik Haji Berkat Sedekah");
        title.add("Kisah Perjalanan Isra' Miraj Nabi Muhammad SAW");
        title.add("Sejarah Singkat Wali Songo");

        viewPager.setAdapter(new SliderAdapter(this, picture, category, title));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

    }

    @Override
    public void onResume(){
        super.onResume();
        //storyAdapter();
        _readMysql();
        _readMysql2();

    }

    private void _readMysql(){

        arusStory.clear();
        list_static_news.setAdapter(null);
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

    private void _readMysql2(){

        arusStory.clear();
        list_static_news.setAdapter(null);
        AndroidNetworking.post(Config.HOST+"read_news.php")
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

        //swipe_refres.setRefreshing(false);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arusStory, R.layout.list_static_news,//input to layout
                new String[] {"story_id", "kategori", "judul", "deskripsi", "isi"},
                new int[] {R.id.text_story_id, R.id.category, R.id.text_judul, R.id.text_deskripsi, R.id.text_isi});
        list_static_news.setAdapter(simpleAdapter);
        list_static_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                story_id = ((TextView)view.findViewById(R.id.text_story_id)).getText().toString();
                kategori = ((TextView)view.findViewById(R.id.category)).getText().toString();
                isi = ((TextView)view.findViewById(R.id.text_isi)).getText().toString();
                judul = ((TextView)view.findViewById(R.id.text_judul)).getText().toString();
                deskripsi = ((TextView)view.findViewById(R.id.text_deskripsi)).getText().toString();

                //ListMenu();
                startActivity(new Intent(MainActivity.this, Detail.class));
            }
        });

    }


    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < picture.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_archieve) {
            startActivity((new Intent(MainActivity.this, ArchiveActivity.class)));
        } else if (id == R.id.nav_write) {
            startActivity(new Intent(MainActivity.this, CreateActivity.class));
        } else if (id == R.id.profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
