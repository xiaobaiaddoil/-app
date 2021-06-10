package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SQL.UserHelper;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.http.BuildPath;


public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    public UserHelper userHelper = new UserHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_regist);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(SPUtils.contains(LoginActivity.this,"username")){Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login_click(View v){
        String user = username.getText().toString();
        String passw = password.getText().toString();
        new Thread(new Runnable() {
            Handler handler = new Handler();
            @Override
            public void run() {
                SQLiteDatabase db = userHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                String[]a= new String[]{user,passw};
                try {
                    Cursor cursor = db.query("users", null, "Username=? and Password =?",a, null, null,null);
                    if (cursor.getCount()==0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"用户不存在或密码错误",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                SPUtils.put(LoginActivity.this,"username",user);
                                SPUtils.put(LoginActivity.this,"userpassword",passw);
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void regist_click(View v){
        String user = username.getText().toString();
        String passw = password.getText().toString();
        new Thread(new Runnable() {
            Handler handler = new Handler();
            @Override
            public void run() {
                SQLiteDatabase db = userHelper.getWritableDatabase();
                String []ua = new String[]{user};
                try {
                    Cursor cursorOnlyUser = db.query("users", null, "Username=? ",ua, null, null,null);
                    if (cursorOnlyUser.getCount()!=0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"用户存在,请登录吧",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        ContentValues values = new ContentValues();
                        String day = BuildPath.getDateString();
                        values.put("Username",user);
                        values.put("Password",passw);
                        values.put("Name","管理员");
                        values.put("Day", day);
                        try {
                            db.insert("users",null,values);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,"注册成功，去登陆吧",Toast.LENGTH_LONG);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}