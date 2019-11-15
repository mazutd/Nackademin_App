package com.sourcey.materiallogindemo.Contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;

public class DataAdapter_contacts extends RecyclerView.Adapter<DataAdapter_contacts.MyViewHolder> {

    private ArrayList<String> mKontaktNamnr = new ArrayList<>();
    private ArrayList<String> mKontaktTelnr = new ArrayList<>();
    private ArrayList<String> mkontaktEmail = new ArrayList<>();
    private ArrayList<String> mBetygt_his = new ArrayList<>();
    private ArrayList<String> mPoang_this = new ArrayList<>();
    private Activity mActivity;
    private Context paretContext;

    public DataAdapter_contacts(ActivityContacts activity, ArrayList<String> mKontaktNamnr, ArrayList<String> mKontaktTelnr, ArrayList<String> mkontaktEmail, ArrayList<String> mBetyg, ArrayList<String> mPoang) {
        this.mActivity = activity;
        this.mKontaktNamnr = mKontaktNamnr;
        this.mKontaktTelnr = mKontaktTelnr;
        this.mkontaktEmail = mkontaktEmail;
        this.mBetygt_his = mBetyg;
        this.mPoang_this = mPoang;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mKontaktNamn, mKontaktRole;
        private ImageView kurse_image;
        private Button callBtn, emailBtn;

        public MyViewHolder(View view) {
            super(view);
            mKontaktNamn = (TextView) view.findViewById(R.id.kontaktNamn);
            mKontaktRole = (TextView) view.findViewById(R.id.kontaktRole);
            callBtn = (Button) view.findViewById(R.id.ButtonRing);
            emailBtn = (Button) view.findViewById(R.id.ButtonMejla);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        paretContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.row_data_contact, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mKontaktNamn.setText(mKontaktNamnr.get(position));
        holder.mKontaktRole.setText(mKontaktTelnr.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(paretContext);
                LayoutInflater inflater = (LayoutInflater) paretContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.betyg_dialog_contact, null);
                TextView kontaktNamn = (TextView) view1.findViewById(R.id.kontaktNamnInfo);
                TextView kontaktEMail = (TextView) view1.findViewById(R.id.kontaktEmail);
                TextView kontaktTel = (TextView) view1.findViewById(R.id.kontaktTelnummer);
                Button callBtn1 = (Button) view1.findViewById(R.id.ButtonRing);
                Button emailBtn = (Button) view1.findViewById(R.id.ButtonMejla);
                kontaktNamn.setText(mKontaktNamnr.get(position));
                kontaktEMail.setText(mkontaktEmail.get(position));
                kontaktTel.setText(mKontaktTelnr.get(position));
                callBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("klickad", "klklklkl");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+mKontaktTelnr.get(position)+""));
                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                if (mActivity.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    Log.v("TAG","Permission is granted");
                                    return;
                                } else {

                                    Log.v("TAG","Permission is revoked");
                                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, 1);
                                    return;
                                }
                            }
                            else { //permission is automatically granted on sdk<23 upon installation
                                Log.v("TAG","Permission is granted");
                                return;
                            }
                        }
                        paretContext.startActivity(callIntent);
                    }
                });
                emailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto","email@email.com", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "HEJ");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        paretContext.startActivity(Intent.createChooser(intent, "Välj mejlklient :"));
                    }
                });


                builder.setView(view1);



                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mKontaktNamnr.size();
    }

}
