package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.TreeMap;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SimpleDynamoProvider extends ContentProvider {
	
	SQLiteDatabase db;
	SimpleDynamoDbHelper dbHelper;
	int deletedRows;
	String successorPort;
	String predecessorPort;
	String successorPortNodeId;
	String predecessorPortNodeId;
	
	String successor1;
	String successor2;
	String predecessor1;
	String predecessor2;
	
	String myPort;
	String nodeId;
	String globalMessage="#";
	String localMessage=null;
	String queryValue;
	
	Object lock = new Object();
	Object lock2 = new Object();
	static Object onCreateLock = new Object();
	
	volatile int lockCount = 4;
	volatile int onCreateLockCount = 3;
	
	String avd0Port = "11108";
	String edgePort = "11124";
	static final String TAG = SimpleDynamoProvider.class.getSimpleName();
	static final int SERVER_PORT = 10000; 
	TreeMap<String,ArrayList<String>> globalDetails;
	ArrayList<String> nodeDetails;
	TreeMap<String,String> globalNodeDetails;
	
	private ContentResolver mContentResolver;
	private  Uri mUri;
	private static final String KEY_FIELD = "key";
	private static final String VALUE_FIELD = "value";
	private static final String OWNER_FIELD = "owner";
	
	private Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if(selection.equalsIgnoreCase("*"))
    	{
    		//String[] deleteMessage = {"isDelete",successorPort,myPort};
    		//new Thread(new ClientTask(deleteMessage)).start();
    		return 0;
    		
    	}
    	else if(selection.equalsIgnoreCase("@"))
    	{
    		deletedRows = db.delete(dbHelper.tableName, "1", selectionArgs);
    	}
    	else
    	{
    	selection = "key="+"'"+selection+"'";
    	deletedRows = db.delete(dbHelper.tableName, selection, selectionArgs);
    	}
        return deletedRows;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		
		String key = (String) values.get(KEY_FIELD);
		String val = (String) values.get(VALUE_FIELD);
		String hashedKey = null;
		String destinationNodeId = "";
		String destinationNode = "unset";
		String destination2 = "";
		String destination3 = "";
		
		try {
			hashedKey = genHash(key);
			Log.e("insert","insert entered with key "+key+ " and genkey"+hashedKey);
			
			
						
			for(String storedNodeId : globalNodeDetails.keySet())
			{
				if(hashedKey.compareTo(storedNodeId)<0)
				{
					Log.e("here","entered here:");
					destinationNodeId = storedNodeId;
					destinationNode = globalNodeDetails.get(destinationNodeId);
					break;
				}
			}
			
			if(destinationNode.equalsIgnoreCase("unset"))
			{
				Log.d("unset","unset node is 5562");
				destinationNode = globalNodeDetails.get(globalNodeDetails.firstKey());
			}
			
			String temp[] = getSuccessor(destinationNode).split(",");
			destination2 = temp[0];
			destination3 = temp[1];
			
			String[] insertMessage = {"insertHere",destinationNode,key,val,destinationNode};
			
			Thread t=new Thread(new ClientTask(insertMessage));
			
			Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					
					Log.e("EXCEPTION","Exception caught in insert of "+myPort);
					
				}
			};
			t.setUncaughtExceptionHandler(handler);
    		t.start();
    		
    		String[] insertMessage2 = {"insertHere",destination2,key,val,destinationNode};
			
			Thread t2=new Thread(new ClientTask(insertMessage2));
			Thread.UncaughtExceptionHandler handler2 = new Thread.UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					
					Log.e("EXCEPTION","Exception caught in insert of "+myPort);
					
				}
			};
			t2.setUncaughtExceptionHandler(handler2);
    		t2.start();    		
   		
    		String[] insertMessage3 = {"insertHere",destination3,key,val,destinationNode};
			
			Thread t3=new Thread(new ClientTask(insertMessage3));
			Thread.UncaughtExceptionHandler handler3 = new Thread.UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread thread, Throwable ex) {
					
					Log.e("EXCEPTION","Exception caught in insert of "+myPort);
					
				}
			};
			t3.setUncaughtExceptionHandler(handler3);
    		t3.start();

    		
    		//Code for checking if failed.
    		
		}catch(Exception e)
		{
			Log.e("error","exception is"+e.getMessage());
			e.printStackTrace();
		
		}
		
        return uri;
		
		
	}
	
	public String getPredecessor(String input)
	{
		String predecessor="";
		
		switch(Integer.parseInt(input))
    	{
    	case 11108:
    		predecessor = "11112";
    		break;
    		
    	case 11112:
    		predecessor = "11124";
    		break;
    		
    	case 11116:
    		predecessor = "11108";
    		break;
    	
    	case 11120:
    		predecessor = "11116";
    		break;
    		
    	case 11124:
    		predecessor = "11120";
    		break;
    	}
		
		return predecessor;
	}
	
	public String getSuccessor(String input)
	{
		String successor1="",successor2="";
	
		switch(Integer.parseInt(input))
    	{
    	case 11108:
    		successor1 = "11116";
    		successor2 = "11120";
    		break;
    		
    	case 11112:
    		successor1 = "11108";
    		successor2 = "11116";
    		break;
    		
    	case 11116:
    		successor1 = "11120";
    		successor2 = "11124";
    		break;
    	
    	case 11120:
    		successor1 = "11124";
    		successor2 = "11112";
    		break;
    		
    	case 11124:
    		successor1 = "11112";
    		successor2 = "11108";
    		break;
    	/*	
    	case 11108:
    		successor1 = "11116";
    		successor2 = "11112";
    		break;
    		
    	case 11112:
    		successor1 = "11108";
    		successor2 = "11116";
    		break;
    		
    	case 11116:
    		successor1 = "11112";
    		successor2 = "11108";
    		break;
    		*/
    	
    	}
		Log.d("successor","returning from fuction with:"+successor1+","+successor2);
		
		String result = successor1+","+successor2;
		return result;
		
	}

	@Override
	public boolean onCreate() {
		
		dbHelper = new SimpleDynamoDbHelper(this.getContext());
    	db = dbHelper.getWritableDatabase();
    	mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledynamo.provider");
    	mContentResolver = getContext().getContentResolver();
    	
    	
    	Log.d("Oncreate","Entered oncreate");
    	
    	TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
		String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
		myPort = String.valueOf((Integer.parseInt(portStr) * 2));
    	try {
			nodeId = genHash(portStr); 
			Log.e("nodeid","nodeId is "+nodeId);
		} catch (NoSuchAlgorithmException e) {
			
			Log.v(TAG, e.toString());
		}
    	
    	/*switch(Integer.parseInt(portStr))
    	{
    	case 5554:
    		successor1 = "11116";
    		successor2 = "11112";
    		break;
    		
    	case 5556:
    		successor1 = "11108";
    		successor2 = "11116";
    		break;
    		
    	case 5558:
    		successor1 = "11112";
    		successor2 = "11108";
    		break;
    	
    	}*/
    	
    	
    	switch(Integer.parseInt(portStr))
    	{
    	case 5554:
    		successor1   = "11116";
    		successor2   = "11120";
    		predecessor1 = "11112";
    		predecessor2 = "11124";
    		break;
    		
    	case 5556:
    		successor1   = "11108";
    		successor2   = "11116";
    		predecessor1 = "11124";
    		predecessor2 = "11120";
    		break;
    		
    	case 5558:
    		successor1   = "11120";
    		successor2   = "11124";
    		predecessor1 = "11108";
    		predecessor2 = "11112";
    		break;
    	
    	case 5560:
    		successor1   = "11124";
    		successor2   = "11112";
    		predecessor1 = "11116";
    		predecessor2 = "11108";
    		break;
    		
    	case 5562:
    		successor1   = "11112";
    		successor2   = "11108";
    		predecessor1 = "11120";
    		predecessor2 = "11116";
    		break;
    		
    			
    	
    	}
    	
    	Log.d("successors","Suc1:"+successor1+" Suc2:"+successor2);
    	
    	//starting server listening for requests
    	
    	
    	    	
    	try {
			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			ServerTask st = new ServerTask(serverSocket);
			Thread serverThread = new Thread(st);
			serverThread.start();
			Log.d("Oncreate","Created server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"Reached here Exception");
			e.printStackTrace();
		}
    	
    	//Code for storing node details on each node
    	globalNodeDetails = new TreeMap<String, String>();
    	try
    	{
    	for(int i=5554;i<=5562;i+=2)
    	{
    		String insertNodeId = genHash(String.valueOf(i));
    		String insertVal = String.valueOf(i*2);
    		globalNodeDetails.put(insertNodeId,insertVal);
    		Log.d("at hash", "inserted "+insertNodeId+" val:"+insertVal);
    	}
    	}catch(Exception e)
    	{
    		Log.e("exception","exception inserting to hash table"+e.getStackTrace());
    	}
    	
    	//Printing the table
    			
		for(String storedNodeId : globalNodeDetails.keySet())
		{
			
				Log.e("printing","StoredNodeId:"+storedNodeId);
							
		}
		
		//check if first run
		Cursor c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName, null);
		
		
		if(c.getCount() > 0)
		{
		
		
		//code for rejoining
		onCreateLockCount = 3;
		String[] partition = {"isPartition",predecessor1,predecessor2,myPort};
		
		
		Thread t = new Thread(new ClientTask(partition));
		
		Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				
				Log.e("EXCEPTION","Exception caught in onCreate of "+myPort+" sending to "+predecessor1+"lock is"+onCreateLockCount);
				onCreateLockCount = onCreateLockCount-1;
				
				String[] partition = {"isPartition",predecessor2,predecessor2,myPort};
				
				try
				{
					Thread t = new Thread(new ClientTask(partition));
				
					t.start();
				}catch(Exception e)
				{
					Log.e("EXCEPTION","Second Exception caught in onCreate of "+myPort);
					
				}
				
				
			}
		};
		
		t.setUncaughtExceptionHandler(handler);
		t.start();
		
		//Contact successsor 1 for predecesor1 data
		String[] partition2 = {"isPartition",successor1,predecessor1,myPort};
		
		
		Thread t2 = new Thread(new ClientTask(partition2));
		
		Thread.UncaughtExceptionHandler handler2 = new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				
				Log.e("EXCEPTION","Exception caught in onCreate of "+myPort+" sending to "+successor1+"lock is "+onCreateLockCount); 
				onCreateLockCount = onCreateLockCount-1;
				String[] partition = {"isPartition",predecessor1,predecessor1,myPort};
				
				try
				{
					Thread t = new Thread(new ClientTask(partition));
				
					t.start();
				}catch(Exception e)
				{
					Log.e("EXCEPTION","Second Exception caught in onCreate of "+myPort);
				}
				
			}
		};
		Log.d("printing","printing by main thread");
		t2.setUncaughtExceptionHandler(handler2);
		t2.start();
		
		//Contact successor 2 for my data
		String[] partition3 = {"isPartition",successor2,myPort,myPort};
		
		
		Thread t3 = new Thread(new ClientTask(partition3));
		
		Thread.UncaughtExceptionHandler handler3 = new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				
				Log.e("EXCEPTION","Exception caught in onCreate of "+myPort+" sending to "+predecessor1+"lock is"+onCreateLockCount); 
				onCreateLockCount = onCreateLockCount-1;
				String[] partition = {"isPartition",successor1,myPort,myPort};
				
				try
				{
					Thread t = new Thread(new ClientTask(partition));
				
					t.start();
				}catch(Exception e)
				{
					Log.e("EXCEPTION","Second Exception caught in onCreate of "+myPort);
				}
				
			}
		};
		
		t3.setUncaughtExceptionHandler(handler3);
		t3.start();
		
		//Lock till 3 replies or exceptions
		
		
		new Thread()
		{
			public void run()
			{
				while(true)
				{
					if(onCreateLockCount == 0)
					{
						synchronized (onCreateLock)
						{
						onCreateLock.notify();
						break;
						}
					}
						
				}
				Log.d("checkthread","notified exciting while");
			}
		}.start();
		
		synchronized (onCreateLock)
		{
			try
			{
				Log.v("Oncreate", "Now waiting in Oncreate");
				onCreateLock.wait();
			}catch(Exception e)
			{
				Log.e("exception","sync excpetion");
				e.printStackTrace();
			}
			
		}
		Log.d("exception","control here");
		}
        /*
    	//Code for 5554 and other avds
    	if(portStr.equalsIgnoreCase("5554"))
    	{
    		Log.d("Oncreate","Inside Avd0");
    		successorPort = new String("11108");
    		predecessorPort = new String("11108");
    		successorPortNodeId = new String("5554");
    		predecessorPortNodeId = new String("5554");
    		globalDetails = new TreeMap<String, ArrayList<String>>();
    		nodeDetails = new ArrayList<String>();
    		
    		
    		nodeDetails.add(successorPort);
    		nodeDetails.add(predecessorPort);
    		nodeDetails.add(myPort);
    		
    		globalDetails.put(nodeId, nodeDetails);
    	}
    	else
    	{
    		//send message to avd0 to join
    		Log.d("Oncreate","Inside non avd0");
    		successorPort = new String(myPort);
    		predecessorPort = new String(myPort);
    		successorPortNodeId = new String("5554");
    		predecessorPortNodeId = new String("5554");
    	
    		
    		String[] joinRequest = {"isJoin",avd0Port,nodeId,myPort};
    		
    	
    		Thread t=new Thread(new ClientTask(joinRequest));
    		t.start();
    	}
		*/
        return true;
		
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		
		Cursor c = null;
    	
    	String fetchKey, fetchValue;
    	String[] dump={};
    	
    	Log.v("query", selection);
    	if(selection.equalsIgnoreCase("*"))
    	{
    		//GLOBAL DUMP
    		MatrixCursor mCursor = new MatrixCursor(new String[]{"key","value"});
    		c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName, null);
    		
			
			if(c.getCount() > 0)
			{
				c.moveToFirst();
				do
				{
					//for each entry less than the nodeId send to origin and delete
					fetchKey = c.getString(c.getColumnIndex(KEY_FIELD));
					fetchValue = c.getString(c.getColumnIndex(VALUE_FIELD));
					mCursor.addRow(new String[]{fetchKey,fetchValue});
				}while(c.moveToNext());
			}
			Log.v("query", "Local dump taken");
			
			String destinationNode="";
			
			for(String storedNodeId : globalNodeDetails.keySet())
			{
				if(!nodeId.equalsIgnoreCase(storedNodeId))
				{
					try {
						Log.e("here","sending to:"+storedNodeId);
						//destinationNodeId = storedNodeId;
						destinationNode = globalNodeDetails.get(storedNodeId);
						
						String[] globalQueryString = {"isGlobaldump",destinationNode,"#",myPort};
						new Thread(new ClientTask(globalQueryString)).start();
					} catch (Exception e) {
						Log.e("Exception","exception in *");
						e.printStackTrace();
					}
				}
			}
			
			lockCount = 4;
			
			synchronized (lock) {
				try
				{
					Log.v("query", "Now waiting");
					lock.wait();
				}catch(Exception e)
				{
					Log.e("exception","sync excpetion");
					e.printStackTrace();
				}
				
			}
			
			if(!(globalMessage.equalsIgnoreCase("#")))
			{
			
			
			//SPLIT THE GLOBAL MESSAGE STORE IN MATRIX CURSOR AND RETURN
			Log.e("in *","After wait global message is"+globalMessage);
			String[] keyValuePairs = globalMessage.split("#");
			for(String temp:keyValuePairs)
			{
				try {
					String[] pair = temp.split(":");
					Log.v("message","string key-value is"+pair[0]+pair[1]);
					mCursor.addRow(pair);
				} catch (Exception e) {
					Log.e("error","exception in mcursor");
					e.printStackTrace();
				}
			}
			
			}
			
			
			/*
			if(!(Integer.parseInt(successorPort) == Integer.parseInt(myPort)))
			{
			
				Log.v("query", "Inside succ condition");
			String[] globalQueryString = {"isGlobaldump",successorPort,"#",myPort};
			
			
			
			new Thread(new ClientTask(globalQueryString)).start();
			
			Log.v("query", "Done creating thread");
			synchronized (lock) {
				try
				{
					Log.v("query", "Now waiting");
					lock.wait();
				}catch(Exception e)
				{
					Log.e("exception","sync excpetion");
					e.printStackTrace();
				}
				
			}
			
			Log.v("query", "Done waiting");
			
			
			if(!(globalMessage.equalsIgnoreCase("#")))
			{
			
			
			//SPLIT THE GLOBAL MESSAGE STORE IN MATRIX CURSOR AND RETURN
			Log.e("in *","After wait global message is"+globalMessage);
			String[] keyValuePairs = globalMessage.split("#");
			for(String temp:keyValuePairs)
			{
				try {
					String[] pair = temp.split(":");
					Log.v("message","string key-value is"+pair[0]+pair[1]);
					mCursor.addRow(pair);
				} catch (Exception e) {
					Log.e("error","exception in mcursor");
					e.printStackTrace();
				}
			}
			
			}
    	}
			*/
    		return mCursor;
    		
    	}
    	else if(selection.equalsIgnoreCase("@"))
    	{
    		
    		c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName, null);
        	
        	return c;
    	}
    	else
    	{
    		final String tempKey = selection;
    		String tempHashKey = null;
    		String destinationNode="unset",destinationNodeId="";
    		try {
				tempHashKey = genHash(tempKey);
			} catch (NoSuchAlgorithmException e) {
				Log.e("error single key","exception");
				e.printStackTrace();
			}
	    	selection = "key="+"'"+selection+"'";
	    	//c = db.query(dbHelper.tableName, projection, selection, selectionArgs, null, null, sortOrder);
	    	//c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName +" WHERE "+selection, null);
	    	
	    	
	    		//not present in local hence query right node
	    		
	    		for(String storedNodeId : globalNodeDetails.keySet())
				{
					if(tempHashKey.compareTo(storedNodeId)<0)
					{
						Log.e("here","entered here:");
						destinationNodeId = storedNodeId;
						destinationNode = globalNodeDetails.get(destinationNodeId);
						break;
					}
				}
				
				if(destinationNode.equalsIgnoreCase("unset"))
				{
					Log.d("unset","unset node is 5562");
					destinationNode = globalNodeDetails.get(globalNodeDetails.firstKey());
				}
				
				String temp[] = getSuccessor(destinationNode).split(",");
				String firstSuccessor = temp[0];
				final String lastSuccessor = temp[1];
				Log.d("query","query entered with "+myPort+" querying from "+lastSuccessor);
	    		String[] queryString = {"isQuery",lastSuccessor,tempKey,myPort};
	    		
	    		//new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, queryString);
	    		//new Thread(new ClientTask(queryString)).start();
	    		
	    		
	    		Thread t = new Thread(new ClientTask(queryString));
	    		
	    		Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
	    			
	    			@Override
	    			public void uncaughtException(Thread thread, Throwable ex) {
	    				
	    				Log.e("EXCEPTION","Exception caught in onCreate of "+myPort);
	    				
	    				String[] queryString = {"isQuery",getPredecessor(lastSuccessor),tempKey,myPort};
	    				
	    				try
	    				{
	    					Thread t = new Thread(new ClientTask(queryString));
	    				
	    					t.start();
	    				}catch(Exception e)
	    				{
	    					Log.e("EXCEPTION","Second Exception caught in onCreate of "+myPort);
	    				}
	    				
	    				
	    			}
	    		};
	    		
	    		t.setUncaughtExceptionHandler(handler);
	    		t.start();
	    		
				
	    		synchronized (lock2) {
					try
					{
						Log.v("query", "Now waiting");
						lock2.wait();
					}catch(Exception e)
					{
						Log.e("exception","sync excpetion");
						e.printStackTrace();
					}
					
				}
				
				MatrixCursor m = new MatrixCursor(new String[]{"key","value"});
				m.addRow(new String[]{tempKey,queryValue});
	    		return m;
	    	
	        //Log.v("query", selection);
	        //return c;
    	}
        //return c;
    	
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


    
    private class ServerTask implements Runnable {
    	ServerSocket serverSocket;
    	ServerTask(ServerSocket s)
    	{
    		this.serverSocket = s;
    	}

        @Override
        public void run() {
            
            ObjectInputStream inputStream = null;
            Socket clientConnection = null;
            String receivedNodeId;
            String tempSuccessor,tempPredecessor,tempSuccessorNode=nodeId,tempPredecessorNode;
            String fetchKey,fetchValue;
            ArrayList<String> newNode,predUpdate,succUpdate;
            String[] received={};
            
            
            Log.d("Server","inside server");
            try {
            	while(true)
            	{	
    			
				try {
					clientConnection = serverSocket.accept();
					Log.v("Server","New client connected");
					inputStream = new ObjectInputStream(clientConnection.getInputStream());
						
					Object receivedObject =   inputStream.readObject();
					received = (String[]) receivedObject;
				} catch (Exception e1) {
					Log.e("error","error in serial");
					e1.printStackTrace();
				}
    			
    			/*Code for processing*/
    			
    			
    			if(myPort.equalsIgnoreCase("11108"))
    			{
    				    				
    				if(received[0].equalsIgnoreCase("isJoin"))
    				{
    					tempSuccessor = "#";
    					Log.e("isjoin","enetered avd0 isjoin "+received[2]+received[3]);
    					receivedNodeId = received[2];
    					Log.e("here","tempsuccessor is"+tempSuccessor);
    					
    					for(String storedNodeId : globalDetails.keySet())
    					{
    						if(receivedNodeId.compareTo(storedNodeId)<0)
    						{
    							Log.e("here","entered here:");
    							tempSuccessorNode = storedNodeId;
    							tempSuccessor = globalDetails.get(storedNodeId).get(2);
    							break;
    						}
    					}
    					
    					if(tempSuccessor.equalsIgnoreCase("#"))
    					{
    						tempSuccessorNode=globalDetails.firstKey();
    						Log.e("here","entered her:"+tempSuccessorNode);
    						tempSuccessor = globalDetails.get(tempSuccessorNode).get(2);
    					}
    					
    					//getting predecessor corresponding to successor
    					tempPredecessor = globalDetails.get(tempSuccessorNode).get(1);
    					
    					Log.e("isjoin","successor of "+received[3]+tempSuccessor+" and pred is "+tempPredecessor);
    					
    					//adding into global details
    					newNode = new ArrayList<String>();
    					newNode.add(tempSuccessor);
    					newNode.add(tempPredecessor);
    					newNode.add(received[3]);
    					
    					globalDetails.put(receivedNodeId, newNode);
    					
    					//update global table
    					succUpdate = new ArrayList<String>();
    					succUpdate.add(globalDetails.get(tempSuccessorNode).get(0));
    					succUpdate.add(globalDetails.get(receivedNodeId).get(2));
    					succUpdate.add(globalDetails.get(tempSuccessorNode).get(2));
    					
    					globalDetails.put(tempSuccessorNode,succUpdate);
    					
    					predUpdate = new ArrayList<String>();
    					tempPredecessorNode = genHash(String.valueOf(Integer.parseInt(globalDetails.get(receivedNodeId).get(1))/2));
    					predUpdate.add(globalDetails.get(receivedNodeId).get(2));
    					predUpdate.add(globalDetails.get(tempPredecessorNode).get(1));
    					predUpdate.add(globalDetails.get(tempPredecessorNode).get(2));
    					
    					globalDetails.put(tempPredecessorNode, predUpdate);
    					
    					for(String storedNodeId : globalDetails.keySet())
    					{
    						Log.e("printing", "global:"+storedNodeId);
    					}
    					
    					
    					
    					String[] joinResponseMessage = {"isJoinResponse",received[3],tempSuccessor,tempPredecessor};
    					
    					
    					new Thread(new ClientTask(joinResponseMessage)).start();
    					
    					
    					
    					
    				}
    				
    			}
    			
    			//LOGIC FOR INSERT HERE
    			if(received[0].equalsIgnoreCase("insertHere"))
    			{
    				String key = received[2];
    				String val = received[3];
    				String source = received[4];
    				
    				ContentValues cv = new ContentValues();
    				cv.put(KEY_FIELD,key);
    				cv.put(VALUE_FIELD, val);
    				cv.put(OWNER_FIELD, source);
				
    				double status = db.replace(dbHelper.tableName, null, cv);
    				Log.d("INSERT","Inserted key-value-source:"+key+"-"+val+"-"+source);
    				
    				//For replication
    				/*if(source.equals(myPort))
    				{
    				String[] insertMessage1 = {"insertHere",successor1,key,val,myPort};
    				String[] insertMessage2 = {"insertHere",successor2,key,val,myPort};
    				Thread t=new Thread(new ClientTask(insertMessage1));
    				Thread t2=new Thread(new ClientTask(insertMessage2));
    	    		t.start();
    	    		t2.start();
    				}*/
		    	
		    	
		    	
    				Log.v("insert", cv.toString());
    				
    			}
    			
    			
    			
    			
    			//LOGIC FOR GOBAL QUERY
    			if(received[0].equalsIgnoreCase("isGlobalDump"))
    			{
    				if(!(received[3].equalsIgnoreCase(myPort)))
					{
    					Log.e("in gdump","inside global dump with"+received[3]+received[2]);
    					
    					String tempDump = new String(received[2]);
    					Cursor c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName, null);
    					
    					if(c.getCount() > 0)
    					{
    						c.moveToFirst();
    						do
    						{
    							//for each entry less than the nodeId send to origin and delete
    							fetchKey = c.getString(c.getColumnIndex(KEY_FIELD));
    							fetchValue = c.getString(c.getColumnIndex(VALUE_FIELD));
    							tempDump = tempDump + fetchKey + ":" + fetchValue + "#";
    						}while(c.moveToNext());
    					}
    					
    					
    					String[] globalQueryMessage = {"isGlobalDump",received[3],tempDump,received[3]};
    					
    					
    					try {
							new Thread(new ClientTask(globalQueryMessage)).start();
						} catch (Exception e) {
							Log.e("Exception","Exception in isGlobalDump");
							e.printStackTrace();
						}
    					
    					
    					
    					
    					
					}
    				else
    				{
    					
    					globalMessage += received[2];
    					Log.d("in notify","Reached notify with lockcount:"+lockCount);
    					synchronized (lock) {
    						lockCount = lockCount - 1;
    						if(lockCount==0)
    	            		lock.notify();
    						
    					}
    					Log.e("else","reached else");
    				
    				}
    			}
    			
    			
    			//LOGIC FOR JOIN RESPONSE
    			if(received[0].equalsIgnoreCase("isJoinResponse"))
    			{
    				Log.d("joinresponse","Join Response entered"+received[2]+received[3]);
    				
    				successorPort = received[2];
    				predecessorPort = received[3];
    				successorPortNodeId = String.valueOf(Integer.parseInt(successorPort)/2);
    				predecessorPortNodeId = String.valueOf(Integer.parseInt(predecessorPort)/2);
    				
    				
    				String[] update = {"isUpdate",successorPort,"P",myPort};
					
					
    				new Thread(new ClientTask(update)).start();
					
					
					
					String[] update2 = {"isUpdate",predecessorPort,"S",myPort};
					
					
					new Thread(new ClientTask(update2)).start();
					
					
					
					String[] partition = {"isPartition",successorPort,nodeId,myPort};
					
					
					new Thread(new ClientTask(partition)).start();
					
    				
    				
    			}
    			
    			//LOGIC FOR PARTITION - ALTERNATIVELY TRY SENDING CUROSR OBJECT
    			if(received[0].equalsIgnoreCase("isPartition"))
    			{
    				String ownerId = received[2];
    				String fetchOwner = "",totalMessage="";
    				Log.d("Oncreate","Partition entered");
    				String selection = "key="+"'"+ownerId+"'";
    				Cursor c = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName + " WHERE "+selection, null);
					if (c == null || c.getCount() == 0) {
						Log.e(TAG, "Result null");
						//throw new Exception();
					}
					
					if(c!=null&&c.getCount()>0)
					{
						c.moveToFirst();
						do
						{
							//for each entry less than the nodeId send to origin and delete
							fetchKey = c.getString(c.getColumnIndex(KEY_FIELD));
							fetchValue = c.getString(c.getColumnIndex(VALUE_FIELD));
							fetchOwner = c.getString(c.getColumnIndex(OWNER_FIELD));
							
							//if each key is lesser than mine and greater than predecessor then retain. Send all else.
							totalMessage = totalMessage +  fetchKey + "," + fetchValue + "," + fetchOwner + "~";
								
								
								/*try {
									mContentResolver.delete(mUri, fetchKey, null);
								} catch (Exception e) {
									Log.e(TAG, "Error in deleting from provider");
									e.printStackTrace();
								}*/
								
							
							
						}
						while(c.moveToNext());
						
						//String[] partitionResponseMessage = {"isPartitionResponse",received[3],genHash(fetchKey),fetchKey,fetchValue,fetchOwner};
						String[] partitionResponseMessage = {"isPartitionResponse",received[3],totalMessage};
						
						new Thread(new ClientTask(partitionResponseMessage)).start();
						
						
					}
					
					c.close();
    				
    			}
    			
    			//LOGiC FOR PARTITION RESPONSE
    			if(received[0].equalsIgnoreCase("isPartitionResponse"))
    			{
    				String totalMessage = received[2];

    				String[] keyValuePairs = totalMessage.split("~");
    				for(String temp:keyValuePairs)
    				{
    					try {
    						String[] pair = temp.split(",");
    						Log.v("message","string key-value is"+pair[0]+pair[1]);

    						ContentValues cv = new ContentValues();
    						cv.put(KEY_FIELD,   pair[0]);
    						cv.put(VALUE_FIELD, pair[1]);
    						cv.put(OWNER_FIELD, pair[2]);

    						try {
    							db.replace(dbHelper.tableName, null, cv);
    						} catch (Exception e) {
    							Log.d("Server","Inserting to provider error");
    							e.printStackTrace();
    						}



    					} catch (Exception e) {
    						Log.e("error","exception in mcursor");
    						e.printStackTrace();
    					}
    				}
    				
    				Log.d("PartitionResponse","Notifying the lock with count"+onCreateLockCount);
    				
    				
					
					synchronized (onCreateLock) {
						onCreateLockCount = onCreateLockCount - 1;
						if(onCreateLockCount==0)
	            		onCreateLock.notify();
						
					}



    			}
    			
    			//LOGIC FOR UPDATE
    			if(received[0].equalsIgnoreCase("isUpdate"))
    			{
    				if(received[2].equalsIgnoreCase("P"))
    				{
    					predecessorPort = received[3];
    					
        				predecessorPortNodeId = String.valueOf(Integer.parseInt(predecessorPort)/2);
        				
    				}
    				else if(received[2].equalsIgnoreCase("S"))
    				{
    					successorPort = received[3];
    					successorPortNodeId = String.valueOf(Integer.parseInt(successorPort)/2);
    				}
    			}
    			
    			//LOGIC FOR DELETE
    			if(received[0].equalsIgnoreCase("isDelete"))
    			{
    				if(!received[2].equalsIgnoreCase(myPort))
    				{
    					int deletedRows = db.delete(dbHelper.tableName, "1", null);
    				}
    			}
    			
    			//LOGIC FOR QUERY RESPONSE
    			if(received[0].equalsIgnoreCase("isQueryResponse"))
    			{
    				queryValue = received[2];
    				
					synchronized (lock2) {
						
	            		lock2.notify();
						
					}
    			}
    			
    			//If incoming request is a query
    			//HANDLE EDGE CASE!!!!! - HANDLED IN IF
    			if(received[0].equalsIgnoreCase("isQuery"))
    			{
    				//if(!received[3].equalsIgnoreCase(myPort))
    				//{	
    					String realKey = received[2];
    					Log.e("isQuery","entered if with key"+realKey);
    					String selection = "key="+"'"+realKey+"'";
    					Cursor resultCursor = db.rawQuery("SELECT key,value FROM " + dbHelper.tableName +" WHERE "+selection, null);
    			    	//Cursor resultCursor = db.query(dbHelper.tableName, null, selection, null, null, null, null);
    					if(resultCursor.moveToFirst())
    					{	
    			    	if(resultCursor.getCount() > 0 && resultCursor != null) 
    			    	{
    						
    					fetchValue = resultCursor.getString(resultCursor.getColumnIndex(VALUE_FIELD));
    					
    					
    					
    					String[] queryResponseMessage = {"isQueryResponse",received[3],fetchValue};
    					
    					//sending to query originator
    					
    					try {
							new Thread(new ClientTask(queryResponseMessage)).start();
						} catch (Exception e) {
							Log.e("Exception","Exception returning queryResponse");
							e.printStackTrace();
						}
    					}
    					}
    					
    				//}
    					
    					
/*    					String receivedHashKey = received[3];
    					String realKey = received[2];
    					//compare if node is correct
    					if( ((receivedHashKey.compareTo(nodeId)<=0) && (receivedHashKey.compareTo(genHash(predecessorPortNodeId))>0))
    							|| ( (receivedHashKey.compareTo(nodeId)>0) && (genHash(predecessorPortNodeId).compareTo(nodeId)>0) && receivedHashKey.compareTo(genHash(predecessorPortNodeId))>0) 
    							|| ((receivedHashKey.compareTo(nodeId)<=0) && (receivedHashKey.compareTo(genHash(predecessorPortNodeId))<0) &&(genHash(predecessorPortNodeId).compareTo(nodeId)>0)))
    					{


    						Log.e("isQuery","entered if with key"+realKey);
    						String selection = "key="+"'"+realKey+"'";
    						Cursor resultCursor = db.query(dbHelper.tableName, null, selection, null, null, null, null);
    						if(resultCursor.moveToFirst())
    						{	
    							if(resultCursor.getCount() > 0 && resultCursor != null) 
    							{

    								fetchValue = resultCursor.getString(resultCursor.getColumnIndex(VALUE_FIELD));



    								String[] queryResponseMessage = {"isQueryResponse",received[4],fetchValue};

    								//sending to query originator

    								new Thread(new ClientTask(queryResponseMessage)).start();
    							}
    						}
    						else
    						{

    							String[] queryString = {"isQuery",successorPort,received[2],received[3],received[4]};

    							new Thread(new ClientTask(queryString)).start();
    						}

    					}
    					else
    					{

    						String[] queryString = {"isQuery",successorPort,received[2],received[3],received[4]};

    						new Thread(new ClientTask(queryString)).start();
    					}
    				}
    				else
    				{
    					Log.e("isQuery","reached HHEERREE");
    				}*/


    			}




            	}
            } 
            catch(Exception e){
            	Log.e(TAG, "This is the exception"+e.getMessage()+" at "+myPort);
            	e.printStackTrace();
            }



        }

        
    }
    
    
    
    

private class ClientTask implements Runnable {
	
	String[] msgs;
	
	ClientTask(String[] msgs)
	{
		this.msgs = msgs; 
	}
	Socket socket = new Socket();
	ObjectOutputStream outputStream;

    @Override
    public void run() {
        try {
        	Log.e(TAG, "Client entered at"+myPort+" with message "+msgs[0]+ " and port "+msgs[1]);
            
        	
          /*  Socket socket=null;
				socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
				        Integer.parseInt(msgs[1]));
				socket.setSoTimeout(300);*/
        	
        	
        	socket.connect(new InetSocketAddress(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),Integer.parseInt(msgs[1])), 500);
			
            Log.e("clienttask","socket created");
            
           
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            Log.e("clienttask","Outputstream created"+msgs[0]+msgs[1]);
           
            outputStream.writeObject(msgs);
            outputStream.flush();
            Log.e("clienttask","sent to "+msgs[1]);
            outputStream.close();
            socket.close();
            Log.e(TAG, "Client done");
            
        } catch (UnknownHostException e) {
            Log.e(TAG, "ClientTask UnknownHostException");
        } catch (IOException e) {
            Log.e(TAG, "ClientTask socket IOException");
            try {
				outputStream.close();
				socket.close();
			} catch (IOException e1) {
				Log.e(TAG, "Exception while closing");
				e1.printStackTrace();
			}
            //e.printStackTrace();
            throw new RuntimeException("Socket timed out");
        }catch(Exception e)
        {
        	Log.e(TAG, "Rare exception");
        }

        //return null;
    }
}

}//End of content provider class