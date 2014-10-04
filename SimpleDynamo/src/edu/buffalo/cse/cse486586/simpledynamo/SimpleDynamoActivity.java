package edu.buffalo.cse.cse486586.simpledynamo;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SimpleDynamoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_dynamo);
    
		TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.button3).setOnClickListener(
                new OnTestClickListener(tv, getContentResolver()));
        
        findViewById(R.id.button1).setOnClickListener(new lDumpListener(tv, getContentResolver()));
        findViewById(R.id.button2).setOnClickListener(new gDumpListener(tv, getContentResolver()));
	}
	
	public Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}
	
    class lDumpListener implements OnClickListener
    {
    	private final TextView mTextView;
    	private final ContentResolver mContentResolver;
    	private final Uri mUri;
    	private static final String KEY_FIELD = "key";
    	private static final String VALUE_FIELD = "value";
    	
    	public lDumpListener(TextView t, ContentResolver c)
    	{
    		mTextView = t;
    		mContentResolver = c;
    		mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
    	}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			localQuery();
			
		}
		protected void localQuery()
		{
			try {
				String fetchKey,fetchValue;
				Cursor c = mContentResolver.query(mUri, null, "@", null, null);
				if(c!=null)
				{
					c.moveToFirst();
					do
					{
						fetchKey = c.getString(c.getColumnIndex(KEY_FIELD));
						fetchValue = c.getString(c.getColumnIndex(VALUE_FIELD));
						mTextView.append(fetchKey);
						mTextView.append(fetchValue);
					}
					while(c.moveToNext());
					
				}
				c.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Error", e.toString());
			}
		}
    	
    }
	
	class gDumpListener implements OnClickListener
    {
    	private final TextView mTextView;
    	private final ContentResolver mContentResolver;
    	private final Uri mUri;
    	private static final String KEY_FIELD = "key";
    	private static final String VALUE_FIELD = "value";
    	
    	public gDumpListener(TextView t, ContentResolver c)
    	{
    		mTextView = t;
    		mContentResolver = c;
    		mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
    	}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			localQuery();
			
		}
		protected void localQuery()
		{
			try {
				String fetchKey,fetchValue;
				Cursor c = mContentResolver.query(mUri, null, "*", null, null);
				Log.d("in *","The value of * cursor is");
				if(c!=null)
				{
					c.moveToFirst();
					do
					{
						fetchKey = c.getString(c.getColumnIndex(KEY_FIELD));
						fetchValue = c.getString(c.getColumnIndex(VALUE_FIELD));
						mTextView.append(fetchKey);
						mTextView.append(fetchValue);
					}
					while(c.moveToNext());
					
				}
				c.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Error", e.toString());
			}
		}
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.simple_dynamo, menu);
		return true;
	}

}
