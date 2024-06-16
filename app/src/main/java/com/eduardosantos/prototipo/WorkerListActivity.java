package com.eduardosantos.prototipo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.List;

public class WorkerListActivity extends AppCompatActivity {
    private ListView listView;
    private WorkerAdapter adapter;
    private List<Worker> allWorkers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);
        initViews();
        setupListView();
        loadAndFilterWorkers();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ImageButton backButton = findViewById(R.id.btn_left2);
        backButton.setOnClickListener(v -> finish());
        listView = findViewById(R.id.listView);
    }

    private void setupListView() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Worker clickedWorker = adapter.getItem(position);
            Intent profileIntent = new Intent(WorkerListActivity.this, WorkerProfileActivity.class);
            profileIntent.putExtra("worker", clickedWorker);
            startActivity(profileIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndFilterWorkers();
    }

    private void loadAndFilterWorkers() {
        Intent intent = getIntent();
        String profession = intent.getStringExtra("profession");

        WorkerDatabaseHelper workerDatabaseHelper = new WorkerDatabaseHelper(this);
        allWorkers = workerDatabaseHelper.getAllWorkers();
        loadWorkerImages();
        filterWorkers(profession);
    }

    private void loadWorkerImages() {
        for (Worker worker : allWorkers) {
            Bitmap profileImage = loadImageFromInternalStorage(worker.getEmail());
            worker.setProfileImage(profileImage);
        }
    }

    private Bitmap loadImageFromInternalStorage(String email) {
        Bitmap bitmap = null;
        File directory = getDir("profile_images", MODE_PRIVATE);
        File imagePath = new File(directory, email + "_profile.jpg");
        if (imagePath.exists()) {
            bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());
        }
        return bitmap;
    }

    private void filterWorkers(String profession) {
        List<Worker> filteredWorkers = WorkerUtil.filterWorkers(allWorkers, profession);
        adapter = new WorkerAdapter(this, R.layout.list_item, filteredWorkers);
        listView.setAdapter(adapter);
    }
}
