package com.sourcey.materiallogindemo.Betyg;

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
import com.sourcey.materiallogindemo.Kurser.ActivityKurser;
import com.sourcey.materiallogindemo.Schema.ActivitySchema;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class ActivityBetyg extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _email;
    private DrawerLayout drawer;
    private String email,password,klassName,fullPost,fullName;
    private  TextView result;
    private ArrayList<String> mKursNamn = new ArrayList<>();
    private ArrayList<String> mBorjar = new ArrayList<>();
    private ArrayList<String> mSlutar = new ArrayList<>();
    private ArrayList<String> mBetyg = new ArrayList<>();
    private ArrayList<String> mPoang = new ArrayList<>();
    private SharedPreferences spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(ActivityBetyg.this,
                R.style.AppTheme_Dark_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betyg);
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

        spl=this.getSharedPreferences("Login", MODE_PRIVATE);
        email    = spl.getString("Unm", null);
        password = spl.getString("Psw", null);
        fullPost = spl.getString("emailNack", null);
        fullName = spl.getString("fullname", null);
        klassName = spl.getString("klassName", null);

        _anvandarnamn.setText(fullName);
        _email.setText(fullPost);
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
                            .connect("https://nackademin.learnpoint.se/LoginForms/LoginForm.aspx?ReturnUrl=%2fLoginForms%2f")
                            .method(Connection.Method.GET)
                            .execute();

                    Document doc = loginForm.parse();
                    String __EVENTTARGET = doc.select("input[name=\"__EVENTTARGET\"]").val();
                    String __EVENTARGUMENT = doc.select("input[name=\"__EVENTARGUMENT\"]").val();
                    String __VIEWSTATE = doc.select("input[name=\"__VIEWSTATE\"]").val();
                    String __VIEWSTATEGENERATOR = doc.select("input[name=\"__VIEWSTATEGENERATOR\"]").val();
                    String ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtUserName = email;
                    String ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtPassword = password;
                    String ctl00$MainContentPlaceHolder$ctlUserLoginControl$btnLogin = "logga in";
                    String ctl00$siteScollPositionStore = "0";
                    Log.d("fff",__VIEWSTATE);
                    Connection.Response response = Jsoup.connect("https://nackademin.learnpoint.se/LoginForms/LoginForm.aspx?ReturnUrl=%2fdefault.aspx")
                            .data("__EVENTTARGET", __EVENTTARGET)
                            .data("__EVENTARGUMENT", __EVENTARGUMENT)
                            .data("__VIEWSTATE", __VIEWSTATE)
                            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
                            .data("ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtUserName", ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtUserName)
                            .data("ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtPassword", ctl00$MainContentPlaceHolder$ctlUserLoginControl$txtPassword)
                            .data("ctl00$MainContentPlaceHolder$ctlUserLoginControl$btnLogin", ctl00$MainContentPlaceHolder$ctlUserLoginControl$btnLogin)
                            .data("ctl00$siteScollPositionStore", ctl00$siteScollPositionStore)
                            .cookies(loginForm.cookies()).maxBodySize(0)
                            .method(Connection.Method.POST)
                            .execute();
                    Document doc2 = response.parse();
                    String myBetygLink = "https://nackademin.learnpoint.se"+doc2.select("a[title=\"Studieplan\"]").first().attr("href");
                    Log.d("ddddl", String.valueOf(myBetygLink));
                    Connection.Response BetygSidan = Jsoup.connect(myBetygLink)
                            .cookies(loginForm.cookies()).cookies(response.cookies()).maxBodySize(0)
                            .method(Connection.Method.GET)
                            .execute();

                    Document doc3 = BetygSidan.parse();
                    Elements mElementDataSize = doc3.select("div[class=\"trimmed-table__row\"]");
                    int mElementSize = mElementDataSize.size();
                    for (int i = 0; i < mElementSize; i++) {
                        Elements mElementKursNae = mElementDataSize.select("a").eq(i);
                        String mKursnamn = mElementKursNae.text();
                        Elements mElementStartar = mElementDataSize.get(i).select("span").eq(0);
                        String mKursstart = mElementStartar.text();

                        Elements mElementSlutar = mElementDataSize.get(i).select("span").eq(1);
                        String mKursSlut = mElementSlutar.text();

                        Elements mElementBetyg = mElementDataSize.get(i).select("span").eq(2);
                        String mKursBetyg = mElementBetyg.text();

                        Elements mElementPoang = mElementDataSize.get(i).select("span").eq(3);
                        String mKursPoang = mElementPoang.text();

                        mKursNamn.add(mKursnamn);
                        mBorjar.add(mKursstart);
                        mSlutar.add(mKursSlut);
                        mBetyg.add(mKursBetyg);
                        mPoang.add(mKursPoang);
                    }






                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
                        DataAdapter_betyg mDataAdapter = new DataAdapter_betyg(ActivityBetyg.this, mKursNamn, mBorjar,mSlutar,mBetyg,mPoang);
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
