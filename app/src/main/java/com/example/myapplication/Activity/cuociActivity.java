package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Bean.Word;
import com.example.myapplication.R;
import com.example.myapplication.SQL.MyWordHelper;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.Utils.WordSQLUtils;
import com.example.myapplication.adapter.danciAdapter;

import java.util.ArrayList;
import java.util.List;

public class cuociActivity extends AppCompatActivity {
    private MyWordHelper myWordHelper = new MyWordHelper(this);
    private WordSQLUtils sql = new WordSQLUtils(this);
    private List<Word>wordList = new ArrayList<>();
    private danciAdapter adapter;
    private ListView lvWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuoci);
        String username = (String) SPUtils.get(cuociActivity.this,"username","test");
        lvWord = (ListView)findViewById(R.id.lv_cuoci);
        wordList = sql.queryAllerrorbyuser(myWordHelper,username);
        if(wordList.size()==0){
            Toast.makeText(cuociActivity.this,"你现在还没有犯错哦",Toast.LENGTH_SHORT).show();
        }
        adapter = new danciAdapter(this,wordList);
        lvWord.setAdapter(adapter);
        lvWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word w = (Word)adapter.getItem(position);
                Intent intent = new Intent();
                intent.setClass(cuociActivity.this,danciinfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("word",w.getWord());
                bundle.putInt("type",2);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}