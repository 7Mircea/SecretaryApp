package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeProiectActivity extends AppCompatActivity {

    private ListView mListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_proiect);

        //facem legatura intre fisierul .xml si codul java
        mListView = findViewById(R.id.listViewAlegeProiect);

        stabilesteLegaturaCuDB();

        //extragem inregistrarile cu exploratori
        final ArrayList<ProiectClass> numeProiecte =  getProiecte();
        ProiectAdapter adapter = new ProiectAdapter(AlegeProiectActivity.this,numeProiecte,db);
        mListView.setAdapter(adapter);
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE,null);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private ArrayList<ProiectClass> getProiecte() {
        String query = "SELECT * FROM " + Proiecte.TABLE_NAME;
        ArrayList<ProiectClass> proiecte = new ArrayList<>();
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()) {
            do {
                ProiectClass proiect = new ProiectClass();
                proiect.setIdProiect(c.getInt(c.getColumnIndex(Proiecte.COLUMN_ID_PROIECTE)));
                proiect.setNume(c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME)));
                proiect.setDataStart(c.getString(c.getColumnIndex(Proiecte.COLUMN_DATA_START)));
                proiect.setDataFinal(c.getString(c.getColumnIndex(Proiecte.COLUMN_DATA_FINAL)));
                proiect.setDescriereScurta(c.getString(c.getColumnIndex(Proiecte.COLUMN_DESCRIERE_SCURTA)));
                proiecte.add(proiect);
            } while (c.moveToNext());
        }
        return proiecte;
    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(AlegeProiectActivity.this, MainActivity.class);
        startActivity(it);
    }
}
