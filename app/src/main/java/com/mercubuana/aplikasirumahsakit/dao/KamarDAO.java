package com.mercubuana.aplikasirumahsakit.dao;

import com.mercubuana.aplikasirumahsakit.model.Kamar;
import com.mercubuana.aplikasirumahsakit.model.RelasiKamarDanPasien;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface KamarDAO {

        @Insert
        public void insert(Kamar... kamar);

        @Update
        public void update(Kamar... kamar);

        @Delete
        public void delete(Kamar kamar);

//        @Query("SELECT * FROM kamar")
//        public List<Kamar> getDaftarKamar();

        @Transaction
        @Query("SELECT * FROM kamar")
        public List<RelasiKamarDanPasien> getDaftarKamar();

//        @Query("SELECT * FROM kamar WHERE id = :id")
//        public Kamar getKamarById(Long id);

        @Transaction
        @Query("SELECT * FROM kamar WHERE id = :id")
        public RelasiKamarDanPasien getKamarById(Long id);

        @Transaction
        @Query("SELECT * FROM kamar WHERE nama_kamar = :namaKamar")
        public RelasiKamarDanPasien[] getKamarByNamaKamar(String namaKamar);

//        @Query("SELECT *, max(id) FROM kamar WHERE nama_kamar = :namaKamar GROUP BY nama_kamar")
//        public Kamar getKamarBaruByNamaKamar(String namaKamar);

        @Transaction
        @Query("SELECT * FROM kamar WHERE nama_kamar = :namaKamar ORDER BY id DESC LIMIT 1")
        public RelasiKamarDanPasien getKamarBaruByNamaKamar(String namaKamar);

        @Transaction
        @Query("SELECT * FROM kamar WHERE jenis_kelamin = :jenisKelamin")
        public RelasiKamarDanPasien[] getDaftarKamarMasihTersisaTempat (char jenisKelamin);
}
