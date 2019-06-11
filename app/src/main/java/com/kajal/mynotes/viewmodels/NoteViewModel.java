package com.kajal.mynotes.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.databases.NoteDao;
import com.kajal.mynotes.databases.NoteRoomDatabase;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private String TAG = "XXXNoteViewModel";
    private NoteDao noteDao;
    private NoteRoomDatabase noteDB;
    private LiveData<List<Note>> mAllNotes;
    FirebaseAuth mAuth;
    private String user_id;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            noteDB = NoteRoomDatabase.getDatabase(application);
            noteDao = noteDB.noteDao();
            user_id = mAuth.getCurrentUser().getUid();
            mAllNotes = noteDao.getAllNotes(user_id);
        }
    }

    public void insert(Note note){
        new InsertAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getMyAllNotes(){
        return mAllNotes;
    }




    public void delete(Note note) {
        new DeleteAsyncTask(noteDao).execute(note);
    }

    private class InsertAsyncTask extends AsyncTask<Note,Void,Void> {

        NoteDao mNoteDao;

        public InsertAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.insert(notes[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Note,Void,Void>{

        NoteDao noteDao;

        public DeleteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplication(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
        }
    }

}
