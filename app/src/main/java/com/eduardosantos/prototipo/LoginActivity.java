package com.eduardosantos.prototipo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.eduardosantos.prototipo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseHelper databaseHelper;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                new LoginTask().execute(email, password);
            }
        });

        binding.signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        binding.loginEmail.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.loginPassword.requestFocus();
                return true;
            }
            return false;
        });
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private String email;
        private String userName;

        @Override
        protected Boolean doInBackground(String... params) {
            email = params[0];
            String password = params[1];
            return databaseHelper.checkEmailPassword(email, password);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                userName = databaseHelper.getUserName(email);
                saveUserPreferences(userName, email);
                userViewModel.setUserName(userName);
                userViewModel.setUserEmail(email);

                Toast.makeText(LoginActivity.this, "Bem-vindo, " + userName + "!", Toast.LENGTH_SHORT).show();
                hideKeyboard();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserPreferences(String userName, String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_name", userName);
        editor.putString("user_email", email);
        editor.apply();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
