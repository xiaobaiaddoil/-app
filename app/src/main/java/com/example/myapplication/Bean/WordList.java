package com.example.myapplication.Bean;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WordList {
    Context context;
    String file;
    String path;
    public WordList(Context context, String filename) {
        this.context = context;
        this.file = filename;
        this.path = file+".txt";
    }

    public String getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    //1.读取txt文件内的单词和释义 单词取第一个，释义取最后一个
    //2.发送网络请求来获取发音，以及音频数据。
    //3.构建wordlist

    public List<Word> getwordlist(){
        List<Word> wordlist =new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open(path));
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt=br.readLine())!=null){
                if(!"".equals(lineTxt)){
                    String []line = lineTxt.split("\\s+");
                    if(line.length<=1) {
                        continue;
                    }
                    String wordTxt = line[0];
                    String shiyi = line[line.length-1];
                    Word word = new Word();
                    word.setWord(wordTxt);
                    word.setTranslate(shiyi);
                    wordlist.add(word);
                }
            }
            isr.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return wordlist;
    }

}
