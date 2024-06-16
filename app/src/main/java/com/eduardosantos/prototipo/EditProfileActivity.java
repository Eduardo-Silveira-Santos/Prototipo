package com.eduardosantos.prototipo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editEmail;
    private String currentName;
    private String currentEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);

        // Recuperar dados atuais do perfil
        Intent intent = getIntent();
        if (intent != null) {
            currentName = intent.getStringExtra("userName");
            currentEmail = intent.getStringExtra("userEmail");
            if (currentName != null && currentEmail != null) {
                editName.setText(currentName);
                editEmail.setText(currentEmail);
            }
        }

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void saveProfileChanges() {
        String newName = editName.getText().toString();
        String newEmail = editEmail.getText().toString();

        if (!newName.isEmpty() && !newEmail.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newName", newName);
            resultIntent.putExtra("newEmail", newEmail);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Os campos não podem estar vazios", Toast.LENGTH_SHORT).show();
        }
    }
}
