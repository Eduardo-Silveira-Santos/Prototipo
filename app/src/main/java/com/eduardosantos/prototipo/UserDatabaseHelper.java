package com.eduardosantos.prototipo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Signup.db";
    private static final String TABLE_NAME = "allusers";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Consultas SQL
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_NAME + " TEXT, " +
            COL_EMAIL + " TEXT PRIMARY KEY, " +
            COL_PASSWORD + " TEXT)";
    private static final String CHECK_EMAIL_QUERY = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?";
    private static final String CHECK_EMAIL_PASSWORD_QUERY = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
    private static final String SELECT_NAME_QUERY = "SELECT " + COL_NAME + " FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String email, String password) {
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, name);
            contentValues.put(COL_EMAIL, email);
            contentValues.put(COL_PASSWORD, hashedPassword);
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
            return result != -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(CHECK_EMAIL_QUERY, new String[]{email})) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        } finally {
            db.close();
        }
    }

    public boolean checkEmailPassword(String email, String password) {
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return false;

        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(CHECK_EMAIL_PASSWORD_QUERY, new String[]{email, hashedPassword})) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
            return false;
        } finally {
            db.close();
        }
    }

    public String getUserName(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(SELECT_NAME_QUERY, new String[]{userEmail})) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COL_NAME);
                if (nameIndex != -1) {
                    return cursor.getString(nameIndex);
                }
            }
            return null;
        } finally {
            db.close();
        }
    }

    public void updateUser(String email, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        db.update(TABLE_NAME, values, COL_EMAIL + "=?", new String[]{email});
        db.close();
    }

    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_EMAIL + "=?", new String[]{email});
        db.close();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
