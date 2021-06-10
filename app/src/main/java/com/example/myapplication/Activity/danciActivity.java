package com.example.myapplication.Activity;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Bean.Word;
import com.example.myapplication.Bean.WordList;
import com.example.myapplication.Calendar.DakaActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SQL.MyWordHelper;
import com.example.myapplication.SQL.UserHelper;
import com.example.myapplication.Utils.RandUtils;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.Utils.WordSQLUtils;
import com.example.myapplication.adapter.dancixueAdapter;
import com.example.myapplication.http.BuildPath;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class danciActivity extends AppCompatActivity {
    //自己定义的类
    private RandUtils rand = new RandUtils();
    private MyWordHelper myWordHelper = new MyWordHelper(this);
    private WordSQLUtils sql= new WordSQLUtils(this);
    //网络请求的相关内容  这个里面我只需要读取一下发音 考虑保存到本地，且和建立联系

    private List<Word>datalist,staticDatalist;
    private Word wordxuexi;
    private List<Word>wordshiyi = new ArrayList<>();
    private String planName = "";
    private int planNum = 0;
    private int status = 0;


    private dancixueAdapter danci;

    private TextView danci_fayin;
    private TextView lastword_tv;
    private ListView xuanxiang_lv;
    private TextView title;
    private final MediaPlayer mMediaPlayerus = new MediaPlayer();
    private String TAG = "danciActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beidanci_xuexi);
        String tag_stauts ="";
        int learn_status = -1;
        //记录用户计划的学习完成度
        tag_stauts = (String) SPUtils.get(danciActivity.this,"isfinish"+planName,tag_stauts);
        learn_status = (int) SPUtils.get(danciActivity.this,"status"+planName,learn_status);
        if(tag_stauts=="1"){
            try {
                Thread.sleep(1000);
                Intent i = new Intent();
                i.putExtra("type",3);
                //value 3代表学习任务完成
                i.setClass(danciActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
            {
                init();
                 getxuexiword();
                initplayer();
                danci=new dancixueAdapter(wordshiyi,this);
                xuanxiang_lv.setAdapter(danci);
                title.setText(wordxuexi.getWord());
                xuanxiang_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                tiaozhuan(position,1);
                                break;
                            case 1:
                                tiaozhuan(position,1);
                                break;
                            case 2:
                                tiaozhuan(position,1);
                                break;
                            case 3:
                                tiaozhuan(position,1);
                                break;
                        }
                    }
                });
        }
    }
    public void init(){
        danci_fayin = (TextView)findViewById(R.id.fayin);
        xuanxiang_lv = (ListView)findViewById(R.id.dancixuanxiang_lv);
        title = (TextView)findViewById(R.id.wordxuexi_tv);
        lastword_tv = (TextView)findViewById(R.id.lastword);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        status = bundle.getInt("status");
        planNum = bundle.getInt("num");
        planName = bundle.getString("filename");
        if(status!=0){
            String text = bundle.getString("lastword")+bundle.getString("lastwordtrans");
            lastword_tv.setText(text);
        }
    }

    public void getxuexiword(){
            staticDatalist = new WordList(this,planName).getwordlist();
            List<Word>xuexilist = sql.querybyPlan(myWordHelper,"XuexiWords",planName);
            datalist = staticDatalist;
            if(datalist==xuexilist){
            }else {
                if(datalist==null){
                    try {
                        Thread.sleep(1000);
                        Intent i = new Intent();
                        i.putExtra("type",3);
                        //value 3代表学习任务完成
                        //Toast.makeText(danciActivity.this,"今日学习计划已完成",Toast.LENGTH_SHORT).show();
                        i.setClass(danciActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                datalist.remove(xuexilist);
                Log.e(TAG,"value------"+datalist.toString());
                wordxuexi=datalist.get(rand.getNum(0,datalist.size()));
                //插入数据
                String name = new String();
                name = (String) SPUtils.get(danciActivity.this,"username",name);
                sql.insert(myWordHelper,"XuexiWords",wordxuexi,name,BuildPath.getDateString(),planName);
                datalist.remove(wordxuexi);
                for(int i =0;i < 3;i++){
                    wordshiyi.add(staticDatalist.get(rand.getNum(0,staticDatalist.size())));
                }
                wordshiyi.add(wordxuexi);
                Collections.shuffle(wordshiyi);
            }
    }
    public void initplayer(){
        new Thread(() -> {
            try {
                mMediaPlayerus.setDataSource(BuildPath.getUsvoicepath(wordxuexi.getWord()));
                mMediaPlayerus.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

    }
    public void click(View v){
        switch (v.getId()){
            case R.id.return_btn:
                Intent intent = new Intent();
                intent.setClass(danciActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.skip_danci:
                //进行跳转  知道该单词意思 下一个单词
                tiaozhuan(0,2);
                break;
            case R.id.voice_danci:
                //获取单词发音并放出来，放一次；多次点击只有一次网络请求
                mMediaPlayerus.start();
                break;
            case R.id.tips_danci:
                //跳转到单词详情页
                Intent intent1 = new Intent();
                intent1.setClass(danciActivity.this,danciinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("word",wordxuexi.getWord());
                bundle.putInt("type",1);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
        }
    }
    public void tiaozhuan(int position,int type){

        Intent intent1 = new Intent();
        intent1.setClass(danciActivity.this,danciActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename",planName);
        bundle.putInt("num",planNum);
        bundle.putString("lastword",wordxuexi.getWord());
        bundle.putString("lastwordtrans",wordxuexi.getTranslate());
        Word a = (Word)danci.getItem(position);

        if(a.getWord().equals(title.getText().toString())||type!=1)
        {
            setLearnProcess();
            if(status<planNum){
                status++;
                //记录学习了几个
                bundle.putInt("status",status);
                intent1.putExtras(bundle);
                startActivity(intent1);
                finish();
            }
            else {
                //跳转到完成学习打卡界面。
                Intent i = new Intent();
                Bundle b = new Bundle();
                b.putInt("type",1);
                b.putString("planName",planName);
                i.putExtras(b);
                i.setClass(danciActivity.this, DakaActivity.class);
                startActivity(i);
                finish();
            }
        }
        else {
            if (sql.queryByWord(myWordHelper,"ErrorWords",wordxuexi))
            {
                Toast.makeText(danciActivity.this,"你已经错过一次了，加油哦",Toast.LENGTH_SHORT).show();
            }else {
                String name = new String();
                name = (String) SPUtils.get(danciActivity.this,"username",name);

                sql.inserttoError(myWordHelper,wordxuexi,name,BuildPath.getDateString(),planName);
            }
            Intent intent = new Intent();
            intent.setClass(danciActivity.this,danciinfoActivity.class);
            bundle.putString("word",wordxuexi.getWord());
            bundle.putInt("type",0);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    public void setLearnProcess(){
        SPUtils.put(danciActivity.this,"status"+planName,status);
        if(status==planNum){
            SPUtils.put(danciActivity.this,"isfinish"+planName,true);
        }
    }
}