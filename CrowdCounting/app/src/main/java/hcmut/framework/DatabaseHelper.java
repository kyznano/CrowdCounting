package hcmut.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private DbProcess dbprocess;
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, DbProcess dbprocess) {
		super(context, name, factory, version);
		this.dbprocess = dbprocess;
	}

	public interface DbProcess {
		/**
		 * VERSION 1.0.0 only
		 */
		String[] createTable();

		/**
		 * This method is recursively called when updating application. Code example:
		 * if(versionFrom==1 && versionTo==2) { // upgrade from version 1.0 -> 1.1 (example)
		 *     // do something...
		 * } else if(versionFrom==2 && versionTo==3) { // upgrade from version 1.1 -> 1.2 (example)
		 *     // do something...
		 * } ...
		 * @param versionFrom 1 means the first version e.g
		 * @param versionTo
		 */
		void upgrade(SQLiteDatabase db, int versionFrom, int versionTo);
	}

	/*
	private void createTables(SQLiteDatabase db) {
		// VERSION 1.0.0
		String[] sqlStrings = dbprocess.createTable();
		if(sqlStrings!=null && sqlStrings.length>0) {
			for(String sql : sqlStrings) {
				db.execSQL(sql);
			}
		}
	}*/

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: BECAREFUL WHEN UPDATE DATABASE (UPDATE APP VERSION)
		if(newVersion<=oldVersion) return;
		onUpgrade(db, oldVersion, newVersion-1);
		int from = newVersion-1;
		int to = newVersion;
		dbprocess.upgrade(db, from, to);
	}
	
	/**
	 * 
	 * @param tableName : name of the table
	 * @param colName : an array of column names
	 * @param colType : an array of column types
	 * @param indexPrimaryKeyInColName : 0 means the first column in colName is primary key of table, -1 means there is no primary key
	 * @return sql query for creating table if not exists
	 */
	public static String generateSQLCreateTable(String tableName, String[] colName, String[] colType, int indexPrimaryKeyInColName) {
		if(colName==null || colType==null || colName.length!=colType.length) return "";
		String sqlQuery = "";
		sqlQuery += "CREATE TABLE IF NOT EXISTS " + tableName;
		sqlQuery += "(" + generateColumnsString(colName, colType, indexPrimaryKeyInColName) + ")";
		return sqlQuery;
	}
	
	private static String generateColumnsString(String[] colName, String[] colType, int indexPrimaryKeyInColName) {
		String colString = "";
		for(int i=0; i<colName.length; i++) {
			String col = colName[i] + " " + colType[i];
			if(i==indexPrimaryKeyInColName) { col += " " + "PRIMARY KEY NOT NULL"; }
			colString += (colString.length()>0)?",":"";
			colString += col;
		}
		return colString;
	}
	
	public static String[] generateColArrayType(int numberOfColumns, String type) {
		if(numberOfColumns<0) return null;
		String[] colType = new String[numberOfColumns];
		for(int i=0; i<numberOfColumns; i++) {
			colType[i] = type;
		}
		return colType;
	}

}
