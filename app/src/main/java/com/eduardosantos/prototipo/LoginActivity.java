package com.eduardosantos.prototipo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardosantos.prototipo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                    if (checkCredentials) {
                        String userName = databaseHelper.getUserName(email);

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_name", userName);
                        editor.putString("user_email", email);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Acesso Permitido", Toast.LENGTH_SHORT).show();

                        // Ocultar teclado antes de iniciar a nova atividade
                        hideKeyboard();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        public void hideKeyboard() {
            InputMethodManager imm = (InputMethodManager) getSystemService( Activity.INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
