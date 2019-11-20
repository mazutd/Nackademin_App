package com.sourcey.materiallogindemo.Attendance;

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

import com.sourcey.materiallogindemo.Betyg.ActivityBetyg;
import com.sourcey.materiallogindemo.Contacts.ActivityContacts;
import com.sourcey.materiallogindemo.Kurser.ActivityKurser;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Schema.ActivitySchema;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class ActivityNarvaro extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _email;
    private DrawerLayout drawer;
    private String email,password, fullName,fullpost,klassName;
    private  TextView result;
    private ArrayList<String> mKursNamn = new ArrayList<>();
    private ArrayList<String> mKursprosent = new ArrayList<>();
    private ArrayList<String> mKursAv = new ArrayList<>();
    private SharedPreferences spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(ActivityNarvaro.this,
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
        result = (TextView) findViewById(R.id.result);
        _anvandarnamn = (TextView) headerView.findViewById(R.id.username_menu);
        _email = (TextView) headerView.findViewById(R.id.username_email);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWebsite();



        spl = this.getSharedPreferences("Login", MODE_PRIVATE);

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
                Intent intent = new Intent(this, ActivityKurser.class);
                startActivity(intent);
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
                    Connection.Response responsess= Jsoup.connect("https://studentportal.nackademin.se/mod/attendance/view.php?id=61566&mode=1").timeout(60000).ignoreContentType(true)
                            .method(Connection.Method.GET)
                            .cookies(response.cookies()).maxBodySize(0)
                            .execute();
                    Elements narvaroTabel = responsess.parse().select("table[class=\"generaltable\"]").select("tr[class=\"\"]");

                    for (int i=0; i < narvaroTabel.size(); i++) {
                        String mKursNamn_string = narvaroTabel.get(i).select("td[class=\"colcourse cell c0\"]").select("a").text();
                        String mKursNarvaroProsent_string = narvaroTabel.get(i).select("td[class=\"colpercentagesessionscompleted cell c4 lastcol\"]").text();
                        String mKursNarvaroAv_string = narvaroTabel.get(i).select("td[class=\"colpointssessionscompleted cell c3\"]").text();

                        mKursNamn.add(mKursNamn_string);
                        mKursprosent.add(mKursNarvaroProsent_string);
                        mKursAv.add(mKursNarvaroAv_string);


                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
                        DataAdapter_Narvaro mDataAdapter = new DataAdapter_Narvaro(ActivityNarvaro.this, mKursNamn,mKursprosent,mKursAv);
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
