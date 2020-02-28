package com.example.bazadedateexplo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

public class ConferintaAdapter extends ArrayAdapter<ConferintaClass> {
    SQLiteDatabase db;

    ConferintaAdapter(Activity context, ArrayList<ConferintaClass> lista, SQLiteDatabase db) {
        super(context, -1, lista);
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        final ConferintaClass conferinta = getItem(position);

        TextView textView = listItemView.findViewById(R.id.textView);
        try {
            textView.setText(conferinta.getNume() + " (" + conferinta.getIdConferinta() + ")");
        } catch (NullPointerException e) {
            Log.e("ConferintaAdapter.java","NullPointerException" + e.toString());
        }

        final String detalii = Conferinte.COLUMN_NUME + ": " + conferinta.getNume() +"\n"
                + "Nume Director: " + getNumeDirector( conferinta.getIdDirector()) + "\n";
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
                Intent it = new Intent(getContext(), AdaugConferintaActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF,1);
                it.putExtra(ConstanteBDExplo.ID,conferinta.getIdConferinta());
                it.putExtra(ConstanteBDExplo.DETALII,conferinta.getNume() + "\n" + conferinta.getIdDirector());
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afiseazaNotificare(position); // afisam faptul ca stergerea Conferintei
                // duce de asemnea la stergerea tuturor cluburilor din ea si a exploratorilor din
                // ele
            }
        });

        return listItemView;

    }

    private void afiseazaNotificare(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Important! ");
        builder.setMessage(R.string.efecte_sterge_conferinta);
        builder.setNegativeButton("Continui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConferintaClass conferinta = getItem(position);
                try {
                    if (stergeConferintaFromDB(conferinta.getIdConferinta())) {
                        remove(conferinta);
                        notifyDataSetChanged();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                    Log.e("ConferintaAdapter.java",e.toString());
                }
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Renunț", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean stergeConferintaFromDB(int id) {
        boolean error = false;
        String stergeConferinta = "DELETE FROM " + Conferinte.TABLE_NAME + " WHERE "
                + Conferinte.COLUMN_ID_CONFERINTA + "=" + id + ";";
        try {
            db.execSQL(stergeConferinta);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(),"Nu s-a putut sterge conferinta" + e,Toast.LENGTH_LONG).show();
            error = true;
        }
        return !error;
    }

    private String getNumeDirector(int idDirector) {
        String nume = null;
        String query = "SELECT " + Explorator.COLUMN_NUME + ", " + Explorator.COLUMN_PRENUME
                + ", " + Explorator.COLUMN_ID_EXPLORATOR + " FROM " + Explorator.TABLE_NAME
                + " WHERE " + Explorator.COLUMN_ID_EXPLORATOR + " = " + idDirector + ";";
        Cursor c = db.rawQuery(query,null);
        if (c.moveToFirst())
            nume = c.getString(0) + " " + c.getString(1) + " ("
                    + c.getString(2) + ")";
        c.close();
        return nume;
    }
}

