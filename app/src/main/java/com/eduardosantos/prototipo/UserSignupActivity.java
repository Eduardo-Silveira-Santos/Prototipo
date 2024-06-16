package com.eduardosantos.prototipo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eduardosantos.prototipo.databinding.ActivitySingupBinding;

public class UserSignupActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivitySingupBinding binding;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDatabaseHelper = new UserDatabaseHelper(this);

        binding.signupButton.setOnClickListener(v -> {
            String name = binding.signupName.getText().toString();
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText( UserSignupActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText( UserSignupActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
            } else {
                new SignupTask(name, email, password).execute();
            }
        });

        binding.loginRedirectText.setOnClickListener(v -> navigateToLogin());

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

    private void requestLocation() {
        if (checkLocationPermission()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = null;
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Use latitude and longitude as needed
            }
            navigateToLogin();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Toast.makeText(this, "Permissão de localização necessária para continuar", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent( UserSignupActivity.this, UserLoginActivity.class);
        startActivity(intent);
    }

    private class SignupTask extends AsyncTask<Void, Void, Boolean> {
        private final String name;
        private final String email;
        private final String password;

        public SignupTask(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (!userDatabaseHelper.checkEmail(email)) {
                return userDatabaseHelper.insertData(name, email, password);
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText( UserSignupActivity.this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                requestLocation();
            } else {
                Toast.makeText( UserSignupActivity.this, "Usuário já existe. Por favor tente acessar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
