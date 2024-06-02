package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewEmail = findViewById(R.id.textViewEmail);
        buttonLogout = findViewById(R.id.buttonLogout);

        ImageButton backButton = findViewById(R.id.backbtn);
        backButton.setOnClickListener(v -> finish());


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            textViewEmail.setText("Email: " + user.getEmail());
        } else {

            textViewEmail.setText("Email не найден");
        }

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Выход пользователя
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });
    }
}
