package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class AdaugConferintaActivity extends AppCompatActivity {
    private Button buttonAdauga;
    private EditText editTextNume;
    private ListView listViewDirector;
    private SQLiteDatabase db;
    private ArrayList<Integer> idsDirectori;
    ListAdapter adapterDirectori;
    private int idDirector;
    private int mModif;
    private int idConferinta;
    private String detalii;

    {
        idDirector = -1;
        mModif = 0;
        idConferinta  = -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaug_conferinta);

        setTitle(R.string.titlu_adaug_conferinta);

        buttonAdauga = findViewById(R.id.buttonAdauga);
        editTextNume = findViewById(R.id.editTextNume);
        listViewDirector = findViewById(R.id.listViewDirector);

        idsDirectori = new ArrayList<>();

        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
        /*
        try {
            String CREATE_TABLE_CONFERINTA = "CREATE TABLE IF NOT EXISTS "
                    + Conferinte.TABLE_NAME + "( "
                    + Conferinte.COLUMN_ID_CONFERINTA + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Conferinte.COLUMN_NUME + " TEXT, "
                    + Conferinte.COLUMN_ID_DIRECTOR + " INTEGER"
                    + ");";
            db.execSQL(CREATE_TABLE_CONFERINTA);
        } catch (Exception e) {
            Toast.makeText(AdaugConferintaActivity.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }*/

        Intent it = getIntent();
        mModif = it.getIntExtra(ConstanteBDExplo.MODIF,0);
        if (mModif == 1) {
            idConferinta = it.getIntExtra(ConstanteBDExplo.ID,-1);
            detalii = it.getStringExtra(ConstanteBDExplo.DETALII);
            seteazaCampurile();
        }

        if (existaDirectori()) {
            ArrayList<String> numeDirectori = getDirectori();
            if (numeDirectori.size() > 0) {
                adapterDirectori = new ListAdapter(this, numeDirectori);
                listViewDirector.setAdapter(adapterDirectori);
                listViewDirector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        idDirector = idsDirectori.get(position);
                    }
                });
            }
        }

        buttonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume = editTextNume.getText().toString();
                if (mModif == 0) { //daca aceasta pagina este folosita pentru adaugare de Conferinte
                    addConferinta(nume);
                } else { // sau pentru modificare de conferinte
                    updateInregistrare(nume);
                }
                addAnimation();
                onBackPressed();
            }
        });


    }

    private void addConferinta(String nume) {
        String ADD_CONFERINTA = null;
        try {
            ADD_CONFERINTA = "INSERT INTO " + Conferinte.TABLE_NAME + "( "
                    + Conferinte.COLUMN_NUME + ", "
                    + Conferinte.COLUMN_ID_DIRECTOR + ") "
                    + "VALUES ("
                    + "'" + nume + "', "
                    + idDirector
                    + ");";
            ADD_CONFERINTA = ADD_CONFERINTA.replace("-1", "NULL");
            db.execSQL(ADD_CONFERINTA);
        } catch (SQLiteConstraintException e){
            afiseazaExceptia(getResources().getString(R.string.notificare_nerespectare_constrangeri));
            Log.e("AdaugConferinta.java",ADD_CONFERINTA);
        } catch (SQLiteException e) {
            afiseazaExceptia(getResources().getString(R.string.notificare_eroare_sqlite));
        }
    }

    private void seteazaCampurile() {
        String[] arr = detalii.split("\n");
        editTextNume.setText(arr[0]);
        try {
            idDirector = Integer.parseInt(arr[1]);
            if (idDirector == 0) idDirector = -1; // pentru a putea fi transformat in NULL usor
            // in functia updateInregistrare()
            Toast toast = Toast.makeText(this,"Directorul este " + getNumeDirector(),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } catch (NumberFormatException e) {
            afiseazaExceptia("Nu ai numar pentru id director" + e.toString());
        }
    }

    private String getNumeDirector() {
        String nume = null;
        String query = "SELECT " + Explorator.COLUMN_NUME + ", " + Explorator.COLUMN_PRENUME
                + ", " + Explorator.COLUMN_ID_EXPLORATOR + " FROM " + Explorator.TABLE_NAME
                + " WHERE " + Explorator.COLUMN_ID_EXPLORATOR + " = " + idDirector + ";";
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst())
            nume = c.getString(0) + " " + c.getString(1) + " " + c.getString(2);
        c.close();
        return nume;
    }

    private void updateInregistrare(String string) {
        String update = "UPDATE " + Conferinte.TABLE_NAME + " SET "
                + Conferinte.COLUMN_ID_CONFERINTA + "= " +  idConferinta +", "
                + Conferinte.COLUMN_NUME + "= '" + string + "', "
                + Conferinte.COLUMN_ID_DIRECTOR + "= " + idDirector
                + " WHERE " + Conferinte.COLUMN_ID_CONFERINTA + "=" + idConferinta + ";";
        update = update.replace("-1", "NULL");
        Log.e("AdaugConf.java",update);
        try {
            db.execSQL(update);
        } catch (SQLiteException e) {
            afiseazaExceptia("Nu s-a putut modifica inregistrarea conferintei.\n"
                    + e.toString() );
        }
    }

    private void afiseazaExceptia(String e) {
        Toast.makeText(this, e,Toast.LENGTH_LONG).show();
    }

    private boolean existaDirectori() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' and name='"
                + Explorator.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query,null);
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        else {
            c.close();
            return true;
        }
    }

    private ArrayList<String> getDirectori() {
        ArrayList<String> numeDirectori = new ArrayList<>();
        String query = "SELECT " + Explorator.COLUMN_NUME + ", "
                + Explorator.COLUMN_PRENUME + ", "
                + Explorator.COLUMN_ID_EXPLORATOR
                + " FROM " + Explorator.TABLE_NAME + " WHERE " + Explorator.COLUMN_INSTRUCTOR + " = 1;";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                int id = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                idsDirectori.add(id);
                numeDirectori.add(nume + " " + prenume + " (" + id + ") ");
            } while (c.moveToNext());
        }
        c.close();
        return numeDirectori;
    }

    private void addAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AdaugConferintaActivity.this, R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        animation.setInterpolator(bounceInterpolator);
        buttonAdauga.startAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        Intent it = null;
        if (mModif == 0) {
            it = new Intent(AdaugConferintaActivity.this, MainActivity.class);
        } else {
            it = new Intent(AdaugConferintaActivity.this, AlegeConferintaActivity.class);
        }
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
