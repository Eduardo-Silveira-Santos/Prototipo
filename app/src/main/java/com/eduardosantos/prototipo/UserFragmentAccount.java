package com.eduardosantos.prototipo;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserFragmentAccount extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int EDIT_PROFILE_REQUEST_CODE = 101;
    private UserViewModel userViewModel;
    private ImageView imageView;
    private Bitmap selectedImageBitmap;
    private String userName;
    private String userEmail;
    private TextView nameTextView;
    private TextView emailTextView;

    // Construtor vazio necessário para o FragmentManager
    public UserFragmentAccount() {}

    // Atualização do construtor para receber os dados de usuário
    @SuppressLint("ValidFragment")
    public UserFragmentAccount(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imageView = view.findViewById(R.id.imageView);
        Button buttonChooseImage = view.findViewById(R.id.buttonChooseImage);
        Button editProfileButton = view.findViewById(R.id.editProfileButton);
        Button deleteAccountButton = view.findViewById(R.id.deleteAccountButton);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        nameTextView.setText(userName);
        emailTextView.setText(userEmail);

        imageView.setOnClickListener(v -> dispatchTakePictureIntent());
        buttonChooseImage.setOnClickListener(v -> requestStoragePermissionOrDispatchPick());

        // Carregar a imagem do armazenamento interno
        selectedImageBitmap = loadImageFromInternalStorage();
        if (selectedImageBitmap != null) {
            imageView.setImageBitmap(selectedImageBitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_list_worker);
        }

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logoutAndNavigateToLogin());

        // Adicionar ações para os botões de editar e excluir
        editProfileButton.setOnClickListener(v -> showEditProfileDialog());
        deleteAccountButton.setOnClickListener(v -> showDeleteAccountDialog());

        return view;
    }

    private void showEditProfileDialog() {
        Intent intent = new Intent(requireContext(), EditProfileActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail", userEmail);
        startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
    }

    private void updateUserProfile(String newName, String newEmail) {
        // Atualização no banco de dados
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
        dbHelper.updateUser(userEmail, newName); // Atualizar nome do usuário

        // Atualização na ViewModel
        userName = newName;
        userEmail = newEmail;
        userViewModel.setUserName(newName);
        userViewModel.setUserEmail(newEmail);

        // Atualizar dados exibidos na tela
        if (nameTextView != null && emailTextView != null) {
            nameTextView.setText(newName);
            emailTextView.setText(newEmail);
        }

        Toast.makeText(requireContext(), "Perfil atualizado com sucesso", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Excluir Conta")
                .setMessage("Tem certeza de que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir", (dialog, which) -> deleteUserProfile())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteUserProfile() {
        // Remover usuário do banco de dados
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(requireContext());
        dbHelper.deleteUser(userEmail); // Supondo que userEmail seja o identificador único do usuário

        // Limpar dados locais
        clearSession();

        // Notificar sucesso
        Toast.makeText(requireContext(), "Conta excluída com sucesso", Toast.LENGTH_SHORT).show();

        // Navegar para a tela de login
        logoutAndNavigateToLogin();
    }

    private void clearSession() {
        SharedPreferences preferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void logoutAndNavigateToLogin() {
        // Limpar qualquer estado de sessão local (por exemplo, usando SharedPreferences)
        SharedPreferences preferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Navegar para a tela de login
        Intent intent = new Intent(requireContext(), UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }


    @SuppressLint("QueryPermissionsNeeded")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void requestStoragePermissionOrDispatchPick() {
        if (ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            dispatchPickPictureIntent();
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                imageBitmap = (Bitmap) data.getExtras().get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri selectedImageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_PROFILE_REQUEST_CODE && data != null) {
                String newName = data.getStringExtra("newName");
                String newEmail = data.getStringExtra("newEmail");
                if (newName != null && newEmail != null) {
                    updateUserProfile(newName, newEmail);
                }
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap) {
        File directory = requireContext().getDir("profile_images", Context.MODE_PRIVATE);
        File imagePath = new File(directory, userEmail + "_profile.jpg");
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
        File directory = requireContext().getDir("profile_images", Context.MODE_PRIVATE);
        File imagePath = new File(directory, userEmail + "_profile.jpg");
        if (imagePath.exists()) {
            bitmap = BitmapFactory.decodeFile(imagePath.getAbsolutePath());
        }
        return bitmap;
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle((float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2,
                (float) bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                dispatchPickPictureIntent();
            } else {
                Toast.makeText(getContext(), "Permissão de armazenamento necessária para escolher uma imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
