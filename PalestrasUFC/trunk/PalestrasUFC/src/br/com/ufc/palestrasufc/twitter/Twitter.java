package br.com.ufc.palestrasufc.twitter;

import java.io.IOException;
import java.net.MalformedURLException;

import com.twitter.android.DialogError;
import com.twitter.android.TwitterError;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

public class Twitter {
	public static final String TAG = "twitter";

	public static final String CALLBACK_URI = "twitter://callback";
	public static final String CANCEL_URI = "twitter://cancel";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String SECRET_TOKEN = "secret_token";

	public static final String REQUEST = "request";
	public static final String AUTHORIZE = "authorize";

	protected static String REQUEST_ENDPOINT = "https://api.twitter.com/1";

	public static final int FORCE_DIALOG_AUTH = -1;
	protected static String OAUTH_REQUEST_TOKEN = "https://api.twitter.com/oauth/request_token";
	protected static String OAUTH_ACCESS_TOKEN = "https://api.twitter.com/oauth/access_token";
	protected static String OAUTH_AUTHORIZE = "https://api.twitter.com/oauth/authorize";

	private String mAccessToken = null;
	private String mSecretToken = null;

	private int mIcon;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private CommonsHttpOAuthProvider mHttpOauthProvider;

	public Twitter(int icon) {
		mIcon = icon;
	}

	public void authorize(Context ctx, String consumerKey, String consumerSecret, final TwitterDialogListener listener) {
		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		mHttpOauthProvider = new CommonsHttpOAuthProvider(OAUTH_REQUEST_TOKEN, OAUTH_ACCESS_TOKEN, OAUTH_AUTHORIZE);
		CookieSyncManager.createInstance(ctx);
		dialog(ctx, new TwitterDialogListener() {
			
			@Override
			public void onTwitterError(TwitterError e) {
				Log.d(TAG, "Login failed: " + e);
				listener.onTwitterError(e);
			}
			
			@Override
			public void onError(DialogError e) {
				Log.d(TAG, "Login failed: " + e);
				listener.onError(e);
			}
			
			@Override
			public void onComplete(Bundle values) {
				CookieSyncManager.getInstance().sync();
				setAccessToken(values.getString(ACCESS_TOKEN));
				setSecretToken(values.getString(SECRET_TOKEN));
				if (isSessionValid()) {
					Log.d(TAG, "token " + getAccessToken() + " " + getSecretToken());
					listener.onComplete(values);
				} else {
					onTwitterError(new TwitterError("failed to receive oauth token"));
				}
			}
			
			@Override
			public void onCancel() {
				Log.d(TAG, "Login cancelled");
				listener.onCancel();
			}
		});
		
	}

	public String logout(Context context) throws MalformedURLException, IOException {
		return "true";
	}

	public void dialog(final Context ctx, final TwitterDialogListener listener) {
		if (ctx.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Util.showAlert(ctx, "Error", "Application requires permission to access the Internet");
			return;
		}
		new TwDialog(ctx, mHttpOauthProvider, mHttpOauthConsumer, listener, mIcon).show();
	}

	public boolean isSessionValid() {
		return getAccessToken() != null && getSecretToken() != null;
	}

	public String getAccessToken() {
		return mAccessToken;
	}

	public void setAccessToken(String accessToken) {
		mAccessToken = accessToken;
	}

	public String getSecretToken() {
		return mSecretToken;
	}

	public void setSecretToken(String secretToken) {
		mSecretToken = secretToken;
	}

	public static interface TwitterDialogListener extends com.twitter.android.Twitter.TwitterDialogListener{
		public void onComplete(Bundle values);

		public void onTwitterError(TwitterError e);

		public void onError(DialogError e);

		public void onCancel();
	}
}
