package com.kajal.mynotes.databases;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kajal.mynotes.databases.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM notes WHERE userId =:userID ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes(String userID);

    @Query("SELECT * FROM notes WHERE id=:noteId")
    LiveData<Note> getNote(int noteId);

    @Update
    void update(Note note);

    @Delete
    int delete(Note note);

}
