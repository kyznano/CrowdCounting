package hcmut.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;

import hcmut.aclab.crowd.counting.BuildConfig;

public class DatabaseAdapter {
	// TODO: check "mIsTesting" before releasing application
	protected boolean mIsTesting = false;

	private static final String DATABASE_NAME = "app.db";
	protected SQLiteDatabase mDatabase;
	private DatabaseHelper mDbHelper;
	
	public DatabaseAdapter(Context context) {
		// Android studio only;
		int DATABASE_VERSION = BuildConfig.VERSION_CODE;
		//int DATABASE_VERSION = Integer.valueOf(context.getString(R.string.database_version));
		String DBPATH = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
		this.mDbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION, new DatabaseHelper.DbProcess() {
			@Override
			public String[] createTable() {
				return createTableSql();
			}

			@Override
			public void upgrade(SQLiteDatabase db, int versionFrom, int versionTo) {
				upgradeDb(db, versionFrom, versionTo);
			}
		});

		File dbFile = new File(DBPATH);
		if(!dbFile.exists()) {
			open();
			init();
		} else {
			if(mIsTesting) {
				// delete & re-create mDatabase - use for testing
				dbFile.delete();
				open();
				init();
			} else {
				open();
			}

		}
	}

	public DatabaseAdapter open() throws SQLException{		
		mDatabase = mDbHelper.getWritableDatabase();
		return this;
	}	
	
	public boolean isOpen() {
		return mDatabase.isOpen();
	}
	
	public void close() { mDatabase.close(); }

	public void setModeTesting() {
		mIsTesting = true;
	}

	public void setModeRelease() {
		mIsTesting = false;
	}
	
	/* Methods must be overrided */

	protected void init(){}

	protected String[] createTableSql() { return new String[0]; }

	protected void upgradeDb(SQLiteDatabase db, int versionFrom, int versionTo) {}

	public Object[] getAllRecord(String tableName) { return null; }

	public Object getRecordAt(String tableName, String colName, String colValue) { return null; }
	
	public Object getRecordAt(String tableName, String[] colNames, String[] colValues) { return null; }
	
	public void insertRecord(Object data) {}

	public void insertRecord(ArrayList<Object> records) {}
	
	public void insertUpdateRecord(Object data) {}
	
	public void updateRecord(String whichTable, String[] colIds, String[] ids, String[] colNames, String[] newValues) {}
	
	public void updateRecord(String whichTable, String colId, String id, String[] colNames, String[] newValues) {}

	/* End - Methods must be overrided */

	public void insertRecord(String tableName, String[] cols, String[] values) {
		if(!isOpen()) { return; }
		ContentValues cvalues = new ContentValues();
		for(int i=0; i<cols.length; i++) {
			cvalues.put(cols[i], values[i]);
		}
		mDatabase.insert(tableName, null, cvalues);
	}

	public void updateRecord(String whichTable, String colId, String id, String colName, String newValue) {
		if(!isOpen()) { return; }
		ContentValues values = new ContentValues();
		values.put(colName, newValue);
		mDatabase.update(whichTable, values, colId + "=?", new String[]{id});
	}

	public void deleteRecord(String whichTable, String colId, String id) {
		if(!isOpen()) { return; }
		mDatabase.delete(whichTable, colId + "=?", new String[]{id});
	}
	
	public void deleteAllRecord(String whichTable) {
		if(!isOpen()) { return; }
        mDatabase.delete(whichTable, null, null);
    }
	
	private static String generateWhere(String[] colNames) {
		String generatedString = "";
		if(colNames==null || colNames.length==0) { return null; }
		for(String cname : colNames) {
			if(generatedString!=null && generatedString.length()>0) { generatedString += " AND "; }
			generatedString += cname + "=?";
		}
		return generatedString;
	}
}
