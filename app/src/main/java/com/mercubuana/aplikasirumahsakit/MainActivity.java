package com.mercubuana.aplikasirumahsakit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.aplikasirumahsakit.AboutActivity;
import com.example.aplikasirumahsakit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.mercubuana.aplikasirumahsakit.dao.PasienDAO;
import com.mercubuana.aplikasirumahsakit.model.Kamar;
import com.mercubuana.aplikasirumahsakit.model.RelasiKamarDanPasien;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DataGlobal dataGlobal = DataGlobal.getInstance();
    private ArrayList<String> daftarNamaKamarBernomorUrut = new ArrayList<String>();
    private ArrayList<String> daftarJenisKerjaanBerurut = new ArrayList<String>();
    private RecyclerView listViewDaftarNamaKamar;
    private RecyclerView.LayoutManager rvlmanager;
    private RecyclerView.Adapter rvadapter;
    private Spinner spinnerJenisKelamin;
    private TextView textViewHeaderDaftarKamar;
    private TextView jenisKerjaanList;
    private ArrayAdapter adapter;
//    AppDatabase database;
//    private KamarDAO kamarDAO;
    private PasienDAO pasienDAO;
    private String namaKamarDicari;
    private Calendar calendar;
    private int year, month, day;
    private EditText editTextNamaKamarBaru;
    private TextView textViewJenisKerjaan;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView nowaday = findViewById(R.id.textViewToday);
        setDate(nowaday);

        TextView day = findViewById(R.id.today);
        setDay(day);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.bringToFront();

        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }

        editTextNamaKamarBaru = findViewById(R.id.editTextNamaKamarBaru);

        textViewJenisKerjaan = findViewById(R.id.jenisKerjaanList);

        //1. Membuat listener untuk event click terhadap button "REKAM"
        //1a. Menentukan komponen GUI dengan membaca file XML
        FloatingActionButton btnRekam = findViewById(R.id.btnRekam);
        //1b. Menambahkan listener untuk event click
        btnRekam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rekamDataKamarBaru();
            }
        });

        //2. Menyiapkan Spinner dan Button untuk Menerima Pasien
        //2a. Menentukan komponen GUI dengan membaca file XML
        spinnerJenisKelamin = findViewById(R.id.spinnerJenisKelaminPasien);
        Button btnCariKamarUntukPasienBaru = findViewById(R.id.btnCariKamar);
        //2b. Menambahkan pilihan untuk Spinner
        ArrayList<String> pilihanJenisKelamin = new ArrayList<String>
                (Arrays.asList("Important", "Casual", "Fun"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.baris_pada_spinner_jenis_kelamin,pilihanJenisKelamin);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.baris_pilihan_pada_spinner_jenis_kelamin);
        spinnerJenisKelamin.setAdapter(adapter);
        //2c. Menambahkan listener untuk event click button Cari Kamar
        btnCariKamarUntukPasienBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                char jenisKelamin = spinnerJenisKelamin.getSelectedItemPosition()==0 ?
                        'L': spinnerJenisKelamin.getSelectedItemPosition()==1 ? 'P' : 'X';
                cariKamarUntukPasienBaru(jenisKelamin);
            }
        });

        //2. Membuat database
        DataGlobal.getInstance().database = Room.databaseBuilder(this, AppDatabase.class,
                "rumahsakitdb")
                .allowMainThreadQueries()
                .build();

        //bacaDataTanpaDatabase();
        bacaDataDenganDatabase();

        textViewHeaderDaftarKamar = findViewById(R.id.textViewHeadingDaftarKamar);
        textViewHeaderDaftarKamar.requestFocus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuabout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutmenu:
                Intent i = new Intent(this, AboutActivity.class);
                this.startActivity(i);
                return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void setDay(TextView day) {
        Date thisday = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        String dateday = formatter.format(thisday);
        day.setText(dateday);
    }

    private void setDate(TextView nowaday) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("d - MMM - yyyy");
        String date = formatter.format(today);
        nowaday.setText(date);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.menu_important) {
                char jenisKelamin = 'L';
                cariKamarUntukPasienBaru1(jenisKelamin);
            } else if (id == R.id.menu_casual) {
                char jenisKelamin = 'P';
                cariKamarUntukPasienBaru2(jenisKelamin);
            } else if (id == R.id.menu_fun){
                char jenisKelamin = 'X';
                cariKamarUntukPasienBaru3(jenisKelamin);
            } else {

            }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void cariKamarUntukPasienBaru(char jenisKelamin) {
        //Toast.makeText(this, "Jenis kelamin: " + jenisKelamin, Toast.LENGTH_SHORT).show();
        RelasiKamarDanPasien[] daftarKamar = DataGlobal.getInstance()
                .database.getKamarDAO().getDaftarKamarMasihTersisaTempat(jenisKelamin);
        String tampilan = "";
        if (null!=daftarKamar && daftarKamar.length>0){
//            for (RelasiKamarDanPasien k:daftarKamar){
//                tampilan += k.kamar.getNamaKamar() + "\n";
//            }
//            Toast.makeText(this, tampilan, Toast.LENGTH_SHORT).show();
            Intent formRincianDataPasien = new Intent(this,
                    RincianDataPasienActivity.class);
            dataGlobal.daftarKamarDenganSisaTempat = daftarKamar;
            dataGlobal.apakahTambahPasienBaru = true;
            startActivity(formRincianDataPasien);
        } else {
            Toast.makeText(this, "Tidak ada list jadwal kerjaan " +
                    "di kerjaan yang berjenis " + spinnerJenisKelamin.getSelectedItem(),
                    Toast.LENGTH_LONG).show();

        }
    }

    private void cariKamarUntukPasienBaru1(char jenisKelamin) {
        //Toast.makeText(this, "Jenis kelamin: " + jenisKelamin, Toast.LENGTH_SHORT).show();
        RelasiKamarDanPasien[] daftarKamar = DataGlobal.getInstance()
                .database.getKamarDAO().getDaftarKamarMasihTersisaTempat(jenisKelamin);
        String tampilan = "";
        if (null!=daftarKamar && daftarKamar.length>0){
//            for (RelasiKamarDanPasien k:daftarKamar){
//                tampilan += k.kamar.getNamaKamar() + "\n";
//            }
//            Toast.makeText(this, tampilan, Toast.LENGTH_SHORT).show();
            Intent formRincianDataPasien = new Intent(this,
                    RincianDataPasienActivity.class);
            dataGlobal.daftarKamarDenganSisaTempat = daftarKamar;
            dataGlobal.apakahTambahPasienBaru = true;
            startActivity(formRincianDataPasien);
        } else {
            Toast.makeText(getBaseContext(), "Tidak ada list jadwal kerjaan " +
                            "di kerjaan yang berjenis " + "Important",
                    Toast.LENGTH_LONG).show();

        }
    }

    private void cariKamarUntukPasienBaru2(char jenisKelamin) {
        //Toast.makeText(this, "Jenis kelamin: " + jenisKelamin, Toast.LENGTH_SHORT).show();
        RelasiKamarDanPasien[] daftarKamar = DataGlobal.getInstance()
                .database.getKamarDAO().getDaftarKamarMasihTersisaTempat(jenisKelamin);
        String tampilan = "";
        if (null!=daftarKamar && daftarKamar.length>0){
//            for (RelasiKamarDanPasien k:daftarKamar){
//                tampilan += k.kamar.getNamaKamar() + "\n";
//            }
//            Toast.makeText(this, tampilan, Toast.LENGTH_SHORT).show();
            Intent formRincianDataPasien = new Intent(this,
                    RincianDataPasienActivity.class);
            dataGlobal.daftarKamarDenganSisaTempat = daftarKamar;
            dataGlobal.apakahTambahPasienBaru = true;
            startActivity(formRincianDataPasien);
        } else {
            Toast.makeText(getBaseContext(), "Tidak ada list jadwal kerjaan " +
                            "di kerjaan yang berjenis " + "Casual",
                    Toast.LENGTH_LONG).show();

        }
    }

    private void cariKamarUntukPasienBaru3(char jenisKelamin) {
        //Toast.makeText(this, "Jenis kelamin: " + jenisKelamin, Toast.LENGTH_SHORT).show();
        RelasiKamarDanPasien[] daftarKamar = DataGlobal.getInstance()
                .database.getKamarDAO().getDaftarKamarMasihTersisaTempat(jenisKelamin);
        String tampilan = "";
        if (null!=daftarKamar && daftarKamar.length>0){
//            for (RelasiKamarDanPasien k:daftarKamar){
//                tampilan += k.kamar.getNamaKamar() + "\n";
//            }
//            Toast.makeText(this, tampilan, Toast.LENGTH_SHORT).show();
            Intent formRincianDataPasien = new Intent(this,
                    RincianDataPasienActivity.class);
            dataGlobal.daftarKamarDenganSisaTempat = daftarKamar;
            dataGlobal.apakahTambahPasienBaru = true;
            startActivity(formRincianDataPasien);
        } else {
            Toast.makeText(getBaseContext(), "Tidak ada list jadwal kerjaan " +
                            "di kerjaan yang berjenis " + "Fun",
                    Toast.LENGTH_LONG).show();

        }
    }

    public void rekamDataKamarBaru() {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String date = formatter.format(today);
        editTextNamaKamarBaru.setText(date);
        //1. Membaca data yang diketikkan oleh pemakai aplikasi
        //1a. Menentukan komponen GUI berdasarkan file layout XML
        EditText editTextNamaKamarBaru = findViewById(R.id
                .editTextNamaKamarBaru);
        //1b. Membaca tulisan yang diketikkan pemakai aplikasi
        String namaKamarBaru = editTextNamaKamarBaru.getText().toString();

        //2. Merekam data ke dalam memori (menambahkan nama kamar baru
        //   ke struktur data array list)
        //2a. Membuat objek baru dari class Kamar
        RelasiKamarDanPasien kamarBaru = new RelasiKamarDanPasien();
        kamarBaru.kamar = new Kamar();
        kamarBaru.kamar.setNamaKamar(namaKamarBaru);

        //2b. Menambahkan objek baru ke dalam daftar/list objek kamar
//        dataGlobal.daftarKamar.add(kamarBaru);
        //3. Melakukan update terhadap tampilan list view
        updateTampilanListView();

        //4. Rekam data daftar kamar terbaru ke dalam storage dan ambil primary key
        tambahDataDenganDatabase(kamarBaru.kamar);
        //rekamDataTanpaDatabase();
    }

    public void ubahDataKamar(View v) {
//        //1. Meminta konfirmasi dari pemakai aplikasi, apakah akan
//        //   menghapus data atau akan menambah rincian kelengkapan data
//
        //1a. Membaca nama tugas dalam text view yang mengalami klik
//        String namaKamarDicari = ((TextView) v).getText().toString();
        namaKamarDicari = ((TextView) v).getText().toString();
        //1b. Memanggil method untuk mengubah data kamar
        lengkapiDataKamar(namaKamarDicari);
//        //1b. Membuat alert dialog
//        new AlertDialog.Builder(this)
//                .setTitle("Hapus/Lengkapi Data")
//                .setMessage("Silakan pilih untuk menghapus atau " +
//                        "melengkapi data:")
//                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        hapusDataKamar(namaKamarDicari);
//                    }
//                })
//                .setNeutralButton("Lengkapi", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        lengkapiDataKamar(namaKamarDicari);
//                    }
//                })
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .show();

    }

    private void hapusDataKamar(String namaKamarDicari) {
        //1. Menghapus data kamar
        //1a. Mencari kamar di dalam memori (struktur data array list)
        int indeksTugasDicari = daftarNamaKamarBernomorUrut
                .indexOf(namaKamarDicari);
//        //1b. Menghapus objek elemen indeks dari database
//        Kamar kamarDicari = dataGlobal.daftarKamar.get(indeksTugasDicari).kamar;
//        hapusDenganDatabase(kamarDicari);
        //2. Melakukan update terhadap tampilan listview
        updateTampilanListView();
//        3. Merekam perubahan daftar kamar
//        rekamDataTanpaDatabase();
    }

    private void lengkapiDataKamar(String namaKamarDicari) {
        Intent formRincianDataKamar = new Intent(this,
                RincianDataKamarActivity.class);
        //1. Mencari objek kamar di dalam memori (struktur data array list)
        int indeksKamarDicari = daftarNamaKamarBernomorUrut
                .indexOf(namaKamarDicari);
        //2. Menyimpan objek kamar dicari dalam data global
//        dataGlobal.kamarDipilih = dataGlobal.daftarKamar.get(indeksKamarDicari).kamar;
        ArrayList<RelasiKamarDanPasien> daftarKamar = (ArrayList<RelasiKamarDanPasien>)
                DataGlobal.getInstance().database.getKamarDAO()
                .getDaftarKamar();
        RelasiKamarDanPasien kamarDipilih = daftarKamar.get(indeksKamarDicari);
        DataGlobal.getInstance().kamarDipilih = kamarDipilih;
        //3. Mengaktifkan activity form rincian data kamar
        startActivityForResult(formRincianDataKamar,0);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ubahDataDenganDatabase(dataGlobal.kamarDipilih);
        if (resultCode == Activity.RESULT_OK && data.getStringExtra("result")
            .equals("delete")){
            //Jika result code adalah untuk menghapus data kamar, maka
            // hapus data dari memori
            hapusDataKamar(namaKamarDicari);
        }
        updateTampilanListView();
    }

    private void updateTampilanListView() {
        //2. Menyiapkan list view untuk menampilkan daftar tugas
        //2a. Menentukan komponen GUI dengan membaca file XML
        listViewDaftarNamaKamar = findViewById(R.id.listViewDaftarTugas);
        //2b. Memberikan nomor urut untuk data nama kamar
        //2b1. Mengosongkan array list yang bernomor urut
        daftarNamaKamarBernomorUrut.clear();
        daftarJenisKerjaanBerurut.clear();
        //2b2. Melakukan loop terhadap seluruh data tugas dalam array list
        //     yang tidak bernomor urut
        int nomorUrut = 1;
        ArrayList<RelasiKamarDanPasien> daftarKamar =
                (ArrayList<RelasiKamarDanPasien>) DataGlobal
                .getInstance().database.getKamarDAO().getDaftarKamar();
//        for (RelasiKamarDanPasien k : dataGlobal.daftarKamar) {
        for (RelasiKamarDanPasien rkp : daftarKamar) {

            String dayName = rkp.kamar.getNamaKamar();
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = null;
            try {
                date = inFormat.parse(dayName);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String result = outFormat.format(date);

            char jenisKerjaanPadaList = rkp.kamar.getJenisKelamin();
            String jenisKerjaan = String.valueOf(jenisKerjaanPadaList=='L'?"Important":jenisKerjaanPadaList=='P'?"Casual":"Fun");

            String tampilanNamaKamar = nomorUrut + ". " + result +  "  :  "  + rkp.kamar.getNamaKamar();
            nomorUrut++;

            daftarJenisKerjaanBerurut.add(jenisKerjaan);
            daftarNamaKamarBernomorUrut.add(tampilanNamaKamar);
        }

        //2c. Menentukan adapter (sumber tampilan/data) bagi list view
        rvlmanager = new LinearLayoutManager(this);
        rvadapter = new MainAdapter(daftarNamaKamarBernomorUrut, daftarJenisKerjaanBerurut);
        listViewDaftarNamaKamar.setLayoutManager(rvlmanager);
        listViewDaftarNamaKamar.setAdapter(rvadapter);

    }

//    private void rekamDataTanpaDatabase() {
//        try {
//            FileOutputStream fos = openFileOutput("rumahsakit",
//                    Context.MODE_PRIVATE);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(DataGlobal.getInstance().daftarKamar);
//            oos.close();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void bacaDataTanpaDatabase() {
//        try {
//            FileInputStream fis = openFileInput("rumahsakit");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            DataGlobal.getInstance().daftarKamar = (ArrayList<Kamar>) ois.readObject();
//            ois.close();
//            fis.close();
//            updateTampilanListView();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void bacaDataDenganDatabase(){
//        kamarDAO = DataGlobal.getInstance().database.getKamarDAO();
//        DataGlobal.getInstance().daftarKamar = (ArrayList<RelasiKamarDanPasien>) kamarDAO.getDaftarKamar();
        updateTampilanListView();
    }

    private void tambahDataDenganDatabase(Kamar kamarBaru){
        //1. Memanggil method Room untuk menambah data kamar baru
//        kamarDAO.insert(kamarBaru);
        DataGlobal.getInstance().database.getKamarDAO().insert(kamarBaru);
        //Long id = kamarDAO.getKamar
        // ByNamaKamar(kamarBaru.getNamaKamar());
        //kamarBaru.setId(id);
        //2. Memberi primary key (id) terhadap objek kamar baru dalam memori, tanpa
        // membaca ulang seluruh data kamar dari memori
        //2a. Melakukan query untuk mencari objek kamar yang baru ditambahkan ke dalam database
//        Kamar kamarBaruDariDatabase = kamarDAO.getKamarBaruByNamaKamar(kamarBaru.getNamaKamar());
        RelasiKamarDanPasien kamarBaruDariDatabase = DataGlobal.getInstance().database
                .getKamarDAO().getKamarBaruByNamaKamar(kamarBaru.getNamaKamar());
        //3. Memanggil activity untuk melengkapi data kamar baru
        //3a. Menyimpan objek kamar dalam data global
        dataGlobal.kamarDipilih = kamarBaruDariDatabase;
        //3b. Mengaktifkan activity form rincian data kamar
        Intent formRincianDataKamar = new Intent(this,
                RincianDataKamarActivity.class);
        startActivityForResult(formRincianDataKamar,0);

    }

//    private void hapusDenganDatabase(Kamar kamarDicari){
//        kamarDAO.delete(kamarDicari);
//    }

//    private void ubahDataDenganDatabase(Kamar kamarDipilih){
//        kamarDAO.update(kamarDipilih);
//    }
}

