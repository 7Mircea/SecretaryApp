package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.util.ArrayList;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;

public class AdaugClubActivity extends AppCompatActivity {
    private EditText editTextNume;
    private Button buttonAdauga;
    private ListView listViewConferinte;
    private ListView listViewConducatori;
    private int idConferinta = -1;
    private int idConducator = -1;
    private int idClub = -1;
    private int mModif = -1;
    private String detalii = null;

    private SQLiteDatabase db;

    private ArrayList<String> arrayListConferinte;
    private ArrayList<Integer> idConferinte;
    private ListAdapter adapter;

    private ArrayList<String> arrayListExploratori;
    private ArrayList<Integer> idExploratori;
    private ListAdapter arrayAdapterConducatori;
    private Ajutator ajutator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaug_club);

        buttonAdauga = findViewById(R.id.buttonAdauga);
        editTextNume = findViewById(R.id.editTextNume);
        listViewConferinte = findViewById(R.id.listViewConferinte);
        listViewConducatori = findViewById(R.id.listViewConducatori);

        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
        /*
        try {
            String CREATE_TABLE_CLUBURI = "CREATE TABLE IF NOT EXISTS "
                    + Cluburi.TABLE_NAME + "( "
                    + Cluburi.COLUMN_ID_CLUB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Cluburi.COLUMN_NUME + " TEXT, "
                    + Cluburi.COLUMN_ID_CONFERINTA + " INTEGER, "
                    + Cluburi.COLUMN_ID_CONDUCATOR + " INTEGER)";
            db.execSQL(CREATE_TABLE_CLUBURI);
            Toast.makeText(AdaugClubActivity.this, "Tabel Club creat", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(AdaugClubActivity.this, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show();
        }*/
        ajutator = new Ajutator(db);

        seteazaLaValoriAnterioare();

        buttonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModif == 0)
                    addClub();
                else
                    modificaClub();
                final Animation animation = AnimationUtils.loadAnimation(AdaugClubActivity.this,R.anim.bounce);
                BounceInterpolator bounceInterpolator = new BounceInterpolator(0.3,16);
                animation.setInterpolator(bounceInterpolator);
                buttonAdauga.startAnimation(animation);
                onBackPressed();

            }
        });
        afiseazaConferinte();
        listViewConferinte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                idConferinta = idConferinte.get(position);
            }
        });
        afiseazaExploratori();
        listViewConducatori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                idConducator = idExploratori.get(position);
            }
        });
    }

    private void seteazaLaValoriAnterioare() {
        Intent it = getIntent();
        mModif = it.getIntExtra(ConstanteBDExplo.MODIF,0);
        if (mModif != 0) {
            idClub = it.getIntExtra(ConstanteBDExplo.ID,-1);
            detalii = it.getStringExtra(ConstanteBDExplo.DETALII);
            Log.e("AdaugConf.java",detalii);
            seteazaCampurile();
        }
    }

    private void modificaClub(){
        String nume = editTextNume.getText().toString();
        String update = "UPDATE " + Cluburi.TABLE_NAME + " SET "
                + Cluburi.COLUMN_ID_CLUB + "= " +  idClub + ", "
                + Cluburi.COLUMN_NUME + "= '" + nume + "', "
                + Cluburi.COLUMN_ID_CONFERINTA + "= " + idConferinta + ", "
                + Cluburi.COLUMN_ID_CONDUCATOR + "= " + idConducator
                + " WHERE " + Cluburi.COLUMN_ID_CLUB + "=" + idClub + ";";
        update = update.replace("-1","NULL"); // converteste id-urile necorespunzatoare
        // in NULL
        Log.e("Adaugclub.java",update);
        try {
            db.execSQL(update);
        } catch (SQLiteException e) {
            afiseazaExceptia("Nu s-a putut modifica inregistrarea clubului.\n"
                    + e.toString() );
        }
    }

    private void afiseazaExceptia(String message) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    private void seteazaCampurile(){
        String[] strings = detalii.split("\n");
        editTextNume.setText(strings[0]);
        try {
            idConferinta = Integer.parseInt(strings[1]);
            idConducator = Integer.parseInt(strings[2]);
            if (idConferinta == 0) idConferinta = -1; //daca valoarea NULL a fost converitita in 0
            if (idConducator == 0) idConducator = -1; //seteaz-o la o valoare ce va fi converitita la loc
            // in NULL in functia modificaClub()
        } catch (NumberFormatException e) {
           Toast.makeText(this,"idConferinta/idConducator nu a fost numar" + e, Toast.LENGTH_LONG).show();
        }
        String mesaj = "Fosta conferinta era " + ajutator.getNumeConferinta(idConferinta)
                + " iar fostul conducator " + ajutator.getNumeExplorator(idConducator);
        Toast toast = Toast.makeText(this, mesaj,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void afiseazaConferinte() {
        String queryAllConferinte = "SELECT * FROM " + Conferinte.TABLE_NAME;
        if (db == null) return;
        if (!existaConferinte()) return;
        arrayListConferinte = new ArrayList<>();
        idConferinte = new ArrayList<>();
        Cursor c = db.rawQuery(queryAllConferinte, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Conferinte.COLUMN_NUME));
                Integer idConf = c.getInt(c.getColumnIndex(Conferinte.COLUMN_ID_CONFERINTA));
                arrayListConferinte.add(nume);
                idConferinte.add(idConf);
            } while (c.moveToNext());
        }
        if (arrayListConferinte.size() == 0) return;
        adapter = new ListAdapter(this, arrayListConferinte);
        c.close();
        listViewConferinte.setAdapter(adapter);
    }

    private void addClub() {
        String nume = editTextNume.getText().toString();
        String ADD_CLUB = null;
        if (idConferinta != -1 && idConducator != -1) {  //daca nu a fost ales nici conducator nici conferinta de care aparteine clubul
            ADD_CLUB = "INSERT INTO " + Cluburi.TABLE_NAME
                    + "(" + Cluburi.COLUMN_NUME + ", "
                    + Cluburi.COLUMN_ID_CONFERINTA + ", "
                    + Cluburi.COLUMN_ID_CONDUCATOR
                    + ") VALUES( '" + nume + "', "
                    + idConferinta + ", "
                    + idConducator
                    + ");";
        } else if (idConducator == -1 && idConferinta != -1) {
            ADD_CLUB = "INSERT INTO " + Cluburi.TABLE_NAME
                    + "(" + Cluburi.COLUMN_NUME + ", "
                    + Cluburi.COLUMN_ID_CONFERINTA + ", "
                    + Cluburi.COLUMN_ID_CONDUCATOR
                    + ") VALUES( '" + nume + "', "
                    + idConferinta + ", NULL);";
        } else if (idConducator != -1 && idConferinta == -1) {
            ADD_CLUB = "INSERT INTO " + Cluburi.TABLE_NAME
                    + "(" + Cluburi.COLUMN_NUME + ", "
                    + Cluburi.COLUMN_ID_CONFERINTA + ", "
                    + Cluburi.COLUMN_ID_CONDUCATOR
                    + ") VALUES( '" + nume + "', NULL, "
                    + idConducator
                    + ");";
        } else {
            ADD_CLUB = "INSERT INTO " + Cluburi.TABLE_NAME
                    + "(" + Cluburi.COLUMN_NUME + ", "
                    + Cluburi.COLUMN_ID_CONFERINTA + ", "
                    + Cluburi.COLUMN_ID_CONDUCATOR
                    + ") VALUES( '" + nume + "', NULL, NULL);";
        }
        try {
            db.execSQL(ADD_CLUB);
        } catch (SQLiteException e) {
            Toast.makeText(this,"Nu s-a putut adauga clubul",Toast.LENGTH_LONG).show();
            Log.e("AdaugClub.java",e.toString());
        }
    }

    /**
     * Funcția verifică dacă există tabela Conferinte nu și dacă există înregistrări în aceasta.
     *
     * @return returnează true dacă tabela Conferințe există, false dacă nu există
     */
    private boolean existaConferinte() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                + Conferinte.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private void afiseazaExploratori() {
        if (db == null) {
            Toast.makeText(this, R.string.notificare_lipsa_bd, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!existaInstructori()) {
            Toast.makeText(this, R.string.notificare_lipsa_instructori, Toast.LENGTH_SHORT).show();
            return;
        }
        String selectInstructori = "SELECT " + Explorator.COLUMN_NUME + ", "
                + Explorator.COLUMN_PRENUME + ", " + Explorator.COLUMN_ID_EXPLORATOR +
                " FROM " + Explorator.TABLE_NAME + " WHERE " + Explorator.COLUMN_INSTRUCTOR + "=1";
        Cursor c = db.rawQuery(selectInstructori, null);
        arrayListExploratori = new ArrayList<>();
        idExploratori = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                Integer idExplorator = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                arrayListExploratori.add(nume + " " + prenume + " (" + idExplorator + ")");
                idExploratori.add(idExplorator);
            } while (c.moveToNext());
        }
        c.close();
        if (arrayListExploratori.size() == 0) return;
        arrayAdapterConducatori = new ListAdapter(this, arrayListExploratori);
        listViewConducatori.setAdapter(arrayAdapterConducatori);
    }

    private boolean existaInstructori() {
        if (db == null) return false;
        if (!existaTabelaInstructori()) return false;
        String queryInstructori = "SELECT * FROM " + Explorator.TABLE_NAME
                + " WHERE " + Explorator.COLUMN_INSTRUCTOR + " = 1;";
        Cursor c = db.rawQuery(queryInstructori, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean existaTabelaInstructori() {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                + Explorator.TABLE_NAME + "'";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else  {
            c.close();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent it = null;
        if (mModif == 0) {
            it = new Intent(AdaugClubActivity.this, MainActivity.class);
        } else {
            it = new Intent(AdaugClubActivity.this, AlegeClubActivity.class);
        }
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
