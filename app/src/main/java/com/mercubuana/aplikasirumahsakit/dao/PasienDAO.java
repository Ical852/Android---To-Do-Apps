package com.mercubuana.aplikasirumahsakit.dao;

import com.mercubuana.aplikasirumahsakit.model.Pasien;
import com.mercubuana.aplikasirumahsakit.model.RelasiPasienDanKamar;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface PasienDAO {
    @Insert
    public void insert(Pasien... pasien);
    @Update
    public void update(Pasien... pasien);
    @Delete
    public void delete(Pasien pasien);

    @Transaction
    @Query("SELECT * FROM pasien")
    public List<RelasiPasienDanKamar> getDaftarPasien();

    @Transaction
    @Query("SELECT * FROM pasien WHERE id = :id")
    public RelasiPasienDanKamar getPasienById(Long id);

    @Transaction
    @Query("SELECT * FROM pasien WHERE nama_pasien = :namaPasien")
    public RelasiPasienDanKamar[] getPasienByNamaPasien(String namaPasien);

}

