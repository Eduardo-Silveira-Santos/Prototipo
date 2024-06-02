package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserFragmentOptions extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        setupCardClickListener(view, R.id.joatCard, "Faz Tudo");
        setupCardClickListener(view, R.id.electricianCard, "Eletricista");
        setupCardClickListener(view, R.id.painterCard, "Pintor");
        setupCardClickListener(view, R.id.plumberCard, "Encanador");
        setupCardClickListener(view, R.id.mechanicCard, "Mecânico");
        setupCardClickListener(view, R.id.pestControlCard, "Dedetizador");

        return view;
    }

    private void setupCardClickListener(View view, int id, final String profession) {
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
