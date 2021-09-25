package com.mercubuana.aplikasirumahsakit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikasirumahsakit.R;
import com.mercubuana.aplikasirumahsakit.model.Pasien;
import com.mercubuana.aplikasirumahsakit.model.RelasiKamarDanPasien;

import java.util.ArrayList;
import java.util.Calendar;

public class RincianDataKamarActivity extends AppCompatActivity {
    private RelasiKamarDanPasien kamarDipilih;
    private DataGlobal dataGlobal = DataGlobal.getInstance();
    private EditText isianNamaKamar;
    private EditText isianKapasitas;
    private EditText isianSisaTempat;
    private TextView headerListView;
    private TextView headerjeniskerjaan;
    private TextView textviewjeniskerjaan;
    private RadioGroup radiobtn;
    private View hrjenis;
    private RadioButton radioLakiLaki;
    private RadioButton radioPerempuan;
    private RadioButton radio3;
    private RecyclerView listViewDaftarNamaPasien;
    private RecyclerView.LayoutManager rvlmanager;
    private RecyclerView.Adapter rvadapter;
    private ArrayList<String> daftarNamaPasienBernomorUrut = new ArrayList<String>();
    private ArrayList<String> daftarJamKerjaBernomorUrut = new ArrayList<String>();
    private String namaPasienDicari;
    private RelasiKamarDanPasien relasiKamarDanPasienDipilih = null;
    private EditText editTextIsianNamaKamar;
    private Calendar calendar;
    private int year, month, day;
    private char jenisKerjaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_data_kamar);

        editTextIsianNamaKamar = findViewById(R.id.editTextIsianNamaKamar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Data Kerjaan");

        Button btnRekam = findViewById(R.id.btnRekamRincianDataKamar);
        btnRekam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rekamRincianDataKamar();
            }
        });
        Button btnHapus = findViewById(R.id.btnHapus);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kamarDipilih.daftarPasien.size()!=0){
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Tidak Dapat Menghapus Tanggal Kerjaan")
                            .setMessage("Masih ada pekerjaan yang harus anda selesaikan,"
                                    + "maka tanggal to do list tidak dapat dihapus")
                            .setPositiveButton("OK", null)
                            .show();
//                    isianKapasitas.setText(""+kamarDipilih.kamar.getKapasitas());
                    return;
                } else {
                    hapusDataKamar();
                }
            }
        });

        hrjenis = findViewById(R.id.hrJenis);

        radiobtn = findViewById(R.id.radioGroup);
        textviewjeniskerjaan = findViewById(R.id.textView2);

        headerjeniskerjaan = findViewById(R.id.txtJenisKejaan);

        headerListView = findViewById(R.id.textViewHeadingDaftarPasien);
        headerListView.requestFocus();
        updateTampilanIsianDataKamar();
        updateTampilanListView();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        ImageButton btnEditTanggalInput = findViewById(R.id.btnEditTanggalInput);
        btnEditTanggalInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" + String.valueOf(year);
                                editTextIsianNamaKamar.setText(date);
                                if (editTextIsianNamaKamar.getText().toString().length() != 0) {
                                    editTextIsianNamaKamar.setError(null);
                                }
                            }
                        }, yy, mm, dd);
                datePicker.show();
            }
        });

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

    private void updateTampilanIsianDataKamar() {
        //1. Mengambil data objek kamar dipilih dari class data global
        kamarDipilih = dataGlobal.kamarDipilih;
        //2. Menampilkan rincian objek data kamar pada form isian
        isianNamaKamar = findViewById(R.id.editTextIsianNamaKamar);
        isianNamaKamar.setText(kamarDipilih.kamar.getNamaKamar());
        radioLakiLaki = findViewById(R.id.radioButtonLakiLaki);
        radioPerempuan = findViewById(R.id.radioButtonPerempuan);
        radio3 = findViewById(R.id.radioButton3);
        if (kamarDipilih.kamar.getJenisKelamin() == 'L') {
            radioLakiLaki.setChecked(true);
        } else if (kamarDipilih.kamar.getJenisKelamin() == 'P') {
            radioPerempuan.setChecked(true);
        } else {
            radio3.setChecked(true);
        }
    }

    private void hapusDataKamar() {
        //Menghapus data kamar dari database
        DataGlobal.getInstance().database
                .getKamarDAO().delete(kamarDipilih.kamar);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "delete");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void rekamRincianDataKamar() {
        String etNamaKamar = editTextIsianNamaKamar.getText().toString();
        if(etNamaKamar.equals("")){
            editTextIsianNamaKamar.setError("Isian tanggal tidak boleh kosong");
            return;
        }
        kamarDipilih.kamar.setNamaKamar(isianNamaKamar.getText().toString());

        if (radioLakiLaki.isChecked()) {
            kamarDipilih.kamar.setJenisKelamin('L');
        } else if (radioPerempuan.isChecked()) {
            kamarDipilih.kamar.setJenisKelamin('P');
        } else {
            kamarDipilih.kamar.setJenisKelamin('X');
        }
        DataGlobal.getInstance().database.getKamarDAO().update(kamarDipilih.kamar);
        finish();
    }

    private void updateTampilanListView() {
        //2. Menyiapkan list view untuk menampilkan daftar tugas
        //2a. Menentukan komponen GUI dengan membaca file XML
        CardView cardView = findViewById(R.id.cardViewTitleHeader);
        CardView cardViewList = findViewById(R.id.cardViewListKerjaan);
        ImageView imageNolist = findViewById(R.id.imageNoList);
        listViewDaftarNamaPasien = findViewById(R.id.listViewDaftarPasien);
        //2b. Memberikan nomor urut untuk data nama pasien
        //2b1. Mengosongkan array list yang bernomor urut
        daftarNamaPasienBernomorUrut.clear();
        daftarJamKerjaBernomorUrut.clear();
        //2b2. Melakukan loop terhadap seluruh data nama pasien dalam array list
        //     yang tidak bernomor urut
        if (kamarDipilih.daftarPasien.size()>0){
            int nomorUrut = 1;
            for (Pasien p : kamarDipilih.daftarPasien) {
                String jamKerja = p.getTanggalLahir();
                String tampilanNamaPasien = nomorUrut + ". " + p.getNamaPasien();

                nomorUrut++;
                daftarJamKerjaBernomorUrut.add(jamKerja);
                daftarNamaPasienBernomorUrut.add(tampilanNamaPasien);
            }
            //2c. Menentukan adapter (sumber tampilan/data) bagi list view
            //2d. Mengaitkan list view dengan adapter
            rvlmanager = new LinearLayoutManager(this);
            rvadapter = new RincianAdapter(daftarNamaPasienBernomorUrut, daftarJamKerjaBernomorUrut);
            listViewDaftarNamaPasien.setLayoutManager(rvlmanager);
            listViewDaftarNamaPasien.setAdapter(rvadapter);
            textviewjeniskerjaan.setVisibility(View.GONE);
            radiobtn.setVisibility(View.GONE);
            imageNolist.setVisibility(View.INVISIBLE);
            jenisKerjaan = kamarDipilih.kamar.getJenisKelamin();
            headerjeniskerjaan.setText(jenisKerjaan=='L'?"Important":jenisKerjaan=='P'?"Casual":"Fun");
            headerListView.setText("Daftar Kerjaan Pada Tanggal "
                    + kamarDipilih.kamar.getNamaKamar());
            findViewById(R.id.radioButton3).setEnabled(false);
            findViewById(R.id.radioButtonPerempuan).setEnabled(false);
            findViewById(R.id.radioButtonLakiLaki).setEnabled(false);
        } else {
            hrjenis.setVisibility(View.GONE);
            headerjeniskerjaan.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            cardViewList.setVisibility(View.GONE);
            findViewById(R.id.radioButton3).setEnabled(true);
            findViewById(R.id.radioButtonPerempuan).setEnabled(true);
            findViewById(R.id.radioButtonLakiLaki).setEnabled(true);
            listViewDaftarNamaPasien.setAdapter(rvadapter);
            headerListView.setText("Belum ada list pekerjaan");
        }
    }

    public void ubahDataPasien(View v){
        Intent formRincianDataPasien = new Intent(this,
                RincianDataPasienActivity.class);
        //1. Mencari objek kamar di dalam memori (struktur data array list)
        namaPasienDicari = ((TextView) v).getText().toString();
        int indeksPasienDicari = daftarNamaPasienBernomorUrut
                .indexOf(namaPasienDicari);
        //2. Menyimpan objek pasien dicari dalam data global
        dataGlobal.pasienDipilih = kamarDipilih
                .daftarPasien.get(indeksPasienDicari);
        dataGlobal.apakahTambahPasienBaru = false;
        //3. Mengaktifkan activity form rincian data kamar
        startActivityForResult(formRincianDataPasien,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateTampilanIsianDataKamar();
        updateTampilanListView();
    }
}


