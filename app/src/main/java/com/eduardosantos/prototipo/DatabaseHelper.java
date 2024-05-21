package com.eduardosantos.prototipo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Signup.db";
    public static final String TABLE_NAME = "allusers";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT PRIMARY KEY, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String email, String password) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, name);
            contentValues.put(COL_EMAIL, email);
            contentValues.put(COL_PASSWORD, password);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
    }

    public boolean checkEmail(String email) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{email})) {
            return cursor.getCount() > 0;
        }
    }

    public boolean checkEmailPassword(String email, String password) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{email, password})) {
            return cursor.getCount() > 0;
        }
    }

    public String getUserName(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String userName = null;
        String selectQuery = "SELECT " + COL_NAME + " FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userEmail});
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COL_NAME);
            if (nameIndex != -1) {
                userName = cursor.getString(nameIndex);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return userName;
    }
}
