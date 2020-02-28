package com.example.bazadedateexplo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.zip.Inflater;
import com.example.bazadedateexplo.ConstanteBDExplo.*;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import static android.content.Context.MODE_PRIVATE;

public class ClubAdapter extends ArrayAdapter<ClubClass> {
    SQLiteDatabase db;
    Ajutator ajutator;

    public ClubAdapter(Activity context, ArrayList<ClubClass> items, SQLiteDatabase db) {
        super(context, 0, items);
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
        ajutator = new Ajutator(context, this.db);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final ClubClass club = getItem(position);
        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(club.getNume() + " ("
                + club.getIdClub() + ")");

        final String detalii = club.getNume() + "\n" +
                club.getIdConferinta() + "\n"
                + club.getIdConducator();
        final Button button1 = listItemView.findViewById(R.id.button1);
        button1.setText(getContext().getResources().getText(R.string.detalii));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pornesteDialog(position);
            }
        });

        Button button2 = listItemView.findViewById(R.id.button2);
        button2.setText(getContext().getResources().getText(R.string.modifica));
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), AdaugClubActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF, 1);
                it.putExtra(ConstanteBDExplo.ID, club.getIdClub());
                it.putExtra(ConstanteBDExplo.DETALII, detalii);
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stergeClub(club.getIdClub())) {
                    remove(club);
                    notifyDataSetChanged();
                }
            }
        });

        return listItemView;
    }

    private void pornesteDialog(int position) {
        try {
            ClubClass club = getItem(position);
            String[] info = getClubInfo(club.getIdClub());

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getResources().getString(R.string.alege));
            builder.setMessage(Cluburi.COLUMN_NUME + ": " + club.getNume() + "\n"
                    + "Aparține de Conferința: " + info[2] + "\n"
                    + "Conducător: " + info[0] + " " + info[1]);
            Button button = new Button(getContext());
            button.setText(R.string.am_citit);
            builder.setView(button);
            final AlertDialog dialog = builder.create();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private String[] getClubInfo(int id) {
        try {
            if (ajutator.existsTableConferinte() && ajutator.existsTableCluburi() && ajutator.existsTableExploratori()) {
                String query = "SELECT E.Nume, E.Prenume, CF.Nume" +
                        " FROM " + Cluburi.TABLE_NAME + " C LEFT JOIN "
                        + Conferinte.TABLE_NAME + " CF ON C.IDConferinta" +
                        "=CF.IDConferinta LEFT JOIN " + Explorator.TABLE_NAME
                        + " E ON C.IDConducator = E.IDExplorator WHERE C.IDClub = " + id;
                SQLiteDatabase db = getContext().openOrCreateDatabase(ConstanteBDExplo.DB_NAME, MODE_PRIVATE, null);
                Cursor c = db.rawQuery(query, null);
                String[] info = new String[3];
                if (c.moveToFirst()) {
                    info[0] = c.getString(0);
                    info[1] = " " + c.getString(1);
                    info[2] = " " + c.getString(2);
                } else {
                    info[0] = "?";
                    info[1] = "?";
                    info[2] = "?";
                }
                c.close();
                return info;
            } else {
                Toast.makeText(getContext(),"Unul din urmatoarele tabeluri nu exista: " +
                        "Exploratori, Conferinte, Cluburi", Toast.LENGTH_LONG).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(getContext(),getContext().getResources().getString(R.string.notificare_eroare_sqlite)
                    + " Eroarea" + e.toString(), Toast.LENGTH_LONG).show();
        }
        return null;

    }

    private boolean stergeClub(int id) {
        boolean error = false;
        String deleteClub = "DELETE FROM " + Cluburi.TABLE_NAME + " WHERE "
                + Cluburi.COLUMN_ID_CLUB + "=" + id + ";";
        Log.e("ClubAdapter.java",deleteClub);
        try {
            db.execSQL(deleteClub);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(), "Nu s-a reusit stergerea clubului",Toast.LENGTH_LONG).show();
            error = true;
        }
        return !error;
    }
}
