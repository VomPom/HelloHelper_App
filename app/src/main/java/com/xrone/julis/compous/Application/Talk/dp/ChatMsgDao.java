package com.xrone.julis.compous.Application.Talk.dp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xrone.julis.compous.Application.Talk.bean.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Julis on 2018/5/27.
 */

public class ChatMsgDao  {
    private DBHelper helper;

    public ChatMsgDao(Context context) {
        helper = new DBHelper(context);
    }
    /**
     * 添加新信息
     *
     * @param msg
     */
    public int insert(ChatMessage msg) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBcolumns.MSG_TEXT, msg.getContent());
        values.put(DBcolumns.MSG_TRAN_TEXT, msg.getTran_content());
        values.put(DBcolumns.MSG_ISCOMING, msg.getIsComing());
        values.put(DBcolumns.MSG_DATE, msg.getDate());
        db.insert(DBcolumns.TABLE_MSG, null, values);
        int id=queryTheLastMsgId();
        db.close();
        return id;
    }

    /**
     * 查询列表,每页返回15条,依据id逆序查询，将时间最早的记录添加进list的最前面
     *
     * @return
     */
    public ArrayList<ChatMessage> queryMsg(int offset) {
        ArrayList<ChatMessage> list = new ArrayList<ChatMessage>();
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from " + DBcolumns.TABLE_MSG + " asc limit ?,?";
        String[] args = new String[]{String.valueOf(offset), "15"};
        Cursor cursor = db.rawQuery(sql, args);
        ChatMessage msg = null;
        while (cursor.moveToNext()) {
            msg = new ChatMessage();
            msg.setMsgId(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID)));
            msg.setContent(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_TEXT)));
            msg.setIsComing(cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ISCOMING)));
            msg.setDate(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_DATE)));
            msg.setTran_content(cursor.getString(cursor.getColumnIndex(DBcolumns.MSG_TRAN_TEXT)));
            list.add(0, msg);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * 查询最新一条记录的id
     *
     * @return
     */
    public int queryTheLastMsgId() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select " + DBcolumns.MSG_ID + " from " + DBcolumns.TABLE_MSG + " order by " + DBcolumns.MSG_ID + " desc limit 1";
        String[] args = new String[]{};
        Cursor cursor = db.rawQuery(sql, args);
        int id = -1;
        if (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(DBcolumns.MSG_ID));
        }
        cursor.close();
        db.close();
        return id;
    }
    /**
     * 清空所有聊天记录
     */
    public void deleteTableData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DBcolumns.TABLE_MSG, null, null);
        db.close();
    }



}
