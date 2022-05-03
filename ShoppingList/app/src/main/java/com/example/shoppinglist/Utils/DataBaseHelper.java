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
    public static final String LIST_ID = "ID"; // PRIMARY KEY
    public static final String LIST_NAME = "NAME";

    // these are the column names for the list item table
    public static final String TABLE_NAME = "LIST_TABLE";
    public static final String PARENT_ID = "PARENT"; // FOREIGN KEY
    public static final String COL_ID = "ID"; // PRIMARY KEY
    public static final String COL_TASK = "TASK";
    public static final String COL_STATUS = "STATUS";
    public static final String COL_TYPE = "TYPE";
    public static final String COL_AGE = "AGE";
    public static final String COL_STOREITEMID = "STOREITEMID";

    public static SQLiteDatabase db;

    // Constructor
    public DataBaseHelper(@Nullable Context context) {
        super(context, "list.db", null, 1);
    }

    // onCreate is called first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        // this parent_list table will probably be attached to a user ID later
        // string to cast command to SQL to create database for the list of lists
        String createMainTableStatement = "CREATE TABLE " + PARENT_TABLE_NAME + " (" + LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LIST_NAME + "  TEXT " + ")";

        db.execSQL(createMainTableStatement);

        // this will probably not be called in onCreate since we need a new one every time a new list is made
        // string to cast command to SQL to create database for an individual list
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + COL_TASK + " TEXT, " + COL_STATUS + " INTEGER, " + COL_TYPE + " TEXT, " + COL_AGE + " INTEGER, "
                + PARENT_ID + " INTEGER, " +COL_STOREITEMID+ " TEXT, " + " FOREIGN KEY " + "(" +PARENT_ID+ ")" + " REFERENCES " + PARENT_TABLE_NAME + "(" + LIST_ID + ")" + ")";

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
        cv.put(COL_AGE, 0); // for distinguishing new items from items already stored
        cv.put(COL_STOREITEMID, task.getStoreItemID());

        long insert = db.insert(TABLE_NAME, null, cv);
        if (insert == -1) {
            return false;
        }
        else {return true;}
    }

    // used on main list activity
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

    // needs to fetch ITEM matching PARENT ID of selected list (int parameter?)
    public List<ToDoModel> getAllListItems(){

        List<ToDoModel> taskList = new ArrayList<>();

        // get data from database
        //String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_TYPE + "=" + "item";
        String queryString = "SELECT * FROM " +TABLE_NAME+ " WHERE " +COL_TYPE+ "=" + "'item'";
        //String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                String taskText = cursor.getString(1);
                int taskStatus = cursor.getInt(2);
                String taskType = cursor.getString(3);
                int taskAge = cursor.getInt(4);
                int parentID = cursor.getInt(5);
                String storeItemID = cursor.getString(6);

                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setParentID(parentID);
                newTask.setTask(taskText);
                newTask.setStatus(taskStatus);
                newTask.setType(taskType);
                newTask.setAge(taskAge);
                newTask.setStoreItemID(storeItemID);

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

    // needs to fetch TASK matching PARENT ID of selected list (int parameter?)
    public List<ToDoModel> getAllListTasks(){

        List<ToDoModel> taskList = new ArrayList<>();

        // get data from database
        //String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_TYPE + "=" + "item";
        String queryString = "SELECT * FROM " +TABLE_NAME+ " WHERE " +COL_TYPE+ "=" + "'task'";
        //String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                String taskText = cursor.getString(1);
                int taskStatus = cursor.getInt(2);
                String taskType = cursor.getString(3);
                int taskAge = cursor.getInt(4);
                int parentID = cursor.getInt(5);
                String storeItemID = cursor.getString(6);


                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setParentID(parentID);
                newTask.setTask(taskText);
                newTask.setStatus(taskStatus);
                newTask.setType(taskType);
                newTask.setAge(taskAge);
                newTask.setStoreItemID(storeItemID);

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

    // used to fetch all items and tasks in a list (probably not used)
    public List<ToDoModel> getAllTasks(int id){

        List<ToDoModel> taskList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + PARENT_ID + "=" + id; // will need to add on parent ID filter later !!!!!!!!!!!!!!
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                String taskText = cursor.getString(1);
                int taskStatus = cursor.getInt(2);
                String taskType = cursor.getString(3);
                int taskAge = cursor.getInt(4);
                int parentID = cursor.getInt(5);
                String storeItemID = cursor.getString(6);

                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setParentID(parentID);
                newTask.setTask(taskText);
                newTask.setStatus(taskStatus);
                newTask.setType(taskType);
                newTask.setAge(taskAge);
                newTask.setStoreItemID(storeItemID);

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

    // used to fetch and refresh list of tasks currently (used in createlist activity)
    public List<ToDoModel> getAllNewTasks(){

        List<ToDoModel> taskList = new ArrayList<>();

        // get NEW data from database (age=0)
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_AGE + "=0"; // will need to add on parent ID filter later !!!!!!!!!!!!!!
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            // loop through results and create new objects then return them as list
            do {
                int taskID = cursor.getInt(0);
                String taskText = cursor.getString(1);
                int taskStatus = cursor.getInt(2);
                String taskType = cursor.getString(3);
                int taskAge = cursor.getInt(4);
                int parentID = cursor.getInt(5);
                String storeItemID = cursor.getString(6);

                ToDoModel newTask = new ToDoModel();

                newTask.setId(taskID);
                newTask.setTask(taskText);
                newTask.setStatus(taskStatus);
                newTask.setType(taskType);
                newTask.setAge(taskAge);
                newTask.setParentID(parentID);
                newTask.setStoreItemID(storeItemID);

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

    public void updateAge(int id, int age) {
        ContentValues cv = new ContentValues();
        cv.put(COL_AGE, age);
        db.update(TABLE_NAME, cv, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateParent(int id, int parentID) {
        ContentValues cv = new ContentValues();
        cv.put(PARENT_ID, parentID);
        db.update(TABLE_NAME, cv, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateListName(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(LIST_NAME, name);
        db.update(PARENT_TABLE_NAME, cv, LIST_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TABLE_NAME, COL_ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteList(int id){
        db.delete(PARENT_TABLE_NAME, LIST_ID + "= ?", new String[] {String.valueOf(id)});
        // need to add code to delete all task entries with same parent ID
        db.delete(TABLE_NAME, PARENT_ID + "= ?", new String[] {String.valueOf(id)});
    }


    public int getLastInsert(){
        SQLiteDatabase db = this.getReadableDatabase(); // command to open database for reading
        String queryString = "SELECT last_insert_rowid()"; // SQlite command to fetch ID of most recent INSERT
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            int lastInsertID = cursor.getInt(0);
            return lastInsertID;
        } else {
            return 0;
        }

    }

    public void deleteAllLists() {
        //SQLiteDatabase db = this.getWritableDatabase(); // command to open database for reading
        //String queryString = "DELETE FROM " + TABLE_NAME; // SQlite command to fetch ID of most recent INSERT
        //dbWrite = getWritableDatabase();
        db.delete(PARENT_TABLE_NAME,null,null);
        db.delete(TABLE_NAME,null,null);
        //db.execSQL(queryString);
        //db.close();

    }

}

