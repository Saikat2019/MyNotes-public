package com.kajal.mynotes.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kajal.mynotes.R;

public class NewNoteActivity extends AppCompatActivity {

    public static final String NOTE_TITLE_ADDED = "title_added";
    public static final String NOTE_BODY_ADDED = "body_added";
    private EditText etNoteTitle;
    private EditText etNoteBody;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        etNoteTitle = findViewById(R.id.newNoteTitle);
        etNoteBody = findViewById(R.id.newNoteBody);
        btnSave = findViewById(R.id.bAdd);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                if(TextUtils.isEmpty(etNoteTitle.getText()) && TextUtils.isEmpty(etNoteBody.getText())){
                    setResult(RESULT_CANCELED,resultIntent);
                }else {
                    String noteTitle = etNoteTitle.getText().toString();
                    String noteBody = etNoteBody.getText().toString();
                    resultIntent.putExtra(NOTE_TITLE_ADDED,noteTitle);
                    resultIntent.putExtra(NOTE_BODY_ADDED,noteBody);
                    setResult(RESULT_OK,resultIntent);
                }
                finish();
            }
        });
    }
}
