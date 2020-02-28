package com.example.bazadedateexplo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private Button mButtonAdauga;
    private Button mButtonCauta;
    private Button mButtonModifica;
    private Animation mAnimation;
    private Ajutator ajutator;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seteazaAnimatie();
        seteazaModModificare(0);

        db = openOrCreateDatabase(ConstanteBDExplo.DB_NAME,MODE_PRIVATE,null);
        db.setForeignKeyConstraintsEnabled(true);
        ajutator = new Ajutator(this,db);
        ajutator.creeazaToateTabelele();

        mButtonAdauga = findViewById(R.id.adauga);
        mButtonAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimation(v);
                pornesteDialog();
            }
        });

        mButtonCauta = findViewById(R.id.cauta);
        mButtonCauta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimation(v);
                Intent it = new Intent(MainActivity.this, CautaActivity.class);
                startActivity(it);
            }
        });

        mButtonModifica = findViewById(R.id.modifica);
        mButtonModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimation(v);
                seteazaModModificare(1);
                pornesteDialogAlege();
            }
        });
    }

    private void seteazaAnimatie() {
        mAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
        BounceInterpolator bounceInterpolator = new BounceInterpolator(0.3,10);
        mAnimation.setInterpolator(bounceInterpolator);
    }

    private void addAnimation(View v) {
        v.startAnimation(mAnimation);
    }

    private void seteazaModModificare(int i) {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstanteBDExplo.SHARED_PREFERENCE_FILE,MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt(ConstanteBDExplo.SHARED_PREFERENCE_MODIF,i);
    }

    private void pornesteDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(R.string.alege);
        mBuilder.setSingleChoiceItems(R.array.lista_tabele, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0 :
                        Intent it1 = new Intent(MainActivity.this, AdaugExploratorActivity.class);
                        it1.putExtra(ConstanteBDExplo.MODIF,0);
                        startActivity(it1);
                        break;
                    case 1:
                        Intent it2 = new Intent(MainActivity.this, AdaugParinteActivity.class);
                        it2.putExtra(ConstanteBDExplo.MODIF,0);
                        startActivity(it2);
                        break;
                    case 2 :
                        Intent it3 = new Intent(MainActivity.this,AdaugClubActivity.class);
                        it3.putExtra(ConstanteBDExplo.MODIF,0);
                        startActivity(it3);
                        break;
                    case 3 :
                        Intent it4 = new Intent(MainActivity.this, AdaugConferintaActivity.class);
                        it4.putExtra(ConstanteBDExplo.MODIF,0);
                        startActivityForResult(it4,1);
                        break;
                    case 4 :
                        Intent it5 = new Intent(MainActivity.this, AdaugProiectActivity.class);
                        it5.putExtra(ConstanteBDExplo.MODIF, 0);
                        startActivity(it5);
                        break;
                    case 5 :
                        Intent it6 = new Intent(MainActivity.this, AdaugExploInProiectActivity.class);
                        it6.putExtra(ConstanteBDExplo.MODIF,0);
                        startActivity(it6);
                        break;
                }
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    private void pornesteDialogAlege() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alege);
        builder.setSingleChoiceItems(R.array.lista_tabele, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0 :
                        Intent it1 = new Intent(MainActivity.this, AlegeExploratorActivity.class);
                        startActivity(it1);
                        break;
                    case 1:
                        Intent it2 = new Intent(MainActivity.this, AlegeParinteActivity.class);
                        startActivity(it2);
                        break;
                    case 2 :
                        Intent it3 = new Intent(MainActivity.this,AlegeClubActivity.class);
                        startActivity(it3);
                        break;
                    case 3 :
                        Intent it4 = new Intent(MainActivity.this, AlegeConferintaActivity.class);
                        startActivity(it4);
                        break;
                    case 4 :
                        Intent it5 = new Intent(MainActivity.this, AlegeProiectActivity.class);
                        startActivity(it5);
                        break;
                    case 5 :
                        Intent it6 = new Intent(MainActivity.this, AlegeExploInProiectActivity.class);
                        startActivity(it6);
                        break;
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
