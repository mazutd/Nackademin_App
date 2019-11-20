package com.sourcey.materiallogindemo.Kurser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Attendance.ActivityNarvaro;
import com.sourcey.materiallogindemo.Contacts.ActivityContacts;
import com.sourcey.materiallogindemo.Schema.ActivitySchema;
import com.sourcey.materiallogindemo.Betyg.ActivityBetyg;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;


public class ActivityKurser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _email;
    private DrawerLayout drawer;
    private String email,password, fullName,fullpost,klassName;
    private  TextView result;
    private ArrayList<String> mAuthorNameList = new ArrayList<>();
    private ArrayList<String> mBlogUploadDateList = new ArrayList<>();
    private ArrayList<String> mBlogTitleList = new ArrayList<>();
    private ArrayList<String> mBlogTitleListImage = new ArrayList<>();
    private SharedPreferences spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(ActivityKurser.this,
        R.style.AppTheme_Dark_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurse);
        Toolbar toolbaren = findViewById(R.id.toolbar);
        setSupportActionBar(toolbaren);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbaren,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        email = getIntent().getStringExtra("EXTRA_SESSION_USERNAME");
        password = getIntent().getStringExtra("EXTRA_SESSION_PASSWORD");

        result = (TextView) findViewById(R.id.result);
        _email = (TextView) headerView.findViewById(R.id.username_email);
        _anvandarnamn = (TextView) headerView.findViewById(R.id.username_menu);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWebsite();


        spl=this.getSharedPreferences("Login", MODE_PRIVATE);
        email    = spl.getString("Unm", null);
        password = spl.getString("Psw", null);
        fullpost = spl.getString("emailNack", null);
        fullName = spl.getString("fullname", null);
        klassName = spl.getString("klassName", null);




        _anvandarnamn.setText(fullName);
        _email.setText(fullpost);
        if (email == null){
           Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Hämtar data...");
            progressDialog.show();
            Log.d("email", email);

        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (VALIDATER == true){
                            progressDialog.dismiss();
                        }
                    }
                }, 6000);



    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_message:
                Intent intent_main = new Intent(this, MainActivity.class);
                startActivity(intent_main);
                break;
            case R.id.nav_chat:
                break;
            case R.id.nav_betyg:
                Intent intent_betyg = new Intent(this, ActivityBetyg.class);
                startActivity(intent_betyg);
                break;
            case R.id.nav_schema:
                Intent intent_schema = new Intent(this, ActivitySchema.class);

                startActivity(intent_schema);
                break;
            case R.id.nav_contacts:
                Intent intent_contacts = new Intent(this, ActivityContacts.class);

                startActivity(intent_contacts);
                break;
            case R.id.nav_narvaro:
                Intent intent_narvaro = new Intent(this, ActivityNarvaro.class);
                startActivity(intent_narvaro);
                break;
            case R.id.nav_share:
                SharedPreferences.Editor editor = spl.edit();
                editor.clear().commit();
                Intent intent_login = new Intent(this,LoginActivity.class);
                startActivity(intent_login);

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Connection.Response loginForm = Jsoup
                            .connect("https://studentportal.nackademin.se/login/index.php")
                            .method(Connection.Method.GET)
                            .execute();

                    Document doc = loginForm.parse();
                    String csrf = doc.select("input[name=logintoken]").val();
                    Connection.Response response = Jsoup.connect("https://studentportal.nackademin.se/login/index.php")
                            .data("anchor", "")
                            .data("logintoken", csrf)
                            .data("username", email)
                            .data("password", password)
                            .cookies(loginForm.cookies()).maxBodySize(0)
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc2 = response.parse();
                    String sesskey1 = doc2.select("input[name=\"sesskey\"]").first().val();
                    String responsess= Jsoup.connect("https://studentportal.nackademin.se/lib/ajax/service.php?sesskey="+sesskey1+"&info=core_course_get_enrolled_courses_by_timeline_classification").timeout(60000).ignoreContentType(true)
                            .method(Connection.Method.POST)
                            .requestBody("[{\"index\":0,\"methodname\":\"core_course_get_enrolled_courses_by_timeline_classification\",\"args\":{\"offset\":0,\"limit\":24,\"classification\":\"all\",\"sort\":\"fullname\"}}]")
                            .cookies(response.cookies()).maxBodySize(0)
                            .execute()
                            .body();
                    JSONArray newJArray = new JSONArray(responsess);
                    newJArray.getJSONObject(0).getJSONObject("data");
                    newJArray = newJArray.getJSONObject(0).getJSONObject("data").getJSONArray("courses");
                    for (int i=0; i < newJArray.length(); i++) {
                        newJArray.getJSONObject(i);
                        String mAuthorName = newJArray.getJSONObject(i).getString("startdate");
                        String mBlogUploadDate = newJArray.getJSONObject(i).getString("coursecategory");
                        String mBlogTitle =  newJArray.getJSONObject(i).getString("fullname");
                        String mBlogImage =  newJArray.getJSONObject(i).getString("courseimage");
                        mAuthorNameList.add(mAuthorName);
                        mBlogUploadDateList.add(mBlogUploadDate);
                        mBlogTitleList.add(mBlogTitle);
                        mBlogTitleListImage.add(mBlogImage);

                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
                        DataAdapter_kurser mDataAdapter = new DataAdapter_kurser(ActivityKurser.this, mBlogTitleList, mAuthorNameList, mBlogUploadDateList,mBlogTitleListImage);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mDataAdapter);
                        VALIDATER = true;

                    }
                });
            }
        }).start();
    }





}
