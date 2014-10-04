package code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import auth.ConfigBuilder;


public class Driver {
	
	private static final transient Logger LOG = LoggerFactory.getLogger(Driver.class);
	
	public static void main(String[] args)
	{
//		LOG.info("Initializing...");
//		LOG.debug("Creating Listener...");
//		EnglishStatusListener listener = new EnglishStatusListener();
//		LOG.debug("Creating Stream...");
//	    TwitterStream twitterStream = new TwitterStreamFactory(ConfigBuilder.getConfig()).getInstance();
//	    twitterStream.addListener(listener);
//	    LOG.info("Initialization Complete.");
//	    twitterStream.sample();
		
		//Posting a tweet
//		Twitter twit = new TwitterFactory(ConfigBuilder.getConfig()).getInstance();
//		try {
//		Status status = twit.updateStatus("Testing twitter4j");
//	    System.out.println("Successfully updated the status to [" + status.getText() + "].");
//	    }
//		catch(TwitterException e)
//		{
//			e.printStackTrace();
//		}
		
		
		LOG.info("Initializing...");
		LOG.debug("Creating Listener...");
		EnglishStatusListener listener = new EnglishStatusListener();
		LOG.debug("Creating Stream...");
	    TwitterStream twitterStream = new TwitterStreamFactory(ConfigBuilder.getConfig()).getInstance();
	    twitterStream.addListener(listener);
	    LOG.info("Initialization Complete.");
	    twitterStream.sample();
		
		
	}

}
