package com.mercubuana.aplikasirumahsakit.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RelasiPasienDanKamar {
    @Embedded
    public Pasien pasien;

    @Relation(parentColumn = "id_kamar_rawat", entityColumn = "id",
            entity = Kamar.class)
    public Kamar kamar;
}
