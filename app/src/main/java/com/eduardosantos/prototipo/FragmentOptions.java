package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FragmentOptions extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);

        CardView joatCard = view.findViewById(R.id.joatCard);
        CardView electricianCard = view.findViewById(R.id.electricianCard);
        CardView painterCard = view.findViewById(R.id.painterCard);
        CardView mechanicCard = view.findViewById(R.id.mechanicCard);
        CardView plumberCard = view.findViewById(R.id.plumberCard);
        CardView pestControlCard = view.findViewById(R.id.pestControlCard);

        joatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Faz Tudo");
                startActivity(intent);
            }
        });

        electricianCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Eletricista");
                startActivity(intent);
            }
        });

        painterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Pintor");
                startActivity(intent);
            }
        });

        mechanicCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Mecânico");
                startActivity(intent);
            }
        });

        plumberCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Encanador");
                startActivity(intent);
            }
        });

        pestControlCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkerListActivity.class);
                intent.putExtra("profession", "Dedetizador");
                startActivity(intent);
            }
        });

        return view;
    }
}
