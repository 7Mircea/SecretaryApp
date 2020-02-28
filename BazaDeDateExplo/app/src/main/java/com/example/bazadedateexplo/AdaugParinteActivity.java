package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.sql.SQLIntegrityConstraintViolationException;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;
import static com.example.bazadedateexplo.ConstanteBDExplo.DETALII;
import static com.example.bazadedateexplo.ConstanteBDExplo.ID;

public class AdaugParinteActivity extends AppCompatActivity {
    private EditText Nume;
    private EditText Prenume;
    private EditText Gen;
    private EditText NrDeTelefon;
    private Button Adauga;
    private SQLiteDatabase db;
    private int mModif;
    private int idParinte;
    private String detalii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaug_parinte);
        setTitle("Adaugă/Modifică Părinte");

        Intent it = getIntent();
        mModif = it.getIntExtra(ConstanteBDExplo.MODIF,0);

        Nume = findViewById(R.id.ParinteEditTextNume);
        Prenume = findViewById(R.id.ParinteEditTextPrenume);
        Gen = findViewById(R.id.ParinteEditTextGen);
        NrDeTelefon = findViewById(R.id.ParinteEditTextNrTelefon);
        Adauga = findViewById(R.id.ParinteButtonAdauga);

        db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        db.setForeignKeyConstraintsEnabled(true);
        /*
        try {
            String CREATE_TABLE_PARINTE = "CREATE TABLE IF NOT EXISTS "
                    + Parinti.TABLE_NAME + "( "
                    + Parinti.COLUMN_ID_PARINTE + " INTEGER primary key AUTOINCREMENT, "
                    + Parinti.COLUMN_NUME + " TEXT, "
                    + Parinti.COLUMN_PRENUME + " TEXT, "
                    + Parinti.COLUMN_GEN + " TEXT, "
                    + Parinti.COLUMN_NR_TELEFON + " TEXT)";
            db.execSQL(CREATE_TABLE_PARINTE);
        } catch (Exception e) {
            Toast.makeText(AdaugParinteActivity.this, "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
        }*/

        if (mModif == 1) {
            detalii = it.getStringExtra(DETALII);
            idParinte = it.getIntExtra(ID,0);
            seteazaCampurile();
        }

        Adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume = Nume.getText().toString();
                String prenume = Prenume.getText().toString();
                String gen = Gen.getText().toString();
                String nrTelefon = NrDeTelefon.getText().toString();
                if (mModif == 0) { //daca suntem in modul adaugă
                    addParinte(nume, prenume, gen, nrTelefon);
                } else {
                    modifParinte(nume, prenume, gen, nrTelefon);
                }
                addAnimation();
                if (mModif == 1) {
                    Intent it = new Intent(AdaugParinteActivity.this, AlegeParinteActivity.class);
                    startActivity(it);
                }
            }
        });
    }

    private void addParinte(String nume, String prenume, String gen, String nrTelefon) {
        String ADD_PARINTE = "INSERT INTO " + Parinti.TABLE_NAME
                + " ( " + Parinti.COLUMN_NUME + ", "
                + Parinti.COLUMN_PRENUME + ", "
                + Parinti.COLUMN_GEN + ", "
                + Parinti.COLUMN_NR_TELEFON + ") "
                + "VALUES ('"
                + nume + "', '"
                + prenume + "', '"
                + gen + "', '"
                + nrTelefon + "');";
        try {
            db.execSQL(ADD_PARINTE);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(AdaugParinteActivity.this,
                    R.string.notificare_nerespectare_constrangeri,Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
            Toast.makeText(AdaugParinteActivity.this,R.string.notificare_eroare_sqlite,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void modifParinte(String nume, String prenume, String gen, String nrTelefon) {
        String UPDATE_PARINTE = "UPDATE " + Parinti.TABLE_NAME + " SET "
                + Parinti.COLUMN_NUME + "= '" + nume + "', "
                + Parinti.COLUMN_PRENUME + "= '" + prenume + "', "
                + Parinti.COLUMN_GEN + "= '" + gen + "', "
                + Parinti.COLUMN_NR_TELEFON + "= '" + nrTelefon + "' WHERE "
                + Parinti.COLUMN_ID_PARINTE + "=" + idParinte + ";";
        //Log.d("AdaugParinte.java", UPDATE_PARINTE);
        try {
            db.execSQL(UPDATE_PARINTE);
        } catch (SQLiteConstraintException e) {
            Toast.makeText(AdaugParinteActivity.this,R.string.notificare_nerespectare_constrangeri,
                    Toast.LENGTH_LONG).show();
        } catch (SQLiteException e) {
            Toast.makeText(AdaugParinteActivity.this,R.string.notificare_eroare_sqlite,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void seteazaCampurile() {
        String[] strings = detalii.split("\n");
        Nume.setText(strings[0]);
        Prenume.setText(strings[1]);
        Gen.setText(strings[2]);
        NrDeTelefon.setText(strings[3]);
    }

    private void addAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AdaugParinteActivity.this, R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        animation.setInterpolator(bounceInterpolator);
        Adauga.startAnimation(animation);
    }

    @Override
    public void onBackPressed(){
        if (mModif == 0) {
            Intent it = new Intent(AdaugParinteActivity.this, MainActivity.class);
            startActivity(it);
        } else {
            Intent it = new Intent(AdaugParinteActivity.this, AlegeParinteActivity.class);
            startActivity(it);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
