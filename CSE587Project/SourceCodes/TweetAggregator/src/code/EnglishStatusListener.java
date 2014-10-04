package code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class EnglishStatusListener implements StatusListener{

	//private static final transient Logger LOG = LoggerFactory.getLogger(EnglishStatusListener.class);
	private long tweetCounter=0;
	private long fileCounter=0;
	private String tweetOutputFolder="";
	private String userDetailsOutputFolder;
	private BufferedWriter bufW_tweet,bufW_User;
	private String outputTweet;
	private String outputUser;
	public EnglishStatusListener()
	{
		tweetCounter=0;
		fileCounter=1;
		tweetOutputFolder="/home/subhranil/Data/Tweets/";
		userDetailsOutputFolder="/home/subhranil/Data/Users/";
		bufW_tweet=bufW_User=null;
		outputTweet="";
		outputUser="";
		
	}
	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Use log4j to write the Tweet Text to disk. Note that 
	 * we only collect Tweet Text content. If you want to collect
	 * additional data, the Status object has several methods including
	 * getUser(), getGeolocation(), and so on. Twitter4j has documentation on
	 * their website on these.
	 */
	@Override
	public void onStatus(Status status) {
		String tweetText = status.getText();
		if(isEnglish(tweetText))
		{
			tweetCounter++;
			boolean retValue=handleTweet(tweetText);
			String username=status.getUser().getScreenName();
			int fCount=status.getUser().getFollowersCount();
			handlerUser(username,fCount,retValue);
			if(tweetCounter%10000==0)
			{
				fileCounter++;
				System.out.println("tweetCounter: "+tweetCounter+" Changing file counter to "+fileCounter);
			}
				
			
			
			
			if(tweetCounter>=10000000)
				System.exit(0);
			//System.out.println(status.getText());
			//LOG.info(status.getText());
		}
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Our hack for filtering English tweets. While we could get the language of the user
	 * object returned by status.getUser(), people with an English language set still tweet
	 * in other languages. We simply check if the Tweet text contains non-ASCII characters;
	 * if it does, we do not collect it.
	 * @param tweetText
	 * @return true if tweetText contains no non-ASCII characters, false otherwise
	 */
	public static boolean isEnglish(String tweetText) {
		for(int i = 0;i < tweetText.length();i++) {
			int c = tweetText.charAt(i);
			if(c > 127) {
				return false;
			}
		}
		return true;
	}
	
	public boolean handleTweet(String tweet)
	{
		try 
		{
			outputTweet+=tweet+"\n";
			if(outputTweet.length()>1024 || tweetCounter%10000==0) //dump to file
			{
				String outputFile=tweetOutputFolder+"file_"+String.valueOf(fileCounter);
				bufW_tweet=new BufferedWriter(new FileWriter(new File(outputFile),true));
				bufW_tweet.write(outputTweet);
				bufW_tweet.close();
				outputTweet="";
				System.gc();
				return true;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public void handlerUser(String username, int followerCount,boolean shouldDump)
	{
		try 
		{
			outputUser+=username+"\t"+String.valueOf(followerCount)+"\n";
			if(shouldDump==true) //dump to file
			{
				String outputFile=userDetailsOutputFolder+"file_"+String.valueOf(fileCounter);
				bufW_User=new BufferedWriter(new FileWriter(new File(outputFile),true));
				bufW_User.write(outputUser);
				bufW_User.close();
				outputUser="";
				System.gc();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
