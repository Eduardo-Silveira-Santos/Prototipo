package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Intent intent = getIntent();
        String profession = intent.getStringExtra("profession");
        allWorkers = WorkerUtil.generateWorkers();
        filterWorkers(profession);
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ImageButton backButton = findViewById(R.id.btn_left2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = findViewById(R.id.listView);
    }

    private void setupListView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker clickedWorker = adapter.getItem(position);
                Intent profileIntent = new Intent(WorkerListActivity.this, WorkerProfileActivity.class);
                profileIntent.putExtra("worker", clickedWorker);
                startActivity(profileIntent);
            }
        });
    }

    private void filterWorkers(String profession) {
        List<Worker> filteredWorkers = WorkerUtil.filterWorkers(allWorkers, profession);
        adapter = new WorkerAdapter(this, R.layout.list_item, filteredWorkers);
        listView.setAdapter(adapter);
    }
}
