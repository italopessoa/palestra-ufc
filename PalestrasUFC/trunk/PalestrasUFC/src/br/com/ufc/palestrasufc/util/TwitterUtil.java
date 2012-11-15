package br.com.ufc.palestrasufc.util;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import br.com.ufc.palestrasufc.twitter.Twitter;
import br.com.ufc.palestrasufc.twitter.Twitter.TwitterDialogListener;

import com.twitter.android.DialogError;


public class TwitterUtil {

	// Twitter constants
	private String CONSUMER_KEY = "nStjqMBoxbq1O9UYYCRoxg";
	private String CONSUMER_SECRET = "Pm8zSfHFmCFiDGCCGqdnhTThdMrscrdeBHvUr5tr4";
	private static final String ACESS_TOKEN = "access_token";
	private static final String SECRET_TOKEN = "secret_token";
	private static final String TWITTER_KEY = "twitter-credentials";
	
	private Twitter twitter;

	private Activity activity;

	private String messageToPost;

	public void twitterShare(View v) {
		Vibrator vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(30);

		if (!twitter.isSessionValid()) {
			loginAndTweet();
		} else {
			tweet(messageToPost);
		}
	}

	public void loginAndTweet() {
		twitter.authorize(activity, CONSUMER_KEY, CONSUMER_SECRET, new TwitterDialogListener() {
			
			@Override
			public void onTwitterError(com.twitter.android.TwitterError e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onComplete(Bundle values) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
			}
		});
	}

	public void tweet(String messageToTweet) {
		try {
			AccessToken a = new AccessToken(twitter.getAccessToken(), twitter.getSecretToken());
			twitter4j.Twitter t = new TwitterFactory().getInstance();
			t.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			t.setOAuthAccessToken(a);
			t.updateStatus(messageToTweet);

			new Handler().post(new Runnable() {
				@Override
				public void run() {
					Functions.showToast(activity, "O imóvel foi twitado com sucesso!");
				}
			});
		} catch (TwitterException e) {
			new Handler().post(new Runnable() {
				@Override
				public void run() {
					Functions.showToast(activity, "Este imóvel já foi twitado na sua conta!");
				}
			});
		}
	}

	public boolean saveCredentials(Twitter twitter) {
		Editor editor = activity.getSharedPreferences(TWITTER_KEY, Context.MODE_PRIVATE).edit();
		editor.putString(ACESS_TOKEN, twitter.getAccessToken());
		editor.putString(SECRET_TOKEN, twitter.getSecretToken());
		Log.i("Twitter", "access_token salved: " + twitter.getAccessToken());
		Log.i("Twitter", "secret_token salved: " + twitter.getSecretToken());
		return editor.commit();
	}

	public boolean restoreCredentials(Twitter twitter) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(TWITTER_KEY,
				Context.MODE_PRIVATE);
		twitter.setAccessToken(sharedPreferences.getString(ACESS_TOKEN, null));
		twitter.setSecretToken(sharedPreferences.getString(SECRET_TOKEN, null));
		Log.i("Twitter", "access_token: " + sharedPreferences.getString(ACESS_TOKEN, null));
		Log.i("Twitter", "secret_token: " + sharedPreferences.getString(SECRET_TOKEN, null));
		return twitter.isSessionValid();
	}

}
