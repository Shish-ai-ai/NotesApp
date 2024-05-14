package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import com.example.notesapp.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private MyAdapter myAdapter;
    private Realm realm;
    private ActivityResultLauncher<Intent> addNoteLauncher;
    private ActivityResultLauncher<Intent> editNoteLauncher;
    private ActivityResultLauncher<Intent> profileLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Пользователь не залогинен, перенаправляем на LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.addnewnotebtn);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            addNoteLauncher.launch(intent);
        });
        ImageButton profileButton = findViewById(R.id.profilebtn);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            profileLauncher.launch(intent);
        });

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Note> notesList = realm.where(Note.class)
                .findAll()
                .sort("createdTime", Sort.DESCENDING);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(getApplicationContext(), notesList);
        recyclerView.setAdapter(myAdapter);

        notesList.addChangeListener((RealmChangeListener<RealmResults<Note>>) notes -> myAdapter.notifyDataSetChanged());

        myAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getApplicationContext(), EditNoteActivity.class);
            intent.putExtra("position", position);
            editNoteLauncher.launch(intent);
        });

        addNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                RealmResults<Note> updatedNotesList = realm.where(Note.class)
                        .findAll()
                        .sort("createdTime", Sort.DESCENDING);
                myAdapter.updateData(updatedNotesList);
            }
        });

        editNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                RealmResults<Note> updatedNotesList = realm.where(Note.class)
                        .findAll()
                        .sort("createdTime", Sort.DESCENDING);
                myAdapter.updateData(updatedNotesList);
            }
        });
        profileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            // Обработка результатов возвращаемых из ProfileActivity, если необходимо
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}
