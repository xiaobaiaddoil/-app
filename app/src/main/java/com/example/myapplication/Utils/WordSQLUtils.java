package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.myapplication.Bean.Word;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordSQLUtils {
    private Cursor cursor;
    private Context context;

    public WordSQLUtils(Context context) {
        this.context = context;
    }

    public void insert(SQLiteOpenHelper myhelper, String tablename, Word w, String username, String date,String planname){

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("Word",w.getWord());
            values.put("Translate",w.getTranslate());
            values.put("Username",username);
            values.put("LearnDate",date);
            values.put("PlanName",planname);
            db.insert(tablename,null,values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void inserttoError(SQLiteOpenHelper myhelper, Word w, String username, String date,String planname){

        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("Word",w.getWord());
            values.put("Translate",w.getTranslate());
            values.put("Username",username);
            values.put("ErrorDate",date);
            values.put("PlanName",planname);
            db.insert("ErrorWords",null,values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean queryByWord(SQLiteOpenHelper myhelper,String tablename,Word w){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[]a = {w.getWord()};
        boolean b;
        try{
            cursor= db.query(tablename, null,"Word=?",a,null,null,null);
            if(cursor.getCount() == 0){
                b=false;
            }else {
                b=true;
            }
        } catch (Exception e) {
            b = false;
            e.printStackTrace();
        }

        cursor.close();
        db.close();
        return b;
    }
    public List<Word> queryAllerrorbyuser(SQLiteOpenHelper myhelper,String username){
        List<Word>datalist = new ArrayList<>();
        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.query("ErrorWords", null, "Username=?", new String[]{username}, null,
                null, null);
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
        } else {
            Word w = new Word();
            cursor.moveToFirst();
            w.setWord(cursor.getString(1));
            w.setTranslate(cursor.getString(2));
            datalist.add(w);
        }
        while (cursor.moveToNext()) {
            Word w = new Word();
            w.setWord(cursor.getString(1));
            w.setTranslate(cursor.getString(2));
            datalist.add(w);
        }
        cursor.close();
        db.close();
        return datalist;
    }
    public List<Word> querybyPlan(SQLiteOpenHelper myhelper,String tablename,String planname){
        List<Word>datalist = new ArrayList<>();
        SQLiteDatabase db = myhelper.getReadableDatabase();
        Cursor cursor = db.query(tablename, null, "PlanName = ?", new String[]{planname}, null,
                null, null);
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
        } else {
            Word w = new Word();
            cursor.moveToFirst();
            w.setWord(cursor.getString(1));
            w.setTranslate(cursor.getString(2));
            datalist.add(w);
        }
        while (cursor.moveToNext()) {
            Word w = new Word();
            w.setWord(cursor.getString(1));
            w.setTranslate(cursor.getString(2));
            datalist.add(w);
        }
        cursor.close();
        db.close();
        return datalist;
    }
    public void delete(SQLiteOpenHelper myhelper,String tablename,Word w){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[]a = {w.getWord()};
        db = myhelper.getWritableDatabase();
        db.delete(tablename, "Word=?",a);
        db.close();
    }
}
