package com.eduardosantos.prototipo;

import java.util.ArrayList;
import java.util.List;

public class WorkerUtil {
    public static List<Worker> generateWorkers() {
        List<Worker> workers = new ArrayList<>();

        workers.add(new Worker("João", "Faz Tudo", 4.5, "(54) 99629-0152", "Caxias do Sul - RS"));
        workers.add(new Worker("Maria", "Faz Tudo", 4.2, "(54) 99629-0153", "Caxias do Sul - RS"));
        workers.add(new Worker("Pedro", "Faz Tudo", 4.8, "(54) 99629-0154", "Caxias do Sul - RS"));

        workers.add(new Worker("Felipe", "Eletricista", 4.4, "(54) 99629-0155", "Caxias do Sul - RS"));
        workers.add(new Worker("Carla", "Eletricista", 4.7, "(54) 99629-0156", "Caxias do Sul - RS"));
        workers.add(new Worker("Ricardo", "Eletricista", 4.3, "(54) 99629-0157", "Caxias do Sul - RS"));

        workers.add(new Worker("Lucas", "Pintor", 4.6, "(54) 99629-0158", "Caxias do Sul - RS"));
        workers.add(new Worker("Júlia", "Pintor", 4.3, "(54) 99629-0159", "Caxias do Sul - RS"));
        workers.add(new Worker("Eduarda", "Pintor", 4.7, "(54) 99629-0160", "Caxias do Sul - RS"));

        workers.add(new Worker("Thiago", "Encanador", 4.4, "(54) 99629-0161", "Caxias do Sul - RS"));
        workers.add(new Worker("Natália", "Encanador", 4.7, "(54) 99629-0162", "Caxias do Sul - RS"));
        workers.add(new Worker("Henrique", "Encanador", 4.3, "(54) 99629-0163", "Caxias do Sul - RS"));

        workers.add(new Worker("Rodrigo", "Mecânico", 4.5, "(54) 99629-0164", "Caxias do Sul - RS"));
        workers.add(new Worker("Bianca", "Mecânico", 4.2, "(54) 99629-0165", "Caxias do Sul - RS"));
        workers.add(new Worker("Rafael", "Mecânico", 4.6, "(54) 99629-0166", "Caxias do Sul - RS"));

        workers.add(new Worker("Márcia", "Dedetizador", 4.4, "(54) 99629-0167", "Caxias do Sul - RS"));
        workers.add(new Worker("Fábio", "Dedetizador", 4.7, "(54) 99629-0168", "Caxias do Sul - RS"));
        workers.add(new Worker("Silvia", "Dedetizador", 4.3, "(54) 99629-0169", "Caxias do Sul - RS"));

        return workers;
    }

    public static List<Worker> filterWorkers(List<Worker> allWorkers, String profession) {
        List<Worker> filteredWorkers = new ArrayList<>();
        for (Worker worker : allWorkers) {
            if (worker.getProfession().equals(profession)) {
                filteredWorkers.add(worker);
            }
        }
        return filteredWorkers;
    }
}
