package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeClubActivity extends AppCompatActivity {
    private ListView mListView;
    private int idClub;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_club);

        //facem legatura intre fisierul .xml si codul java
        mListView = findViewById(R.id.listViewCluburi);

        stabilesteLegaturaCuDB();

        //extragem inregistrarile cu exploratori
        final ArrayList<ClubClass> numeCluburi = getCluburi();
        ClubAdapter adapter = new ClubAdapter(AlegeClubActivity.this, numeCluburi, db);
        mListView.setAdapter(adapter);
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private ArrayList<ClubClass> getCluburi() {
        String query = "SELECT * FROM " + Cluburi.TABLE_NAME;
        ArrayList<ClubClass> cluburi = new ArrayList<>();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                ClubClass club = new ClubClass();
                club.setIdClub(c.getInt(c.getColumnIndex(Cluburi.COLUMN_ID_CLUB)));
                club.setNume(c.getString(c.getColumnIndex(Cluburi.COLUMN_NUME)));
                club.setIdConferinta(c.getInt(c.getColumnIndex(Cluburi.COLUMN_ID_CONFERINTA)));
                club.setIdConducator(c.getInt(c.getColumnIndex(Cluburi.COLUMN_ID_CONDUCATOR)));
                cluburi.add(club);
            } while (c.moveToNext());
        }
        return cluburi;
    }
}
