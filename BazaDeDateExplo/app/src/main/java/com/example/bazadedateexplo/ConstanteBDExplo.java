package com.example.bazadedateexplo;

import android.database.sqlite.SQLiteDatabase;

public class ConstanteBDExplo {
    public static final String DB_NAME = "Explo.db";
    public static final String SHARED_PREFERENCE_FILE = "Info";
    public static final String SHARED_PREFERENCE_MODIF = "modif";
    public static final String MODIF = "modif";
    public static final String ID = "id";
    public static final String DETALII = "detalii";

    private ConstanteBDExplo() {
    }

    public static class Explorator {
        public static final String TABLE_NAME = "Exploratori";
        public static final String COLUMN_ID_EXPLORATOR = "IDExplorator";
        public static final String COLUMN_NUME = "Nume";
        public static final String COLUMN_PRENUME = "Prenume";
        public static final String COLUMN_CNP = "CNP";
        public static final String COLUMN_NR_SPECIALIZARI = "NrSpecializari";
        public static final String COLUMN_GRAD = "Grad";
        public static final String COLUMN_INSTRUCTOR = "Instructor";
        public static final String COLUMN_ID_PARINTE = "IDParinte";
        public static final String COLUMN_ID_CLUB = "IDClub";
        public static final String COLUMN_DATA_START = "DataStart";
        public static final String COLUMN_DATA_FINAL = "DataFinal";
    }

    public static class Parinti {
        public static final String TABLE_NAME = "Parinti";
        public static final String COLUMN_ID_PARINTE = "IDParinte";
        public static final String COLUMN_NUME = "Nume";
        public static final String COLUMN_PRENUME = "Prenume";
        public static final String COLUMN_GEN = "Gen";
        public static final String COLUMN_NR_TELEFON = "NrTelefon";
    }

    public static class Cluburi {
        public static final String TABLE_NAME = "Cluburi";
        public static final String COLUMN_ID_CLUB = "IDClub";
        public static final String COLUMN_NUME = "Nume";
        public static final String COLUMN_ID_CONFERINTA = "IDConferinta";
        public static final String COLUMN_ID_CONDUCATOR = "IDConducator";
    }

    public static class Conferinte {
        public static final String TABLE_NAME = "Conferinte";
        public static final String COLUMN_ID_CONFERINTA = "IDConferinta";
        public static final String COLUMN_NUME = "Nume";
        public static final String COLUMN_ID_DIRECTOR = "IDDirector";
    }

    public static class Proiecte {
        public static final String TABLE_NAME = "Proiecte";
        public static final String COLUMN_ID_PROIECTE = "IDProiecte";
        public static final String COLUMN_NUME = "Nume";
        public static final String COLUMN_DATA_START = "DataStart";
        public static final String COLUMN_DATA_FINAL = "DataFinal";
        public static final String COLUMN_DESCRIERE_SCURTA = "DescriereScurta";
    }

    public static class ExploratoriInProiecte {
        public static final String TABLE_NAME = "ExploratoriInProiecte";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_ID_PROIECT = "IDProiect";
        public static final String COLUMN_ID_EXPLORATOR = "IDExplorator";
        public static final String COLUMN_NR_ORE = "NrOre";
    }
}
