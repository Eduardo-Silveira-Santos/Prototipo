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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout for this list item if necessary
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        // Get the worker object located at this position in the list
        Worker worker = getItem(position);

        // Get references to the views in the layout
        ShapeableImageView imageView = convertView.findViewById( R.id.listImage);
        TextView nameTextView = convertView.findViewById(R.id.listName);
        TextView professionTextView = convertView.findViewById(R.id.listProfession);
        TextView ratingTextView = convertView.findViewById(R.id.listRating);

        // Set the data for each view
        imageView.setImageResource(R.drawable.ic_list_worker); // Set worker image here if available
        nameTextView.setText(worker.getName());
        professionTextView.setText(worker.getProfession());
        ratingTextView.setText(String.valueOf(worker.getRating())); // Assuming rating is a double

        return convertView;
    }
}
