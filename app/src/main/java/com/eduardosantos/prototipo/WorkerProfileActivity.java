package com.eduardosantos.prototipo;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WorkerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Worker worker = getIntent().getParcelableExtra("worker");

        if (worker != null) {
            TextView nameTextView = findViewById(R.id.listName3);
            TextView professionTextView = findViewById(R.id.listProfession3);
            TextView ratingTextView = findViewById(R.id.listRating3);

            nameTextView.setText(worker.getName());
            professionTextView.setText(worker.getProfession());
            ratingTextView.setText(String.valueOf(worker.getRating()));
        }
    }
}
