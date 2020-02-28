package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.util.ArrayList;

import static com.example.bazadedateexplo.ConstanteBDExplo.DB_NAME;
import static com.example.bazadedateexplo.ConstanteBDExplo.MODIF;

public class AdaugProiectActivity extends AppCompatActivity {
    private EditText editTextNume;
    private EditText editTextDataStart;
    private EditText editTextDataFinal;
    private EditText editTextDescriereScurta;

    private Button buttonAdauga;
    private SQLiteDatabase db;

    private int mModif;
    private int idProiect;
    private String detalii;

    //bloc de initializare
    {
        mModif = 0;
        idProiect = -1;
        detalii = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaug_proiect);

        // salvăm referințele obiectelor de pe layout pentru a putea să le accesăm
        // conținutul
        editTextNume = findViewById(R.id.editTextNume);
        editTextDataStart = findViewById(R.id.editTextDataStart);
        editTextDataFinal = findViewById(R.id.editTextDataFinal);
        editTextDescriereScurta = findViewById(R.id.editTextDescriereScurta);

        buttonAdauga = findViewById(R.id.buttonAdauga);

        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);

        //creeazaTabelProiecte();

        Intent it = getIntent();
        seteazaLaValorileAnterioare(it);

        seteazaButonAdauga();
    }

    private void creeazaTabelProiecte() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + Proiecte.TABLE_NAME
                    + "(" + Proiecte.COLUMN_ID_PROIECTE + " INTEGER primary key AUTOINCREMENT, "
                    + Proiecte.COLUMN_NUME + " TEXT, "
                    + Proiecte.COLUMN_DATA_START + " TEXT, "
                    + Proiecte.COLUMN_DATA_FINAL + " TEXT, "
                    + Proiecte.COLUMN_DESCRIERE_SCURTA + " TEXT);";
            db.execSQL(query);
        } catch (SQLiteException e) {
            Toast.makeText(AdaugProiectActivity.this, R.string.notificare_eroare_sqlite + ". Eroare : " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void seteazaLaValorileAnterioare(Intent it) {
        mModif = it.getIntExtra(MODIF,0);
        if (mModif == 0) return;
        idProiect = it.getIntExtra(ConstanteBDExplo.ID, -1);
        detalii = it.getStringExtra(ConstanteBDExplo.DETALII);
        Log.e("AdaugConf.java", detalii);
        seteazaCampurile();
    }

    private void seteazaCampurile() {
        String[] arr = detalii.split("\n");
        editTextNume.setText(arr[0]);
        editTextDataStart.setText(arr[1]);
        editTextDataFinal.setText(arr[2]);
        editTextDescriereScurta.setText(arr[3]);
    }


    private void seteazaButonAdauga() {
        buttonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume = editTextNume.getText().toString();
                String dataStart = editTextDataStart.getText().toString();
                String dataFinal = editTextDataFinal.getText().toString();
                String descriereScurta = editTextDescriereScurta.getText().toString();
                if (mModif == 0) {
                    String addProiecte = "INSERT INTO " + Proiecte.TABLE_NAME +
                            " ( " + Proiecte.COLUMN_NUME + ", " + Proiecte.COLUMN_DATA_START
                            + ", " + Proiecte.COLUMN_DATA_FINAL + ", " + Proiecte.COLUMN_DESCRIERE_SCURTA
                            + ") VALUES ('" + nume + "', '" + dataStart + "', '"
                            + dataFinal + "', '" + descriereScurta + "');";
                    Log.i("AdaugProiecte.java", addProiecte);
                    db.execSQL(addProiecte);
                } else {
                    String update = "UPDATE " + Proiecte.TABLE_NAME + " SET "
                            + Proiecte.COLUMN_ID_PROIECTE + "= " + idProiect + ", "
                            + Proiecte.COLUMN_NUME + "= '" + nume + "', "
                            + Proiecte.COLUMN_DATA_START + "= '" + dataStart + "', "
                            + Proiecte.COLUMN_DATA_FINAL + "= '" + dataFinal + "', "
                            + Proiecte.COLUMN_DESCRIERE_SCURTA + "= '" + descriereScurta + "'"
                            + " WHERE " + Proiecte.COLUMN_ID_PROIECTE + "=" + idProiect + ";";
                    Log.e("AdaugProiect.java", update);
                    try {
                        db.execSQL(update);
                    } catch (SQLiteException e) {
                        afiseazaExceptia("Nu s-a putut modifica inregistrarea conferintei.\n"
                                + e.toString());
                    }
                }
                addAnimation();
            }
        });
    }

    private void afiseazaExceptia(String e) {
        Toast.makeText(this, e,Toast.LENGTH_LONG).show();
    }

    private void addAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AdaugProiectActivity.this, R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        animation.setInterpolator(bounceInterpolator);
        buttonAdauga.startAnimation(animation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        Intent it;
        if (mModif == 0)
            it = new Intent(this, MainActivity.class);
        else
            it = new Intent(this, AlegeProiectActivity.class);
        startActivity(it);
    }
}
