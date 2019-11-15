package com.sourcey.materiallogindemo.Betyg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;

public class DataAdapter_betyg extends RecyclerView.Adapter<DataAdapter_betyg.MyViewHolder> {

    private ArrayList<String> mKursnman_this = new ArrayList<>();
    private ArrayList<String> mBorjar_this = new ArrayList<>();
    private ArrayList<String> mSlutarthis = new ArrayList<>();
    private ArrayList<String> mBetygt_his = new ArrayList<>();
    private ArrayList<String> mPoang_this = new ArrayList<>();
    private Activity mActivity;
    private int lastPosition = -1;
    private Context paretContext;

    public DataAdapter_betyg(ActivityBetyg activity, ArrayList<String> mKursnamn, ArrayList<String> mBorjar, ArrayList<String> mSLutar, ArrayList<String> mBetyg, ArrayList<String> mPoang) {
        this.mActivity = activity;
        this.mKursnman_this = mKursnamn;
        this.mBorjar_this = mBorjar;
        this.mSlutarthis = mSLutar;
        this.mBetygt_his = mBetyg;
        this.mPoang_this = mPoang;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mKursnamn_act, mBorjar_act, mBetyg_act;
        private ImageView  kurse_image;
        public MyViewHolder(View view) {
            super(view);
            mKursnamn_act = (TextView) view.findViewById(R.id.mKursnman);
            mBorjar_act = (TextView) view.findViewById(R.id.mBorjar);
            mBetyg_act = (TextView) view.findViewById(R.id.mBetyg);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        paretContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.row_data_betyg, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.mKursnamn_act.setText(mKursnman_this.get(position));
        holder.mBorjar_act.setText(mBorjar_this.get(position));
        holder.mBetyg_act.setText(mBetygt_his.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(paretContext);
                LayoutInflater inflater = (LayoutInflater) paretContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View view1 = inflater.inflate(R.layout.betyg_dialog,null);
                TextView kursNamn = (TextView) view1.findViewById(R.id.kurceName);
                TextView borjar_dialog = (TextView) view1.findViewById(R.id.borjar_dialog);
                TextView slutar_dialog = (TextView) view1.findViewById(R.id.slutar_dialog);
                TextView betyg_dialog = (TextView) view1.findViewById(R.id.betyg_dialog);
                TextView poang_dialog = (TextView) view1.findViewById(R.id.poang_dialog);
                kursNamn.setText(mKursnman_this.get(position));

                borjar_dialog.setText("Börjar : "+"             "+mBorjar_this.get(position));
                slutar_dialog.setText("Slutar : "+"             "+mSlutarthis.get(position));
                betyg_dialog.setText("Betyg : "+"             "+mBetygt_his.get(position));
                poang_dialog.setText("Poäng : "+"             "+mPoang_this.get(position));

                builder.setView(view1);

                builder.setPositiveButton("STÄNG", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Add ok operation here
                    }
                });

                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mKursnman_this.size();
    }
}