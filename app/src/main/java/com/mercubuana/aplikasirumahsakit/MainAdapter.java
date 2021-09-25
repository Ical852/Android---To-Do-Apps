package com.mercubuana.aplikasirumahsakit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasirumahsakit.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<String> daftarNamaKamarBernomorUrut ;
    ArrayList<String> daftarJenisKerjaanBerurut;

    public MainAdapter(ArrayList<String> data, ArrayList<String> data1) {
        this.daftarNamaKamarBernomorUrut = data;
        this.daftarJenisKerjaanBerurut = data1;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlistview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MainAdapter.ViewHolder holder, int position) {
        holder.dataKerja.setText(daftarNamaKamarBernomorUrut.get(position));
        holder.jenisKerja.setText(daftarJenisKerjaanBerurut.get(position));
    }

    @Override
    public int getItemCount() {
        return daftarNamaKamarBernomorUrut.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView dataKerja, jenisKerja;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            jenisKerja = itemVIew.findViewById(R.id.jenisKerjaanList);

            dataKerja = itemVIew.findViewById(R.id.tanggalToDoList);
        }
    }
}
