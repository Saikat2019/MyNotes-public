package com.kajal.mynotes.databases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "notes")
public class Note {



    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;


    @NonNull
    @ColumnInfo(name = "userId")
    private String userID;

    @NonNull
    @ColumnInfo(name = "noteTitle")
    private String mNoteTitle;

    @NonNull
    @ColumnInfo(name = "noteBody")
    private String mNoteBody;

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setUserID(@NonNull String userID) {
        this.userID = userID;
    }


    public void setNoteTitle(@NonNull String mNoteTitle) {
        this.mNoteTitle = mNoteTitle;
    }

    public void setNoteBody(@NonNull String mNoteBody) {
        this.mNoteBody = mNoteBody;
    }



//    public Note(String noteTitle,String noteBody){
//        this.id = id;
//        this.mNoteTitle = noteTitle;
//        this.mNoteBody = noteBody;
//    }

    @NonNull
    public int getId() {
        return this.id;
    }

    @NonNull
    public String getUserID() {
        return userID;
    }

    @NonNull
    public String getNoteTitle() {
        return this.mNoteTitle;
    }

    @NonNull
    public String getNoteBody(){
        return this.mNoteBody;
    }
}
