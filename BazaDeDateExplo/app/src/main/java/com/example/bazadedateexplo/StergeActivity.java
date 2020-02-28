package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;

public class StergeActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayListConferinte;
    private ListView listViewConferinte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sterge);

        listViewConferinte = findViewById(R.id.listViewConferinte);
        db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        afiseazaConferinte();
    }

    private void afiseazaConferinte() {
        String queryAllConferinte = "SELECT * FROM " + ConstanteBDExplo.Conferinte.TABLE_NAME;
        if (db == null) return;
        arrayListConferinte = new ArrayList<String>();
        Cursor c = db.rawQuery(queryAllConferinte,null);
        int sterge=0;
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(ConstanteBDExplo.Conferinte.COLUMN_NUME));
                arrayListConferinte.add(nume);
                ++sterge;
            } while (c.moveToNext());
        }
        //Log.i("AdaugClub.java","am " + sterge + " conferinte");
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayListConferinte);
        c.close();
        listViewConferinte.setAdapter(arrayAdapter);
    }
}
