package edu.buffalo.cse.cse486586.groupmessenger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupMessengerDbHelper extends SQLiteOpenHelper {
	
	public static final String dbName = "messenger_db";
	public static final String tableName = "messengerTable";
	public static final String key = "key";
	public static final String value = "value";

	public GroupMessengerDbHelper(Context context) {
		super(context,dbName , null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		//database.execSQL("CREATE TABLE if not exists messengerTable ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
				//"sequenceNumber INTEGER, messageId INTEGER, message TEXT, avdNumber INTEGER)");
		database.execSQL("CREATE TABLE MESSENGERTABLE( key TEXT PRIMARY KEY, value TEXT)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
