package auth;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ConfigBuilder {
	private static Configuration config;
	
	private static final String API_KEY = "64tRKOrgjpLkn226f1qGZtX1L";
	private static final String API_SECRET = "SSHFN9RfUSnCtnbzwtrmyaDdx0MLzfU60ryKSFDrlwGmKjz9Mi";
	private static final String ACCESS_TOKEN = "113590186-EzfAlckvuNH661Wlifggk38T8glDc8okjrZ2yQui";
	private static final String ACCESS_SECRET = "8KfN9TKR4bNN4q9OWCMJ7ne1eRohSlLUZojobQ4NiRsOU";
	
	static {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(API_KEY);
		cb.setOAuthConsumerSecret(API_SECRET);
		cb.setOAuthAccessToken(ACCESS_TOKEN);
		cb.setOAuthAccessTokenSecret(ACCESS_SECRET);
		config = cb.build();
	}
	
	public static Configuration getConfig() {
		return config;
	}
}
