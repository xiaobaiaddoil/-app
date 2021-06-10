package com.example.myapplication.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activity.danciActivity;
import com.example.myapplication.Bean.Juzi;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SQL.UserHelper;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.http.BuildPath;
import com.othershe.calendarview.weiget.CalendarView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DakaActivity extends AppCompatActivity {

    private TextView yw_tv;
    private TextView zw_tv;
    private Button daka_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_for_daka);
       // getmarkday();
        init();
    }
    public void init(){
        CalendarView cv = findViewById(R.id.calendar);
        yw_tv = findViewById(R.id.mryj_yw);
        zw_tv = findViewById(R.id.mryj_zw);
        Juzi z1 =  MainActivity.jz1;
        yw_tv.setText(z1.getContent());
        zw_tv.setText(z1.getTrans());
        daka_btn = findViewById(R.id.daka);
        TextView daka_month_tv = findViewById(R.id.daka_month);
        Date d = new Date(System.currentTimeMillis());
        daka_month_tv.setText(DateFormat.format("yyyy年MM月",d));
        List<String>finishday = getmarkday();
        cv
                .setStartEndDate("2020.6","2050.1")
                .setInitDate("2021.6")
                .setMultiDate(finishday)
                .init();

    }


    public List<String> getmarkday(){
        String username = (String) SPUtils.get(DakaActivity.this,"username","test");
        UserHelper userHelper = new UserHelper(this);
        SQLiteDatabase db = userHelper.getReadableDatabase();
        Cursor cursor;
        List<String>finishday=new ArrayList<>();
        cursor = db.query("LearnProcess", null,"Username=?", new String[]{username},null,null,null);
        if(cursor.getCount()==0){

        }
        else {
            while (cursor.moveToNext()){

                if(cursor.getInt(2)==1){
                    finishday.add(cursor.getString(3));
                }
            }
        }
        return finishday;
    }
    public void tianjiadaka(View v){
        Bundle i = getIntent().getExtras();
        int from = i.getInt("type");
        String planName = i.getString("planName");
        if(from==1){
            daka();
            Intent intent = new Intent();
            intent.setClass(DakaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(from==0){
            Boolean isfinish = (Boolean) SPUtils.get(DakaActivity.this,"isfinish"+planName,false);
            if(isfinish)daka();
            else Toast.makeText(DakaActivity.this,"打卡失败，今天学习计划没有完成哦",Toast.LENGTH_SHORT).show();
            return;
        }

    }
    public void daka(){
        UserHelper h = new UserHelper(DakaActivity.this);
        SQLiteDatabase db1 = h.getWritableDatabase();
        SQLiteDatabase db2 = h.getReadableDatabase();

        ContentValues value = new ContentValues();
        String name = "";
        name = (String) SPUtils.get(DakaActivity.this,"username",name);
        value.put("Username",name);
        value.put("isLearn",true);
        value.put("Date",BuildPath.getDateStringCalendar());
        Cursor cursor = db2.query("LearnProcess",null,"Username=?and Date=? and isLearn=?",new String[]{name,BuildPath.getDateStringCalendar(),"1"},null,null,null);
        if(cursor.getCount()==0){
            db1.insert("LearnProcess",null,value);
        }
        CalendarView cv = findViewById(R.id.calendar);
        List<String> x= getmarkday();
        cv.setMultiDate(x);
        Toast.makeText(DakaActivity.this,"打卡成功！！！",Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}