package com.eduardosantos.prototipo;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorkerMainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView cityTextView;
    private TextView professionTextView;
    private TextView ratingTextView;
    private Button logoutButton;
    private ImageView imageView;
    private Bitmap selectedImageBitmap;

    private String workerEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        cityTextView = findViewById(R.id.cityTextView);
        professionTextView = findViewById(R.id.professionTextView2);
        ratingTextView = findViewById(R.id.ratingTextView);
        logoutButton = findViewById(R.id.logoutButton);
        imageView = findViewById(R.id.imageView);
        Button buttonChooseImage = findViewById(R.id.buttonChooseImage);

        Intent intent = getIntent();
        workerEmail = intent.getStringExtra("WORKER_EMAIL");

        // Obter detalhes do trabalhador pelo email
        WorkerDatabaseHelper workerDatabaseHelper = new WorkerDatabaseHelper(this);
        Worker worker = workerDatabaseHelper.getWorkerByEmailOnly(workerEmail);
        if (worker != null) {
            // Atualizar visualizações com os detalhes do trabalhador
            nameTextView.setText(worker.getName());
            emailTextView.setText(worker.getEmail());
            professionTextView.setText(worker.getProfession());
            ratingTextView.setText(String.valueOf(worker.getRating()));
            phoneTextView.setText(worker.getPhoneNumber());
            cityTextView.setText(worker.getCity());
        } else {
            // Handle caso em que o trabalhador não foi encontrado
            Toast.makeText(this, "Trabalhador não encontrado", Toast.LENGTH_SHORT).show();
        }

        Bitmap profileImage = loadImageFromInternalStorage();
        if (profileImage != null) {
            selectedImageBitmap = profileImage;
            imageView.setImageBitmap(selectedImageBitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_list_worker);
        }

        imageView.setOnClickListener(v -> dispatchTakePictureIntent());
        buttonChooseImage.setOnClickListener(v -> requestStoragePermissionOrDispatchPick());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        Intent intent = new Intent(WorkerMainActivity.this, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void requestStoragePermissionOrDispatchPick() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            dispatchPickPictureIntent();
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageBitmap != null) {
                selectedImageBitmap = getCroppedBitmap(imageBitmap);
                imageView.setImageBitmap(selectedImageBitmap);
                saveImageToInternalStorage(selectedImageBitmap);
            }
        }
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle((float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2, (float) bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getDir("profile_images", MODE_PRIVATE);
        File imagePath = new File(directory, workerEmail + "_profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap loadImageFromInternalStorage() {
        Bitmap bitmap = null;
        File directory = getDir("profile_images", MODE_PRIVATE);
        File imagePath = new File(directory, workerEmail + "_profile.jpg");
        if (imagePath.exists()) {
            bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());
        }
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                dispatchPickPictureIntent();
            }
        }
    }
}
