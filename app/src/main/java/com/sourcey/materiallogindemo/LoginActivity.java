package com.sourcey.materiallogindemo;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.NFC.TagTechList;
import com.sourcey.materiallogindemo.NFC.TagWrapper;
import com.sourcey.materiallogindemo.NFC.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static boolean VALIDATER = false;
    static private ArrayList<TagWrapper> tags = new ArrayList<TagWrapper>();
    static private int currentTagIndex = -1;

    private NfcAdapter adapter = null;
    private PendingIntent pendingIntent = null;

    private TextView currentTagView,scranTag;
    private EditText username_input,password_input;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        currentTagView = (TextView) findViewById(R.id.currentTagView);
        scranTag = (TextView) findViewById(R.id.scranTag);
        scranTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTagView.setText("Placera skoltaggen på backsidan av din mobil...");
                scranTag.setText("");
                ScanTag();

            }
        });


        adapter = NfcAdapter.getDefaultAdapter(this);

    }




    public void ScanTag() {
        super.onResume();

        if (!adapter.isEnabled()) {
            Utils.showNfcSettingsDialog(this);
            return;
        }

        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        }

        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }









    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Discovered tag with intent " + intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = Utils.bytesToHex(tag.getId());
        TagWrapper tagWrapper = new TagWrapper(tagId);

        ArrayList<String> misc = new ArrayList<String>();
        misc.add("scanned at: " + Utils.now());

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        String tagData = "";

        if (rawMsgs != null) {

            NdefMessage msg = (NdefMessage) rawMsgs[0];
            NdefRecord cardRecord = msg.getRecords()[0];
            try {
                tagData = readRecord(cardRecord.getPayload());
            } catch (UnsupportedEncodingException e) {
                Log.e("TagScan", e.getMessage());
                return;
            }
        }
        Log.d("tagdatttaa",tagData);
        misc.add("tag data: " + tagData);

        String[] Infoary = tagData.replace("{","").replace("}","").replace("'","").split(",");
        username_input = (EditText)findViewById(R.id.input_email);
        password_input = (EditText)findViewById(R.id.input_password);
        username_input.setText(Infoary[0], TextView.BufferType.EDITABLE);
        password_input.setText(Infoary[1], TextView.BufferType.EDITABLE);

        currentTagView.setText(Infoary[3]+" ?");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        }, 4000);
        tagWrapper.techList.put("Misc", misc);

        for (String tech : tag.getTechList()) {
            tech = tech.replace("android.nfc.tech.", "");
            List<String> info = getTagInfo(tag, tech);
            tagWrapper.techList.put("Technology: " + tech, info);
        }

        if (tags.size() == 1) {
        }

        tags.add(tagWrapper);
        currentTagIndex = tags.size() - 1;
        showTag();
    }



    String readRecord(byte[] payload) throws UnsupportedEncodingException {
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 63;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }







    private void showTag() {
        if (tags.size() == 0) return;

        final TagWrapper tagWrapper = tags.get(currentTagIndex);
        final TagTechList techList = tagWrapper.techList;
        final ArrayList<String> expandableListTitle = new ArrayList<String>(techList.keySet());

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final List<String> getTagInfo(final Tag tag, final String tech) {
        List<String> info = new ArrayList<String>();

        switch (tech) {
            case "NfcA":
                info.add("aka ISO 14443-3A");

                NfcA nfcATag = NfcA.get(tag);
                info.add("atqa: " + Utils.bytesToHexAndString(nfcATag.getAtqa()));
                info.add("sak: " + nfcATag.getSak());
                info.add("maxTransceiveLength: " + nfcATag.getMaxTransceiveLength());
                break;

            case "NfcF":
                info.add("aka JIS 6319-4");

                NfcF nfcFTag = NfcF.get(tag);
                info.add("manufacturer: " + Utils.bytesToHex(nfcFTag.getManufacturer()));
                info.add("systemCode: " + Utils.bytesToHex(nfcFTag.getSystemCode()));
                info.add("maxTransceiveLength: " + nfcFTag.getMaxTransceiveLength());
                break;

            case "NfcV":
                info.add("aka ISO 15693");

                NfcV nfcVTag = NfcV.get(tag);
                info.add("dsfId: " + nfcVTag.getDsfId());
                info.add("responseFlags: " + nfcVTag.getResponseFlags());
                info.add("maxTransceiveLength: " + nfcVTag.getMaxTransceiveLength());
                break;

            case "Ndef":
                Ndef ndefTag = Ndef.get(tag);
                NdefMessage ndefMessage = null;

                try {
                    ndefTag.connect();
                    ndefMessage = ndefTag.getNdefMessage();
                    ndefTag.close();

                    for (final NdefRecord record : ndefMessage.getRecords()) {
                        final String id = record.getId().length == 0 ? "null" : Utils.bytesToHex(record.getId());
                        info.add("record[" + id + "].tnf: " + record.getTnf());
                        info.add("record[" + id + "].type: " + Utils.bytesToHexAndString(record.getType()));
                        info.add("record[" + id + "].payload: " + Utils.bytesToHexAndString(record.getPayload()));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        info.add("messageSize: " + ndefMessage.getByteArrayLength());
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    info.add("error reading message: " + e.toString());
                }

                HashMap<String, String> typeMap = new HashMap<String, String>();
                typeMap.put(Ndef.NFC_FORUM_TYPE_1, "typically Innovision Topaz");
                typeMap.put(Ndef.NFC_FORUM_TYPE_2, "typically NXP MIFARE Ultralight");
                typeMap.put(Ndef.NFC_FORUM_TYPE_3, "typically Sony Felica");
                typeMap.put(Ndef.NFC_FORUM_TYPE_4, "typically NXP MIFARE Desfire");

                String type = ndefTag.getType();
                if (typeMap.get(type) != null) {
                    type += " (" + typeMap.get(type) + ")";
                }
                info.add("type: " + type);

                info.add("canMakeReadOnly: " + ndefTag.canMakeReadOnly());
                info.add("isWritable: " + ndefTag.isWritable());
                info.add("maxSize: " + ndefTag.getMaxSize());
                break;

            case "NdefFormatable":
                info.add("nothing to read");

                break;

            case "MifareUltralight":
                MifareUltralight mifareUltralightTag = MifareUltralight.get(tag);
                info.add("type: " + mifareUltralightTag.getType());
                info.add("tiemout: " + mifareUltralightTag.getTimeout());
                info.add("maxTransceiveLength: " + mifareUltralightTag.getMaxTransceiveLength());
                break;

            case "IsoDep":
                info.add("aka ISO 14443-4");

                IsoDep isoDepTag = IsoDep.get(tag);
                info.add("historicalBytes: " + Utils.bytesToHexAndString(isoDepTag.getHistoricalBytes()));
                info.add("hiLayerResponse: " + Utils.bytesToHexAndString(isoDepTag.getHiLayerResponse()));
                info.add("timeout: " + isoDepTag.getTimeout());
                info.add("extendedLengthApduSupported: " + isoDepTag.isExtendedLengthApduSupported());
                info.add("maxTransceiveLength: " + isoDepTag.getMaxTransceiveLength());
                break;

            default:
                info.add("unknown tech!");
        }

        return info;
    }















    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loggar in...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.
        getWebsite();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.d("statusss", String.valueOf(VALIDATER));
                        if (VALIDATER == true){
                            onLoginSuccess();
                        }else{
                            onLoginFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 6000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the ActivityContacts
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        String email1 = _emailText.getText().toString();
        String password1 = _passwordText.getText().toString();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("EXTRA_SESSION_USERNAME", email1);
        intent.putExtra("EXTRA_SESSION_PASSWORD", password1);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                String email = _emailText.getText().toString();
                String password = _passwordText.getText().toString();
                Log.d("statusName",email);
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
                    Log.d("num", String.valueOf(doc2.select("a[title=\"Studieplan\"]").size()));
                    if (doc2.select("a[title=\"Studieplan\"]").size() > 0) {
                        VALIDATER=true;
                        Log.d("fddd", "NULLLLLLLL");
                    }else
                    {
                        VALIDATER=false;
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        result.setText(builder.toString());

                    }
                });
            }
        }).start();
    }
}
