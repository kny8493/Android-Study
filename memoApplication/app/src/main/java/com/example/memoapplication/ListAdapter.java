package com.example.memoapplication;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Optional;

public class ListAdapter extends BaseAdapter {

    private TextView textView;

    private ArrayList<SampleData> listItems = new ArrayList<SampleData>();

    public ListAdapter(){}

    @Override
    public int getCount() {
        return listItems.size();
    }

    // 현재 position에 있는 데이터 return
    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    // 지정한 위치에 있는 데이터와 관계된 아이템 id get
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        // layout inflate하여 convertView참조 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 view(위에 레이아웃)으로부터 위젯에대한 참조 획득
        textView = (TextView) convertView.findViewById(R.id.memo_text);

        SampleData sampleData = listItems.get(position);

        // 각각의 아이탬 각 위젯에 데이터 세팅
        textView.setText(sampleData.getStr());

        return convertView;
    }

    // 데이터 추가함수
    public void addItem(String str){
        SampleData data = new SampleData(str);
        listItems.add(data);

    }

}
