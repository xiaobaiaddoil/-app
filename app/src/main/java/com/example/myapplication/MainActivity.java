package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telecom.CallScreeningService;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Activity.cuociActivity;
import com.example.myapplication.Activity.danciActivity;
import com.example.myapplication.Activity.danciinfoActivity;
import com.example.myapplication.Bean.Juzi;
import com.example.myapplication.Bean.Word;
import com.example.myapplication.Bean.WordInfo;
import com.example.myapplication.Bean.WordList;

import com.example.myapplication.Calendar.DakaActivity;
import com.example.myapplication.SQL.UserHelper;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.http.BuildPath;
import com.example.myapplication.http.GetBgInfoAPI;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

//我们要做的内容：
//要有用户登陆注册的内容 为了让我们进行不同角色的不同计划和学习进度的查询以及更改
//  1.将选择的计划默认选中
//  2.如果第一次进入或者选择新计划则有计划设计界面，设置完计划后我们要存入相关计划进数据库，且玩家可修改计划。
//  3.将设置好的计划存入数据库
//  4.主页面得到数据我们每次到学习页面时进行传递，通过选中的计划相关的内容
public class MainActivity extends AppCompatActivity {
    private UserHelper myUserHelper = new UserHelper(this);
    private RadioGroup rp_btn ;
    private WordList wordList;
    public static List<Word>data;
    private TextView jihua_tv;
    private TextView num_tv;
    private TextView day_tv;
    private TextView num_day;
    private EditText search_et;
    private String filename;
    private String plannum;
    private TextView sp;
    private String tempdays ;
    private int mrdcs;
    private int hfdst;
    private String TAG=  "MainActivity";
    private Boolean setting = false;
    private int status_le;
    public static Juzi jz1 = new Juzi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initmenu();
        rp_btn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                day_tv.setText(tempdays);
                switch (checkedId) {
                    case R.id.siji_rtb:
                        filename = "四级";
                        setjihua(filename);
                        showDialog(filename,data.size(),10);
                        if (SPUtils.contains(MainActivity.this, "planname")) {
                            SPUtils.put(MainActivity.this, "planname", filename);
                        }
                        break;
                    case R.id.liuji_rtb:
                        filename = "六级";
                        setjihua(filename);
                        showDialog(filename,data.size(),10);
                        if (SPUtils.contains(MainActivity.this, "planname")) {
                            SPUtils.put(MainActivity.this, "planname", filename);
                        }
                        break;
                    case R.id.tuofu_rtb:
                        filename = "test";
                        setjihua(filename);
                        showDialog(filename,data.size(),10);
                        if (SPUtils.contains(MainActivity.this, "planname")) {
                            SPUtils.put(MainActivity.this, "planname", filename);
                        }
                        break;
                }
            }
        });
        final DrawerLayout drawerLayout=findViewById(R.id.drawerlayout);
        //给按钮添加一个监听器
        findViewById(R.id.top_view_left_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开侧滑菜单
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void gotoxuexiclick(View v){
        if(!setting){
            Toast.makeText(MainActivity.this,"你还没选择学习的词库哦",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, danciActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("num",mrdcs);
            if(SPUtils.contains(MainActivity.this,"status"+filename)){
                int status = 0;
                //这个计划今天学了几个单词
                status = (int) SPUtils.get(MainActivity.this,"status"+filename,status);
                bundle.putInt("status",status);
            }
            else {
                bundle.putInt("status",0);
            }
            bundle.putString("filename",filename);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    public void init(){
        rp_btn = (RadioGroup)findViewById(R.id.btn_rg);
        jihua_tv = (TextView)findViewById(R.id.jihua);
        num_tv = (TextView)findViewById(R.id.num);
        day_tv = (TextView)findViewById(R.id.day);
        num_day = (TextView)findViewById(R.id.wordday);
        plannum = num_tv.getText().toString();
        sp = (TextView)findViewById(R.id.num_sp);
        tempdays = day_tv.getText().toString();
        search_et = (EditText)findViewById(R.id.search_text);
    }
    public void setjihua(String filename){
        wordList = new WordList(this,filename);
        data = wordList.getwordlist();
        Log.e(TAG,"data个数"+data.size());
        jihua_tv.setText(filename+"计划");
        String strlast ="";
        strlast = plannum+data.size()+"个";
        num_tv.setText(strlast);
    }
    public void cunchujihua(String plan,int num){

            SQLiteDatabase dbr = myUserHelper.getReadableDatabase();
            SQLiteDatabase dbw = myUserHelper.getWritableDatabase();
            String name = new String();
            name = (String) SPUtils.get(this,"username",name);
            Log.i(TAG, "username---------------"+name);
            Cursor cursor;
            String [] query = new String[]{name,plan};
            ContentValues value = new ContentValues();
            value.put("Username",name);
            value.put("Planname",plan);
            value.put("PlanDayNum",num);
            try {
                cursor = dbr.query("userPlan",null,"Username=?and PlanName=?",query,null,null,null);
                if(cursor.getCount()==0){
                    dbw.insert("userPlan",null,value);
                }
                else {
                    dbw.update("userPlan",value,"Username=?and PlanName=?",query);
                }
                } catch (Exception e) {
                    e.printStackTrace();
               }
    }
    public void search(View view) {
        if(!search_et.getText().toString().trim().isEmpty()){
            String word = search_et.getText().toString().trim();
            word = word.replace("\n","");
            Intent i = new Intent();
            i.setClass(MainActivity.this, danciinfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("word",search_et.getText().toString());
            bundle.putInt("type",0);
            i.putExtras(bundle);
            startActivity(i);
        }
        else {
            Toast.makeText(MainActivity.this,"请输入正确的单词",Toast.LENGTH_SHORT).show();
        }
    }
    public void showDialog(String planname,int sum_num,int num){
        setting = true;
        final int[] real_num = {num};
        final AlertDialog.Builder setPlanDialog = new AlertDialog.Builder(MainActivity.this);
        setPlanDialog.setTitle("设置"+planname+"计划");
        setPlanDialog.setIcon(R.drawable.setting);
        setPlanDialog.setMessage("来设置/修改你的计划");
        View v = (LinearLayout)getLayoutInflater().inflate(R.layout.dialog_view,null);
        setPlanDialog.setView(v);
        Spinner sp_num = (Spinner)v.findViewById(R.id.sp_set_num);
        TextView sp_day = (TextView)v.findViewById(R.id.sp_set_day);
        sp_num.setSelection(num/5-2,true);
        sp_day.setText(String.valueOf(sum_num/num)+"天");
        sp_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                real_num[0] = Integer.parseInt(parent.getItemAtPosition(position).toString());
                String days = String.valueOf(sum_num/ real_num[0]);
                String text = days+"天";
                sp_day.setText(text);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setPlanDialog.setPositiveButton("设置/修改",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mrdcs = real_num[0];
                hfdst = sum_num/real_num[0];
                sp.setText(String.valueOf(mrdcs));
                day_tv.setText(tempdays+hfdst+"天");
                cunchujihua(filename,mrdcs);
                Toast.makeText(MainActivity.this,"计划每天"+mrdcs+"这么多天"+hfdst,Toast.LENGTH_SHORT).show();
            }
        });
        setPlanDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mrdcs = num;
                hfdst = sum_num/num; sp.setText(String.valueOf(mrdcs)+"个");
                day_tv.setText(tempdays+hfdst+"天");
                cunchujihua(filename,mrdcs);
                Toast.makeText(MainActivity.this,"计划每天"+mrdcs+"这么多天"+hfdst,Toast.LENGTH_SHORT).show();
            }
        });
        setPlanDialog.create().show();
    }
    public void initmenu(){
        TextView tv_username = findViewById(R.id.username_show);
        String name = (String) SPUtils.get(MainActivity.this,"username","--");
        tv_username.setText("用户："+name);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.youdao.com/api/")
                .build();
        String url = BuildPath.getTodayEnglish();
        GetBgInfoAPI server = retrofit.create(GetBgInfoAPI.class);
        Call<ResponseBody> task = server.getJson(url);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()== HttpURLConnection.HTTP_OK){
                    try {
                        String result = response.body().string();
                        JSONObject j1 = new JSONObject(result);
                        jz1= new Juzi();
                        jz1.setContent(j1.getString("content"));
                        jz1.setTrans(j1.getString("translation"));
                        JSONArray image = j1.getJSONArray("share_img_urls");
                        String url = (String) image.get(0);
                        ImageView i1 = findViewById(R.id.image_main);
                        Picasso.with(MainActivity.this).load(url).into(i1);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    public void menuClick(View v){
        Intent i = new Intent();
        switch (v.getId()){
            case R.id.btn1:
                gotoxuexiclick(v);
                break;
            case R.id.btn2:
                i.setClass(MainActivity.this, cuociActivity.class);
                startActivity(i);
                break;
            case R.id.btn3:
                Bundle b = new Bundle();
                b.putInt("type",0);
                b.putString("planName",filename);
                i.putExtras(b);
                i.setClass(MainActivity.this, DakaActivity.class);
                startActivity(i);
                break;
            case R.id.btn4:
                SPUtils.clear(MainActivity.this);
                finish();
                break;
        }
    }
    public void getian_init(){
        String lastLaunchTime = (String) SPUtils.get(MainActivity.this,"LaunchLastTime","ss");
        //说明不是上次登陆的时间
        if(lastLaunchTime!=BuildPath.getDateString()){
            setting = false;
            status_le = 0;
        }
    }
}