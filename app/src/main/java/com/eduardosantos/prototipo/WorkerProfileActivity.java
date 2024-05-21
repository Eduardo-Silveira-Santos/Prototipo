package com.eduardosantos.prototipo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WorkerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Worker worker = getIntent().getParcelableExtra("worker");

        if (worker != null) {
            TextView nameTextView = findViewById( R.id.listName3 );
            TextView professionTextView = findViewById( R.id.listProfession3 );
            TextView ratingTextView = findViewById( R.id.listRating3 );
            TextView contactTextView = findViewById( R.id.listContact );  // Assuming contact details are part of the worker object
            TextView avaliationTextView = findViewById( R.id.listAvaliation );

            nameTextView.setText( worker.getName() );
            professionTextView.setText( worker.getProfession() );
            ratingTextView.setText( String.valueOf( worker.getRating() ) );
            contactTextView.setText( "(99) 99999-9999" );

            /*// Adding 3 sample reviews
            String reviews = "Avaliações:\n\n" +
                    "   1. Excelente profissional, muito competente e dedicado.\n\n" +
                    "   2. Trabalho bem feito, recomendo!\n\n" +
                    "   3. Serviço razoável, poderia ser mais rápido.";

            avaliationTextView.setText(reviews);*/
        }

        ImageButton backButton = findViewById(R.id.btn_left3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
