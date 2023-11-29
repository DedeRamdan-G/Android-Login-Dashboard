package com.example.mylogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Temukan button kembali
        com.google.android.material.button.MaterialButton backButton = findViewById(R.id.kembali_button);

        // Tambahkan OnClickListener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke MainActivity (halaman login) ketika button diklik
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
                finish(); // Selesaikan aktivitas saat ini (DashboardActivity) dan kembali ke MainActivity
            }
        });
    }
}
