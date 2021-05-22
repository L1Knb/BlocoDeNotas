package com.ifsc.bloccodenotas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class Nota extends Activity {
	
	private static final String TAG = "BlocoNotas";
	private TextView noteTitulo;
	private TextView createdAt;
	private TextView notaConte;
	private DBAjuda dbajuda;
	private SQLiteDatabase db;
	private String titulo = "defaultTitulo";
	private int id = 0;
	private String conte = "defaultConteudo";
	private String data = "data";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.onenote);

		dbajuda = new DBAjuda(getApplicationContext());

		noteTitulo = (TextView) findViewById(R.id.noteTitle);
		notaConte = (TextView) findViewById(R.id.noteContent);
		createdAt = (TextView) findViewById(R.id.createdAt);

		Intent mIntent = getIntent();
		
		id = mIntent.getIntExtra("id", 0);

		db = dbajuda.getReadableDatabase();
		Cursor c = dbajuda.getNote(db, id);
		db.close();

		titulo = c.getString(0).toString();
		conte = c.getString(1).toString();
		data = c.getString(2).toString();

		noteTitulo.setText(titulo);
		notaConte.setText(conte);
		createdAt.setText(data);
	}
	

}
