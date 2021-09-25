package com.mercubuana.aplikasirumahsakit.model;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RelasiKamarDanPasien {

    @Embedded
    public Kamar kamar;

    @Relation(parentColumn = "id", entityColumn = "id_kamar_rawat",
            entity = Pasien.class)
    public List<Pasien> daftarPasien;
}
