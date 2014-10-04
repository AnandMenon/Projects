package edu.buffalo.cse.cse486586.simpledynamo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleDynamoDbHelper extends SQLiteOpenHelper {
	
	public static final String dbName = "dynamo_db";
	public static final String tableName = "dynamoTable";
	public static final String key = "key";
	public static final String value = "value";
	
	public SimpleDynamoDbHelper(Context context) {
		super(context,dbName , null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE DYNAMOTABLE( key TEXT PRIMARY KEY, value TEXT, owner TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
