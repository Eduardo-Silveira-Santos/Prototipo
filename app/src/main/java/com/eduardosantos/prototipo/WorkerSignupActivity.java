package com.eduardosantos.prototipo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WorkerSignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, cityEditText, passwordEditText, confirmPasswordEditText;
    private Spinner professionSpinner;
    private Button signupButton;
    private TextView loginRedirectText;
    private String selectedProfession;
    private static final int PERMISSION_REQUEST_LOCATION = 100;
    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private WorkerDatabaseHelper workerDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup_worker );

        workerDatabaseHelper = new WorkerDatabaseHelper( this );

        nameEditText = findViewById( R.id.signupWorker_name );
        emailEditText = findViewById( R.id.signupWorker_email );
        phoneEditText = findViewById( R.id.signupWorker_phone );
        cityEditText = findViewById( R.id.signupWorker_city );
        passwordEditText = findViewById( R.id.signupWorker_password );
        confirmPasswordEditText = findViewById( R.id.signupWorker_confirm );
        professionSpinner = findViewById( R.id.signupWorker_profession );
        signupButton = findViewById( R.id.signupWorker_button );
        loginRedirectText = findViewById( R.id.loginWorkerRedirectText );

        // Adicionar o TextWatcher ao phoneEditText
        phoneEditText.addTextChangedListener( new PhoneNumberFormattingTextWatcher( phoneEditText ) );

        // Configurar o Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>( this, R.layout.spinner_item ) {
            @Override
            public boolean isEnabled(int position) {
                // Desabilitar o primeiro item (hint)
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView( position, convertView, parent );
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Mudar a cor do hint para cinza
                    tv.setTextColor( Color.GRAY );
                } else {
                    tv.setTextColor( Color.BLACK );
                }
                return view;
            }
        };

        spinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        // Adicionar o hint como o primeiro item
        spinnerAdapter.add( "Profissão" );
        spinnerAdapter.addAll( getResources().getStringArray( R.array.professions_array ) );
        professionSpinner.setAdapter( spinnerAdapter );

        // Setar o hint como selecionado
        professionSpinner.setSelection( 0 );

        professionSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) parent.getChildAt( 0 );
                if (position != 0) {
                    selectedProfession = (String) parent.getItemAtPosition( position );
                    if (tv != null) {
                        tv.setTextColor( Color.BLACK ); // Mudar a cor do texto para preto
                    }
                } else {
                    selectedProfession = null;
                    if (tv != null) {
                        tv.setTextColor( Color.GRAY ); // Manter o hint em cinza
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProfession = null;
            }
        } );

        signupButton.setOnClickListener( v -> signupWorker() );

        loginRedirectText.setOnClickListener( v -> redirectToLogin() );

        // Solicitar permissões de localização, se necessário
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            cityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        getLocation();
                    }
                }
            });

        } else {
            // Se a permissão não foi concedida, solicite-a ao usuário
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, obtenha a localização
                getLocation();
                // Também preencha a cidade diretamente no campo
                cityEditText.setText(getCityFromLocation());
            } else {
                // Permissão negada pelo usuário, informe ao usuário ou tome outra ação apropriada
                Toast.makeText( this, "Permissão de localização negada", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private String getCityFromLocation() {
        // Obter a localização do provedor de rede
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    return getCityFromCoordinates(latitude, longitude);
                }
            }
        }
        return "";
    }


    private void signupWorker() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || city.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedProfession == null) {
            Toast.makeText( this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT ).show();
            return;
        }

        if (!password.equals( confirmPassword )) {
            Toast.makeText( this, "As senhas não coincidem", Toast.LENGTH_SHORT ).show();
            return;
        }

        WorkerDatabaseHelper dbHelper = new WorkerDatabaseHelper( this );

        if (dbHelper.checkEmailExists( email )) {
            Toast.makeText( this, "Este e-mail já está cadastrado", Toast.LENGTH_SHORT ).show();
            return;
        }

        boolean inserted = dbHelper.insertWorker( name, email, phone, city, password, selectedProfession );
        if (inserted) {
            Toast.makeText( this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT ).show();
            Intent intent = new Intent( WorkerSignupActivity.this, WorkerLoginActivity.class );
            intent.putExtra( "USER_NAME", name );
            intent.putExtra( "USER_EMAIL", email );
            intent.putExtra( "USER_PHONE", phone );
            intent.putExtra( "USER_CITY", city );
            startActivity( intent );
            finish();
        } else {
            Toast.makeText( this, "Erro ao cadastrar trabalhador", Toast.LENGTH_SHORT ).show();
        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permissão não concedida, solicite-a ao usuário
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                return; // Retorne para evitar a execução do código de obtenção de localização sem permissão
            }
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // Obter a latitude e longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Usar a latitude e longitude para obter a cidade
                    String city = getCityFromCoordinates(latitude, longitude);

                    // Definir a cidade no campo EditText
                    cityEditText.setText(city);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            }, null);
        }
    }


    private String getCityFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, WorkerLoginActivity.class);
        startActivity(intent);
        finish(); // Finalizar a atividade atual para que o usuário não possa voltar para ela pressionando o botão "Voltar"
    }
}
