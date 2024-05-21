package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentOptions extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        view.findViewById(R.id.joatCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Faz Tudo");
            }
        });

        view.findViewById(R.id.electricianCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Eletricista");
            }
        });

        view.findViewById(R.id.painterCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Pintor");
            }
        });

        view.findViewById(R.id.plumberCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Encanador");
            }
        });

        view.findViewById(R.id.mechanicCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Mecânico");
            }
        });

        view.findViewById(R.id.pestControlCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity("Dedetizador");
            }
        });

        return view;
    }

    private void startWorkerListActivity(String profession) {
        Intent intent = new Intent(getActivity(), WorkerListActivity.class);
        intent.putExtra("profession", profession);
        startActivity(intent);
    }
}
