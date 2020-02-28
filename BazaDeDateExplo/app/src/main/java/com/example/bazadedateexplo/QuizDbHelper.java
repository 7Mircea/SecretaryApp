package com.example.bazadedateexplo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bazadedateexplo.ConstanteBDExplo.*;
import com.example.bazadedateexplo.QuizContract.*;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 2;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        this.db.setForeignKeyConstraintsEnabled(true);

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                Explorator.TABLE_NAME + " ( " +
                Explorator.COLUMN_ID_EXPLORATOR + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Explorator.COLUMN_NUME + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    public void executeSQL(String statement) {
        db.execSQL(statement);
    }

    public String getStudentAn() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Explorator.TABLE_NAME, null);
        String an = null;
        while(c.moveToFirst()) {
            an = c.getString(c.getColumnIndex(Explorator.COLUMN_NUME));
        }
        c.close();
        return an;
    }
}