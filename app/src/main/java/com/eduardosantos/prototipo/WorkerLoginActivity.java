package com.eduardosantos.prototipo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardosantos.prototipo.databinding.ActivityWorkerLoginBinding;

public class WorkerLoginActivity extends AppCompatActivity {
    private ActivityWorkerLoginBinding binding;
    private WorkerDatabaseHelper workerDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        workerDatabaseHelper = new WorkerDatabaseHelper(this);

        binding.loginWorkerButton.setOnClickListener(v -> attemptLogin());

        binding.signupWorkerRedirectText.setOnClickListener(v -> startActivity(new Intent(WorkerLoginActivity.this, WorkerSignupActivity.class)));

        binding.loginWorkerEmail.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.loginWorkerPassword.requestFocus();
                return true;
            }
            return false;
        });

        binding.signupUserRedirectText.setOnClickListener(v -> startActivity(new Intent(WorkerLoginActivity.this, UserSignupActivity.class)));
    }

    private void attemptLogin() {
        String email = binding.loginWorkerEmail.getText().toString();
        String password = binding.loginWorkerPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(WorkerLoginActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        new LoginTask().execute(email, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Worker> {
        private String email;

        @Override
        protected Worker doInBackground(String... params) {
            email = params[0];
            String password = params[1];
            return workerDatabaseHelper.getWorkerByEmail(email, password);
        }

        @Override
        protected void onPostExecute(Worker worker) {
            if (worker != null) {
                Toast.makeText(WorkerLoginActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
                hideKeyboard();
                // Salva apenas o email do trabalhador nas preferências compartilhadas
                saveUserPreferences(worker.getEmail());
                Intent intent = new Intent(WorkerLoginActivity.this, WorkerMainActivity.class);
                intent.putExtra("WORKER_EMAIL", email); // Passar o email do trabalhador para a próxima atividade
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(WorkerLoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(WorkerLoginActivity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void saveUserPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", email);
        editor.apply();
    }
}
