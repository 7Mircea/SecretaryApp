package com.example.bazadedateexplo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CautaActivity extends AppCompatActivity {
    private Spinner spinnerQ1; // contine variantele pentru interogarea 1
    private Spinner spinnerQ2; // contine variantele pentru interogarea 2
    private Spinner spinnerQ3; // contine proiectele posibile pentru interogarea 3
    private Spinner spinnerQ4_1; // contine cluburile posibile pentru interogarea 4
    private Spinner spinnerQ4_2; // contine proiectele posibile pentru interogarea 4
    private Spinner spinnerQ5; // contine proiectele posibile pentru interogarea 5
    private Button buttonQ1; // butonul care porneste cautarea pentru interogarea 1
    private Button buttonQ2; // butonul care porneste cautarea pentru interogarea 2
    private Button buttonQ3; // butonul care porneste cautarea pentru interogarea 3
    private Button buttonQ4; // butonul care porneste cautarea pentru interogarea 4
    private Button buttonQ5; // butonul care porneste cautarea pentru interogarea 5
    private Button buttonQ6; // butonul care porneste cautarea pentru interogarea 6
    private Button buttonNext; // butonul pentru trecerea la urmatoarea pagina
    private EditText editTextQ3; // contine numarul de ore din query 3
    private TextView textView4;

    private Ajutator ajutator;
    private Cautator cautator;
    private SQLiteDatabase db;
    private int idConferinta1 = -1;
    private int idConferinta2 = -1;
    private int idProiect1 = -1;
    private int idProiect4 = -1;
    private int idClub4 = -1;
    private String numeProiect5 = null;
    private String numeConferinta1 = null;
    private String numeConferinta2 = null;
    private String numeProiect3 = null;
    private String numeProiect4 = null;
    private String numeClub4 = null;
    ArrayList<String> numeConferinte;
    ArrayList<String> numeProiecte;
    ArrayList<String> numeCluburi;
    ArrayList<Integer> idsCluburi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauta);

        // stabilim legatura intre obiectele din fisierul .xml si cel .java al acestei activitati
        spinnerQ1 = findViewById(R.id.spinnerConferinte1);
        spinnerQ2 = findViewById(R.id.spinnerConferinte2);
        spinnerQ3 = findViewById(R.id.spinnerQ3);
        spinnerQ4_1 = findViewById(R.id.spinnerQ4_1);
        spinnerQ4_2 = findViewById(R.id.spinnerQ4_2);
        spinnerQ5 = findViewById(R.id.spinnerQ5);
        buttonQ1 = findViewById(R.id.buttonQ1);
        buttonQ2 = findViewById(R.id.buttonQ2);
        buttonQ3 = findViewById(R.id.buttonQ3);
        buttonQ4 = findViewById(R.id.buttonQ4);
        buttonQ5 = findViewById(R.id.buttonQ5);
        buttonQ6 = findViewById(R.id.buttonQ6);
        buttonNext = findViewById(R.id.buttonNext);
        editTextQ3 = findViewById(R.id.editTextQ3);
        textView4 = findViewById(R.id.textViewQ4_4);

        stabilesteLegaturaCuDB();

        seteazaPrimulQuery();
        seteazaAlDoileaQuery();
        seteazaAlTreileaQuery();
        seteazaAlPatruleaQuery();
        seteazaAlCincileaQuery();
        seteazaAlSaseleaQuery();
        seteazaNext();
    }

    private void stabilesteLegaturaCuDB() {
        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
        db.setForeignKeyConstraintsEnabled(true);
        ajutator = new Ajutator(CautaActivity.this, db);
        cautator = new Cautator(CautaActivity.this, db);
    }

    public void seteazaPrimulQuery() {
        numeConferinte = ajutator.getNumeConferinte();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeConferinte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ1.setAdapter(adapter);
        spinnerQ1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeConferinta1 = numeConferinte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeConferinta1 = numeConferinte.get(0);
            }
        });
        seteazaButtonCautare1();
    }

    private void seteazaButtonCautare1() {
        buttonQ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CautaActivity.this);
                builder.setTitle("Numele: ");
                String message = getResources().getString(R.string.InstructoriConf) + " "
                        + numeConferinta1 + "\n";
                message += cautator.getSimpleQuery1(numeConferinta1);
                builder.setMessage(message);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeConferinte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ2.setAdapter(adapter);
        spinnerQ2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeConferinta2 = numeConferinte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeConferinta2 = numeConferinte.get(0);
            }
        });
        seteazaButtonCautare2();
    }

    public void seteazaButtonCautare2() {
        buttonQ2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CautaActivity.this);
                String message = getResources().getString(R.string.ExploratoriConf)
                        + numeConferinta2 + "\n";
                message += cautator.getSimpleQuery2(numeConferinta2);
                builder.setMessage(message);
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

    public void seteazaAlTreileaQuery() {
        numeProiecte = ajutator.getNumeProiecte();
        final ArrayList<Integer> idsProiecte = ajutator.getIdsProiecte();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                numeProiecte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ3.setAdapter(adapter);
        spinnerQ3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeProiect3 = numeProiecte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeProiect3 = numeProiecte.get(0);
            }
        });
        seteazaButtonCautare3();
    }

    public void seteazaButtonCautare3() {
        buttonQ3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nrOreString = editTextQ3.getText().toString();
                int nrOre;
                try {
                    nrOre = Integer.parseInt(nrOreString);
                    AlertDialog.Builder builder = new AlertDialog.Builder(CautaActivity.this);
                    builder.setTitle("Numele: ");
                    String message = getResources().getString(R.string.textViewQ3_1) + " "
                            + numeProiect3 + " "
                            + getResources().getString(R.string.textViewQ3_2) + "  "
                            + nrOre + " ore." + "\n";
                    message += cautator.getSimpleQuery3(numeProiect3, nrOre);
                    builder.setMessage(message);
                    builder.setNegativeButton(R.string.am_citit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (NumberFormatException e) {
                    String mesage = "Nu ati introdus un numar pentru ore.";
                    Toast.makeText(CautaActivity.this, mesage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void seteazaAlPatruleaQuery() {
        numeCluburi = ajutator.getNumeCluburi();
        idsCluburi = ajutator.getIdsCluburi();
        final ArrayList<Integer> idsProiecte = ajutator.getIdsProiecte();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                numeCluburi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ4_1.setAdapter(adapter);
        spinnerQ4_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeClub4 = numeCluburi.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeClub4 = numeCluburi.get(0);
            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(CautaActivity.this, android.R.layout.simple_spinner_item,
                numeProiecte);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ4_2.setAdapter(adapter1);
        spinnerQ4_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeProiect4 = numeProiecte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeProiect4 = numeProiecte.get(0);
            }
        });
        seteazaButtonCautare4();
    }

    public void seteazaButtonCautare4() {
        buttonQ4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int nrParticipanti = cautator.getSimpleQuery4(numeClub4, numeProiect4);
                    textView4.setText(String.valueOf(nrParticipanti));
                } catch (Exception e) {
                    afiseazaEroarea(e.toString());
                }
            }
        });
    }

    public void seteazaAlCincileaQuery() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                numeProiecte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQ5.setAdapter(adapter);
        spinnerQ5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeProiect5 = numeProiecte.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numeProiect5 = numeProiecte.get(0);
            }
        });
        seteazaButtonCautare5();
    }

    public void seteazaButtonCautare5() {
        buttonQ5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CautaActivity.this);
                builder.setTitle("Numele: ");
                StringBuilder message = new StringBuilder(getResources().getString(R.string.textViewQ5) + " "
                        + numeProiect5 + " sunt: \n");
                ArrayList<String> numeParinti = cautator.getSimpleQuery5(numeProiect5);
                for (String nume : numeParinti)
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

    public void seteazaAlSaseleaQuery() {
        // desi am fi putut sa chemam direct seteazaButtonCautare6 din onCreate
        // nu am facut acest lucru pentru pastra structura
        seteazaButtonCautare6();
    }

    public void seteazaButtonCautare6() {
        buttonQ6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CautaActivity.this);
                StringBuilder message =
                        new StringBuilder(getResources().getString(R.string.textViewQ6_1) +
                                " sunt: \n");
                try {
                    ArrayList<String> numeProiecte = cautator.getSimpleQuery6();
                } catch (Exception e) {
                    afiseazaEroarea(e.toString());
                }
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

    private void seteazaNext() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CautaActivity.this, Cauta2Activity.class);
                startActivity(it);
            }
        });
    }

    private void afiseazaEroarea(String message) {
        Toast.makeText(CautaActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }
}
