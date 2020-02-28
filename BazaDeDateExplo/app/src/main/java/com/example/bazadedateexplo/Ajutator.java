package com.example.bazadedateexplo;

import android.database.sqlite.SQLiteDatabase;

import com.example.bazadedateexplo.ConstanteBDExplo.*;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;

import java.util.ArrayList;


/**
 * Clasa Ajutator are rolul de a ajuta la pastrarea functiilor de accesare a bazei de date intr-un
 * singur loc. Altfel multe functii ar fi rescrise pentru fiecare activitate.
 */
public class Ajutator {
    SQLiteDatabase db;
    Activity context;

    Ajutator(SQLiteDatabase db) {
        this.db = db;
    }

    Ajutator(Activity context, SQLiteDatabase db) {
        this.db = db;
        this.context = context;
        this.db.setForeignKeyConstraintsEnabled(true); //acest rand asigura ca constrangerile
        //legate de key straine sunt aplicate
    }

    public void creeazaExploratori() {
        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS "
                    + Explorator.TABLE_NAME + "("
                    + Explorator.COLUMN_ID_EXPLORATOR + " INTEGER NOT NULL primary key AUTOINCREMENT, "
                    + Explorator.COLUMN_NUME + " TEXT NOT NULL, "
                    + Explorator.COLUMN_PRENUME + " TEXT NOT NULL, "
                    + Explorator.COLUMN_CNP + " TEXT CHECK(length(CNP) = 13) UNIQUE NOT NULL, "
                    + Explorator.COLUMN_NR_SPECIALIZARI + " INTEGER NOT NULL, "
                    + Explorator.COLUMN_GRAD + " TEXT CHECK(Grad IN ('ucenic','calator','navigator','mesager','ghid-asistent','ghid','master-ghid')), "
                    + Explorator.COLUMN_INSTRUCTOR + " INTEGER NOT NULL CHECK(Instructor IN (0,1)), "
                    + Explorator.COLUMN_ID_PARINTE + " INTEGER, "
                    + Explorator.COLUMN_ID_CLUB + " INTEGER, "
                    + Explorator.COLUMN_DATA_START + " TEXT NOT NULL, "
                    + Explorator.COLUMN_DATA_FINAL + " TEXT NOT NULL"
                    + ", \n"
                    + " CONSTRAINT fk_exploratori_parinte\n"
                    + " FOREIGN KEY (" + Explorator.COLUMN_ID_PARINTE + ")\n"
                    + " REFERENCES " + Parinti.TABLE_NAME + "(" + Parinti.COLUMN_ID_PARINTE + ")\n"
                    + " ON DELETE SET NULL,\n"
                    + " CONSTRAINT fk_exploratori_club\n"
                    + " FOREIGN KEY (" + Explorator.COLUMN_ID_CLUB + ")\n"
                    + " REFERENCES " + Cluburi.TABLE_NAME + "(" + Cluburi.COLUMN_ID_CLUB + ")\n"
                    + " ON DELETE CASCADE\n)";
            //Log.e("AdaugExplo.java", CREATE_TABLE_CONTAIN);
            db.execSQL(CREATE_TABLE_CONTAIN);
        } catch (Exception e) {
            if (context != null)
                Toast.makeText(context, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaParinti() {
        try {
            String CREATE_TABLE_PARINTE = "CREATE TABLE IF NOT EXISTS "
                    + Parinti.TABLE_NAME + "( "
                    + Parinti.COLUMN_ID_PARINTE + " INTEGER NOT NULL primary key AUTOINCREMENT, "
                    + Parinti.COLUMN_NUME + " TEXT NOT NULL, "
                    + Parinti.COLUMN_PRENUME + " TEXT NOT NULL, "
                    + Parinti.COLUMN_GEN + " TEXT NOT NULL CHECK(Gen IN ('m','f')), "
                    + Parinti.COLUMN_NR_TELEFON + " TEXT CHECK(LENGTH(NrTelefon) = 10))";
            db.execSQL(CREATE_TABLE_PARINTE);
        } catch (Exception e) {
            if (context != null)
                Toast.makeText(context, "ERROR: " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaProiecte() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + Proiecte.TABLE_NAME
                    + "(" + Proiecte.COLUMN_ID_PROIECTE + " INTEGER NOT NULL primary key AUTOINCREMENT, "
                    + Proiecte.COLUMN_NUME + " TEXT NOT NULL, "
                    + Proiecte.COLUMN_DATA_START + " TEXT NOT NULL, "
                    + Proiecte.COLUMN_DATA_FINAL + " TEXT NOT NULL, "
                    + Proiecte.COLUMN_DESCRIERE_SCURTA + " TEXT NOT NULL);";
            db.execSQL(query);
        } catch (SQLiteException e) {
            if (context != null)
                Toast.makeText(context, R.string.notificare_eroare_sqlite + ". Eroare : " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaCluburi() {
        try {
            String CREATE_TABLE_CLUBURI = "CREATE TABLE IF NOT EXISTS "
                    + Cluburi.TABLE_NAME + "( "
                    + Cluburi.COLUMN_ID_CLUB + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Cluburi.COLUMN_NUME + " TEXT NOT NULL, "
                    + Cluburi.COLUMN_ID_CONFERINTA + " INTEGER, "
                    + Cluburi.COLUMN_ID_CONDUCATOR + " INTEGER, "
                    + " CONSTRAINT fk_cluburi_conferinte\n"
                    + " FOREIGN KEY ("+ Cluburi.COLUMN_ID_CONFERINTA + ")\n"
                    + " REFERENCES " + Conferinte.TABLE_NAME + "(" + Conferinte.COLUMN_ID_CONFERINTA + ")\n"
                    + " ON DELETE CASCADE,\n "
                    + " CONSTRAINT fk_cluburi_exploratori\n"
                    + " FOREIGN KEY (" + Cluburi.COLUMN_ID_CONDUCATOR + ")\n"
                    + " REFERENCES " + Explorator.TABLE_NAME + "(" + Explorator.COLUMN_ID_EXPLORATOR + ")\n"
                    + " ON DELETE SET NULL\n"
                    + " );\n";
            //Log.e("Ajutator.java", CREATE_TABLE_CLUBURI);
            db.execSQL(CREATE_TABLE_CLUBURI);
        } catch (Exception e) {
            if (context != null)
                Toast.makeText(context, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaConferinte() {
        try {
            String CREATE_TABLE_CONFERINTA = "CREATE TABLE IF NOT EXISTS "
                    + Conferinte.TABLE_NAME + "( "
                    + Conferinte.COLUMN_ID_CONFERINTA + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + Conferinte.COLUMN_NUME + " TEXT NOT NULL, "
                    + Conferinte.COLUMN_ID_DIRECTOR + " INTEGER,"
                    + "CONSTRAINT fk_conferinte_exploratori\n" +
                    " FOREIGN KEY (IDDirector)\n" +
                    " REFERENCES Exploratori(IDExplorator)\n" +
                    " ON DELETE SET NULL);\n";
            db.execSQL(CREATE_TABLE_CONFERINTA);
        } catch (Exception e) {
            if (context != null)
                Toast.makeText(context, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaExploInProiecte() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + ConstanteBDExplo.ExploratoriInProiecte.TABLE_NAME
                    + "( " + ConstanteBDExplo.ExploratoriInProiecte.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ConstanteBDExplo.ExploratoriInProiecte.COLUMN_ID_PROIECT + " INTEGER, "
                    + ConstanteBDExplo.ExploratoriInProiecte.COLUMN_ID_EXPLORATOR + " INTEGER, "
                    + ConstanteBDExplo.ExploratoriInProiecte.COLUMN_NR_ORE + " INTEGER NOT NULL, " +
                    "CONSTRAINT fk_exploinproiect_proiecte \n" +
                    "FOREIGN KEY (IDProiect)\n" +
                    "REFERENCES Proiecte(IDProiecte)\n" +
                    "ON DELETE CASCADE,\n" +
                    "CONSTRAINT fk_exploinproiect_exploratori\n" +
                    "FOREIGN KEY (IDExplorator)\n" +
                    "REFERENCES Exploratori(IDExplorator)\n" +
                    "ON DELETE CASCADE\n);";
            //Log.e("AdaugExploInProiec.java", query);
            db.execSQL(query);
        } catch (SQLiteException e) {
            if (context != null)
                Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
            Log.e("Ajutator.java", e.toString());
        }
    }

    public void creeazaToateTabelele() {
        creeazaExploratori();
        creeazaParinti();
        creeazaProiecte();
        creeazaCluburi();
        creeazaConferinte();
        creeazaExploInProiecte();
    }

    public String getNumeExplorator(int id) {
        if (!existsTableExploratori()) return null;
        String nume = null;
        String prenume = null;
        String sel = "SELECT " + Explorator.COLUMN_NUME + ", "
                + Explorator.COLUMN_PRENUME
                + " FROM " + Explorator.TABLE_NAME + " WHERE "
                + Explorator.COLUMN_ID_EXPLORATOR + " = " + id;
        Cursor c = db.rawQuery(sel, null);
        if (c.moveToFirst()) {
            nume = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
            prenume = c.getString(c.getColumnIndex(Explorator.COLUMN_PRENUME));
        }
        nume = nume + " " + prenume + " (" + id + ")";
        c.close();
        return nume;
    }

    public String getNumeParinte(int id) {
        if (!existsTableParinti()) return null;
        String nume = null;
        String prenume = null;
        String sel = "SELECT " + Parinti.COLUMN_NUME + ", "
                + Parinti.COLUMN_PRENUME
                + " FROM " + Parinti.TABLE_NAME + " WHERE "
                + Parinti.COLUMN_ID_PARINTE + " = " + id;
        Cursor c = db.rawQuery(sel, null);
        if (c.moveToFirst()) {
            nume = c.getString(c.getColumnIndex(Parinti.COLUMN_NUME));
            prenume = c.getString(c.getColumnIndex(Parinti.COLUMN_PRENUME));
        }
        nume = nume + " " + prenume + " " + "(" + id + ")";
        c.close();
        return nume;
    }

    public String getNumeClub(int id) {
        if (!existsTableCluburi()) return null;
        String nume = null;
        String sel = "SELECT " + Cluburi.COLUMN_NUME
                + " FROM " + Cluburi.TABLE_NAME + " WHERE "
                + Cluburi.COLUMN_ID_CLUB + " = " + id;
        Cursor c = db.rawQuery(sel, null);
        if (c.moveToFirst()) {
            nume = c.getString(c.getColumnIndex(Parinti.COLUMN_NUME));
        }
        c.close();
        return nume;
    }

    public ArrayList<String> getNumeConferinte() {
        ArrayList<String> numeConferinte = new ArrayList<>();
        if (!existsTableConferinte()) return numeConferinte;
        String query = "SELECT " + Conferinte.COLUMN_NUME + " FROM "
                + Conferinte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Conferinte.COLUMN_NUME));
                numeConferinte.add(nume);
            } while (c.moveToNext());
        }
        c.close();
        return numeConferinte;
    }

    public ArrayList<Integer> getIdsConferinte() {
        ArrayList<Integer> idsConferinte = new ArrayList<>();
        if (!existsTableConferinte()) return idsConferinte;
        String query = "SELECT " + Conferinte.COLUMN_ID_CONFERINTA + " FROM "
                + Conferinte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(Conferinte.COLUMN_ID_CONFERINTA));
                idsConferinte.add(id);
            } while (c.moveToNext());
        }
        c.close();
        return idsConferinte;
    }

    public String getNumeConferinta(int id) {
        if (!existsTableConferinte()) return null;
        String numeConferinta = null;
        String query = "SELECT " + Conferinte.COLUMN_NUME + " FROM "
                + Conferinte.TABLE_NAME + " WHERE " + Conferinte.COLUMN_ID_CONFERINTA + "=" + id;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            numeConferinta = c.getString(c.getColumnIndex(Conferinte.COLUMN_NUME));
        }
        c.close();
        return numeConferinta;
    }

    public ArrayList<String> getNumeProiecte() {
        ArrayList<String> numeProiecte = new ArrayList<>();
        if (!existsTableProiecte()) return numeProiecte;
        String query = "SELECT " + Proiecte.COLUMN_NUME + " FROM "
                + Proiecte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME));
                numeProiecte.add(nume);
            } while (c.moveToNext());
        }
        c.close();
        return numeProiecte;
    }

    public ArrayList<Integer> getIdsProiecte() {
        ArrayList<Integer> idsProiecte = new ArrayList<>();
        if (!existsTableProiecte()) return idsProiecte;
        String query = "SELECT " + Proiecte.COLUMN_ID_PROIECTE + " FROM "
                + Proiecte.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(Proiecte.COLUMN_ID_PROIECTE));
                idsProiecte.add(id);
            } while (c.moveToNext());
        }
        c.close();
        return idsProiecte;
    }

    public String getNumeProiect(int id) {
        if (!existsTableProiecte()) return null;
        String numeProiect = null;
        String query = "SELECT " + Proiecte.COLUMN_NUME + " FROM "
                + Proiecte.TABLE_NAME + " WHERE " + Proiecte.COLUMN_ID_PROIECTE + "=" + id;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            numeProiect = c.getString(c.getColumnIndex(Proiecte.COLUMN_NUME));
        }
        c.close();
        return numeProiect;
    }

    public ArrayList<String> getNumeCluburi() {
        ArrayList<String> numeCluburi = new ArrayList<>();
        if (!existsTableCluburi()) return numeCluburi;
        String query = "SELECT " + Cluburi.COLUMN_NUME + " FROM "
                + Cluburi.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                String nume = c.getString(c.getColumnIndex(Cluburi.COLUMN_NUME));
                numeCluburi.add(nume);
            } while (c.moveToNext());
        }
        c.close();
        return numeCluburi;
    }

    public ArrayList<Integer> getIdsCluburi() {
        ArrayList<Integer> idsCluburi = new ArrayList<>();
        if (!existsTableCluburi()) return idsCluburi;
        String query = "SELECT " + Cluburi.COLUMN_ID_CLUB + " FROM "
                + Cluburi.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex(Cluburi.COLUMN_ID_CLUB));
                idsCluburi.add(id);
            } while (c.moveToNext());
        }
        c.close();
        return idsCluburi;
    }

    public boolean existsTableParinti() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + Parinti.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai parinti.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai parinti.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }

    public boolean existsTableExploratori() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + Explorator.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai exploratori.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai exploratori.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }

    public boolean existsTableExploratoriInProiecte() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + ExploratoriInProiecte.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai exploratori in proiecte.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai exploratori in proiecte.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }

    public boolean existsTableProiecte() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + Proiecte.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai proiecte.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai proiecte.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }

    public boolean existsTableCluburi() {
        if (db == null) return false;
        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + Cluburi.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai cluburi.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai cluburi.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }

    /**
     * verifica daca exista tabela Conferinte
     *
     * @return true daca tabela exista si false daca nu exista sau nici baza de date nu exista
     */
    public boolean existsTableConferinte() {
        if (db == null) return false;

        String query = "SELECT name FROM sqlite_master WHERE type = 'table' AND name='"
                + Conferinte.TABLE_NAME + "';";
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() == 0) {
            if (context != null)
                Toast.makeText(context, "Adauga mai intai conferinte.", Toast.LENGTH_SHORT).show();
            else
                Log.e("Ajutator.java", "Adauga mai intai conferinte.");
            c.close();
            return false;
        } else {
            c.close();
            return true;
        }
    }
}
