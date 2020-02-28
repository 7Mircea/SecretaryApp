package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;
import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class AlegeParinteActivity extends AppCompatActivity {
    private ListView listViewAlegeParinte;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alege_parinte);

        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);

        ArrayList<ParinteClass> parinti = getParinti();
        ParinteAdapter adapter = new ParinteAdapter(this,parinti,db);
        listViewAlegeParinte = findViewById(R.id.listViewAlegeParinte);
        listViewAlegeParinte.setAdapter(adapter);
    }

    private ArrayList<ParinteClass> getParinti() {
        String query = "SELECT * FROM " + Parinti.TABLE_NAME;
        ArrayList<ParinteClass> parinti = new ArrayList<>();
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()) {
            do {
                ParinteClass parinte = new ParinteClass();
                parinte.setIdParinte(c.getInt(c.getColumnIndex(Parinti.COLUMN_ID_PARINTE)));
                parinte.setNume(c.getString(c.getColumnIndex(Parinti.COLUMN_NUME)));
                parinte.setPrenume(c.getString(c.getColumnIndex(Parinti.COLUMN_PRENUME)));
                parinte.setGen(c.getString(c.getColumnIndex(Parinti.COLUMN_GEN)));
                parinte.setNrDeTelefon(c.getString(c.getColumnIndex(Parinti.COLUMN_NR_TELEFON)));
                parinti.add(parinte);
            } while (c.moveToNext());
        }
        return parinti;
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(AlegeParinteActivity.this, MainActivity.class);
        startActivity(it);
    }
}

