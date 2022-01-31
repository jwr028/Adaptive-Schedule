package com.example.shoppinglist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.shoppinglist.Model.ParentToDoModel;
import com.example.shoppinglist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // these are the column names for the list of lists table
    public static final String PARENT_TABLE_NAME = "LIST_OF_LISTS";
    public static final String LIST_ID = "ID";
    public static final String LIST_NAME = "NAME";

    // these are the column names for the list item table
    public static final String TABLE_NAME = "LIST_TABLE";
    public static final String PARENT_ID = "PARENT_ID"; // used for linking item lists to main list of lists
    public static final String COL_ID = "ID"; // ID for items
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
        // this parent_list table will probably be attached to a user ID later
        // string to cast command to SQL to create database for the list of lists
        String createMainTableStatement = "CREATE TABLE " + PARENT_TABLE_NAME + " (" + LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LIST_NAME + " TEXT )";

        db.execSQL(createMainTableStatement);

        // this will probably not be called in onCreate since we need a new one every time a new list is made
        // string to cast command to SQL to create database for an individual list
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PARENT_ID + " INTEGER, " + COL_TASK + " TEXT, " + COL_STATUS + " INTEGER, " + COL_TYPE + " TEXT )";

        db.execSQL(createTableStatement);
    }

    // called when database version changes preventing app from breaking when format changes
    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVer, int newVer){
        db.execSQL("DROP TABLE IF EXISTS " + PARENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // method to add list to parent_list
    public boolean insertList(ParentToDoModel list){
        SQLiteDatabase db = this.getWritableDatabase(); // command to open database for writing
        ContentValues cv = new ContentValues();

        cv.put(LIST_NAME, list.getName());

        long insert = db.insert(PARENT_TABLE_NAME, null, cv);
        if (insert == -1) {
            return false;
        }
        else {return true;}
    }

    // method to add an entry to list table
    public boolean insertTask(ToDoModel task){
        SQLiteDatabase db = this.getWritableDatabase(); // command to open database for writing
        ContentValues cv = new ContentValues();

        cv.put(PARENT_ID, task.getParentID());
        cv.put(COL_TASK, task.getTask());
        cv.put(COL_STATUS, 0); // for checkbox
        cv.put(COL_TYPE, task.getType());

        long insert = db.insert(TABLE_NAME, null, cv);
        if (insert == -1) {
            return false;
        }
        else {return true;}
    }

    public List<ParentToDoModel> getAllLists(){

        List<ParentToDoModel> parentList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + PARENT_TABLE_NAME; // will need to filter by ID?
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int listID = cursor.getInt(0);
                String listName = cursor.getString(1);

                ParentToDoModel newList = new ParentToDoModel();

                newList.setId(listID);
                newList.setName(listName);

                parentList.add(newList);

            } while(cursor.moveToNext());
        }
        else {
            // failure, don't add anything
        }

        cursor.close();
        db.close();
        return parentList;
    }

    public List<ToDoModel> getAllTasks(){

        List<ToDoModel> taskList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + TABLE_NAME; // will need to add on parent ID filter later !!!!!!!!!!!!!!
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                int parentID = cursor.getInt(1);
                String taskText = cursor.getString(2);
                int taskStatus = cursor.getInt(3);
                String taskType = cursor.getString(4);

                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setParentID(parentID);
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

    public void deleteList(int id){
        db.delete(PARENT_TABLE_NAME, LIST_ID + "= ?", new String[] {String.valueOf(id)});
    }

}

