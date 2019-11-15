package com.sourcey.materiallogindemo.Contacts;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Attendance.ActivityNarvaro;
import com.sourcey.materiallogindemo.Betyg.ActivityBetyg;
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


public class ActivityContacts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _email;
    private DrawerLayout drawer;
    private String email,password,klassName,fullPost,fullName;
    private  TextView result;
    private ArrayList<String> mKontaktNamn = new ArrayList<>();
    private ArrayList<String> mKontaktTelnr = new ArrayList<>();
    private ArrayList<String> mEmial = new ArrayList<>();
    private ArrayList<String> mBetyg = new ArrayList<>();
    private ArrayList<String> mPoang = new ArrayList<>();
    private ArrayList<String> ContactList = new ArrayList<>();
    private DataAdapter_contacts contactAdapter;
    private RecyclerView.LayoutManager mLayoutMang;
    private EditText searchDiteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(ActivityContacts.this,
                R.style.AppTheme_Dark_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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

        email = getIntent().getStringExtra("EXTRA_SESSION_USERNAME");
        password = getIntent().getStringExtra("EXTRA_SESSION_PASSWORD");
        fullPost= getIntent().getStringExtra("EXTRA_SESSION_EMAIL");
        fullName = getIntent().getStringExtra("EXTRA_SESSION_FULLNAME");
        klassName = getIntent().getStringExtra("EXTRA_SESSION_KLASSNAME");
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

        searchDiteText = (EditText) findViewById(R.id.searchText);
        searchDiteText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterContact(editable.toString());
            }
        });

    }
    private void filterContact(String text){
        ArrayList mKursNamn1 = new ArrayList<>();
        Log.d("kdks","kksksk"+ mKontaktNamn.size());
        for (String item : mKontaktNamn){
            if(item.toLowerCase().contains(text.toLowerCase())){
                mKursNamn1.add(item);
            }

        }

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
        DataAdapter_contacts mDataAdapter = new DataAdapter_contacts(ActivityContacts.this, mKursNamn1, mKontaktTelnr, mEmial,mBetyg,mPoang);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mDataAdapter);
        VALIDATER = true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_message:
                Intent intent_main = new Intent(this, MainActivity.class);
                intent_main.putExtra("EXTRA_SESSION_USERNAME", email);
                intent_main.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent_main.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent_main.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent_main.putExtra("EXTRA_SESSION_KLASSNAME",klassName);
                startActivity(intent_main);
                break;
            case R.id.nav_chat:
                Intent intent = new Intent(this, ActivityKurser.class);
                intent.putExtra("EXTRA_SESSION_USERNAME", email);
                intent.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent.putExtra("EXTRA_SESSION_KLASSNAME",klassName);
                startActivity(intent);
                break;
            case R.id.nav_betyg:
                Intent intent_betyg = new Intent(this, ActivityBetyg.class);
                intent_betyg.putExtra("EXTRA_SESSION_USERNAME", email);
                intent_betyg.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent_betyg.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent_betyg.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent_betyg.putExtra("EXTRA_SESSION_KLASSNAME",klassName);

                startActivity(intent_betyg);
                break;
            case R.id.nav_schema:
                Intent intent_schema = new Intent(this, ActivitySchema.class);
                intent_schema.putExtra("EXTRA_SESSION_USERNAME", email);
                intent_schema.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent_schema.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent_schema.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent_schema.putExtra("EXTRA_SESSION_KLASSNAME",klassName);

                startActivity(intent_schema);
                break;
            case R.id.nav_contacts:
                break;
            case R.id.nav_narvaro:
                Intent intent_narvaro = new Intent(this, ActivityNarvaro.class);
                intent_narvaro.putExtra("EXTRA_SESSION_USERNAME", email);
                intent_narvaro.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent_narvaro.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent_narvaro.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent_narvaro.putExtra("EXTRA_SESSION_KLASSNAME",klassName);
                startActivity(intent_narvaro);
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

                    //Connection.Response BetygSidan = Jsoup.connect("https://nackademin.learnpoint.se/GroupForms/Group_Staff.aspx?Id=3570")
                    Connection.Response BetygSidan = Jsoup.connect("https://nackademin.learnpoint.se/GroupForms/Group_Staff.aspx?Id=1")
                            .cookies(loginForm.cookies()).cookies(response.cookies()).maxBodySize(0)
                            .method(Connection.Method.GET)
                            .execute();

                    Document doc3 = BetygSidan.parse();
                    Elements mElementDataSize = doc3.select("div[class=\"trimmed-table__row\"]");
                    int mElementSize = mElementDataSize.size();
                    for (int i = 0; i < mElementSize ; i++) {
                        String linkTel = "ctl00_MainContentPlaceHolder_ctlStaffInGroupControl_rptStaff_ctl"+String.format("%02d", i)+"_htmlPhoneContainer";
                        String linkEmail = "ctl00_MainContentPlaceHolder_ctlStaffInGroupControl_rptStaff_ctl"+String.format("%02d", i)+"_htmlWorkEmailContainer";

                        Elements mElementKursNae = mElementDataSize.select("a[class=\"staff-in-group__staff-name-link\"]").eq(i);
                        String mKursnamn = mElementKursNae.text();
                        Elements mElementKursInfo = mElementDataSize.select("div[class=\"staff-in-group__staff-roles\"]").eq(i);
                        String info = mElementKursInfo.text();
                        Log.d("rol",info);

                        String mElementKursImage = mElementDataSize.select("div[class=\"staff-in-group__staff-image-panel\"]").eq(i).select("img").attr("src");
                        String Image = mElementKursImage;

                        Elements mElementKursTelnr = mElementDataSize.select("div[id='"+linkTel+"']");
                        String telnr = mElementKursTelnr.text();
                        Log.d("teltel",telnr);
                        Elements mElementKursEmail = mElementDataSize.select("div[id='"+linkEmail+"']");
                        String workEmail = mElementKursEmail.text();
                        Log.d("teltel",workEmail);

                        mKontaktNamn.add(mKursnamn);
                        mKontaktTelnr.add(telnr);
                        mEmial.add(workEmail);
                        mBetyg.add(mKursnamn);
                        mPoang.add(mKursnamn);

                    }






                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.act_recyclerview);
                        DataAdapter_contacts mDataAdapter = new DataAdapter_contacts(ActivityContacts.this, mKontaktNamn, mKontaktTelnr, mEmial,mBetyg,mPoang);
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
