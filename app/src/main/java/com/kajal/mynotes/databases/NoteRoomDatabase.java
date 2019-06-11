package com.kajal.mynotes.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Note.class},version = 1)
public abstract class NoteRoomDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();


    private static volatile NoteRoomDatabase noteRoomInstance;


    public static NoteRoomDatabase getDatabase(final Context context){
        if(noteRoomInstance == null) {
            synchronized (NoteRoomDatabase.class) {
                if (noteRoomInstance == null) {
                    noteRoomInstance = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "notes_database")
                            .build();
                }
            }
        }
        return  noteRoomInstance;
    }

}
