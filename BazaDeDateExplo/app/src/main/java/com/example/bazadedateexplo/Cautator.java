package com.example.bazadedateexplo;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import java.util.ArrayList;

public class Cautator {
    SQLiteDatabase db;
    Activity context;
    Ajutator ajutator;
    Cautator(SQLiteDatabase db) {
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
    }
    Cautator(Activity context,SQLiteDatabase db) {
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);
        this.context = context;
        ajutator = new Ajutator(context, db);
    }

    public String getSimpleQuery1(String numeConferinta) {
        String rezultat = null;
        String query = "SELECT E." + Explorator.COLUMN_NUME + ", E." + Explorator.COLUMN_PRENUME
                + ", E." + Explorator.COLUMN_ID_EXPLORATOR
                + " FROM Exploratori E INNER JOIN Cluburi C ON E.IDClub == C.IDClub"
                + " INNER JOIN Conferinte Co ON Co.IDConferinta == C.IDConferinta"
                + " WHERE Co.Nume = '" + numeConferinta + "' AND E." + Explorator.COLUMN_INSTRUCTOR
                + "= 1";
        try {
            Cursor c = db.rawQuery(query, null);
            StringBuilder stringBuilder = new StringBuilder();
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                    String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                    int id = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                    stringBuilder.append(nume + " " + prenume + " (" + id + ")\n");
                } while (c.moveToNext());
            }
            c.close();
            rezultat = stringBuilder.toString();
        } catch (SQLiteException e) {
            Log.e("Cautator.java","\n\n"+e.toString());
        }
        return rezultat;
    }

    public String getSimpleQuery2(String numeConferinta) {
        String rezultat = null;
        String query = "SELECT E." + Explorator.COLUMN_NUME + ", E." + Explorator.COLUMN_PRENUME
                + ", E." + Explorator.COLUMN_ID_EXPLORATOR
                + " FROM Exploratori E INNER JOIN Cluburi C ON E.IDClub == C.IDClub"
                + " INNER JOIN Conferinte Co ON Co.IDConferinta == C.IDConferinta"
                + " WHERE Co.Nume = " + numeConferinta + Explorator.COLUMN_INSTRUCTOR
                + "= 0";
        try {
            Cursor c = db.rawQuery(query, null);
            StringBuilder stringBuilder = new StringBuilder();
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                    String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                    int id = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                    stringBuilder.append(nume + " " + prenume + " (" + id + ")\n");
                } while (c.moveToNext());
            }
            c.close();
            rezultat = stringBuilder.toString();
        } catch (SQLiteException e) {
            Log.e("Cautator.java","\n\n"+e.toString());
        }
        return rezultat;
    }

    public String getSimpleQuery3(String numeProiect,int nrOre) {
        String rezultat = null;
        String query = "SELECT E." + Explorator.COLUMN_NUME + ", E." + Explorator.COLUMN_PRENUME
                + ", E." + Explorator.COLUMN_ID_EXPLORATOR + " FROM " + Explorator.TABLE_NAME
                + " E INNER JOIN " + ExploratoriInProiecte.TABLE_NAME
                + " EP ON E." + Explorator.COLUMN_ID_EXPLORATOR + " == EP."
                + Explorator.COLUMN_ID_EXPLORATOR
                + " INNER JOIN " + Proiecte.TABLE_NAME
                + " P ON EP." + ExploratoriInProiecte.COLUMN_ID_PROIECT + " == P."
                + Proiecte.COLUMN_ID_PROIECTE + " WHERE EP." + ExploratoriInProiecte.COLUMN_NR_ORE
                + " > " + nrOre +" AND P." + Proiecte.COLUMN_NUME + "='" + numeProiect +"';";
        try {
            Cursor c = db.rawQuery(query, null);
            StringBuilder stringBuilder = new StringBuilder();
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                    String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                    int id = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                    stringBuilder.append(nume + " " + prenume + " (" + id + ")\n");
                } while (c.moveToNext());
            }
            c.close();
            rezultat = stringBuilder.toString();
        } catch (SQLiteException e) {
            Log.e("Cautator.java","\n\n"+e.toString());
        }
        return rezultat;
    }

    public int getSimpleQuery4(String numeClub, String numeProiect) throws Exception {
        int rezultat = 0;
        String query = "SELECT COUNT(E.IDExplorator) AS NrExploratori FROM Exploratori E INNER JOIN "
                + " ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator "
                + " INNER JOIN Cluburi C ON E.IDClub = C.IDClub "
                + " INNER JOIN Proiecte P ON EP.IDProiect = P.IDProiecte "
                + " WHERE P.Nume = '" + numeProiect + "' AND C.Nume = '" + numeClub + "';";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                rezultat = c.getInt(c.getColumnIndex("NrExploratori"));
            }
            c.close();
        } catch (SQLiteException e) {
            Log.e("Cautator.java",e.toString());
            throw new Exception("Nu a putut fi gasit numarul");
        }
        return rezultat;
    }

    public ArrayList<String> getSimpleQuery5(String numeProiect) {
        ArrayList<String> numeParinti = new ArrayList<>();
        Ajutator ajutator;
        if (context != null)
            ajutator = new Ajutator(context, db);
        else
            ajutator = new Ajutator(db);

        if (!ajutator.existsTableExploratori() && !ajutator.existsTableParinti()
                && !ajutator.existsTableExploratoriInProiecte()
                && !ajutator.existsTableProiecte()) return numeParinti;

        String query  = "SELECT P.Nume, P.Prenume FROM Exploratori E INNER JOIN Parinti P "
                + " ON E.IDParinte = P.IDParinte "
                + " INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator "
                + " INNER JOIN Proiecte Pr ON Pr.IDProiecte = EP.IDProiect "
                + " WHERE Pr.Nume = \'"+ numeProiect +"\'";

        Log.e("Cautator.java",query);
        try {
            Cursor c = db.rawQuery(query,null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                    String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                    numeParinti.add(nume + " " + prenume);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            Log.e("Cautator.java", " in getSimpleQuery5()\n" + e.toString());
        }
        return numeParinti;
    }


    public ArrayList<String> getSimpleQuery6() throws Exception {
        ArrayList<String> numeProiecte = new ArrayList<>();
        if (db == null) {
            throw new Exception("Lipseste baza de date");
        }
        //if ()
        String query = "SELECT P.Nume FROM Proiecte P INNER JOIN ExploratoriInProiecte EP ON P.IDProiecte = EP.IDProiect " +
                " INNER JOIN Exploratori E ON EP.IDExplorator = E.IDExplorator " +
                " GROUP BY P.IDProiecte, P.Nume " +
                " HAVING SUM(EP.NrOre) > 0 " +
                " LIMIT 3;";
        Log.e("Cautator.java", query);
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME));
                    numeProiecte.add(nume);
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            Log.e("Cautator.java", "Nu s-a putut citi din tabelul proiecte query simplu 6.");
            if (context != null) {
                Toast.makeText(context, "Nu s-a putut citi din tabelul proiecte query simplu 6.", Toast.LENGTH_LONG).show();
            }
        }
        return numeProiecte;
    }

    public ArrayList<String> getComplexQuery1(String numeConferinta) {
        ArrayList<String> numeExploratori = new ArrayList<>();
        if (!ajutator.existsTableExploratori() && !ajutator.existsTableExploratoriInProiecte()
                && !ajutator.existsTableCluburi() && !ajutator.existsTableConferinte())
            return numeExploratori;
        String query = "SELECT E.Nume, E.Prenume, E.IDExplorator FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator\n" +
                "GROUP BY  E.Nume, E.Prenume, E.IDExplorator\n" +
                "HAVING EP.NrOre > (SELECT AVG(sumaOre) FROM (SELECT Sum(EP.NrOre) AS sumaOre FROM Exploratori E INNER JOIN ExploratoriInProiecte EP ON E.IDExplorator = EP.IDExplorator\n" +
                "INNER JOIN Cluburi C ON E.IDClub = C.IDClub INNER JOIN Conferinte CO ON C.IDConferinta = CO.IDConferinta \n" +
                "WHERE CO.Nume = 'Muntenia'\n" +
                "GROUP BY  E.Nume, E.Prenume, E.IDExplorator));";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
                    String prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
                    int id = c.getInt(c.getColumnIndex(Explorator.COLUMN_ID_EXPLORATOR));
                    numeExploratori.add(nume + " " + prenume + " (" + id + ")");
                } while(c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            Log.e("Cautator.java",e.toString());
        }
        return numeExploratori;
    }

    public ArrayList<String> getComplexQuery2() {
        ArrayList<String> numeCluburi = new ArrayList<>();
        if (!ajutator.existsTableCluburi() && !ajutator.existsTableExploratori())
            return numeCluburi;
        String query = "SELECT C.Nume FROM Cluburi C INNER JOIN Exploratori E ON E.IDClub = C.IDClub\n" +
                "GROUP BY C.IDClub\n" +
                "HAVING COUNT(E.IDExplorator) > (SELECT AVG(nrExplo) FROM (SELECT COUNT(IDExplorator) As nrExplo FROM Exploratori E1 INNER JOIN Cluburi C1 \n" +
                "ON E1.IDClub = C1.IDClub GROUP BY C1.IDClub)) LIMIT 2;";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(c.getColumnIndex(Cluburi.COLUMN_NUME));
                    numeCluburi.add(nume);
                } while(c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            Log.e("Cautator.java",e.toString());
        }
        return numeCluburi;
    }

    public ArrayList<String> getComplexQuery3() {
        ArrayList<String> numeProiecte = new ArrayList<>();
        try {
            String query  = "SELECT P.Nume, SUM(EP.NrOre) FROM Proiecte P INNER JOIN ExploratoriInProiecte EP ON P.IDProiecte = EP.IDProiect \n" +
                    "WHERE EP.IDExplorator NOT IN (SELECT E1.IDExplorator FROM Exploratori E1 INNER JOIN Cluburi C1 ON E1.IDClub = C1.IDClub\n" +
                    "\tINNER JOIN Conferinte Cf1 ON C1.IDConferinta = Cf1.IDConferinta WHERE C1.Nume = 'Muntenia') AND \n" +
                    "\tEP.IDExplorator IN (SELECT E2.IDExplorator FROM Exploratori E2 INNER JOIN Cluburi C2 ON E2.IDClub = C2.IDClub\n" +
                    "\tINNER JOIN Conferinte Cf2 ON C2.IDConferinta = Cf2.IDConferinta WHERE C2.Nume = 'Moldova' AND C2.IDConducator = E2.IDExplorator)\n" +
                    "GROUP BY P.Nume\n" +
                    "HAVING SUM(EP.NrOre) > 10\n";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(0);

                    numeProiecte.add(nume);
                } while(c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            afiseazaEroarea(e.toString());
        }
        return numeProiecte;
    }

    public ArrayList<String> getComplexQuery4(int nrOre) {
        ArrayList<String> numeConferinte = new ArrayList<>();
        try {
            String query  = "SELECT * FROM Conferinte Cf INNER JOIN Cluburi C ON Cf.IDConferinta = C.IDConferinta INNER JOIN Exploratori E ON E.IDClub = C.IDClub\n" +
                    "INNER JOIN ExploratoriInProiecte EP\n" +
                    "GROUP BY CF.Nume \n" +
                    "HAVING SUM(EP.NrOre > " + nrOre +") AND Cf.Nume IN\n" +
                    "(SELECT Cf.Nume FROM Conferinte Cf1 INNER JOIN Cluburi C1 ON Cf1.IDConferinta = C1.IDConferinta INNER JOIN Exploratori E1 ON E1.IDClub = C1.IDClub\n" +
                    "GROUP BY CF1.Nume\n" +
                    "HAVING E1.IDExplorator IN (SELECT E2.IDExplorator FROM Exploratori E2 INNER JOIN ExploratoriInProiecte EP2 ON E2.IDExplorator = EP2.IDExplorator))\n" +
                    " ";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                do {
                    String nume = c.getString(0);

                    numeConferinte.add(nume);
                } while(c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
            afiseazaEroarea(e.toString());
        }
        return numeConferinte;
    }

    private void afiseazaEroarea(String eroarea) {
        if (context != null) {
            Toast.makeText(context, eroarea, Toast.LENGTH_LONG).show();
        }
        Log.e("Cautator.java", eroarea);
    }
}
