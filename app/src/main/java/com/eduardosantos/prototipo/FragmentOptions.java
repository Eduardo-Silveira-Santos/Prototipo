package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentOptions extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        setClickListener(view, R.id.joatCard, "Faz Tudo");
        setClickListener(view, R.id.electricianCard, "Eletricista");
        setClickListener(view, R.id.painterCard, "Pintor");
        setClickListener(view, R.id.plumberCard, "Encanador");
        setClickListener(view, R.id.mechanicCard, "Mecânico");
        setClickListener(view, R.id.pestControlCard, "Dedetizador");

        return view;
    }

    private void setClickListener(View view, int id, final String profession) {
        view.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWorkerListActivity(profession);
            }
        });
    }

    private void startWorkerListActivity(String profession) {
        Intent intent = new Intent(getActivity(), WorkerListActivity.class);
        intent.putExtra("profession", profession);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
    }
}
