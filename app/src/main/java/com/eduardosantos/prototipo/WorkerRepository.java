package com.eduardosantos.prototipo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class WorkerRepository {
    private static WorkerRepository instance;
    private final WorkerDatabaseHelper workerDatabaseHelper;
    private final MutableLiveData<List<Worker>> workersLiveData = new MutableLiveData<>();

    private WorkerRepository(Context context) {
        workerDatabaseHelper = new WorkerDatabaseHelper(context);
        loadWorkers();
    }

    public static synchronized WorkerRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WorkerRepository(context);
        }
        return instance;
    }

    private void loadWorkers() {
        List<Worker> workers = workerDatabaseHelper.getAllWorkers();
        workersLiveData.setValue(workers);
    }

    public LiveData<List<Worker>> getAllWorkers() {
        return workersLiveData;
    }
}
