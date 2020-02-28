package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeExploInProiectActivity extends AppCompatActivity {
    private ListView mListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_explo_in_proiect);

        //facem legatura intre fisierul .xml si codul java
        mListView = findViewById(R.id.listViewAlegeExploInProiect);

        stabilesteLegaturaCuDB();

        //extragem inregistrarile cu exploratori
        final ArrayList<ExploInProiectClass> numeProiecte = getExploInProiecte();
        ExploInProiectAdapter adapter = new ExploInProiectAdapter(AlegeExploInProiectActivity.this, numeProiecte, db);
        mListView.setAdapter(adapter);
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private ArrayList<ExploInProiectClass> getExploInProiecte() {
        String query = "SELECT * FROM " + ExploratoriInProiecte.TABLE_NAME;
        ArrayList<ExploInProiectClass> exploInProiecte = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                ExploInProiectClass exploInProiect = new ExploInProiectClass();
                exploInProiect.setId(c.getInt(c.getColumnIndex(ExploratoriInProiecte.COLUMN_ID)));
                exploInProiect.setIdProiect(c.getInt(c.getColumnIndex(ExploratoriInProiecte.COLUMN_ID_PROIECT)));
                exploInProiect.setIdExplorator(c.getInt(c.getColumnIndex(ExploratoriInProiecte.COLUMN_ID_EXPLORATOR)));
                exploInProiect.setNrOre(c.getInt(c.getColumnIndex(ExploratoriInProiecte.COLUMN_NR_ORE)));

                exploInProiecte.add(exploInProiect);
            } while (c.moveToNext());
        }
        c.close();
        return exploInProiecte;
    }

    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(AlegeExploInProiectActivity.this, MainActivity.class);
        startActivity(it);
    }
}
