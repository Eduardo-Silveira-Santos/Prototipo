package com.eduardosantos.prototipo;

import java.util.List;
import java.util.stream.Collectors;

public class WorkerUtil {
    public static List<Worker> filterWorkers(List<Worker> allWorkers, String profession) {
        return allWorkers.stream()
                .filter(worker -> {
                    String workerProfession = worker.getProfession();
                    return workerProfession != null && workerProfession.equals(profession);
                })
                .collect(Collectors.toList());
    }
}
