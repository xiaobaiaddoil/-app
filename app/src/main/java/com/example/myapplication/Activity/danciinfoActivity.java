package com.example.myapplication.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;

import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Bean.WordInfo;
import com.example.myapplication.R;
import com.example.myapplication.http.BuildPath;
import com.example.myapplication.http.GetWordInfoAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class danciinfoActivity extends AppCompatActivity {
    private static final String TAG = "danciinfoActivity";
    //
    //
    private int type;
    //
    private ImageView fanhui;
    private TextView word_info;
    private TextView shiyi_info;
    private TextView info_yb_uk;
    private TextView info_yb_us;

    private TextView liju;
    private final MediaPlayer mMediaPlayerus = new MediaPlayer();
    private final MediaPlayer mMediaPlayeruk = new MediaPlayer();
    private String Word = "body";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danciinfo);
        init();
        initplayer();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.youdao.com/api/")
                .build();
        GetWordInfoAPI service = retrofit.create(GetWordInfoAPI.class);
        Call<ResponseBody> task = service.getJson(BuildPath.getwordpath(Word,"auto","auto"));
        Log.i("path---------",BuildPath.getwordpath(Word,"auto","auto"));
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG,"info=========>"+response.code());
                if(response.code()== HttpURLConnection.HTTP_OK){
                    try {
                        String result = response.body().string();
                        WordInfo info = new WordInfo();
                        info = parseDiffJson(result);
                        updateUI(info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"error =========>"+t.toString());
            }
        });
    }

    public void init(){
        word_info = (TextView)findViewById(R.id.info_word);
        shiyi_info =(TextView)findViewById(R.id.info_shiyi);
        info_yb_uk = (TextView)findViewById(R.id.info_yb_uk);
        info_yb_us = (TextView)findViewById(R.id.info_yb_us);

        liju = (TextView)findViewById(R.id.info_liju);
        Bundle bundle = getIntent().getExtras();
        Word = bundle.getString("word");
        type = bundle.getInt("type");
        word_info.setText(Word);
    }
    public WordInfo parseDiffJson(String json) {
        WordInfo info = new WordInfo();
        try {
            JSONObject jsonObject1 = new JSONObject(json);
            JSONArray translation = jsonObject1.getJSONArray("translation");
            Map<String, JSONArray> Word = new HashMap<>();
            JSONArray web =jsonObject1.getJSONArray("web");
            String us_phonetic = jsonObject1.getJSONObject("basic").getString("us-phonetic");
            String uk_phonetic = jsonObject1.getJSONObject("basic").getString("uk-phonetic");
            JSONArray x = new JSONArray();
            for(int i = 0;i<web.length();i++){
                JSONObject jvalue = web.getJSONObject(i);
                Log.i("---------------",jvalue.toString());
                x = jvalue.getJSONArray("value");
                String key = jvalue.getString("key");
                Word.put(key,x);
            }
//            if(basic.getJSONArray("wfs")==null){
//            }else {
//                JSONArray wfs = basic.getJSONArray("wfs");
//                for(int i = 0;i<wfs.length();i++){
//                    JSONObject jvalue = wfs.getJSONObject(i);
//                    Log.i("---------------",jvalue.toString());
//                    JSONObject jvalue1 = jvalue.getJSONObject("wf");
//                    String name = jvalue1.getString("name");
//                    String values = jvalue1.getString("value");
//                    Wfs.put(name,values);
//                }
//            }

            String translate = (String) translation.get(0);
            info.setTranslate(translate);
            info.setUk_phonetic(uk_phonetic);
            info.setUs_phonetic(us_phonetic);
            info.setWord(Word);
//            info.setWfs(Wfs);
        } catch (Exception e) {
            info = null;
            e.printStackTrace();
        }
        return info;
    }
    public void updateUI(WordInfo info){
        if(info.getTranslate().isEmpty()){
            shiyi_info.setText(Word);
            info_yb_us.append("null");
            info_yb_uk.append("null");
            liju.setText("null");
        }
        else{
            Log.e(TAG,"info____________"+info.getTranslate());
            shiyi_info.setText(info.getTranslate());
            info_yb_us.append(info.getUs_phonetic());
            info_yb_uk.append("\t\t英式"+info.getUk_phonetic());
            for (Map.Entry<String, JSONArray> entry:info.getWord().entrySet()){
                liju.append("\n\n"+entry.getKey()+"\n");
                liju.append(entry.getValue().toString()
                        .replace("\"","")
                        .replace("[","")
                        .replace("]",""));
         }
        }
//        for (Map.Entry<String, String> entry:info.getWfs().entrySet()){
//            liju.append("\n\n"+entry.getKey()+"\n");
//            liju.append(entry.getValue().toString()
//                    .replace("\"","")
//                    .replace("[","")
//                    .replace("]",""));
//            Log.i("map-------元素--","Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
    }
    public void getback() {
        if (type == 0) {

        } else if (type == 1) {

        } else {

        }
    }
    public void initplayer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mMediaPlayerus.setDataSource(BuildPath.getUsvoicepath(word_info.getText().toString()));
                    mMediaPlayeruk.setDataSource(BuildPath.getUkvoicepath(word_info.getText().toString()));
                    mMediaPlayeruk.prepare();
                    mMediaPlayerus.prepare();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void playVoice(View v) {
        switch (v.getId()){
            case R.id.info_voice_us:
                mMediaPlayerus.start();
                break;
            case R.id.info_voice_uk:
                mMediaPlayeruk.start();
                break;
        }
    }
}