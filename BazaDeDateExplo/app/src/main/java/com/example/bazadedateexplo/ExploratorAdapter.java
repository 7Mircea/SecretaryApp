package com.example.bazadedateexplo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

public class ExploratorAdapter extends ArrayAdapter<ExploratorClass> {
    SQLiteDatabase db;
    Ajutator ajutator;
    public ExploratorAdapter(Activity context, ArrayList<ExploratorClass> exploratori, SQLiteDatabase db) {
        super(context, 0, exploratori);
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
        ajutator = new Ajutator(db);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        final ExploratorClass explorator = getItem(position);

        TextView textView = listItemView.findViewById(R.id.textView);
        textView.setText(explorator.getmNume() + " " + explorator.getmPrenume() + " ("
        + explorator.getmId() + ")");

        final String detalii = explorator.getmNume() +"\n" +
                explorator.getmPrenume() + "\n"
                + explorator.getmCNP() + "\n"
                + explorator.getmNrSpecializari() + "\n"
                + explorator.getmGrad() + "\n"
                + explorator.getmInstructor() + "\n"
                + explorator.getmIDParinte() + "\n"
                + explorator.getmIDClub() + "\n"
                + explorator.getmDataStart() + "\n"
                + explorator.getmDataFinal();
        final String mesaj = Explorator.COLUMN_NUME + ": " + explorator.getmNume() +"\n"
                + Explorator.COLUMN_PRENUME + ": " + explorator.getmPrenume() + "\n"
                + Explorator.COLUMN_CNP + ": " + explorator.getmCNP() + "\n"
                + "Număr specializări: " + explorator.getmNrSpecializari() + "\n"
                + Explorator.COLUMN_GRAD + ": " + explorator.getmGrad() + "\n"
                + Explorator.COLUMN_INSTRUCTOR + ": " + convertToLogic(explorator.getmInstructor()) + "\n"
                + "Părinte : " + ajutator.getNumeParinte(explorator.getmIDParinte()) + "\n"
                + "Club : "+ ajutator.getNumeClub(explorator.getmIDClub()) + "\n"
                + "Data start: " + explorator.getmDataStart() + "\n"
                + "Data final: " + explorator.getmDataFinal();
        final Button button1 = listItemView.findViewById(R.id.button1);
        button1.setText(getContext().getResources().getText(R.string.detalii));
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Detalii : ");
                builder.setMessage(mesaj);
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
                Intent it = new Intent(getContext(), AdaugExploratorActivity.class);
                it.putExtra(ConstanteBDExplo.MODIF,1);
                it.putExtra(ConstanteBDExplo.ID,explorator.getmId());
                it.putExtra(ConstanteBDExplo.DETALII,detalii);
                getContext().startActivity(it);
            }
        });

        Button buttonSterge = listItemView.findViewById(R.id.buttonSterge);
        buttonSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stergeExploratorFromDB(explorator.getmId());
                remove(explorator);
                notifyDataSetChanged();
            }
        });

        return listItemView;

    }

    private boolean convertToLogic(int i) {
        return i != 0;
    }

    private void stergeExploratorFromDB(int id) {
        String stergeExplorator = "DELETE FROM " + Explorator.TABLE_NAME + " WHERE "
                + Explorator.COLUMN_ID_EXPLORATOR + "=" + id + ";";
        try {
            db.execSQL(stergeExplorator);
        } catch (SQLiteException e) {
            Toast.makeText(getContext(),"Nu s-a putut sterge exploratorul" + e,Toast.LENGTH_LONG).show();
        }
    }
}
