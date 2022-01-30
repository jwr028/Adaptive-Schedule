package com.example.shoppinglist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.shoppinglist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "LIST_TABLE";
    public static final String COL_ID = "ID";
    public static final String COL_TASK = "TASK";
    public static final String COL_STATUS = "STATUS";
    public static final String COL_TYPE = "TYPE";

    private SQLiteDatabase db;

    // Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, "list.db", null, 1);
    }

    // onCreate is called first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        // string to cast command to SQL to create database
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TASK + " TEXT, " + COL_STATUS + " INTEGER, " + COL_TYPE + " TEXT )";

        db.execSQL(createTableStatement);
    }

    // called when database version changes preventing app from breaking when format changes
    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVer, int newVer){

    }

    // method to start using database?
    //public void openDatabase() { db = this.getWritableDatabase(); }

    // method to add an entry to database
    public boolean insertTask(ToDoModel task){
        SQLiteDatabase db = this.getWritableDatabase(); // command to open database for writing
        ContentValues cv = new ContentValues();

        cv.put(COL_TASK, task.getTask());
        cv.put(COL_STATUS, 0); // for checkbox
        cv.put(COL_TYPE, task.getType());

        long insert = db.insert(TABLE_NAME, null, cv);
        if (insert == -1) {
            return false;
        }
        else {return true;}
    }

    public List<ToDoModel> getAllTasks(){

        List<ToDoModel> taskList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                String taskText = cursor.getString(1);
                int taskStatus = cursor.getInt(2);
                String taskType = cursor.getString(3);

                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setTask(taskText);
                newTask.setStatus(taskStatus);
                newTask.setType(taskType);

                taskList.add(newTask);

            } while(cursor.moveToNext());
        }
        else {
            // failure, don't add anything

        }

        cursor.close();
        db.close();
        return taskList;
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    // used for checkboxes
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(COL_STATUS, status);
        db.update(TABLE_NAME, cv, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TASK, task);
        db.update(TABLE_NAME, cv, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TABLE_NAME, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

}

