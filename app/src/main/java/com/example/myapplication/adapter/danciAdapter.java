package com.example.myapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Bean.Word;
import com.example.myapplication.R;

import java.util.List;

public class danciAdapter extends BaseAdapter {
    private Context context;
    private List<Word>wordList;

    public danciAdapter(Context context, List<Word> List) {
        this.context = context;
        this.wordList = List;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        danciAdapter.danci danci = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cuoci_item,null);
            danci = new danciAdapter.danci(convertView);
            convertView.setTag(danci);
        }else {
            danci = (danciAdapter.danci) convertView.getTag();
        }
        Word word = (Word)getItem(position);
        danci.tvshiyi.setText(word.getTranslate());
        danci.tvword.setText(word.getWord());

        return convertView;
    }
    class danci{
        TextView tvword;
        TextView tvshiyi;

        public danci(View view) {
            tvword = view.findViewById(R.id.tv_word);
            tvshiyi = view.findViewById(R.id.tv_word_shiyi);
        }
    }
}
