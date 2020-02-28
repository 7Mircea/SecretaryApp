package com.example.bazadedateexplo;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class ExploInProiectAdapter extends ArrayAdapter<ExploInProiectClass> {
    SQLiteDatabase db;

    ExploInProiectAdapter(Activity context, ArrayList<ExploInProiectClass> lista, SQLiteDatabase db) {
        super(context,-1,lista);
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        final ExploInProiectClass exploInProiect = getItem(position);

        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(getNumeExplorator(exploInProiect.getIdExplorator())
                + " participa in proiectul " + getNumeProiect(exploInProiect.getIdProiect()));

        final String detalii = "Nume Proiect: " + getNumeProiect(exploInProiect.getIdProiect()) +"\n"
                + "Nume explorator: " + getNumeExplorator(exploInProiect.getIdExplorator()) + "\n"
                + "Nr ore petrecute de explorator în proiect: " + exploInProiect.getNrOre();
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
                Intent it = new Intent(getContext(), AdaugExploInProiectActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF,1);
                it.putExtra(ConstanteBDExplo.ID,exploInProiect.getId());
                String proiectString = getNumeProiect(exploInProiect.getIdProiect()) + "\n"
                        + getNumeExplorator(exploInProiect.getIdExplorator()) + "\n"
                        + exploInProiect.getNrOre();
                it.putExtra(ConstanteBDExplo.DETALII,proiectString);
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stergeExploInProiectFromDB(exploInProiect.getId())) {
                    remove(exploInProiect);
                    notifyDataSetChanged();
                }
            }
        });

        return listItemView;

    }

    private String getNumeProiect(int id) {
        String nume = null;
        String query = "SELECT " + Proiecte.COLUMN_NUME + " FROM " + Proiecte.TABLE_NAME
                + " WHERE " + Proiecte.COLUMN_ID_PROIECTE + "=" + id + ";";
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()) {
            nume = c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME));
        }
        nume = nume + " (" + id + ")";
        c.close();
        return nume;
    }

    private String getNumeExplorator(int id) {
        String nume = null;
        String prenume = null;
        String query = "SELECT " + Explorator.COLUMN_NUME + ", " + Explorator.COLUMN_PRENUME
                +" FROM " + Explorator.TABLE_NAME
                + " WHERE " + Explorator.COLUMN_ID_EXPLORATOR + "=" + id + ";";
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst()) {
            nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
            prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
        }
        nume = nume + " " + prenume + " (" + id + ")";
        c.close();
        return nume;
    }

    private boolean stergeExploInProiectFromDB(int id) {
        boolean error = false;
        String stergeExploInProiect = "DELETE FROM " + ExploratoriInProiecte.TABLE_NAME + " WHERE "
                + ExploratoriInProiecte.COLUMN_ID + "=" + id + ";";
        try {
            db.execSQL(stergeExploInProiect);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(),
                    "Nu s-a putut sterge legatura dintre explorator si proiect " + e,
                    Toast.LENGTH_LONG).show();
            error = true;
        }
        return !error;
    }
}
