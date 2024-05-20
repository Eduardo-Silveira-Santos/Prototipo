package com.eduardosantos.prototipo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class WorkerAdapter extends ArrayAdapter<Worker> {

    private Context context;
    private List<Worker> workers;

    public WorkerAdapter(Context context, List<Worker> workers) {
        super(context, 0, workers);
        this.context = context;
        this.workers = workers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Worker currentWorker = workers.get(position);

        TextView nameTextView = listItem.findViewById(R.id.listName);
        TextView professionTextView = listItem.findViewById(R.id.listProfession);
        TextView ratingTextView = listItem.findViewById(R.id.listRating);

        if (currentWorker != null) {
            nameTextView.setText(currentWorker.getName());
            professionTextView.setText(currentWorker.getProfession());
            ratingTextView.setText(String.valueOf(currentWorker.getRating()));
        }

        return listItem;
    }
}

