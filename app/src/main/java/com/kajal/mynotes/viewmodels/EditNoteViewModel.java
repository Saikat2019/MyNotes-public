package com.kajal.mynotes.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.databases.NoteDao;
import com.kajal.mynotes.databases.NoteRoomDatabase;

public class EditNoteViewModel extends AndroidViewModel {

    private NoteDao noteDao;
    private NoteRoomDatabase roomDB;
    private String TAG = "XXXEditNoteVM";

    public EditNoteViewModel(@NonNull Application application) {
        super(application);

        roomDB = NoteRoomDatabase.getDatabase(application);
        noteDao = roomDB.noteDao();
    }

    public LiveData<Note> getNote(int noteId){
        return noteDao.getNote(noteId);
    }

    public void update(Note note){
        new UpdateAsyncTask(noteDao).execute(note);
    }

    private class UpdateAsyncTask extends AsyncTask<Note,Void,Void> {

        NoteDao noteDao;

        public UpdateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
//            Log.d(TAG, "doInBackground: "+notes[0].getNoteBody());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplication(),"Note successfully updated",Toast.LENGTH_SHORT).show();
        }
    }

}
