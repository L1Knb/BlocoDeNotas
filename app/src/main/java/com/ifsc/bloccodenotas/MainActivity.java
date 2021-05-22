package com.ifsc.bloccodenotas;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "BlocoNotas";

	private ListView noteList;
	private Button addNoteBtn;
	private ArrayAdapter<String> adapter;
	private Cursor notes;
	private DBAjuda dbajuda;
	private ArrayList<String> titlos;
	private ArrayList<Item> items;
	private int position = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		noteList = (ListView) findViewById(R.id.noteList);
		addNoteBtn = (Button) findViewById(R.id.addNote);

		dbajuda = new DBAjuda(getApplicationContext());
		setNotes();
		this.registerForContextMenu(noteList);
		addNoteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), CriarNota.class));

			}
		});

	}

	public void setNotes() {
		titlos = new ArrayList<String>();
		items = new ArrayList<Item>();

		SQLiteDatabase db = dbajuda.getReadableDatabase();
		notes = dbajuda.getNotes2(db);
		startManagingCursor(notes);
		db.close();

		if (notes.moveToFirst()) {
			do {
				items.add(new Item(notes.getShort(0), notes.getString(1)));
			} while (notes.moveToNext());
		}
		
		
		for (Item i : items) {
			titlos.add(i.getTitle());
		}

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titlos);
		noteList.setAdapter(adapter);
		noteList.setOnItemClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		setNotes();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		position = info.position;
		menu.setHeaderTitle(getResources().getString(R.string.CtxMenuHeader));
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		TextView tv = (TextView) noteList.getChildAt(position);
		String title = tv.getText().toString();
		switch (item.getItemId()) {

		case R.id.showNote:
			Intent mIntent = new Intent(this, Nota.class);
			mIntent.putExtra("id", items.get(position).getId());
			startActivity(mIntent);
			break;

		case R.id.editNote:
			Intent i = new Intent(this, CriarNota.class);
			i.putExtra("id", items.get(position).getId());
			Log.d(TAG, title);
			i.putExtra("editado", true);
			startActivity(i);
			break;

		case R.id.removeNote:
			dbajuda.removeNote(items.get(position).getId());
			setNotes();
			break;

		}

		return false;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TextView tv = (TextView) arg1;
		String title = tv.getText().toString();
		Intent mIntent = new Intent(this, Nota.class);
		mIntent.putExtra("titulo", title);
		mIntent.putExtra("id", items.get(arg2).getId());
		startActivity(mIntent);
	}
	
}