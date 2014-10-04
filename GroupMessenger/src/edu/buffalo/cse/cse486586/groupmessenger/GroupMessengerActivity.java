package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//import edu.buffalo.cse.cse486586.simplemessenger.SimpleMessengerActivity.ClientTask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
//import edu.buffalo.cse.cse486586.simplemessenger.SimpleMessengerActivity.ServerTask;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {
	
	//Variables for db
	//final static String columns[] = {"_id","sequenceNumber","messageId","message","avdNumber"};
	static SQLiteDatabase db = null;
	static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    
    String myPort;
    String avdName;
    int sequencerStatus=0;
    static int sequenceNumber=0;
    
    static final int SERVER_PORT = 10000;
    
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        
        //Code for adding dbhelper class
        
        GroupMessengerDbHelper dbHelper = new GroupMessengerDbHelper(this.getApplicationContext());
        db = dbHelper.getWritableDatabase();
        
        
        Log.e(TAG,"Reached here1"+myPort);
        
        //Code from PA1 from PA1 setting up
        
        try {
			TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
			String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
			myPort = String.valueOf((Integer.parseInt(portStr) * 2));
			if(Integer.parseInt(myPort)==11108)
			{
				sequencerStatus = 1;
				Log.e(TAG, "This is sequencer");
			}
			else
			{
				sequencerStatus = 0;
			}
		} catch (Exception e1) {
			Log.e(TAG,"Reached here Exception");
			e1.printStackTrace();
		}
        /* Getting AvdName*/
        avdName = getAvdName(myPort);
        
        try {
			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"Reached here Exception");
			e.printStackTrace();
		}
        
        

        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        final EditText editText = (EditText) findViewById(R.id.editText1);
        
        Log.e(TAG,"Reached here3 in"+myPort);
        
        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));
        
        /*
         * TODO: You need to register and implement an OnClickListener for the "Send" button.
         * In your implementation you need to get the message from the input box (EditText)
         * and send it to other AVDs in a total-causal order.
         */
        
        findViewById(R.id.button4).setOnClickListener(new OnSendClickListener(editText,tv));
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }
    
    public String getAvdName(String port)
    {
    	if(port.equalsIgnoreCase(REMOTE_PORT0))
    		return "AVD0";
    	if(port.equalsIgnoreCase(REMOTE_PORT1))
    		return "AVD1";
    	if(port.equalsIgnoreCase(REMOTE_PORT2))
    		return "AVD2";
    	if(port.equalsIgnoreCase(REMOTE_PORT3))
    		return "AVD3";
    	if(port.equalsIgnoreCase(REMOTE_PORT4))
    		return "AVD4";
    	return null;
    }
    
    /*Server Class*/
    
    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            DataInputStream inputStream = null;
            Socket clientConnection = null;
            try {
            	while(true)
            	{	
    			clientConnection = serverSocket.accept();
    			inputStream = new DataInputStream(clientConnection.getInputStream());
    			//while(true)
    			//{
    				/*byte[] buffer = new byte[128];
    				String newMessage="x";
    				
    				//int readBytes = inputStream.read(buffer);
    				
    				Log.e(TAG, "bytes read"+String.valueOf(readBytes));
    				if(readBytes!=0)
    				newMessage = new String(buffer);
    				Log.e(TAG, "after fetch"+newMessage);*/
    				
    			String newMessage = inputStream.readUTF();
    			Log.e(TAG, "after fetch"+newMessage);
    			publishProgress(newMessage);
    				
    			}
    		} catch (IOException e) {
    			Log.e(TAG, "client connection failed");
    			e.printStackTrace();
    		}
            catch(Exception e){
            	Log.e(TAG, "This is the exception"+e.getMessage());
            }
            
            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */
            return null;
        }

        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            TextView text = (TextView) findViewById(R.id.textView1);
            Log.e(TAG, "Entered Server1"+myPort);
            ContentResolver resolver = getContentResolver(); 
            ContentValues values = new ContentValues();
            String storeStr,strReceivedNew;
            char[] tempStr;
            Log.e(TAG, "Entered Server"+myPort);
            Uri uri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger.provider");
            
            //if(sequencerStatus == 1 && sequenceNumber ==0)
            if(sequencerStatus == 1)
            {
            	Log.e(TAG, "Entered sequencer specific Code with message"+strReceived);
            	
            	//tempStr = strReceived.toCharArray();
            	if(strReceived.charAt(4)!='~')
            	{
            			
            	strReceivedNew = strReceived.substring(0,4)+"~"+strReceived.substring(5);
            	strReceivedNew = strReceivedNew + "=" + String.valueOf(sequenceNumber);
            	Log.e(TAG, "Before async task new string is "+strReceivedNew);
            	new ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strReceivedNew);
            	storeStr = strReceivedNew.substring(5, strReceivedNew.indexOf("="));
            	Log.e(TAG,"In sequencer message stored is:"+storeStr);
            	values.put("key", String.valueOf(sequenceNumber));
            	values.put("value",storeStr);
            	resolver.insert(uri, values);
            	sequenceNumber+=1; //incrementing Global Sequence number if sequencer
            	}
            	
            }
            else
            {
            	if(strReceived.charAt(4)=='~')
            	{
            		//Order Message
            		int index = strReceived.indexOf("=");
            		String sequence = strReceived.substring(index+1, strReceived.length());
            		int sNum =  Integer.parseInt(sequence);
            		storeStr = strReceived.substring(5, index);
                	Log.e(TAG,"Not in sequencer message stored is:"+storeStr);
                	values.put("key", String.valueOf(sNum));
                	values.put("value",storeStr);
                	resolver.insert(uri, values);
            		
            	}
            }
            
           
            
            /*Inserting into Database*/
            
            
            
            text.append(strReceived + "\t\n");
            /*TextView localTextView = (TextView) findViewById(R.id.local_text_display);
            localTextView.append("\n");*/
            
            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             * 
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */
            
            /*String filename = "SimpleMessengerOutput";
            String string = strReceived + "\n";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                Log.e(TAG, "File write failed");
            }*/

            return;
        }
    }

    /* Client Code*/
    
    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            try {
            	Log.e(TAG, "Client entered at"+myPort+" with message "+msgs[0]);
                /*String remotePort = REMOTE_PORT0;
                if (msgs[1].equals(REMOTE_PORT0))
                    remotePort = REMOTE_PORT1;
                //else if()
                 */
            	//Log.e(TAG, "Client entered,message is"+msgs[1]+""+msgs[0]);
            	//String remotePort[] = {"11108","11112","11116","11120","11124"};
            	//String temp;
            	/*Log.e(TAG, remotePort[0]+" "+remotePort[1]);
            	//remotePort[0]=REMOTE_PORT0;
            	for(int i=0; i<5;i++)
            	{
            		if(remotePort[i].equalsIgnoreCase(msgs[1]))
            		{
            			temp = new String(remotePort[0]);
            			remotePort[0]=remotePort[i];
            			remotePort[i]=temp;
            		}	
            	}
            	Log.e(TAG,"mutability not issue");*/
            	
            	
            	Socket socket1 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),11108);
            	Socket socket2 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),11112);
            	Socket socket3 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),11116);
            	//Socket socket4 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),11120);
            	//Socket socket5 = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),11124);
                
            	
                String msgToSend = msgs[0];
                Log.e(TAG, "Client entered at"+myPort+" with message "+msgToSend);
                /*
                 * TODO: Fill in your client code that sends out a message.
                 */
                //DataOutputStream outputStream[]=new DataOutputStream[4];
                
                
                DataOutputStream outputStream1 = new DataOutputStream(socket1.getOutputStream());
                DataOutputStream outputStream2 = new DataOutputStream(socket2.getOutputStream());
                DataOutputStream outputStream3 = new DataOutputStream(socket3.getOutputStream());
                //DataOutputStream outputStream4 = new DataOutputStream(socket4.getOutputStream());
                //DataOutputStream outputStream5 = new DataOutputStream(socket5.getOutputStream());
                
                //while(!msgToSend.equalsIgnoreCase("EXIT"))
                //{
                //byte[] buffer = msgToSend.getBytes();
                Log.e(TAG, "Before fetch"+msgToSend);
                
                
                
                outputStream1.writeUTF(msgToSend);
                outputStream2.writeUTF(msgToSend);
                outputStream3.writeUTF(msgToSend);
                
                //outputStream1.write(buffer);
                //outputStream2.write(buffer);
                //outputStream3.write(buffer);
                //outputStream4.write(buffer);
                //outputStream5.write(buffer);
                
                
                outputStream1.close();
                outputStream2.close();
                outputStream3.close();
                //outputStream4.close();
                //outputStream5.close();
                
                socket1.close();
                socket2.close();
                socket3.close();
                //socket4.close();
                //socket5.close();
                
                
                Log.e(TAG, "Client done");
                //msgToSend = processMessage();
                //}
                
            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
            }

            return null;
        }
    }

    private class OnSendClickListener implements OnClickListener
    {
    	String str = "randomData";
    	EditText text;
    	TextView view;
    	
    	public OnSendClickListener(EditText text, TextView view)
    	{
    		this.text = text;
    		this.view = view;
    		
    		
    		
    	}

    	@Override
    	public void onClick(View arg0) {
    		// TODO Auto-generated method stub
    		Log.e(TAG, "Clicked on "+myPort);
    		str = avdName+":"+text.getText().toString()+"\n";
    		text.setText("");
    		//view.append(str);
    		new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, str, myPort);
    		
    	}
    	
    }


}

