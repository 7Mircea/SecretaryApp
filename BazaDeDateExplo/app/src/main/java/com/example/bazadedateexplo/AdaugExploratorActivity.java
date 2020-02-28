package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import android.util.Log;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bazadedateexplo.ConstanteBDExplo.*;


public class AdaugExploratorActivity extends AppCompatActivity {
    private Button buttonAdauga;
    private EditText editTextNume;
    private EditText editTextPrenume;
    private EditText editTextCNP;
    private EditText editTextNrSpecializarii;
    private EditText editTextGrad;
    private EditText editTextInstructor;
    private SQLiteDatabase db;
    private ListView listViewIDParinte;
    private ListView listViewIDClub;
    private EditText editTextDataStart;
    private EditText editTextDataFinal;
    private int idParinte;
    private int idClub;
    private ArrayList<Integer> idsParinte;
    private ArrayList<Integer> idsClub;
    private int idExplorator;
    private int mModif; //daca este unul activitatea doar modifica inregistrari
    // altfel adauga pentru prima data
    private String detalii;

    {
        idParinte = -1;
        idClub = -1;
        idExplorator = -1;
    }

    /**
     * <p>onCreate are rolul de a seta afisarea in pagina și a seta interacțiunea cu utilizatorul</p>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*
         * Funcția onCreate este chemată la crearea fiecarei activități. Setam layout-ul activității. Activitatea reprezintă o pagină ce poate fi văzută
         * de utilizator. Layout-ul reprezintă aranjarea în pagină a textelor, butoanelor și
         * imaginilor.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_explorator);

        setTitle("Adaugă/Modifică explo");

        /*
         * Salvăm referința fiecărui element din layout în variabile locale
         * cu nume sugestive.
         */
        buttonAdauga = findViewById(R.id.buttonAdauga);
        editTextNume = findViewById(R.id.editTextNume);
        editTextPrenume = findViewById(R.id.editTextPrenume);
        editTextCNP = findViewById(R.id.editTextCNP);
        editTextNrSpecializarii = findViewById(R.id.editTextNrSpecializari);
        editTextGrad = findViewById(R.id.editTextGrad);
        editTextInstructor = findViewById(R.id.editTextInstructor);
        listViewIDParinte = findViewById(R.id.listViewIDParinte);
        listViewIDClub = findViewById(R.id.listViewIDClub);
        editTextDataStart = findViewById(R.id.editTextDataStart);
        editTextDataFinal = findViewById(R.id.editTextDataFinal);



        /*
         * initializam lista de id-uri de cluburi si părinți
         */
        idsClub = new ArrayList<>();
        idsParinte = new ArrayList<>();

        /*
         * Creăm baza de date Explo.db dacă nu a fost creată deja.
         * Creăm tabela Exploratori daca nu fost creată deja.
         */
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);

        /*
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS "
                    + Explorator.TABLE_NAME + "("
                    + Explorator.COLUMN_ID_EXPLORATOR + " INTEGER NOT NULL primary key AUTOINCREMENT, "
                    + Explorator.COLUMN_NUME + " TEXT NOT NULL, "
                    + Explorator.COLUMN_PRENUME + " TEXT NOT NULL, "
                    + Explorator.COLUMN_CNP + " TEXT CHECK(length(CNP) = 13) UNIQUE NOT NULL, "
                    + Explorator.COLUMN_NR_SPECIALIZARI + " INTEGER NOT NULL, "
                    + Explorator.COLUMN_GRAD + " TEXT CHECK(Grad IN ('ucenic','calator','navigator','mesager','ghid-asistent','ghid','master-ghid')), "
                    + Explorator.COLUMN_INSTRUCTOR + " INTEGER NOT NULL CHECK(Instructor IN (0,1)), "
                    + Explorator.COLUMN_ID_PARINTE + " INTEGER DEFAULT -1, "
                    + Explorator.COLUMN_ID_CLUB + " INTEGER, "
                    + Explorator.COLUMN_DATA_START + " TEXT NOT NULL, "
                    + Explorator.COLUMN_DATA_FINAL + " TEXT NOT NULL"
                    + ", \n"
                    + " CONSTRAINT fk_exploratori_parinte\n"
                    + " FOREIGN KEY (" + Explorator.COLUMN_ID_PARINTE +")\n"
                    + " REFERENCES "+Parinti.TABLE_NAME + "(" +Parinti.COLUMN_ID_PARINTE + ")\n"
                    + " ON DELETE SET DEFAULT,\n"
                    + " CONSTRAINT fk_exploratori_club\n"
                    + " FOREIGN KEY (" + Explorator.COLUMN_ID_CLUB +")\n"
                    + " REFERENCES " + Cluburi.TABLE_NAME + "(" + Cluburi.COLUMN_ID_CLUB + ")\n"
                    + " ON DELETE CASCADE\n)";
            //Log.e("AdaugExplo.java", CREATE_TABLE_CONTAIN);
            db.execSQL(CREATE_TABLE_CONTAIN);
            Toast.makeText(AdaugExploratorActivity.this, "table created ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(AdaugExploratorActivity.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
        }*/

        /*
         * setam listViewIDParinte pentru a putea prelua IDParintelui
         */
        if (existaParinti()) {
            ArrayList<String> numeParinti = getParinti();
            if (numeParinti.size() > 0) {
                ListAdapter adapterParinti = new ListAdapter(this, numeParinti);
                listViewIDParinte.setAdapter(adapterParinti);
                listViewIDParinte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        idParinte = idsParinte.get(position);
                    }
                });
            }
        }

        /*
         * setam listViewIDCLub pentru a putea prelua IDClubului
         */
        if (existaCluburi()) {
            ArrayList<String> numeCluburi = getCluburi();
            if (numeCluburi.size() > 0) {
                ListAdapter adapterCluburi = new ListAdapter(this,  numeCluburi);
                listViewIDClub.setAdapter(adapterCluburi);
                listViewIDClub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        idClub = idsClub.get(position);
                    }
                });
            }
        }

        /*
         * Setăm butonul de adăugat exploratori să adauge înregistrări în tabelă ori de câte
         * ori este apăsat. Mai întâi preia informația din fiecare editText sau ListView și
         * o adaugă într-o declarație de insert pe care apoi o aplică bazei de date.
         */
        buttonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume = editTextNume.getText().toString();
                String prenume = editTextPrenume.getText().toString();
                String CNP = editTextCNP.getText().toString();
                String nrSpecializari = editTextNrSpecializarii.getText().toString();
                String grad = editTextGrad.getText().toString();
                String instructor = editTextInstructor.getText().toString();
                String dataStart = editTextDataStart.getText().toString();
                String dataFinal = editTextDataFinal.getText().toString();
                String date = null;
                if (idParinte != -1 && idClub != -1)
                    date = nume + " " + prenume + " " + CNP + " " + nrSpecializari + " " +
                        grad + " " + instructor + " " + idParinte + " " + idClub + " " +
                        dataStart + " " + dataFinal;
                 else if (idClub == -1 && idParinte == -1)
                    date = nume + " " + prenume + " " + CNP + " " + nrSpecializari + " " +
                            grad + " " + instructor + " NULL NULL " +
                            dataStart + " " + dataFinal;
                else if (idClub == -1)
                    date = nume + " " + prenume + " " + CNP + " " + nrSpecializari + " " +
                            grad + " " + instructor + " " + idParinte + " NULL " +
                            dataStart + " " + dataFinal;
                else
                    date = nume + " " + prenume + " " + CNP + " " + nrSpecializari + " " +
                            grad + " " + instructor + " NULL " + idClub + " " +
                            dataStart + " " + dataFinal;
                Log.e("AdaugExplorator.java",date);
                if (mModif == 0) {
                    try {
                        String sql = "INSERT INTO " + Explorator.TABLE_NAME + " ("
                                + Explorator.COLUMN_NUME + ","
                                + Explorator.COLUMN_PRENUME + ","
                                + Explorator.COLUMN_CNP + ","
                                + Explorator.COLUMN_NR_SPECIALIZARI + ","
                                + Explorator.COLUMN_GRAD + ","
                                + Explorator.COLUMN_INSTRUCTOR + ","
                                + Explorator.COLUMN_ID_PARINTE + ", "
                                + Explorator.COLUMN_ID_CLUB + ", "
                                + Explorator.COLUMN_DATA_START + ", "
                                + Explorator.COLUMN_DATA_FINAL
                                + ") " + " VALUES ('"
                                + nume + "',"
                                + "'" + prenume + "',"
                                + "'" + CNP + "',"
                                + "'" + nrSpecializari + "',"
                                + "'" + grad + "',"
                                + Integer.parseInt(instructor) + ", "
                                + idParinte + ", "
                                + idClub + ", "
                                + "'" + dataStart + "', "
                                + "'" + dataFinal + "'"
                                + ");";
                        sql = sql.replace("-1","NULL");
                        Log.e("AdaugExplo.java", sql);
                        db.execSQL(sql);
                    } catch (NumberFormatException e) {
                        afiseazaExceptia(getResources().getString(R.string.notificare_format_numar));
                    } catch (SQLiteConstraintException e) {
                        afiseazaExceptia(getResources().getString(R.string.notificare_nerespectare_constrangeri));
                        Log.e("AdaugExplorator.java","constrangere sql : " + e.toString());
                    } catch (Exception e) {
                        afiseazaExceptia(e.toString());
                    }
                } else if (mModif == 1) {
                    updateInregistrare(date);
                    Intent it = new Intent(AdaugExploratorActivity.this,AlegeExploratorActivity.class);
                    startActivity(it);
                }
                startAnimation();
            }
        });

        Intent it = getIntent();
        mModif = it.getIntExtra(ConstanteBDExplo.MODIF,0);
        if (mModif == 1) {
            idExplorator = it.getIntExtra(ConstanteBDExplo.ID,-1);
            detalii = it.getStringExtra(ConstanteBDExplo.DETALII);
            seteazaCampurile();
        }
    }

    private void seteazaCampurile() {
        Log.e("AdaugExplorator.ja","In seteaza campurile" +  detalii.replace("\n", " "));
        String[] arr = detalii.split("\n");
        editTextNume.setText(arr[0]);
        editTextPrenume.setText(arr[1]);
        editTextCNP.setText(arr[2]);
        editTextNrSpecializarii.setText(arr[3]);
        editTextGrad.setText(arr[4]);
        editTextInstructor.setText(arr[5]);
        try {
            idParinte = Integer.parseInt(arr[6]);
            idClub = Integer.parseInt(arr[7]);
        } catch (NumberFormatException e) {
            afiseazaExceptia("Nu ai numere pentru id-uri" + e.toString());
        }
        editTextDataStart.setText(arr[8]);
        editTextDataFinal.setText(arr[9]);
    }

    private void updateInregistrare(String string) {
        String[] stringArray = string.split(" ");
        String update = "UPDATE " + Explorator.TABLE_NAME + " SET "
                + Explorator.COLUMN_ID_EXPLORATOR + "= " +  idExplorator +", "
                + Explorator.COLUMN_NUME + "= '" + stringArray[0] + "', "
                + Explorator.COLUMN_PRENUME + "= '" + stringArray[1] + "', "
                + Explorator.COLUMN_CNP + "= '" + stringArray[2] + "', "
                + Explorator.COLUMN_NR_SPECIALIZARI + "= " + stringArray[3] + ", "
                + Explorator.COLUMN_GRAD + "= '" + stringArray[4] + "', "
                + Explorator.COLUMN_INSTRUCTOR + "= " + stringArray[5] + ", "
                + Explorator.COLUMN_ID_PARINTE + "= " + stringArray[6] + ", "
                + Explorator.COLUMN_ID_CLUB + "= " + stringArray[7] + ", "
                + Explorator.COLUMN_DATA_START + "= '" + stringArray[8] + "', "
                + Explorator.COLUMN_DATA_FINAL + "= '" + stringArray[9] + "'"
                + " WHERE " + Explorator.COLUMN_ID_EXPLORATOR + "=" + idExplorator + ";";
        update = update.replace("-1", "NULL"); //deoarece int nu poate lua valoarea
        // NULL, trebuie sa convertim -1 in NULL
        Log.e("AdaugExplo.java",stringArray[6] + " " + stringArray[7]);
        Log.e("AdaugExplo.java",update);
        try {
            db.execSQL(update);
        } catch (SQLiteException e) {
            afiseazaExceptia("Nu s-a putut modifica inregistrarea exeploratorului\n"
                    + e.toString() );
        }
    }

    private void afiseazaExceptia(String e) {
        Toast.makeText(this, e,Toast.LENGTH_LONG).show();
    }

    /**
     * <p>funcția returnează numele parintilor din BD si id-urile acestora</p>
     * @return ArrayList<String> numeParinti
     */
    private ArrayList<String> getParinti() {
        ArrayList<String> numeParinti = new ArrayList<>();
        if (db == null) return numeParinti;
        String SQLquery = "SELECT " + Parinti.COLUMN_ID_PARINTE + ", " + Parinti.COLUMN_NUME
                + ", " + Parinti.COLUMN_PRENUME + " FROM " + Parinti.TABLE_NAME;
        Cursor c = db.rawQuery(SQLquery, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Parinti.COLUMN_NUME));
                String prenume = c.getString(c.getColumnIndex(Parinti.COLUMN_PRENUME));
                int id = c.getInt(c.getColumnIndex(Parinti.COLUMN_ID_PARINTE));
                idsParinte.add(id);
                numeParinti.add(nume + " " + prenume + " (" + id + ")");
            } while (c.moveToNext());
        }
        c.close();
        return numeParinti;
    }

    /**
     * Functia existaParinti are rolul de a verifica daca exista tabela Parinti in baza de date
     * @return true daca exista tabela parinti in baza de date altfel false
     */
    private boolean existaParinti() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='" + Parinti.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "Adauga mai intai parinti.", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    private boolean existaCluburi() {
        if (db == null) {
            Toast.makeText(this, "Creaza mai întâi baza de date și apoi cauta in ea", Toast.LENGTH_SHORT).show();
            return false;
        }
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + Cluburi.TABLE_NAME + "'";
        Cursor c = db.rawQuery(query, null);
        boolean existaCluburi = c.getCount() != 0;
        c.close();
        return existaCluburi;
    }

    /**
     * Functia are rolul de a extrage din baza de date nume și id-urile Conferințelor
     * @return numeCluburi
     */
    private ArrayList<String> getCluburi() {
        ArrayList<String> numeCluburi = new ArrayList<>();
        String query = "SELECT " + Cluburi.COLUMN_NUME + ", " + Cluburi.COLUMN_ID_CLUB
                + " FROM " + Cluburi.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Cluburi.COLUMN_NUME));
                int id = c.getInt(c.getColumnIndex(Cluburi.COLUMN_ID_CLUB));
                idsClub.add(id);
                numeCluburi.add(nume + " (" + id + ")");
            } while (c.moveToNext());
        }
        c.close();
        return numeCluburi;
    }

    private void startAnimation(){
        Animation animation = AnimationUtils.loadAnimation(AdaugExploratorActivity.this,R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator(0.3,16);
        animation.setInterpolator(bounceInterpolator);
        buttonAdauga.startAnimation(animation);
    }

    public void onBackPressed(){
        if (mModif == 0) {
            Intent it = new Intent(AdaugExploratorActivity.this, MainActivity.class);
            startActivity(it);
        } else {
            Intent it = new Intent(AdaugExploratorActivity.this, AlegeExploratorActivity.class);
            startActivity(it);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
