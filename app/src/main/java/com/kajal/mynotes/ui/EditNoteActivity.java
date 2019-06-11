package com.kajal.mynotes.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.kajal.mynotes.viewmodels.EditNoteViewModel;
import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.R;

public class EditNoteActivity extends AppCompatActivity {

    private String TAG = "XXXEditNoteActivity";
    private EditText etNoteTitle;
    private EditText etNoteBody;
    private EditNoteViewModel noteViewModel;
    private Bundle bundle;
    private int noteId;
    private LiveData<Note> note;

    public static final String NOTE_ID = "note_id";
    public static String UPDATED_NOTE = "updated note";

    FirebaseAuth mAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mAuth = FirebaseAuth.getInstance();

        etNoteTitle = findViewById(R.id.etEditNoteTitle);
        etNoteBody = findViewById(R.id.etEditNoteBody);

        bundle = getIntent().getExtras();

        if(bundle != null){
            noteId =bundle.getInt("note_id");//Integer.parseInt( bundle.getString("note_id"));
        }

        noteViewModel = ViewModelProviders.of(this).get(EditNoteViewModel.class);

        note = noteViewModel.getNote(noteId);

        note.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note note) {
                etNoteTitle.setText(note.getNoteTitle());
                etNoteBody.setText(note.getNoteBody());
            }
        });
    }

    public void updateNote(View view){
        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            String updatedNoteTitle = etNoteTitle.getText().toString();
            String updatedNoteBody = etNoteBody.getText().toString();
            Note note = new Note();//noteId,updatedNoteTitle,updatedNoteBody);
            note.setId(noteId);
            note.setUserID(user_id);
            note.setNoteTitle(updatedNoteTitle);
            note.setNoteBody(updatedNoteBody);
            noteViewModel.update(note);
//            Log.d(TAG, "updateNote: - updated");
            finish();
        }
    }

    public void deleteNote(View view){
        finish();
    }
}
