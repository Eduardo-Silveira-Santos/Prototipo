package com.eduardosantos.prototipo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class WorkerAdapter extends ArrayAdapter<Worker> {

    private Context mContext;
    private int mResource;

    public WorkerAdapter(@NonNull Context context, int resource, @NonNull List<Worker> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    static class ViewHolder {
        ShapeableImageView imageView;
        TextView nameTextView;
        TextView professionTextView;
        TextView ratingTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.listImage);
            viewHolder.nameTextView = convertView.findViewById(R.id.listName);
            viewHolder.professionTextView = convertView.findViewById(R.id.listProfession);
            viewHolder.ratingTextView = convertView.findViewById(R.id.listRating);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Worker worker = getItem(position);

        if (worker != null) {
            viewHolder.imageView.setImageResource(R.drawable.ic_list_worker);
            viewHolder.nameTextView.setText(worker.getName());
            viewHolder.professionTextView.setText(worker.getProfession());
            viewHolder.ratingTextView.setText(String.valueOf(worker.getRating()));
        }

        return convertView;
    }
}
