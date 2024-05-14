package com.example.notesapp;

import static com.example.notesapp.MyAdapter.notesList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.model.Note;

import io.realm.Realm;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private int position;  // Переменная для хранения позиции выбранной заметки
    private Realm realm;  // Realm объект для работы с базой данных

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.titleinput);
        descriptionEditText = findViewById(R.id.descriptioninput);
        ImageButton saveBtn = findViewById(R.id.savebtn);

        // Извлечь позицию заметки из Intent
        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            position = intent.getIntExtra("position", -1);

            if (position != -1) {
                // Если позиция корректная, то попробуйте получить заметку по позиции
                if (position < notesList.size()) {
                    Note selectedNote = notesList.get(position);

                    // Отобразите данные заметки в EditText для редактирования
                    titleEditText.setText(selectedNote.getTitle());
                    descriptionEditText.setText(selectedNote.getDescription());
                }
            }
        }

        realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = titleEditText.getText().toString();
                String newDescription = descriptionEditText.getText().toString();

                // Обновление данных заметки в базе данных
                realm.beginTransaction();
                Note selectedNote = notesList.get(position);
                selectedNote.setTitle(newTitle);
                selectedNote.setDescription(newDescription);
                realm.commitTransaction();

                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), notesList);
                myAdapter.updateData(notesList);

                // Вернитесь к предыдущей активности (список заметок)
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Обновление данных заметки в базе данных Realm при выходе из активности
        realm.beginTransaction();
        Note selectedNote = notesList.get(position);
        selectedNote.setTitle(titleEditText.getText().toString());
        selectedNote.setDescription(descriptionEditText.getText().toString());
        realm.commitTransaction();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();  // Важно закрыть Realm при завершении активности
    }
}
