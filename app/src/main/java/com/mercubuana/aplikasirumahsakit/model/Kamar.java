package com.mercubuana.aplikasirumahsakit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "kamar")
public class Kamar {

    @PrimaryKey
    @NonNull private Long id;
    
    //Daftar atribut/field yang merupakan atribut sendiri
    @ColumnInfo(name = "nama_kamar")
    private String namaKamar;

    @ColumnInfo(name = "jenis_kelamin")
    private char jenisKelamin = 'L';

    @ColumnInfo(name = "kapasitas")
    private int kapasitas;

    @ColumnInfo(name = "sisa_tempat")
    private int sisaTempat;

    //Method get dan set untuk mengubah nilai atribut class
    public String getNamaKamar() {
        return namaKamar;
    }
    public void setNamaKamar(String namaKamar) {
        this.namaKamar = namaKamar;
    }
    public char getJenisKelamin() {
        return jenisKelamin;
    }
    public void setJenisKelamin(char jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
    public int getKapasitas() {
        return kapasitas;
    }
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
    public int getSisaTempat() {
        return sisaTempat;
    }
    public void setSisaTempat(int sisaTempat) {
        this.sisaTempat = sisaTempat;
    }

    @NonNull
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
