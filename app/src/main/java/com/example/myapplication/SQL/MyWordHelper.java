package com.example.myapplication.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyWordHelper extends SQLiteOpenHelper {

    public MyWordHelper(Context context) {
        super(context, "word.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //记录用户每天学习的单词表
        db.execSQL("CREATE TABLE XuexiWords(_id INTEGER PRIMARY KEY AUTOINCREMENT, Word VARCHAR(20),  Translate VARCHAR(50), Username varchar(20),LearnDate date,PlanName varchar(10))");
        //记录用户每天错误的单词
        db.execSQL("CREATE TABLE ErrorWords(_id INTEGER PRIMARY KEY AUTOINCREMENT, Word VARCHAR(20),  Translate VARCHAR(50),Username varchar(20),ErrorDate date,PlanName varchar(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
