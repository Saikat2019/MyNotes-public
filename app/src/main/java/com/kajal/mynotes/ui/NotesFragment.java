package com.kajal.mynotes.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.viewmodels.NoteViewModel;
import com.kajal.mynotes.adapters.NotesListAdapter;
import com.kajal.mynotes.R;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment implements NotesListAdapter.OnDeleteclickListener {

    private static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;
    private String TAG = "XXXNotesFragment";
    private NoteViewModel noteViewModel;
    private FloatingActionButton fab;

    FirebaseAuth mAuth;
    private String user_id;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        mAuth = FirebaseAuth.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.notes_recycler_view);

        final NotesListAdapter notesListAdapter = new NotesListAdapter(getActivity(),this);//layoutInflater, mContext);
        recyclerView.setAdapter(notesListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        noteViewModel.getMyAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                notesListAdapter.setNotes(notes);
            }
        });

        fab = view.findViewById(R.id.fab_notes_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewNoteActivity.class);
                startActivityForResult(intent,NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

//            final String id = UUID.randomUUID().toString();
//            Note note = new Note(data.getStringExtra(NewNoteActivity.NOTE_TITLE_ADDED),
//                                    data.getStringExtra(NewNoteActivity.NOTE_BODY_ADDED));
            Note note = new Note();
            if(mAuth.getCurrentUser() != null) {
                user_id = mAuth.getCurrentUser().getUid();
                note.setUserID(user_id);
                note.setNoteTitle(data.getStringExtra(NewNoteActivity.NOTE_TITLE_ADDED));
                note.setNoteBody(data.getStringExtra(NewNoteActivity.NOTE_BODY_ADDED));
                noteViewModel.insert(note);
                Toast.makeText(getContext(), "Note added", Toast.LENGTH_SHORT).show();
            }

        }else {

            Toast.makeText(getContext(),"Unable to add",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnDeleteClicked(Note note) {
        noteViewModel.delete(note);
    }
}
