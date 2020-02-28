package com.example.bazadedateexplo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.strictmode.SqliteObjectLeakedViolation;
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

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class ParinteAdapter extends ArrayAdapter<ParinteClass> {
    SQLiteDatabase db;

    public ParinteAdapter(Activity context, ArrayList<ParinteClass> parinti, SQLiteDatabase db) {
        super(context, 0,parinti);
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null ) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final ParinteClass parinte = getItem(position);

        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(parinte.getNume() + " " + parinte.getPrenume() + " ("
                + parinte.getIdParinte() + ")");

        final String detalii = parinte.getNume() + "\n" +
                parinte.getPrenume() + "\n"
                + parinte.getGen() + "\n"
                + parinte.getNrDeTelefon();
        Button button1 = listItemView.findViewById(R.id.button1);
        button1.setText(R.string.detalii);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pornesteDialog(position);
            }
        });

        Button button2 = listItemView.findViewById(R.id.button2);
        button2.setText(R.string.modifica);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(),AdaugParinteActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF,1);
                it.putExtra(ConstanteBDExplo.ID,parinte.getIdParinte());
                it.putExtra(ConstanteBDExplo.DETALII, detalii);
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stergeParinte(parinte.getIdParinte())) {
                    remove(parinte);
                    notifyDataSetChanged();
                }
            }
        });

        return listItemView;
    }

    private void pornesteDialog(int position) {
        try {
            ParinteClass parinte = getItem(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(getContext().getResources().getString(R.string.alege));
            builder.setMessage(Parinti.COLUMN_NUME + ": " + parinte.getNume() + " "
                    + parinte.getPrenume() + " (" + parinte.getIdParinte() + ")\n"
                    + Parinti.COLUMN_GEN + ": " + parinte.getGen() + "\n"
                    + Parinti.COLUMN_NR_TELEFON + ": " + parinte.getNrDeTelefon());
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
        } catch (NullPointerException e) {
            Log.e("ParinteAdapter.java","NullPointerException " + e.toString());
        }
    }

    private boolean stergeParinte(int id) {
        boolean error = false;
        String deleteParinte = "DELETE FROM " + Parinti.TABLE_NAME + " WHERE "
                + Parinti.COLUMN_ID_PARINTE + "=" + id + ";";
        try {
            db.execSQL(deleteParinte);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(), "Nu a putut fi sters parintele. " + e,Toast.LENGTH_LONG).show();
            error = true;
        }
        return !error;
    }
}
