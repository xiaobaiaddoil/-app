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

public class dancixueAdapter extends BaseAdapter {
    private List<Word>data;
    private Context context;

    public dancixueAdapter(List<Word> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dancixueAdapter.Holder danci = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.xuanxiang_item,null);
            danci = new dancixueAdapter.Holder(convertView);
            convertView.setTag(danci);
        }else {
            danci = (dancixueAdapter.Holder) convertView.getTag();
        }
        Word word = data.get(position);
        String str = word.getTranslate();
        danci.btn.setText(str.toLowerCase());
        return convertView;
    }
    class Holder{
        TextView btn;
        public Holder(View v) {
            btn = (TextView) v.findViewById(R.id.btn_xuanxiang);
        }
    }
}
