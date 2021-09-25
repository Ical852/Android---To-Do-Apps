package com.mercubuana.aplikasirumahsakit;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikasirumahsakit.R;
import com.mercubuana.aplikasirumahsakit.model.Pasien;
import com.mercubuana.aplikasirumahsakit.model.RelasiKamarDanPasien;

import java.util.ArrayList;
import java.util.Calendar;

public class RincianDataPasienActivity extends AppCompatActivity {
    private EditText editTextNamaPasien;
    private EditText editTextJamlKerja;
    private ImageButton btnTimePicker;
    private TimePickerDialog timePickerDialog;
    private TextView textViewJenisKelamin;
    private Spinner spinnerKamarRawat;
    private RelasiKamarDanPasien kamarRawat;
    private char jenisKelamin;
    private Calendar calendar;
    private int year, month, day;
    private int hour, minute;
    private ArrayList<String> daftarKamarRawat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_data_pasien);
        textViewJenisKelamin = findViewById(R.id.textViewJenisKelamin);
        editTextNamaPasien = findViewById(R.id.editTextNamaPasien);
        daftarKamarRawat = new ArrayList<String>();
        spinnerKamarRawat = findViewById(R.id.spinnerKamarRawat);

        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        editTextJamlKerja = findViewById(R.id.etJamKerja);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Calendar calendar = Calendar.getInstance();
               int h = calendar.get(Calendar.HOUR_OF_DAY);
               int m = calendar.get(Calendar.MINUTE);
               TimePickerDialog timePicker = new TimePickerDialog(view.getContext(),
                       new TimePickerDialog.OnTimeSetListener() {
                           @Override
                           public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                               String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                               editTextJamlKerja.setText(time);
                               if (editTextJamlKerja.getText().toString().length() != 0) {
                                   editTextJamlKerja.setError(null);
                               }
                           }
                       }, h, m, true);
               timePicker.show();
            }

        });

        if (DataGlobal.getInstance().apakahTambahPasienBaru==true){
            //1. Menentukan jenis kelamin pasien berdasarkan jenis kelamin
            // kamar yang masih memiliki sisa tempat > 0

            jenisKelamin = DataGlobal.getInstance().daftarKamarDenganSisaTempat[0].kamar
                    .getJenisKelamin();

            textViewJenisKelamin.setText(jenisKelamin=='L'?"Important":jenisKelamin=='P'?"Casual":"Fun");
            //2. Menyiapkan Spinner untuk daftar kamar masih tersisa tempat
            //2a. Menambahkan pilihan untuk Spinner Kamar Rawat
            for (RelasiKamarDanPasien k:DataGlobal.getInstance()
                    .daftarKamarDenganSisaTempat){
                daftarKamarRawat.add(k.kamar.getNamaKamar());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
                    .getApplicationContext(),
                    R.layout.baris_pada_spinner_jenis_kelamin, daftarKamarRawat);
            adapter.setDropDownViewResource(R.layout
                    .baris_pilihan_pada_spinner_jenis_kelamin);
            spinnerKamarRawat.setAdapter(adapter);
            findViewById(R.id.btnHapusDataPasien).setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("Data Kerjaan Baru");

        } else {
//            //1. Mengambil objek kamar dipilih dari database, dengan merujuk
//             //id kamar rawat dari pasien dipilih
//            Long idKamarRawat = DataGlobal.getInstance().pasienDipilih
//                    .getIdKamarRawat();
//            kamarRawat = DataGlobal.getInstance().database.getKamarDAO()
//                    .getKamarById(idKamarRawat);
            //1. Mengambil objek kamar dipilih dari data global
            kamarRawat = DataGlobal.getInstance().kamarDipilih;
            daftarKamarRawat.add(kamarRawat.kamar.getNamaKamar());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                    R.layout.baris_pada_spinner_jenis_kelamin, daftarKamarRawat);
            adapter.setDropDownViewResource(R.layout.baris_pilihan_pada_spinner_jenis_kelamin);
            spinnerKamarRawat.setAdapter(adapter);
            jenisKelamin = DataGlobal.getInstance().pasienDipilih.getJenisKelamin();
            textViewJenisKelamin.setText(jenisKelamin=='L'?"Important":jenisKelamin=='P'?"Casual":"Fun");
            editTextJamlKerja.setText(DataGlobal.getInstance().pasienDipilih.getTanggalLahir());
            editTextNamaPasien.setText(DataGlobal.getInstance().pasienDipilih
                .getNamaPasien());
            findViewById(R.id.btnHapusDataPasien).setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Detail Data Kerjaan");

        }

        Button btnRekamDataPasien = findViewById(R.id.btnRekamDataPasien);
        btnRekamDataPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rekamDataPasien();
            }
        });

        Button btnHapusDataPasien = findViewById(R.id.btnHapusDataPasien);
        btnHapusDataPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hapusDataPasien();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewJenisKelamin.requestFocus();
    }

    private void hapusDataPasien() {
        DataGlobal.getInstance().database.getPasienDAO().delete(DataGlobal.getInstance()
                .pasienDipilih);
        RelasiKamarDanPasien kamarDipilih = DataGlobal.getInstance().kamarDipilih;
        kamarDipilih.kamar.setSisaTempat(kamarRawat.kamar.getSisaTempat()+1);
        DataGlobal.getInstance().database.getKamarDAO().update(kamarDipilih.kamar);
        DataGlobal.getInstance().kamarDipilih =
                DataGlobal.getInstance().database.getKamarDAO()
                .getKamarById(kamarDipilih.kamar.getId());
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void rekamDataPasien() {
        if (DataGlobal.getInstance().apakahTambahPasienBaru==true){
            //1. Membuat objek pasien baru dan mengubah atribut nama dan
            // jenis kelamin
            Pasien pasienBaru = new Pasien();

            EditText editTextJamKerja = findViewById(R.id.etJamKerja);
            String stringJamKerja = editTextJamKerja.getText().toString();
            if (stringJamKerja.equals("")){
                editTextJamKerja.setError("Isian jam kerja tidak boleh kosong");
                return;
            }

            EditText editTextNamaPasien = findViewById(R.id.editTextNamaPasien);
            String namaPasien = editTextNamaPasien.getText().toString();
            if (namaPasien.equals("")){
                editTextNamaPasien.setError("Isian nama kerjaan tidak boleh kosong");
                return;
            }
            pasienBaru.setTanggalLahir(stringJamKerja);
            pasienBaru.setNamaPasien(namaPasien);

            pasienBaru.setJenisKelamin(jenisKelamin);

            //3. Menentukan kamar rawat bagi pasien
            //3a. Menentukan kamar yang dipilih dalam antarmuka spinner
            RelasiKamarDanPasien[] daftarKamarDenganSisaTempat = DataGlobal.getInstance()
                    .daftarKamarDenganSisaTempat;
            RelasiKamarDanPasien kamarRawatDipilih =
                    daftarKamarDenganSisaTempat[spinnerKamarRawat
                    .getSelectedItemPosition()];
            //3b. Menentukan id kamar rawat
            pasienBaru.setIdKamarRawat(kamarRawatDipilih.kamar.getId());
            //4. Merekam data pasien baru ke dalam database
            DataGlobal.getInstance().database.getPasienDAO().insert(pasienBaru);
            //5. Mengurangi sisa tempat dalam kamar yang dipilih dengan 1 (sisaTempat-1)
            kamarRawatDipilih.kamar.setSisaTempat(kamarRawatDipilih.kamar.getSisaTempat()-1);
            DataGlobal.getInstance().database.getKamarDAO().update(kamarRawatDipilih.kamar);
        } else {
            Pasien pasienDipilih = DataGlobal.getInstance().pasienDipilih;
            EditText editTextNamaPasien = findViewById(R.id.editTextNamaPasien);
            String namaPasien = editTextNamaPasien.getText().toString();
            if (namaPasien.equals("")){
                editTextNamaPasien.setError("Isian nama kerjaan tidak boleh kosong");
                return;
            }

            String stringJamKerja = editTextJamlKerja.getText().toString();
            if (stringJamKerja.equals("")){
                editTextJamlKerja.setError("Isian jam kerja tidak boleh kosong");
                return;
            }

            pasienDipilih.setNamaPasien(namaPasien);
            pasienDipilih.setTanggalLahir(stringJamKerja);
            DataGlobal.getInstance().database.getPasienDAO().update(pasienDipilih);

        }
        finish();
    }
}