package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notesapp.model.Note;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNoteActivity extends AppCompatActivity {

    private CollectionReference notesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        EditText titleInput = findViewById(R.id.titleinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        ImageButton saveBtn = findViewById(R.id.savebtn);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesRef = FirebaseFirestore.getInstance().collection("notes").document(userId).collection("userNotes");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                if (!title.isEmpty() || !description.isEmpty()) {
                    Timestamp createdTime = Timestamp.now();
                    String noteId = notesRef.document().getId();
                    Note newNote = new Note(noteId, title, description, createdTime);

                    // Сохранение новой заметки в Firestore
                    notesRef.document(noteId).set(newNote);

                    Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    finish();
                }
            }
        });
    }
}
