package com.example.bazadedateexplo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Aceasta clasă conține interogările complexe ale baze de date.
 */
public class Cauta2Activity extends AppCompatActivity {
    private SQLiteDatabase db; // baza de date a aplicatiei
    private Spinner spinner2Q1; // contine variantele de conferinte pentru interogarea 1
    private Spinner spinner2Q3_1; // contine variantele de conferinte din care sa nu fie
    // participanti in proiecte pentru interogarea 3
    private Spinner spinner2Q3_2; // contine variantele de conferinte din care sa fie
    // participanti in proiecte pentru interogarea 3
    private EditText editText2Q3; // contine numarul de ore introdus de utilizator
    private EditText editText2Q4; // contine numarul de ore introdus de utilizator
    private Button button2Q1; // butonul care porneste cautarea pentru interogarea 1
    private Button button2Q2; // butonul care porneste cautarea pentru interogarea 2
    private Button button2Q3; // butonul care porneste cautarea pentru interogarea 3
    private Button button2Q4; // butonul care porneste cautarea pentru interogarea 4

    private Ajutator ajutator;
    private Cautator cautator;
    private ArrayList<String> numeConferinte;
    private String numeConferinta2Q1;
    private String numeConferinta2Q3_1;
    private String numeConferinta2Q3_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauta_complex);
        setTitle(R.string.titluCauta2);

        // setam legatura cu fisierul .xml care contine descriere interfetei vizuale
        spinner2Q1  =findViewById(R.id.spinner2Q1);
        spinner2Q3_1 = findViewById(R.id.spinner2Q3_1);
        spinner2Q3_2 = findViewById(R.id.spinner2Q3_2);
        button2Q1 = findViewById(R.id.button2Q1);
        button2Q2 = findViewById(R.id.button2Q2);
        button2Q3 = findViewById(R.id.button2Q3);
        button2Q4 = findViewById(R.id.button2Q4);
        editText2Q3 = findViewById(R.id.editText2Q3);
        editText2Q4 = findViewById(R.id.editText2Q4_1);

        // setam legatura cu baza de date
        stabilesteLegaturaCuDB();

        //setam fiecare interogare complexa
        seteazaPrimulQuery();
        seteazaAlDoileaQuery();
        seteazaAlTreileaQuery();
        seteazaAlPatruleaQuery();
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
        ajutator = new Ajutator(Cauta2Activity.this,db);
        cautator = new Cautator(Cauta2Activity.this,db);
    }

    private void seteazaPrimulQuery() {
        numeConferinte = ajutator.getNumeConferinte();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numeConferinte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2Q1.setAdapter(adapter);
        spinner2Q1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //parent.getChildAt(position).setSelected(true);
                numeConferinta2Q1 = numeConferinte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeConferinta2Q1 = numeConferinte.get(0);
            }
        });
        seteazaButtonCautare1();
    }

    private void seteazaButtonCautare1() {
        button2Q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cauta2Activity.this);
                StringBuilder message = new StringBuilder(getResources().getString(R.string.textView2Q1) + " "
                        + numeConferinta2Q1 + "\n") ;
                ArrayList<String> numeExploratori = cautator.getComplexQuery1(numeConferinta2Q1);
                for (String nume : numeExploratori)
                    message.append(nume + "\n");
                builder.setMessage(message.toString());
                builder.setNegativeButton(R.string.am_citit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void seteazaAlDoileaQuery() {
        seteazaButtonCautare2();
    }

    private void seteazaButtonCautare2() {
        button2Q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cauta2Activity.this);
                StringBuilder message = new StringBuilder(
                        getResources().getString(R.string.textView2Q2) + ":\n") ;
                ArrayList<String> numeCluburi = cautator.getComplexQuery2();
                for (String nume : numeCluburi)
                    message.append(nume + "\n");
                builder.setMessage(message.toString());
                builder.setNegativeButton(R.string.am_citit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void seteazaAlTreileaQuery() {
        numeConferinte = ajutator.getNumeConferinte();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numeConferinte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2Q3_1.setAdapter(adapter);
        spinner2Q3_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // parent.getChildAt(position).setSelected(true);
                numeConferinta2Q3_1 = numeConferinte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeConferinta2Q3_1 = numeConferinte.get(0);
            }
        });
        spinner2Q3_1.setAdapter(adapter);
        spinner2Q3_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //parent.getChildAt(position).setSelected(true);
                numeConferinta2Q3_2 = numeConferinte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeConferinta2Q3_2 = numeConferinte.get(0);
            }
        });
        seteazaButtonCautare3();
    }

    private void seteazaButtonCautare3() {
        button2Q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // la apasarea butonului pentru interogarea trei va aparea o pagina de tip notificare
                // care practic va afișa numele tuturor proiectelor
                AlertDialog.Builder builder = new AlertDialog.Builder(Cauta2Activity.this);
                StringBuilder message = new StringBuilder();
                ArrayList<String> numeProiecte = cautator.getComplexQuery3();
                for (String nume : numeProiecte)
                    message.append(nume + "\n");
                builder.setMessage(message.toString());
                builder.setNegativeButton(R.string.am_citit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void seteazaAlPatruleaQuery() {
        int nrOre = 0;
        try {
            nrOre = Integer.parseInt(editText2Q4.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this,R.string.notificare_format_numar,Toast.LENGTH_LONG).show();
        }
        seteazaButtonCautare4(nrOre);
    }

    private void seteazaButtonCautare4(final int nrOre) {
        button2Q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // la apasarea butonului pentru interogarea 4 va aparea o pagina de tip notificare
                // care practic va afișa numele tuturor conferințelor
                AlertDialog.Builder builder = new AlertDialog.Builder(Cauta2Activity.this);
                StringBuilder message = new StringBuilder(
                        getResources().getString(R.string.textView2Q4_1) + nrOre
                                + getResources().getString(R.string.textView2Q4_2) + "\n");
                ArrayList<String> numeProiecte = cautator.getComplexQuery4(nrOre);
                for (String nume : numeConferinte)
                    message.append(nume + "\n");
                builder.setMessage(message.toString());
                builder.setNegativeButton(R.string.am_citit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(Cauta2Activity.this, CautaActivity.class);
        startActivity(it);
    }
}
