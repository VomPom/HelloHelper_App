package com.xrone.julis.compous.Application.Talk.dp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julis on 2018/5/27.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="hello_talk";
    private static final int DB_VERSION=1;
    private static DBHelper dbInstance;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }
    public synchronized static DBHelper getDbInstance(Context context){
        if(dbInstance==null){
            dbInstance=new DBHelper(context);
        }
        return dbInstance;
    }



    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 聊天记录
         */
        String sql_msg = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSG
                + "(" + DBcolumns.MSG_ID + " integer primary key autoincrement,"
                + DBcolumns.MSG_TEXT + " text,"
                + DBcolumns.MSG_ISCOMING + " integer,"
                + DBcolumns.MSG_DATE + " text,"
                + DBcolumns.MSG_TRAN_TEXT + " text);";
        db.execSQL(sql_msg);
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
