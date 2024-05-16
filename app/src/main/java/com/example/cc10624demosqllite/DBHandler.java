package com.example.cc10624demosqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "dbstudents";
    private static final String TABLE_NAME = "tblstudent";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOC = "location";
    private static final String KEY_COURSE = "course";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT, " +
                KEY_LOC + " TEXT, " +
                KEY_COURSE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void saveStudentInfo(String name, String loc, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues studValues = new ContentValues();

        studValues.put(KEY_NAME, name);
        studValues.put(KEY_LOC, loc);
        studValues.put(KEY_COURSE, course);

        long newrowid = db.insert(TABLE_NAME, null, studValues);

        db.close();
    }

    ArrayList<String> readStudent() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> data = new ArrayList<String>();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String studData = null;

        while (cursor.moveToNext()) {
            studData = cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3);
            data.add(studData);
        }
        cursor.close();
        return data;
    }

    // Modify your DBHandler class
    public boolean updateStudentInfo(int id, String name, String loc, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_LOC, loc);
        values.put(KEY_COURSE, course);

        // Perform the update operation
        int rowsAffected = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        // Return true if at least one row was affected, indicating success
        return rowsAffected > 0;
    }


    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<String> getStudentDetails(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> studentDetails = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_NAME, KEY_LOC, KEY_COURSE},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            studentDetails.add(cursor.getString(0)); // Name
            studentDetails.add(cursor.getString(1)); // Location
            studentDetails.add(cursor.getString(2)); // Course
            cursor.close();
            return studentDetails;
        } else {
            return null;
        }
    }
}
