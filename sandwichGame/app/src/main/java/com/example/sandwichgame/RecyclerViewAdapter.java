package com.example.sandwichgame;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<RecycleViewItem> itemList;
    private MainActivity ac;

    public RecyclerViewAdapter(Activity activity, List<RecycleViewItem> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        TextView score;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.lank_item_name);
            number = (TextView) itemView.findViewById(R.id.lank_item_number);
            score = (TextView) itemView.findViewById(R.id.lank_item_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            itemList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                            itemList.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lank_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RecycleViewItem data = itemList.get(position);

        // 데이터 결합
        holder.name.setText(data.getName());
        holder.number.setText(data.getNumber());
        holder.score.setText(data.getScore());
    }

    private void removeItemView(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size()); // 지워진 만큼 다시 채워넣기.
    }
}