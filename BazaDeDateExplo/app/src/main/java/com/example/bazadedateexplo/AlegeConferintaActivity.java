package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.content.Intent;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeConferintaActivity extends AppCompatActivity {
    private ListView mListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_conferinta);

        //facem legatura intre fisierul .xml si codul java
        mListView = findViewById(R.id.listViewAlegeConferinta);

        stabilesteLegaturaCuDB();

        //extragem inregistrarile cu exploratori
        final ArrayList<ConferintaClass> numeConferinte =  getConferinte();
        ConferintaAdapter adapter = new ConferintaAdapter(AlegeConferintaActivity.this,numeConferinte,db);
        mListView.setAdapter(adapter);
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE,null);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private ArrayList<ConferintaClass> getConferinte() {
        String query = "SELECT * FROM " + Conferinte.TABLE_NAME;
        ArrayList<ConferintaClass> conferinte = new ArrayList<>();
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()) {
            do {
                ConferintaClass conferinta = new ConferintaClass();
                conferinta.setIdConferinta(c.getInt(c.getColumnIndex(Conferinte.COLUMN_ID_CONFERINTA)));
                conferinta.setNume(c.getString(c.getColumnIndex(Conferinte.COLUMN_NUME)));
                conferinta.setIdDirector(c.getInt(c.getColumnIndex(Conferinte.COLUMN_ID_DIRECTOR)));
                conferinte.add(conferinta);
            } while (c.moveToNext());
        }
        return conferinte;
    }

    @Override
    public void onBackPressed(){
        Intent it = new Intent(AlegeConferintaActivity.this, MainActivity.class);
        startActivity(it);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }
}
