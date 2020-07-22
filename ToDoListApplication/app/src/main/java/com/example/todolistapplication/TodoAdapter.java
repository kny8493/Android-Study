package com.example.todolistapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Map;

public class TodoAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<TodoData> todoDataArrayList;
    private Map<Integer, Boolean> states;

    public TodoAdapter(Context context, ArrayList<TodoData> data) {
        mContext = context;
        todoDataArrayList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public TodoAdapter() {}

    @Override
    public int getCount() {
        return todoDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return todoDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        Log.d("convertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.todo_icon);
        TextView title = (TextView) convertView.findViewById(R.id.todo_title);
        TextView priority = (TextView) convertView.findViewById(R.id.todo_priority_data);
        TextView date = (TextView) convertView.findViewById(R.id.todo_date_data);
        SwitchButton switchcompact = (SwitchButton) convertView.findViewById(R.id.todo_status);
        switchcompact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                states.put(position, isChecked);
            }
        });

        TodoData todo = todoDataArrayList.get(position);

        imageView.setImageResource(todo.getImageView());
        title.setText(todo.getTitle());
        date.setText(todo.getDate());
        priority.setText(todo.getPriority());
        switchcompact.setChecked(false);



        return convertView;
    }
    // 추가
    public void addItem(int img, String addTitle, String addStatus, String addDate, String addPriority, String addMemo){

        TodoData data = new TodoData();

        data.setImageView(img);
        data.setPriority(addPriority);
        data.setStatus(addStatus);
        data.setTitle(addTitle);
        data.setDate(addDate);
        data.setMemo(addMemo);

        todoDataArrayList.add(data);

    }
}
