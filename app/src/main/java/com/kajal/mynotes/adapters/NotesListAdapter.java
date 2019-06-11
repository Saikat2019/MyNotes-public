package com.kajal.mynotes.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kajal.mynotes.R;
import com.kajal.mynotes.databases.Note;
import com.kajal.mynotes.ui.EditNoteActivity;
import com.kajal.mynotes.ui.NotesFragment;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Note> mNotes;

    public interface OnDeleteclickListener{
        void OnDeleteClicked(Note note);
    }

    private OnDeleteclickListener onDeleteclickListener;

    public NotesListAdapter(Context mContext,OnDeleteclickListener deleteclickListener) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.onDeleteclickListener = deleteclickListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_note_layout,
                                                                            viewGroup,false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder viewHolder, int i) {
        if(mNotes != null){
            Note note = mNotes.get(i);
            viewHolder.setData(note,i);
            viewHolder.setListeners();
        }else {
            viewHolder.textNoteBody.setText("No note available");
            viewHolder.textNoteTitle.setText("");
        }
    }

    @Override
    public int getItemCount() {
        if(mNotes != null) return mNotes.size();
        else return 0;
    }

    public void setNotes(List<Note> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textNoteTitle;
        private TextView textNoteBody;
        private int mPosition;
        private ImageView img_update;
        private ImageView img_delete;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textNoteTitle = itemView.findViewById(R.id.note_title_text);
            textNoteBody = itemView.findViewById(R.id.note_body_text);
            img_update = itemView.findViewById(R.id.iv_update);
            img_delete =  itemView.findViewById(R.id.iv_delete);
        }

        @Override
        public void onClick(View v) {

        }

        public void setData(Note note, int i) {
            textNoteTitle.setText(note.getNoteTitle());
            textNoteBody.setText(note.getNoteBody());
            mPosition = i;
        }

        public void setListeners() {
            img_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditNoteActivity.class);
                    intent.putExtra("note_id",mNotes.get(mPosition).getId());
                    ((Activity)mContext).startActivityForResult(intent, NotesFragment.UPDATE_NOTE_ACTIVITY_REQUEST_CODE);
                }
            });

            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = mNotes.get(mPosition);
                    if(onDeleteclickListener != null){
                        onDeleteclickListener.OnDeleteClicked(note);
                    }
                }
            });
        }

    }
}
