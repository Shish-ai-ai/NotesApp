package com.example.notesapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> createAccount(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()));
    }

    private void createAccount(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email и пароль не должны быть пустыми.", Toast.LENGTH_LONG).show();
            return;
        }

        // Логирование попытки регистрации
        Log.d("RegisterActivity", "Trying to register with email: " + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Регистрация прошла успешно
                        Log.d("RegisterActivity", "Registration successful");
                        sendEmailVerification();
                    } else {
                        // Если регистрация не прошла
                        Log.e("RegisterActivity", "Registration failed: " + task.getException().getMessage());
                        Toast.makeText(RegisterActivity.this, "Регистрация не удалась: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Показать индикатор загрузки
        progressDialog.setMessage("Регистрация...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // Скрыть индикатор загрузки
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Регистрация не удалась: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendEmailVerification() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Подтвердите вашу почту. Проверьте вашу почту.", Toast.LENGTH_LONG).show();
                        finish(); // Закрыть активность и вернуться назад
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка отправки email подтверждения.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
