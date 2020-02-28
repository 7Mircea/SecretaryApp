package com.example.bazadedateexplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
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

public class AdaugExploInProiectActivity extends AppCompatActivity {
    private EditText editTextNrOre;
    private ListView listViewExploratori;
    private ListView listViewProiecte;
    private ArrayList<Integer> idsExploratori;
    private ArrayList<Integer> idsProiecte;
    private int idExplorator = -1;
    private int idProiect = -1;
    private int mModif;
    private int idExploInProiecte = -1;
    private SQLiteDatabase db;
    private Button buttonAdauga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explo_in_proiect);

        editTextNrOre = findViewById(R.id.editTextNrOre);
        listViewExploratori = findViewById(R.id.listViewExploratori);
        listViewProiecte = findViewById(R.id.listViewProiecte);
        buttonAdauga = findViewById(R.id.buttonAdauga);

        db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);

        seteazaValorileAnterioare(); //se executa doar daca se ajunge aici din AlegeExploInProiectActivity.java
        seteazaListViewExploratori();
        seteazaListViewProiecte();

        seteazaButonAdauga();
    }

    private void seteazaListViewExploratori() {
        try {
            ArrayList<String> numeExploratori = getExploratori();
            ListAdapter adapterExplo = new ListAdapter(this, numeExploratori);
            listViewExploratori.setAdapter(adapterExplo);
            listViewExploratori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    idExplorator = idsExploratori.get(position);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void seteazaValorileAnterioare() {
        Intent it = getIntent();
        mModif = it.getIntExtra(ConstanteBDExplo.MODIF,0);
        if (mModif == 1) {
            String string = it.getStringExtra(ConstanteBDExplo.DETALII);
            idExploInProiecte = it.getIntExtra(ConstanteBDExplo.ID,-1);
            try {
                String[] strings = string.split("\n");
                Toast toast = Toast.makeText(this,strings[1] + " participa in proiectul" + strings[0],Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                editTextNrOre.setText(Integer.parseInt(strings[2]) + "");
            } catch (NullPointerException | NumberFormatException e) {
                afiseazaEroare(e.toString());
            }
        }
    }

    private ArrayList<String> getExploratori() throws Exception {
        ArrayList<String> arrayListExploratori = new ArrayList<>();
        if (db == null) {
            Toast.makeText(this, R.string.notificare_lipsa_bd, Toast.LENGTH_SHORT).show();
            throw new Exception(getResources().getString(R.string.notificare_lipsa_bd));
        }
        if (!existaExploratori()) {
            Toast.makeText(this, R.string.notificare_lipsa_instructori, Toast.LENGTH_SHORT).show();
            throw new Exception(getResources().getString(R.string.notificare_lipsa_exploratori));
        }
        String selectExploratori = "SELECT " + ConstanteBDExplo.Explorator.COLUMN_NUME + ", "
                + ConstanteBDExplo.Explorator.COLUMN_ID_EXPLORATOR
                + " FROM " + ConstanteBDExplo.Explorator.TABLE_NAME;
        Cursor c = db.rawQuery(selectExploratori, null);

        idsExploratori = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(ConstanteBDExplo.Explorator.COLUMN_NUME));
                Integer idExplorator = c.getInt(c.getColumnIndex(ConstanteBDExplo.Explorator.COLUMN_ID_EXPLORATOR));
                arrayListExploratori.add(nume + "(" + idExplorator + ")");
                idsExploratori.add(idExplorator);
            } while (c.moveToNext());
        }
        c.close();
        return arrayListExploratori;
    }

    public boolean existaExploratori() {
        if (!existaTabelExploratori()) return false;
        String existaInreg = "SELECT * FROM " + ConstanteBDExplo.Explorator.TABLE_NAME;
        Cursor c = db.rawQuery(existaInreg, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public boolean existaTabelExploratori() {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                + ConstanteBDExplo.Explorator.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private void seteazaButonAdauga() {
        buttonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nrOreString = editTextNrOre.getText().toString();
                int nrOre;
                try {
                    nrOre = Integer.parseInt(nrOreString);
                } catch (NumberFormatException e) {
                    afiseazaEroare("Nu ați introdus numarul de ore ca numar\n"+e.toString());
                    return;
                }
                // daca suntem pe aceasta pagina pentru a adauga doar exploratori
                // in proiecte
                if (mModif == 0)
                    addExploInProiect(nrOre);
                else //altfel daca suntem pentru a actualiza legatura dintre exploratori si proiecte
                    updateInregistrare(nrOre);
                addAnimation();
                onBackPressed();
            }
        });
    }

    private void updateInregistrare(int nrOre) {
        try {
            String sql = "UPDATE " + ExploratoriInProiecte.TABLE_NAME + " SET "
                    + ExploratoriInProiecte.COLUMN_ID_PROIECT + "=" + idProiect + ","
                    + ExploratoriInProiecte.COLUMN_ID_EXPLORATOR + "=" + idExplorator + ","
                    + ExploratoriInProiecte.COLUMN_NR_ORE + "=" + nrOre
                    + " WHERE " + ExploratoriInProiecte.COLUMN_ID + "=" + idExploInProiecte
                    + ";";
            // Log.e("AdaugExploInProi",sql);
            db.execSQL(sql);
        } catch (SQLiteConstraintException e) {
            afiseazaEroare(getResources().getString(R.string.notificare_nerespectare_constrangeri));
            Log.e("AdaugExploInProiec.java",e.toString());
        } catch (SQLiteException e) {
            afiseazaEroare(getResources().getString(R.string.notificare_eroare_sqlite));
            Log.e("AdaugExploInProiec.java",e.toString());
        }
    }

    private void addExploInProiect(int nrOre) {
        String addExploProiecte;
        try {
            addExploProiecte = "INSERT INTO " + ExploratoriInProiecte.TABLE_NAME
                    + "(" + ExploratoriInProiecte.COLUMN_ID_PROIECT + ", " +
                    ExploratoriInProiecte.COLUMN_ID_EXPLORATOR + ", " +
                    ExploratoriInProiecte.COLUMN_NR_ORE +
                    ") VALUES (" + idProiect + ", " + idExplorator + ", " + nrOre
                    + ");";
            // Log.e("AdaugProiecte.java", addExploProiecte);
            db.execSQL(addExploProiecte);
        } catch (SQLiteConstraintException e) {
            afiseazaEroare(getResources().getString(R.string.notificare_nerespectare_constrangeri));
            Log.e("AdaugExploInProie.java",e.toString());
        } catch (SQLiteException e) {
            afiseazaEroare(getResources().getString(R.string.notificare_eroare_sqlite));
            Log.e("AdaugExploInProie.java",e.toString());
        }
    }

    private void seteazaListViewProiecte() {
        try {
            ArrayList<String> numeProiecte = getProiecte();
            ListAdapter adapterProiecte = new ListAdapter(this, numeProiecte);
            listViewProiecte.setAdapter(adapterProiecte);
            listViewProiecte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    view.setSelected(true);
                    idProiect = idsProiecte.get(position);
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addAnimation() {
        Animation animation = AnimationUtils.loadAnimation(AdaugExploInProiectActivity.this,R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        animation.setInterpolator(bounceInterpolator);
        buttonAdauga.startAnimation(animation);
    }

    private ArrayList<String> getProiecte() throws Exception {
        if (db == null) throw new Exception(getResources().getString(R.string.notificare_lipsa_bd));
        if (!existaProiecte()) throw new Exception(getResources().getString(R.string.notificare_lipsa_proiecte));
        ArrayList<String> numeProiecte = new ArrayList<>();
        String query = "SELECT " + Proiecte.COLUMN_NUME + ", "
                + Proiecte.COLUMN_ID_PROIECTE + " FROM "
                + Proiecte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        idsProiecte = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME));
                int id = c.getInt(c.getColumnIndex(Proiecte.COLUMN_ID_PROIECTE));
                numeProiecte.add(nume + " (" + id + ")");
                idsProiecte.add(id);
            } while (c.moveToNext());
        }
        return numeProiecte;
    }

    private boolean existaProiecte() {
        if (!existaTabelProiect()) return false;
        String query = "SELECT * FROM " + Proiecte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean existaTabelProiect() {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                Proiecte.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private void afiseazaEroare(String eroare) {
        Toast.makeText(this,eroare,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent it;
        if (mModif == 0) {  //daca suntem pe aceasta pagina pentru a adauga inregistrari
            it = new Intent(AdaugExploInProiectActivity.this, MainActivity.class);
        } else { //altfel daca suntem pentru a actualiza inregistrari
            it = new Intent(AdaugExploInProiectActivity.this, AlegeExploInProiectActivity.class);
        }
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
