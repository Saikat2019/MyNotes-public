package com.kajal.mynotes.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.viewmodels.NoteViewModel;
import com.kajal.mynotes.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBackUpAllNow;
    private Button btnRestore;
    private LiveData<List<Note>> allNotes;
    private NoteViewModel noteViewModel;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();


        btnBackUpAllNow = findViewById(R.id.btn_backup_all);
        btnBackUpAllNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null)
                    backUpAllNow();
            }
        });

        btnRestore = findViewById(R.id.btn_restore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null)
                    restoreNotes();
            }
        });
    }

    private void backUpAllNow() {
        allNotes = noteViewModel.getMyAllNotes();
        allNotes.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                Note note;
                Map<String ,Object> noteMap = new HashMap<>();
                for(int i=0;i<notes.size();i++){
                    note = notes.get(i);
                    noteMap.put("noteID",note.getId());
                    noteMap.put("noteUserID",note.getUserID());
                    noteMap.put("noteTitle",note.getNoteTitle());
                    noteMap.put("noteBody",note.getNoteBody());
                    firebaseFirestore.collection("Notes").document(user_id).collection("notes")
                            .document(note.getNoteTitle()).set(noteMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplication(),"Uploaded",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplication(),"error : "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void restoreNotes() {
        firebaseFirestore.collection("Notes").document(user_id).collection("notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
//                        Log.d("XXXSettings", "onComplete: "+ documentSnapshot.get("noteBody"));
                        Note note = new Note();
                        note.setId(Integer.parseInt(documentSnapshot.get("noteID")+""));
                        note.setUserID(""+documentSnapshot.get("noteUserID"));
                        note.setNoteTitle(""+documentSnapshot.get("noteTitle"));
                        note.setNoteBody(""+documentSnapshot.get("noteBody"));
                        noteViewModel.insert(note);
                    }
                }
            }
        });
    }

}
