package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class WorkerListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<Worker> adapter;
    private List<Worker> allWorkers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Worker clickedWorker = adapter.getItem(position);
                Intent profileIntent = new Intent(WorkerListActivity.this, WorkerProfileActivity.class);
                profileIntent.putExtra("worker", clickedWorker);
                startActivity(profileIntent);
            }
        });

        Intent intent = getIntent();
        String profession = intent.getStringExtra("profession");

        allWorkers = generateWorkers();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allWorkers);
        listView.setAdapter(adapter);

        filterWorkers(profession);
    }


    private List<Worker> generateWorkers() {
        List<Worker> workers = new ArrayList<>();

        // Faz Tudo
        workers.add( new Worker( "João", "Faz Tudo", 4.5 ) );
        workers.add( new Worker( "Maria", "Faz Tudo", 4.2 ) );
        workers.add( new Worker( "Pedro", "Faz Tudo", 4.8 ) );
        workers.add( new Worker( "Ana", "Faz Tudo", 4.3 ) );
        workers.add( new Worker( "Luiz", "Faz Tudo", 4.6 ) );
        workers.add( new Worker( "Clara", "Faz Tudo", 4.1 ) );
        workers.add( new Worker( "Marcos", "Faz Tudo", 4.7 ) );

        // Eletricista
        workers.add( new Worker( "Felipe", "Eletricista", 4.4 ) );
        workers.add( new Worker( "Carla", "Eletricista", 4.7 ) );
        workers.add( new Worker( "Ricardo", "Eletricista", 4.3 ) );
        workers.add( new Worker( "Aline", "Eletricista", 4.5 ) );
        workers.add( new Worker( "Roberto", "Eletricista", 4.2 ) );
        workers.add( new Worker( "Fernanda", "Eletricista", 4.6 ) );
        workers.add( new Worker( "Gustavo", "Eletricista", 4.8 ) );

        // Pintor
        workers.add( new Worker( "Lucas", "Pintor", 4.6 ) );
        workers.add( new Worker( "Júlia", "Pintor", 4.3 ) );
        workers.add( new Worker( "Eduarda", "Pintor", 4.7 ) );
        workers.add( new Worker( "Guilherme", "Pintor", 4.5 ) );
        workers.add( new Worker( "Isabela", "Pintor", 4.2 ) );
        workers.add( new Worker( "Mateus", "Pintor", 4.8 ) );
        workers.add( new Worker( "Camila", "Pintor", 4.4 ) );

        // Encanador
        workers.add( new Worker( "Thiago", "Encanador", 4.4 ) );
        workers.add( new Worker( "Natália", "Encanador", 4.7 ) );
        workers.add( new Worker( "Henrique", "Encanador", 4.3 ) );
        workers.add( new Worker( "Juliana", "Encanador", 4.5 ) );
        workers.add( new Worker( "Diego", "Encanador", 4.2 ) );
        workers.add( new Worker( "Vitória", "Encanador", 4.6 ) );
        workers.add( new Worker( "Leandro", "Encanador", 4.8 ) );

        // Mecânico
        workers.add( new Worker( "Rodrigo", "Mecânico", 4.5 ) );
        workers.add( new Worker( "Bianca", "Mecânico", 4.2 ) );
        workers.add( new Worker( "Rafael", "Mecânico", 4.6 ) );
        workers.add( new Worker( "Gabriela", "Mecânico", 4.3 ) );
        workers.add( new Worker( "Leonardo", "Mecânico", 4.7 ) );
        workers.add( new Worker( "Patrícia", "Mecânico", 4.4 ) );
        workers.add( new Worker( "Vinícius", "Mecânico", 4.8 ) );

        // Dedetizador
        workers.add( new Worker( "Márcia", "Dedetizador", 4.4 ) );
        workers.add( new Worker( "Fábio", "Dedetizador", 4.7 ) );
        workers.add( new Worker( "Silvia", "Dedetizador", 4.3 ) );
        workers.add( new Worker( "Marcelo", "Dedetizador", 4.5 ) );
        workers.add( new Worker( "Carolina", "Dedetizador", 4.2 ) );
        workers.add( new Worker( "Alexandre", "Dedetizador", 4.6 ) );
        workers.add( new Worker( "Tatiane", "Dedetizador", 4.8 ) );

        return workers;
    }

    private void filterWorkers(String profession) {
        List<Worker> filteredWorkers = new ArrayList<>();
        for (Worker worker : allWorkers) {
            if (worker.getProfession().equals(profession)) {
                filteredWorkers.add(worker);
            }
        }
        WorkerAdapter adapter = new WorkerAdapter(this, filteredWorkers);
        listView.setAdapter(adapter);
    }
}