package com.sourcey.materiallogindemo;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import com.sourcey.materiallogindemo.Attendance.ActivityNarvaro;
import com.sourcey.materiallogindemo.Betyg.ActivityBetyg;
import com.sourcey.materiallogindemo.Contacts.ActivityContacts;
import com.sourcey.materiallogindemo.Kurser.ActivityKurser;
import com.sourcey.materiallogindemo.Schema.ActivitySchema;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _personnummer, _post1, _post2, _mobil,_adress,_anvandarnamn_menu,_email_menu,_klassNamn,_mark;
    private DrawerLayout drawer;
    private String email,password,klassNamn;
    private String fullPost,fullName;

    private SharedPreferences spl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbaren = findViewById(R.id.toolbar);
        setSupportActionBar(toolbaren);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        _anvandarnamn_menu= (TextView) headerView.findViewById(R.id.username_menu);
        _email_menu  = (TextView) headerView.findViewById(R.id.username_email);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbaren,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        _anvandarnamn = (TextView) findViewById(R.id.tv_name);
        _personnummer = (TextView) findViewById(R.id.tv_personnummer);
        _post1 = (TextView) findViewById(R.id.tv_epost1);
        _post2 = (TextView) findViewById(R.id.tv_epost2);
        _mobil = (TextView) findViewById(R.id.tv_mobil);
        _adress = (TextView) findViewById(R.id.tv_adress);
        _klassNamn = (TextView) findViewById(R.id.klassNamn);
        _mark = (TextView) findViewById(R.id.mark);
        spl=this.getSharedPreferences("Login", MODE_PRIVATE);

        email    = spl.getString("Unm", null);
        password = spl.getString("Psw", null);
        fullPost = spl.getString("emailNack", null);
        fullName = spl.getString("fullname", null);
        klassNamn = spl.getString("klassName", null);


        if (email == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Hämtar data...");
            progressDialog.show();
            getWebsite();

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
                Intent intent_narvaro = new Intent(this, ActivityNarvaro.class);
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
                String anvandarNamn = null;
                String anvandarFullNamn = null;
                String persoNnr = null;
                String post1 = null;
                String post2 = null;
                String mobil = null;
                String adress = null;
                String KlassNamn = null;


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
                    String link = doc2.select("a[class=\"user-name\"]").first().attr("href");
                    String[] parts = link.split("\\?id=");
                    Log.d("dd", String.valueOf(parts));
                    String myBetygLink = "https://nackademin.learnpoint.se/"+doc2.select("a[class=\"user-name\"]").first().attr("href");
                    Elements klassIDel = doc2.select("li[class=\"category\"]").select("ul").select("li").select("a");
                    String[] str = myBetygLink.split("id=");
                    String klassLink = "https://nackademin.learnpoint.se/"+klassIDel.get(1).attr("href");
                    String[] klassLinkID = klassLink.split("Id=");
                    String KlassInfoLink = "https://nackademin.learnpoint.se/GroupForms/Group_About.aspx?Id="+klassLinkID[1];

                    Log.d("IDIDI",KlassInfoLink);


                    Connection.Response BetygSidan = Jsoup.connect(myBetygLink)
                            .cookies(loginForm.cookies()).cookies(response.cookies()).maxBodySize(0)
                            .method(Connection.Method.GET)
                            .execute();



                    Document doc3 = BetygSidan.parse();
                    Connection.Response KlassIno = Jsoup.connect(KlassInfoLink)
                            .cookies(loginForm.cookies()).cookies(response.cookies()).maxBodySize(0)
                            .method(Connection.Method.GET)
                            .execute();
                    Document doc4 = KlassIno.parse();
                    KlassNamn = doc4.select("div[class=\"group-information__attribute\"]").first().select("div[class=\"group-information__attribute-value\"]").text();

                    anvandarFullNamn = doc3.select("span[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblName\"]").text();
                    anvandarNamn = doc3.select("a[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblUsername\"]").text();
                    persoNnr     = doc3.select("span[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblSSN\"]").text();
                    post1        = doc3.select("a[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblEmail\"]").text();
                    post2        = doc3.select("a[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblEmailPrivate\"]").text();
                    mobil        = doc3.select("a[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblMobilePhone\"]").text();
                    adress       = doc3.select("span[id=\"ctl00_MainContentPlaceHolder_ctlStudentInformation_lblLegalAddress\"]").text();

                    SharedPreferences.Editor Ed=spl.edit();
                    Ed.putString("emailNack",post1 );
                    Ed.putString("fullname",anvandarFullNamn );
                    Ed.putString("klassName",KlassNamn);
                    Ed.commit();
                    VALIDATER = true;

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                final String finalAnvandarNamn = anvandarNamn;
                final String finalAnvandarNamnFull = anvandarFullNamn;
                final String finalPersonnummer = persoNnr;
                final String finalPost1 = post1;
                final String finalPost2 = post2;
                final String finalMobil = mobil;
                final String finaladress = adress;
                final String finalKlassName = KlassNamn;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _anvandarnamn.setText(finalAnvandarNamnFull);
                        _personnummer.setText(finalPersonnummer);
                        _post1.setText(finalPost1);
                        _post2.setText(finalPost2);
                        _mobil.setText(finalMobil);
                        _adress.setText(finaladress);
                        _anvandarnamn_menu.setText(finalAnvandarNamnFull);
                        _email_menu.setText(finalPost1);
                        _klassNamn.setText(finalKlassName);
                        String[] myName = finalAnvandarNamnFull.split(" ");
                        String initials = "";
                        for (int i = 0; i <= 1; i++) {
                            String s = myName[i];
                            initials+=s.charAt(0);
                        }
                        _mark.setText(initials);



                    }
                });
            }
        }).start();
    }


}
