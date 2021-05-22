package com.ifsc.bloccodenotas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CriarNota extends Activity {
	

	private static final String TAG = "BlocoNotas";

	private Button addNoteToDB;
	private EditText titleEditText;
	private EditText contentEditText;
	private boolean isEdit;
	private DBAjuda dbajuda;
	private SQLiteDatabase db;
	private String editTitle;
	private int id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.createlayout);
		addNoteToDB = (Button) findViewById(R.id.addNoteToDB);
		titleEditText = (EditText) findViewById(R.id.TitleEditText);
		contentEditText = (EditText) findViewById(R.id.ContentEditText);
		dbajuda = new DBAjuda(getApplicationContext());
		Intent mIntent = getIntent();
		editTitle = mIntent.getStringExtra("titulo");
		id = mIntent.getIntExtra("id", 0);
		isEdit = mIntent.getBooleanExtra("editado", false);

		if(isEdit) {
			Log.d(TAG, "editado");
			db = dbajuda.getReadableDatabase();
			Cursor c = dbajuda.getNote(db, id);
			db.close();
			titleEditText.setText(c.getString(0));
			contentEditText.setText(c.getString(1));
			addNoteToDB.setText(getResources().getString(R.string.updateNoteButton));
		}

		addNoteToDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String title = titleEditText.getText().toString();
				String content = contentEditText.getText().toString();
				
				if (title.equals("") || content.equals("")) {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.validation), Toast.LENGTH_LONG).show();
					return;
				}

				if (!isEdit) {
					dbajuda = new DBAjuda(getApplicationContext());
					dbajuda.addNote(title, content);
					finish();
				} else {
					dbajuda.updateNote(title, content, editTitle);
					finish();
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		dbajuda.close();
	}
	

}
