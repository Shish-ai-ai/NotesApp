package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private int position;  // Переменная для хранения позиции выбранной заметки
    private List<Note> notesList; // Переменная для хранения списка заметок
    private CollectionReference notesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.titleinput);
        descriptionEditText = findViewById(R.id.descriptioninput);
        ImageButton saveBtn = findViewById(R.id.savebtn);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesRef = FirebaseFirestore.getInstance().collection("notes").document(userId).collection("userNotes");

        // Извлечь позицию заметки и список заметок из Intent
        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            position = intent.getIntExtra("position", -1);
            notesList = intent.getParcelableArrayListExtra("notesList");

            if (position != -1 && notesList != null && position < notesList.size()) {
                Note selectedNote = notesList.get(position);

                // Отобразите данные заметки в EditText для редактирования
                titleEditText.setText(selectedNote.getTitle());
                descriptionEditText.setText(selectedNote.getDescription());
            }
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();

                Note selectedNote = notesList.get(position);
                selectedNote.setTitle(newTitle);
                selectedNote.setDescription(newDescription);

                notesRef.document(selectedNote.getId()).set(selectedNote);

                // Вернитесь к предыдущей активности (список заметок)
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Note selectedNote = notesList.get(position);
        selectedNote.setTitle(titleEditText.getText().toString());
        selectedNote.setDescription(descriptionEditText.getText().toString());

        notesRef.document(selectedNote.getId()).set(selectedNote);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
