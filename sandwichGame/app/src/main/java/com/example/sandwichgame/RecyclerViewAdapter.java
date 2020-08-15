package com.example.sandwichgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;

    private Activity activity;
    private List<RecycleViewItem> itemList;

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
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.lank_item_name);
            number = (TextView) itemView.findViewById(R.id.lank_item_number);
            score = (TextView) itemView.findViewById(R.id.lank_item_score);
            image = (ImageView) itemView.findViewById(R.id.lank_item_img);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lank_item, parent, false);
        return new ViewHolder(view);
    }

    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터를 결합
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RecycleViewItem data = itemList.get(position);

        holder.name.setText(data.getName());
        holder.number.setText(data.getNumber());
        holder.score.setText(data.getScore());

        // 이미지 url통해 이미지 가져오기
        storageRef = storage.getReferenceFromUrl("gs://sandwichgameimage.appspot.com").child("images/" + data.getImageUrl());

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.image)
                        .load(uri)
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}