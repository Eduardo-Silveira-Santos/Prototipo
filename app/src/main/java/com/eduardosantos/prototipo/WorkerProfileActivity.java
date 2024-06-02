package com.eduardosantos.prototipo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkerProfileActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private EditText descriptionEditText;
    private Button submitReviewButton;
    private Worker worker;
    private static final String TAG = "WorkerProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        worker = getIntent().getParcelableExtra("worker");
        ratingBar = findViewById(R.id.ratingBar);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        if (worker != null) {
            Log.d(TAG, "Worker object received: " + worker.toString());
            populateViews(worker);
        } else {
            Log.e(TAG, "Worker object is null!");
        }

        ImageButton backButton = findViewById(R.id.btn_left3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void populateViews(Worker worker) {
        TextView nameTextView = findViewById(R.id.listName3);
        TextView professionTextView = findViewById(R.id.listProfession3);
        TextView ratingTextView = findViewById(R.id.listRating3);
        TextView contactTextView = findViewById(R.id.listContact);
        TextView cityTextView = findViewById(R.id.listCity);

        nameTextView.setText(getStringOrDefault(worker.getName(), R.string.default_name));
        professionTextView.setText(getStringOrDefault(worker.getProfession(), R.string.default_profession));
        ratingTextView.setText(String.valueOf(worker.getRating()));
        cityTextView.setText(getStringOrDefault(worker.getCity(), R.string.default_city));

        String phoneNumber = worker.getPhoneNumber();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            contactTextView.setText(phoneNumber);
            contactTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String formattedNumber = formatPhoneNumberForWhatsApp(phoneNumber);
                    openWhatsApp(formattedNumber);
                }
            });
        } else {
            contactTextView.setText(R.string.default_contact);
        }
    }

    private String getStringOrDefault(String value, int defaultResId) {
        return value != null && !value.isEmpty() ? value : getString(defaultResId);
    }

    private String formatPhoneNumberForWhatsApp(String phoneNumber) {
        return phoneNumber.replaceAll("[^\\d]", "");
    }

    private void openWhatsApp(String phoneNumber) {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error opening WhatsApp", e);
        }
    }

    private void submitReview() {
        float rating = ratingBar.getRating();
        String description = descriptionEditText.getText().toString();

        if (worker != null) {
            insertReviewIntoDatabase(worker.getEmail(), rating, description);
        } else {
            Log.e(TAG, "Worker object is null, cannot submit review.");
        }
        Toast.makeText(this, getString(R.string.review_submitted, String.valueOf(rating), description), Toast.LENGTH_SHORT).show();
        resetFields();
    }

    private void insertReviewIntoDatabase(String workerEmail, float rating, String description) {
        WorkerDatabaseHelper dbHelper = new WorkerDatabaseHelper(this);
        dbHelper.updateRating(workerEmail, rating);
    }

    private void resetFields() {
        ratingBar.setRating(0);
        descriptionEditText.setText("");
    }
}
