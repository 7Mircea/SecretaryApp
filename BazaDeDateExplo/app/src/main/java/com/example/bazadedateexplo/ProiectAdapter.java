package com.example.bazadedateexplo;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class ProiectAdapter extends ArrayAdapter<ProiectClass> {
    SQLiteDatabase db;

    ProiectAdapter(Activity context, ArrayList<ProiectClass> list, SQLiteDatabase db) {
        super(context,-1,list);
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final ProiectClass proiect = getItem(position);

        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(proiect.getNume() + " (" + proiect.getIdProiect() + ")");

        final String detalii = Proiecte.COLUMN_NUME + ": " + proiect.getNume() +"\n"
                + Proiecte.COLUMN_DATA_START + ": " + proiect.getDataStart() + "\n"
                + Proiecte.COLUMN_DATA_FINAL + ": " + proiect.getDataFinal() + "\n"
                + Proiecte.COLUMN_DESCRIERE_SCURTA + ": " + proiect.getDescriereScurta();
        final Button button1 = listItemView.findViewById(R.id.button1);
        button1.setText(getContext().getResources().getText(R.string.detalii));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Detalii : ");
                builder.setMessage(detalii);
                Button button1 = new Button(getContext());
                button1.setText("Am citit");
                builder.setView(button1);
                final AlertDialog dialog = builder.create();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        Button button2 = listItemView.findViewById(R.id.button2);
        button2.setText(getContext().getResources().getText(R.string.modifica));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), AdaugProiectActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF,1);
                it.putExtra(ConstanteBDExplo.ID,proiect.getIdProiect());
                String proiectString = proiect.getNume() + "\n" + proiect.getDataStart()
                        + "\n" + proiect.getDataFinal() + "\n" + proiect.getDescriereScurta();
                it.putExtra(ConstanteBDExplo.DETALII,proiectString);
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stergeProiectFromDB(proiect.getIdProiect())) {
                    remove(proiect);
                    notifyDataSetChanged();
                }
            }
        });

        return listItemView;

    }

    private boolean stergeProiectFromDB(int id) {
        boolean error = false;
        String stergeProiect = "DELETE FROM " + Proiecte.TABLE_NAME + " WHERE "
                + Proiecte.COLUMN_ID_PROIECTE + "=" + id + ";";
        try {
            db.execSQL(stergeProiect);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(),"Nu s-a putut sterge proiectul " + e,Toast.LENGTH_LONG).show();
            error = true;
        }
        return !error;
    }
}

