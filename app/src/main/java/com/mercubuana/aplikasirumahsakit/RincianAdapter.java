package com.mercubuana.aplikasirumahsakit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasirumahsakit.R;

import java.util.ArrayList;

public class RincianAdapter extends RecyclerView.Adapter<RincianAdapter.ViewHolder> {

    ArrayList<String> daftarNamaPasienBernomorUrut;
    ArrayList<String> daftarJamKerjaBernomorUrut;

    public RincianAdapter(ArrayList<String> data, ArrayList<String> data1) {
        daftarNamaPasienBernomorUrut = data;
        daftarJamKerjaBernomorUrut = data1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rincianlistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RincianAdapter.ViewHolder holder, int position) {
        holder.rincianKerjaan.setText(daftarNamaPasienBernomorUrut.get(position));
        holder.jamKerja.setText("[ " +daftarJamKerjaBernomorUrut.get(position)+ " ]");
    }

    @Override
    public int getItemCount() {
        return daftarNamaPasienBernomorUrut.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView rincianKerjaan, jamKerja;

        public ViewHolder(View itemview) {
            super(itemview);

            jamKerja = itemview.findViewById(R.id.jamKerjaCardList);

            rincianKerjaan = itemview.findViewById(R.id.rincianKerjaan);
        }
    }
}
