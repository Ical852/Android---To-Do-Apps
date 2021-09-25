package com.mercubuana.aplikasirumahsakit;

import com.mercubuana.aplikasirumahsakit.dao.KamarDAO;
import com.mercubuana.aplikasirumahsakit.dao.PasienDAO;
import com.mercubuana.aplikasirumahsakit.model.DateTypeConverter;
import com.mercubuana.aplikasirumahsakit.model.Kamar;
import com.mercubuana.aplikasirumahsakit.model.Pasien;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Kamar.class, Pasien.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract KamarDAO getKamarDAO();
    public abstract PasienDAO getPasienDAO();
}