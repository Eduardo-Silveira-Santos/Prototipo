package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardosantos.prototipo.databinding.ActivitySingupBinding;

public class SignupActivity extends AppCompatActivity {
    private ActivitySingupBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(v -> {
            String name = binding.signupName.getText().toString();
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
            } else {
                new SignupTask().execute(name, email, password);
            }
        });

        binding.loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.signupName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.signupEmail.requestFocus();
                return true;
            }
            return false;
        });

        binding.signupEmail.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.signupPassword.requestFocus();
                return true;
            }
            return false;
        });
    }

    private class SignupTask extends AsyncTask<String, Void, Boolean> {
        private String email;

        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            email = params[1];
            String password = params[2];

            if (!databaseHelper.checkEmail(email)) {
                return databaseHelper.insertData(name, email, password);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success == null) {
                Toast.makeText(SignupActivity.this, "Usuário já existe. Por favor tente acessar", Toast.LENGTH_SHORT).show();
            } else if (success) {
                Toast.makeText(SignupActivity.this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignupActivity.this, "Cadastro falhou", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
