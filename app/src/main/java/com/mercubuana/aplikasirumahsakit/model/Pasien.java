package com.mercubuana.aplikasirumahsakit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pasien")
public class Pasien {

    //Atribut/field primary key
    @PrimaryKey
    @NonNull
    private Long id;
    //Getter/setter untuk atribut/field primary key
    @NonNull
    public Long getId() {
        return id;
    }
    public void setId(@NonNull Long id) {
        this.id = id;
    }


    //Daftar atribut/field yang merupakan atribut sendiri
    @ColumnInfo(name = "nama_pasien")
    private String namaPasien;
    @ColumnInfo(name = "jenis_kelamin")
    private char jenisKelamin;
    @ColumnInfo(name = "tanggal_lahir")
    private String tanggalLahir;

    //Daftar getter/setter untuk atribut/field yang merupakan atrbut
    // sendiri
    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public char getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(char jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    //Daftar atribut/field akibat adanya relasi/asosiasi dengan
    //class/object lain (many-to-one)
    @ColumnInfo(name = "id_kamar_rawat")
    private Long idKamarRawat;

    //Daftar getter/setter untuk atribut relasional

    public Long getIdKamarRawat() {
        return idKamarRawat;
    }

    public void setIdKamarRawat(Long idKamarRawat) {
        this.idKamarRawat = idKamarRawat;
    }


}
