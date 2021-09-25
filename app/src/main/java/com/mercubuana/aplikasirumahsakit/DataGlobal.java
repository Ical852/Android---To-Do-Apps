package com.mercubuana.aplikasirumahsakit;

import com.mercubuana.aplikasirumahsakit.model.Pasien;
import com.mercubuana.aplikasirumahsakit.model.RelasiKamarDanPasien;

public class DataGlobal  {
    public static DataGlobal instance;
    public AppDatabase database;

    public RelasiKamarDanPasien kamarDipilih;
    public Pasien pasienBaru;
    public RelasiKamarDanPasien[] daftarKamarDenganSisaTempat;
    public Pasien pasienDipilih;
    public boolean apakahTambahPasienBaru;

    public static DataGlobal getInstance(){
        if (instance==null){
            instance = new DataGlobal();
        }
        return instance;
    }
}
