package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeExploratorActivity extends AppCompatActivity {
    private ListView mListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_explorator);

        //facem legatura intre fisierul .xml si codul java
        mListView = findViewById(R.id.listViewExploratori);

        stabilesteLegaturaCuDB();

        //extragem inregistrarile cu exploratori
        final ArrayList<ExploratorClass> numeExploratori = getExploratori();
        ExploratorAdapter adapter = new ExploratorAdapter(AlegeExploratorActivity.this, numeExploratori, db);
        mListView.setAdapter(adapter);
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private ArrayList<ExploratorClass> getExploratori() {
        String query = "SELECT * FROM " + Explorator.TABLE_NAME;
        ArrayList<ExploratorClass> exploratori = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                ExploratorClass explorator = new ExploratorClass();
                explorator.setmId(c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR)));
                explorator.setmNume(c.getString(c.getColumnIndex(Explorator.COLUMN_NUME)));
                explorator.setmPrenume(c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME)));
                explorator.setmCNP(c.getString(c.getColumnIndex(Explorator.COLUMN_CNP)));
                explorator.setmNrSpecializari(c.getInt(c.getColumnIndex(Explorator.COLUMN_NR_SPECIALIZARI)));
                explorator.setmGrad(c.getString(c.getColumnIndex(Explorator.COLUMN_GRAD)));
                explorator.setmInstructor(c.getInt(c.getColumnIndex(Explorator.COLUMN_INSTRUCTOR)));
                explorator.setmIDParinte(c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_PARINTE)));
                explorator.setmIDClub(c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_CLUB)));
                explorator.setmDataStart(c.getString(c.getColumnIndex(Explorator.COLUMN_DATA_START)));
                explorator.setmDataFinal(c.getString(c.getColumnIndex(Explorator.COLUMN_DATA_FINAL)));
                if (explorator.getmIDParinte() == 0)
                    explorator.setmIDParinte(-1);
                if (explorator.getmIDClub() == 0)
                    explorator.setmIDClub(-1);
                exploratori.add(explorator);
            } while (c.moveToNext());
        }
        c.close();
        return exploratori;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }
}
