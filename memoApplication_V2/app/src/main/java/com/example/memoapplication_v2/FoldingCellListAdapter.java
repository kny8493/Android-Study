package com.example.memoapplication_v2;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FoldingCellListAdapter extends ArrayAdapter<DiaryItem> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    public FoldingCellListAdapter(Context context, List<DiaryItem> objects) {
        super(context, 0, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FirebaseApp.initializeApp(getContext());

        // get item for selected view
        DiaryItem item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.day = cell.findViewById(R.id.main_date_day);
            viewHolder.month = cell.findViewById(R.id.main_date_month);
            viewHolder.year = cell.findViewById(R.id.main_date_year);
            viewHolder.title = cell.findViewById(R.id.main_title);

            viewHolder.cellContent = cell.findViewById(R.id.cell_content_data);
            viewHolder.cellDate = cell.findViewById(R.id.cell_date_data);
            viewHolder.cellTitle = cell.findViewById(R.id.cell_title);
            viewHolder.cellImage = cell.findViewById(R.id.cell_img);


            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        // bind data from selected element to view through view holder
        String[] tmp = item.getDate().split("\\.");
        viewHolder.year.setText(tmp[0]);
        viewHolder.month.setText(tmp[1]);
        viewHolder.day.setText(tmp[2]);
        viewHolder.title.setText(item.getTitle());

        viewHolder.cellTitle.setText(item.getTitle());
        viewHolder.cellDate.setText(item.getDate());
        viewHolder.cellContent.setText(item.getContent());

        StorageReference storageRef;
        // 이미지 url통해 이미지 가져오기
        if(!item.getImageUri().isEmpty()){
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://memoapplicationv2.appspot.com").child("images/" + item.getImageUri());

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(viewHolder.cellImage)
                            .load(uri)
                            .apply(new RequestOptions().override(600, 300))
                            .into(viewHolder.cellImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } else {
            viewHolder.cellImage.setImageResource(0);
        }
        return cell;
    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position)){
            displayDetail(position);
            registerFold(position);}
        else {

            registerUnfold(position);
        }
    }

    public void displayDetail (int position) {

    }


    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView day;
        TextView month;
        TextView year;
        TextView title;
        TextView cellDate;
        TextView cellTitle;
        TextView cellContent;
        ImageView cellImage;
    }
}
