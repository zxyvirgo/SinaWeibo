package com.fangjie.weibo.db;

import com.fangjie.weibo.bean.UserInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context)
    {
        super(context,"weibo",null,1);//数据库名称
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql="CREATE TABLE IF NOT EXISTS  "+UserInfo.TB_NAME+" ( _id INTEGER PRIMARY KEY,userId TEXT, userName TEXT, token TEXT,isDefault TEXT,userIcon BLOB)";
        sqLiteDatabase.execSQL(sql);      //表明
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


}
