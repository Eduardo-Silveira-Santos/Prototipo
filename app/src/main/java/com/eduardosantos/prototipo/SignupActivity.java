package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eduardosantos.prototipo.databinding.ActivitySingupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySingupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.signupName.getText().toString();
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();

                if (name.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText( SignupActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT ).show();
                }
                else {
                    if(password.equals(confirmPassword)) {
                        Boolean checkUserEmail = databaseHelper.checkEmail(email);

                        if (checkUserEmail == false) {
                            Boolean insert = databaseHelper.insertData(name, email, password);

                            if (insert == true) {
                                Toast.makeText(SignupActivity.this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText( SignupActivity.this, "Cadastro falhou", Toast.LENGTH_SHORT ).show();
                            }
                        }
                        else {
                            Toast.makeText( SignupActivity.this, "Usuário já existe. Por favor tente acessar", Toast.LENGTH_SHORT ).show();
                        }
                    }
                    else {
                        Toast.makeText( SignupActivity.this, "Senha inválida", Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        } );

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        } );
    }
}
