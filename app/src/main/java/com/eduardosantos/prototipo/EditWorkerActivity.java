package com.eduardosantos.prototipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditWorkerActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText cityEditText;
    private Spinner professionSpinner;
    private Button saveButton;
    private String workerEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_worker);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        cityEditText = findViewById(R.id.cityEditText);
        professionSpinner = findViewById(R.id.professionSpinner);
        saveButton = findViewById(R.id.saveButton);

        workerEmail = getIntent().getStringExtra("WORKER_EMAIL");

        // Obter detalhes do trabalhador pelo email
        WorkerDatabaseHelper workerDatabaseHelper = new WorkerDatabaseHelper(this);
        Worker worker = workerDatabaseHelper.getWorkerByEmailOnly(workerEmail);
        if (worker != null) {
            // Preencher campos com os detalhes do trabalhador
            nameEditText.setText(worker.getName());
            phoneEditText.setText(worker.getPhoneNumber());
            cityEditText.setText(worker.getCity());

            // Preencher Spinner com a profissão
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.professions_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            professionSpinner.setAdapter(adapter);
            if (worker.getProfession() != null) {
                int spinnerPosition = adapter.getPosition(worker.getProfession());
                professionSpinner.setSelection(spinnerPosition);
            }
        } else {
            Toast.makeText(this, "Trabalhador não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String city = cityEditText.getText().toString();
            String profession = professionSpinner.getSelectedItem().toString();

            workerDatabaseHelper.updateWorker(workerEmail, name, phone, city, profession);
            Toast.makeText(this, "Informações atualizadas com sucesso", Toast.LENGTH_SHORT).show();

            // Enviar resultados de volta para a WorkerMainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_NAME", name);
            resultIntent.putExtra("UPDATED_PHONE", phone);
            resultIntent.putExtra("UPDATED_CITY", city);
            resultIntent.putExtra("UPDATED_PROFESSION", profession);
            setResult(Activity.RESULT_OK, resultIntent);

            finish();
        });
    }
}
