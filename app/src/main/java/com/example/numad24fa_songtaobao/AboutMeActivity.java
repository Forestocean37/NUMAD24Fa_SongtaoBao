package com.example.numad24fa_songtaobao;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        // Set up the name and email TextViews
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);

        nameTextView.setText("Name: Songtao Bao");
        emailTextView.setText("Email: bao.so@northeastern.edu");
    }
}
