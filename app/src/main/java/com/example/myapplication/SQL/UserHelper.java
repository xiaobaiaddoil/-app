package com.example.myapplication.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class UserHelper extends SQLiteOpenHelper {
    public UserHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户数据  _userid,主键  Username 登陆账号  Password  登陆密码   用户名
        db.execSQL(" CREATE TABLE if not exists users(_userid INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR(20),  Password VARCHAR(20), Name varchar(10),Day varchar(10))");
       //用户指定的计划表  _id 唯一标示符  Username和user表的账号绑定  PlanName计划名  PlanDayNum每天计划的单词数
        db.execSQL("CREATE TABLE if not exists userPlan(_id INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR(20),  PlanName varchar(10),PlanDayNum int)");
        //记录每日学习情况
        db.execSQL("CREATE TABLE if not exists LearnProcess(_id INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR(20),isLearn bool,Date date)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
