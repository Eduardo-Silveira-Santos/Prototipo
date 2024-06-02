package com.eduardosantos.prototipo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WorkerDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "WorkerDatabaseHelper";
    private static final String DATABASE_NAME = "WorkerSignup.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "workers";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_CITY = "city";
    private static final String COL_PASSWORD = "password";
    private static final String COL_PROFESSION = "profession";
    private static final String COL_RATING = "rating";

    public WorkerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PHONE + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_PROFESSION + " TEXT, " +
                COL_RATING + " REAL)";
        db.execSQL(createTableQuery);
        insertDefaultWorkers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertDefaultWorkers(SQLiteDatabase db) {
        // Seu código de inserção de trabalhadores padrão aqui...
    }

    // Adicionando tratamento de exceções
    public boolean insertWorker(String name, String email, String phone, String city, String password, String profession) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PHONE, phone);
        contentValues.put(COL_CITY, city);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_PROFESSION, profession);
        try {
            long result = db.insertOrThrow(TABLE_NAME, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir trabalhador: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Consulta parametrizada e tratamento de exceções
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=?", new String[]{email})) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao verificar existência do email: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Atualização de classificação com contagem total de avaliações
    public void updateRating(String email, float newRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT " + COL_RATING + " FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=?", new String[]{email})) {
            if (cursor.moveToFirst()) {
                double currentRating = cursor.getDouble(0);
                int totalReviews = cursor.getCount();

                double updatedRating = ((currentRating * totalReviews) + newRating) / (totalReviews + 1);

                ContentValues values = new ContentValues();
                values.put(COL_RATING, updatedRating);
                db.update(TABLE_NAME, values, COL_EMAIL + "=?", new String[]{email});
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar classificação: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public Worker getWorkerByEmail(String email, String password) {
        Worker worker = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_NAME, COL_EMAIL, COL_PROFESSION, COL_RATING, COL_PHONE, COL_CITY},
                    COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COL_NAME);
                int professionIndex = cursor.getColumnIndex(COL_PROFESSION);
                int ratingIndex = cursor.getColumnIndex(COL_RATING);
                int phoneIndex = cursor.getColumnIndex(COL_PHONE);
                int cityIndex = cursor.getColumnIndex(COL_CITY);
                if (nameIndex != -1 && professionIndex != -1 && ratingIndex != -1 && phoneIndex != -1 && cityIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String profession = cursor.getString(professionIndex);
                    double rating = cursor.getDouble(ratingIndex);
                    String phone = cursor.getString(phoneIndex);
                    String city = cursor.getString(cityIndex);
                    worker = new Worker(name, email, profession, rating, phone, city);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar trabalhador por email: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return worker;
    }

    public List<Worker> getAllWorkers() {
        List<Worker> workerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_NAME, COL_EMAIL, COL_PROFESSION, COL_RATING, COL_PHONE, COL_CITY};
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int nameIndex = cursor.getColumnIndex(COL_NAME);
                    int emailIndex = cursor.getColumnIndex(COL_EMAIL);
                    int professionIndex = cursor.getColumnIndex(COL_PROFESSION);
                    int ratingIndex = cursor.getColumnIndex(COL_RATING);
                    int phoneIndex = cursor.getColumnIndex(COL_PHONE);
                    int cityIndex = cursor.getColumnIndex(COL_CITY);

                    if (nameIndex >= 0 && emailIndex >= 0 && professionIndex >= 0 && ratingIndex >= 0 && phoneIndex >= 0 && cityIndex >= 0) {
                        String name = cursor.getString(nameIndex);
                        String email = cursor.getString(emailIndex);
                        String profession = cursor.getString(professionIndex);
                        double rating = cursor.getDouble(ratingIndex);
                        String phone = cursor.getString(phoneIndex);
                        String city = cursor.getString(cityIndex);

                        Worker worker = new Worker(name, email, profession, rating, phone, city);
                        workerList.add(worker);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving workers: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return workerList;
    }

    public Worker getWorkerByEmailOnly(String email) {
        Worker worker = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COL_NAME, COL_EMAIL, COL_PROFESSION, COL_RATING, COL_PHONE, COL_CITY},
                    COL_EMAIL + "=?", new String[]{email}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(COL_NAME);
                int professionIndex = cursor.getColumnIndex(COL_PROFESSION);
                int ratingIndex = cursor.getColumnIndex(COL_RATING);
                int phoneIndex = cursor.getColumnIndex(COL_PHONE);
                int cityIndex = cursor.getColumnIndex(COL_CITY);
                if (nameIndex != -1 && professionIndex != -1 && ratingIndex != -1 && phoneIndex != -1 && cityIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String profession = cursor.getString(professionIndex);
                    double rating = cursor.getDouble(ratingIndex);
                    String phone = cursor.getString(phoneIndex);
                    String city = cursor.getString(cityIndex);
                    worker = new Worker(name, email, profession, rating, phone, city);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao recuperar trabalhador por email: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return worker;
    }
}
