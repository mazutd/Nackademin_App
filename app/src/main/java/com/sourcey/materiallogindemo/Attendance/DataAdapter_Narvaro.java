package com.sourcey.materiallogindemo.Attendance;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sourcey.materiallogindemo.R;

import java.util.ArrayList;

public class DataAdapter_Narvaro extends RecyclerView.Adapter<DataAdapter_Narvaro.MyViewHolder> {

    private ArrayList<String> mKursnamn = new ArrayList<>();
    private ArrayList<String> mKursprosent = new ArrayList<>();
    private ArrayList<String> mKursAv = new ArrayList<>();
    private ArrayList<String> mBlogUploadDateListImage = new ArrayList<>();
    private Activity mActivity;
    private int lastPosition = -1;

    public DataAdapter_Narvaro(ActivityNarvaro activity, ArrayList<String> mKursnamn,ArrayList<String> mKursprosent,ArrayList<String> mKursAv) {
        this.mActivity = activity;
        this.mKursnamn = mKursnamn;
        this.mKursprosent = mKursprosent;
        this.mKursAv = mKursAv;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_kursNamn, tv_kursprosent, tv_mKursAv;
        public MyViewHolder(View view) {
            super(view);
            tv_kursNamn = (TextView) view.findViewById(R.id.row_tv_Kursnamn);
            tv_kursprosent = (TextView) view.findViewById(R.id.row_tv_KursProsent);
            tv_mKursAv = (TextView) view.findViewById(R.id.row_tv_KursAv);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data_narvaro, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_kursNamn.setText(mKursnamn.get(position));
        holder.tv_kursprosent.setText(mKursprosent.get(position));
        holder.tv_mKursAv.setText(mKursAv.get(position));



    }

    @Override
    public int getItemCount() {
        return mKursnamn.size();
    }
}