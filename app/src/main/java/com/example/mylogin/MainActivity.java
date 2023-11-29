package com.example.mylogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView slideImageView;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox);
        slideImageView = findViewById(R.id.slide_image_view);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Set Password EditText dengan ikon untuk menampilkan atau menyembunyikan kata sandi
        setPasswordVisibilityToggle();

        loginButton.setOnClickListener(v -> loginUser());

        // Setelah pengguna login, periksa apakah checkbox dicentang
        // Jika dicentang, simpan status ke SharedPreferences
        boolean isChecked = sharedPreferences.getBoolean("isChecked", false);
        rememberMeCheckbox.setChecked(isChecked);
        if (isChecked) {
            // Lakukan sesuatu seperti menyimpan informasi login jika diperlukan
            String savedEmail = sharedPreferences.getString("savedEmail", "");
            String savedPassword = sharedPreferences.getString("savedPassword", "");
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
        }

        // Menangani perubahan pada checkbox
        rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            // Simpan status checkbox ke SharedPreferences saat checkbox diubah
            editor.putBoolean("isChecked", isChecked1);
            editor.apply();
        });

        // Menangani klik pada ImageView
        ImageView slideImageView = findViewById(R.id.slide_image_view);
        final boolean[] isClicked = {false};

        slideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked[0]) {
                    // Misalnya, jika ingin mengubah gambar menjadi terbuka ketika diklik pertama kali
                    slideImageView.setImageResource(R.drawable.baseline_remove_24);
                    isClicked[0] = true;
                } else {
                    // Misalnya, jika ingin mengubah gambar menjadi tertutup ketika diklik kedua kali
                    slideImageView.setImageResource(R.drawable.baseline_remove_24); // Ganti dengan sumber gambar asli yang ingin Anda gunakan di sini
                    isClicked[0] = false;

                    // Set ukuran gambar kembali ke 50sp
                    int imageSizeInSp = 50; // Ukuran yang diinginkan dalam sp
                    float scale = getResources().getDisplayMetrics().density;
                    int imageSizeInPixels = (int) (imageSizeInSp * scale + 0.5f);

                    // Atur ulang ukuran gambar
                    ViewGroup.LayoutParams layoutParams = slideImageView.getLayoutParams();
                    layoutParams.width = imageSizeInPixels;
                    layoutParams.height = imageSizeInPixels;
                    slideImageView.setLayoutParams(layoutParams);
                }
            }
        });



    }

    private boolean isPasswordVisible = false;

    private void setPasswordVisibilityToggle() {
        // Menampilkan atau menyembunyikan password saat ikon pada EditText diklik
        passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Jika password terlihat, ubah menjadi input type password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            // Jika password tersembunyi, ubah menjadi input type teks biasa
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }
        // Setel kursor ke akhir teks pada EditText
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.equals("user@example.com") && password.equals("password123")) {
            // Credentials are valid, show success message or proceed to next screen
            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

            // Simpan informasi login jika "Remember Me" dicentang
            if (rememberMeCheckbox.isChecked()) {
                editor.putString("savedEmail", email);
                editor.putString("savedPassword", password);
                editor.apply();
            } else {
                // Hapus informasi login jika checkbox tidak dicentang
                editor.remove("savedEmail");
                editor.remove("savedPassword");
                editor.apply();
            }

            // Redirect to DashboardActivity after successful login
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
            finish(); // Optional: close the MainActivity so user can't go back using back button
        } else {
            // Credentials are invalid, show error message
            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
