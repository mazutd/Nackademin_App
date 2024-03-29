package com.sourcey.materiallogindemo.Schema;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Attendance.ActivityNarvaro;
import com.sourcey.materiallogindemo.Betyg.ActivityBetyg;
import com.sourcey.materiallogindemo.Contacts.ActivityContacts;
import com.sourcey.materiallogindemo.Kurser.ActivityKurser;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;


public class ActivitySchema extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static boolean VALIDATER = false;
    private  TextView _anvandarnamn, _email;
    private DrawerLayout drawer;
    private String email,password,webViewUrl,searchKlass,fullPost,fullName,klassName;
    private  TextView result;
    private ArrayList<String> mAuthorNameList = new ArrayList<>();
    private ArrayList<String> mBlogUploadDateList = new ArrayList<>();
    private ArrayList<String> mBlogTitleList = new ArrayList<>();
    private ArrayList<String> mBlogTitleListImage = new ArrayList<>();
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(ActivitySchema.this,
                R.style.AppTheme_Dark_Dialog);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schema);
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
        _anvandarnamn = (TextView) headerView.findViewById(R.id.username_menu);
        _email = (TextView) headerView.findViewById(R.id.username_email);
        webView = (WebView) findViewById(R.id.webView);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        getWebsite();


        email = getIntent().getStringExtra("EXTRA_SESSION_USERNAME");
        password = getIntent().getStringExtra("EXTRA_SESSION_PASSWORD");
        fullPost = getIntent().getStringExtra("EXTRA_SESSION_EMAIL");
        fullName = getIntent().getStringExtra("EXTRA_SESSION_FULLNAME");
        klassName = getIntent().getStringExtra("EXTRA_SESSION_KLASSNAME");
        Log.d("KLASSSSSS",klassName);
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
            case R.id.nav_schema:

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
            case R.id.nav_contacts:
                Intent intent_contacts = new Intent(this, ActivityContacts.class);
                intent_contacts.putExtra("EXTRA_SESSION_USERNAME", email);
                intent_contacts.putExtra("EXTRA_SESSION_PASSWORD", password);
                intent_contacts.putExtra("EXTRA_SESSION_EMAIL",fullPost);
                intent_contacts.putExtra("EXTRA_SESSION_FULLNAME",fullName);
                intent_contacts.putExtra("EXTRA_SESSION_KLASSNAME",klassName);

                startActivity(intent_contacts);
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
                            .connect("https://cloud.timeedit.net/nackademin/web/1/objects.html?max=15&fr=t&partajax=t&im=f&sid=12&l=sv_SE&search_text="+klassName+"&types=6")
                            .method(Connection.Method.GET)
                            .execute();
                    Document doc1 = loginForm.parse();
                    searchKlass = doc1.select("div[id=\"objectbasketitemX0\"]").first().attr("data-id");
                    //String divKlass = doc1.select("div[id=\"objectbasketitemX0\"]").first().attr("data-idonly");
                    Log.d("KLASSIDDD", String.valueOf( searchKlass));




                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        webViewUrl = "https://cloud.timeedit.net/nackademin/web/1/ri.html?h=f&sid=12&p=0.m%2C20191202.x&objects="+searchKlass+"&ox=0&types=0&fe=0&cch=f1%2C5-13&h2=2";
                        webView.loadUrl(webViewUrl);
                        webView.setWebViewClient(new WebViewClient());
                        WebSettings webbSett = webView.getSettings();
                        webbSett.setJavaScriptEnabled(true);
                        VALIDATER = true;
                    }
                });
            }
        }).start();
    }





}
