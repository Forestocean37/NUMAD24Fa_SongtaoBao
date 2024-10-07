package com.example.numad24fa_songtaobao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the About Me button
        Button aboutMeButton = findViewById(R.id.aboutMeButton);

        // Set a click listener on the button
        aboutMeButton.setOnClickListener(v -> {
            // Display a Toast with your name and email
            Toast.makeText(MainActivity.this, "Name: Songtao Bao\nEmail: bao.so@northeastern.edu", Toast.LENGTH_LONG).show();
        });

        Button quickCalcButton = findViewById(R.id.quickCalcButton);
        quickCalcButton.setOnClickListener(v -> {
            // Launch the Quick Calc Activity
            Intent intent = new Intent(MainActivity.this, QuickCalcActivity.class);
            startActivity(intent);
        });

    }
}