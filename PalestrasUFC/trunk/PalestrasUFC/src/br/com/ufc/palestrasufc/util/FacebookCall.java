package br.com.ufc.palestrasufc.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;

public class FacebookCall {

	// Facebook constants
	private static final String API_KEY = "372276436192533";
	private static final String[] PERMISSIONS = new String[] { "publish_stream", "read_stream", "offline_access" };
	public static final String TOKEN = "access_token";
	public static final String EXPIRES = "expires_in";
	public static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private Activity activity;
	private ProgressDialog pd;
	private WebView likeButton;

	public FacebookCall(Activity activity) {
		facebook = new Facebook(API_KEY);
//		restoreCredentials(facebook);

		this.activity = activity;
	}

	public void loadLikeButton(final String messageToPost, final DialogListener dialogListener) {
		pd = ProgressDialog.show(activity, null, "Aguarde...", false, false);

		new Thread() {
			@Override
			public void run() {
				String url = "http://www.facebook.com/plugins/like.php?" + "href=" + messageToPost + "&"
						+ "layout=standard&" + "show_faces=0&";

				DisplayMetrics dm = new DisplayMetrics();
				activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

				if (dm.density == 1.5) {
					likeButton.setInitialScale(175);
					url = url + "width=100&height=50&";
				} else if (dm.density == 1) {
					likeButton.setInitialScale(140);
					url = url + "width=100&height=50&";
				} else {
					likeButton.setInitialScale(103);
					url = url + "width=100&height=50&";
				}

				url = url + "locale=pt_BR&colorscheme=light";

				likeButton.setWebViewClient(new WebViewClient() {
					// you tell the webclient you want to catch when a url is about to load
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						if (url.contains("campaign")) {
							Functions
									.showToast(activity,
											"Este sistema não efetua cadastro no Facebook. Para isso você pode usar o navegador do seu aparelho.");
							return true;
						}
						if (url.contains("connect")) {
							if (!facebook.isSessionValid()) {
								loginAndLike(dialogListener);
								return true;
							} else {
								return super.shouldOverrideUrlLoading(view, url);
							}
						} else {
							return super.shouldOverrideUrlLoading(view, url);
						}
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						pd.dismiss();
					}
				});

				likeButton.loadUrl(url);
			}
		}.start();
	}

	public void facebookLike(DialogListener dialogListener) {
		if (!facebook.isSessionValid()) {
			loginAndLike(dialogListener);
		} else {
			// TODO deve usar o webview ou webviewclient para continuar o fluxo de like
		}
	}

	public void facebookShare(String messageToPost, DialogListener dialogListener) {
		if (!facebook.isSessionValid()) {
			loginAndPostToWall(dialogListener);
		} else {
			postToWall(messageToPost);
		}
	}

	public void loginAndLike(DialogListener dialogListener) {
		facebook.authorize(activity, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, dialogListener);
	}

	public void loginAndPostToWall(DialogListener dialogListener) {
		facebook.authorize(activity, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, dialogListener);
	}

	public void postToWall(String message) {
		Bundle parameters = new Bundle();
		parameters.putString("message", message);
		parameters.putString("description", "topic share");
		try {
			facebook.request("me");
			String response = facebook.request("me/feed", parameters, "POST");
			Log.d("Tests", "got response: " + response);
			if (response == null || response.equals("") || response.equals("false")) {
				Functions.showToast(activity, "Falha ao postar! Favor tentar novamente!");
			} else {
				Functions.showToast(activity, "O imóvel foi compartilhado com sua conta!");
			}
		} catch (Exception e) {
			Functions.showToast(activity, "Falha ao postar! Favor tentar novamente!");
			e.printStackTrace();
		}
	}

	public String getAccessToken() {
		return facebook.getAccessToken();
	}

	public long getAccessExpires() {
		return facebook.getAccessExpires();
	}
	
}
