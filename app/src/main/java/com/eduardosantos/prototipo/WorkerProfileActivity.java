package com.eduardosantos.prototipo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WorkerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Worker worker = getIntent().getParcelableExtra("worker");

        if (worker != null) {
            populateViews(worker);
        }

        ImageButton backButton = findViewById(R.id.btn_left3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void populateViews(Worker worker) {
        TextView nameTextView = findViewById(R.id.listName3);
        TextView professionTextView = findViewById(R.id.listProfession3);
        TextView ratingTextView = findViewById(R.id.listRating3);
        TextView contactTextView = findViewById(R.id.listContact);
        TextView cityTextView = findViewById(R.id.listCity);

        nameTextView.setText(worker.getName());
        professionTextView.setText(worker.getProfession());
        ratingTextView.setText(String.valueOf(worker.getRating()));

        String phoneNumber = worker.getPhoneNumber();
        String cidade = worker.getCity();

        contactTextView.setText(worker.getPhoneNumber());
        cityTextView.setText(worker.getCity());
    }
}
