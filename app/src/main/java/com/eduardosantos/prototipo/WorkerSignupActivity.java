package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WorkerSignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, cityEditText, passwordEditText, confirmPasswordEditText, signupWorker_profission;
    private Button signupButton;
    private TextView loginRedirectText;

    private WorkerDatabaseHelper workerDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_worker);

        workerDatabaseHelper = new WorkerDatabaseHelper(this);

        nameEditText = findViewById(R.id.signupWorker_name);
        emailEditText = findViewById(R.id.signupWorker_email);
        phoneEditText = findViewById(R.id.signupWorker_phone);
        cityEditText = findViewById(R.id.signupWorker_city);
        passwordEditText = findViewById(R.id.signupWorker_password);
        confirmPasswordEditText = findViewById(R.id.signupWorker_confirm);
        signupWorker_profission = findViewById(R.id.signupWorker_profission);
        signupButton = findViewById(R.id.signupWorker_button);
        loginRedirectText = findViewById(R.id.loginWorkerRedirectText);

        signupButton.setOnClickListener(v -> signupWorker());

        loginRedirectText.setOnClickListener(v -> redirectToLogin());
    }

    private void signupWorker() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String profession = signupWorker_profission.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || city.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        WorkerDatabaseHelper dbHelper = new WorkerDatabaseHelper(this);

        if (dbHelper.checkEmailExists(email)) {
            Toast.makeText(this, "Este e-mail já está cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.insertWorker(name, email, phone, city, password, profession);
        if (inserted) {
            Toast.makeText(this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( WorkerSignupActivity.this, WorkerLoginActivity.class);
            intent.putExtra("USER_NAME", name);
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_PHONE", phone);
            intent.putExtra("USER_CITY", city);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar trabalhador", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, WorkerLoginActivity.class);
        startActivity(intent);
        finish(); // Finalizar a atividade atual para que o usuário não possa voltar para ela pressionando o botão "Voltar"
    }
}
