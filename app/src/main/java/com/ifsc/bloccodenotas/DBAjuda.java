package com.ifsc.bloccodenotas;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAjuda extends SQLiteOpenHelper {

	private Context ctx;
	private static final int version = 1;
	private static final String DB_NAME = "notasDB";
	private static final String TABLE_NAME = "notas";
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "notaTitulo";
	private static final String KEY_CONTENT = "notaConteudo";
	private static final String KEY_DATE = "data";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TITLE+" TEXT NOT NULL, "+KEY_CONTENT+" TEXT NOT NULL, "+KEY_DATE+" TEXT);";

	public DBAjuda(Context context) {
		super(context, DB_NAME, null, version);
		this.ctx = context;
	}

	public void addNote(String title, String content) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("notaTitulo", title);
		cv.put("notaConteudo", content);
		cv.put("data", new Date().toString());

		db.insert(TABLE_NAME, null, cv);
		db.close();
	}

	public Cursor getNotes(SQLiteDatabase db) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_CONTENT}, null, null, null, null, "id DESC");
		c.moveToFirst();
		return c;
	}
	
	public Cursor getNotes2(SQLiteDatabase db) {
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE}, null, null, null, null, "id DESC");
		c.moveToFirst();
		return c;
	}
	
	public Cursor getNote(SQLiteDatabase db, int id) {		
		Cursor c = db.query(TABLE_NAME, new String[] {KEY_TITLE, KEY_CONTENT, KEY_DATE}, KEY_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null);
		c.moveToFirst();
		return c;
	}
	
	public void removeNote(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(id) });
		db.close();
	}
	
	public void updateNote(String title, String content, String editTitle) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("notaTitulo", title);
		cv.put("notaConteudo", content);
		cv.put("data", new Date().toString());
		
		db.update(TABLE_NAME, cv, KEY_TITLE + " LIKE '" +  editTitle +  "'", null);
		
		db.close();
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
