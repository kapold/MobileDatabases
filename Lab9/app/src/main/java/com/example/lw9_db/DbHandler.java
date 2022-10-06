package com.example.lw9_db;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "Lab_DB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "SimpleTable";

    private static final String ID_COL = "id";
    private static final String FLOAT_COL = "f";
    private static final String TEXT_COL = "t";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FLOAT_COL + " REAL,"
                + TEXT_COL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTable(String id, String f, String t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (!id.isEmpty())
            values.put(ID_COL, id);
        values.put(FLOAT_COL, f);
        values.put(TEXT_COL, t);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateTable(String savedID, String f, String t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FLOAT_COL, f);
        values.put(TEXT_COL, t);

        db.update(TABLE_NAME, values, "id=?", new String[]{savedID});
        db.close();
    }

    public void deleteTable(String savedID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id=?", new String[]{savedID});
        db.close();
    }

    //    public ArrayList<SimpleTable> getSimpleTables() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        ArrayList<SimpleTable> tablesList = new ArrayList<>();
//
//        if (cursor.moveToFirst()) {
//            do {
//                tablesList.add(new SimpleTable(
//                        cursor.getInt(0),
//                        cursor.getFloat(1),
//                        cursor.getString(2)
//                ));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return tablesList;
//    }

    public SimpleTable getTableRaw(String tableID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + String.format(" WHERE id = %s", tableID),
                null);
        SimpleTable st = new SimpleTable();

        if (cursor.moveToFirst()){
            st.id = cursor.getInt(0);
            st.f = cursor.getFloat(1);
            st.t = cursor.getString(2);
        }

        cursor.close();
        return st;
    }

    public SimpleTable getTable(String tableID) {
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "f", "t" }, " id = ?",
                    new String[] { tableID }, null, null, null);
            SimpleTable st = new SimpleTable();

            if (cursor.moveToFirst()) {
                st.id = cursor.getInt(0);
                st.f = cursor.getFloat(1);
                st.t = cursor.getString(2);
            }

            cursor.close();
            return st;
        }
        catch (Exception ex){
            return null;
        }
    }
}