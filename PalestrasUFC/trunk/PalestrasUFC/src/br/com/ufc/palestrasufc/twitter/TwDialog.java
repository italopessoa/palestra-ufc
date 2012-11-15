/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.ufc.palestrasufc.twitter;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twitter.android.Twitter.TwitterDialogListener;

public class TwDialog extends Dialog {
	public static final String TAG = "twitter";

	static final int TW_BLUE = 0xFFC0DEED;
	static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
	static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;

	private int mIcon;
	private String mUrl;
	private TwitterDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private FrameLayout mContent;

	private Handler mHandler;

	private CommonsHttpOAuthConsumer mConsumer;
	private CommonsHttpOAuthProvider mProvider;

	private ImageView mCrossImage;

	public TwDialog(Context context, CommonsHttpOAuthProvider provider, CommonsHttpOAuthConsumer consumer,
			TwitterDialogListener listener, int icon) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mProvider = provider;
		mConsumer = consumer;
		mListener = listener;
		mIcon = icon;
		mHandler = new Handler();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContent = new FrameLayout(getContext());
		mContent.setBackgroundColor(Color.TRANSPARENT);

		createCrossImage();
		int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
		setUpWebView(crossWidth / 2);

		mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addContentView(mContent, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		retrieveRequestToken();
	}

	private void createCrossImage() {
		mCrossImage = new ImageView(getContext());
		mCrossImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onCancel();
				TwDialog.this.dismiss();
			}
		});

		mCrossImage.setImageDrawable(getContext().getResources().getDrawable(mIcon));
		mCrossImage.setVisibility(View.INVISIBLE);
	}

	private void setUpWebView(int margin) {
		LinearLayout webViewContainer = new LinearLayout(getContext());
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new TwDialog.TwWebViewClient());
		mWebView.setLayoutParams(FILL);
		mWebView.setVisibility(View.INVISIBLE);
		webViewContainer.setPadding(margin, margin, margin, margin);
		webViewContainer.addView(mWebView);
		mContent.addView(webViewContainer);
	}

	private void retrieveRequestToken() {
		mSpinner = ProgressDialog.show(this.getContext(), null, "Loading...", false, false);
		new Thread() {
			@Override
			public void run() {
				try {
					mUrl = mProvider.retrieveRequestToken(mConsumer, Twitter.CALLBACK_URI);
					mWebView.loadUrl(mUrl);
				} catch (OAuthMessageSignerException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthNotAuthorizedException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthExpectationFailedException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, Twitter.OAUTH_REQUEST_TOKEN));
				} catch (OAuthCommunicationException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, Twitter.OAUTH_REQUEST_TOKEN));
				}
			}
		}.start();
	}

	private void retrieveAccessToken(final String url) {
		mSpinner = ProgressDialog.show(this.getContext(), null, "Loading...", false, false);
		new Thread() {
			@Override
			public void run() {
				Uri uri = Uri.parse(url);
				String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
				final Bundle values = new Bundle();
				try {
					mProvider.retrieveAccessToken(mConsumer, verifier);
					values.putString(Twitter.ACCESS_TOKEN, mConsumer.getToken());
					values.putString(Twitter.SECRET_TOKEN, mConsumer.getTokenSecret());
					mListener.onComplete(values);
				} catch (OAuthMessageSignerException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, verifier));
				} catch (OAuthNotAuthorizedException e) {
					// mListener.onTwitterError(new TwitterError(e.getMessage()));
				} catch (OAuthExpectationFailedException e) {
					// mListener.onTwitterError(new TwitterError(e.getMessage()));
				} catch (OAuthCommunicationException e) {
					// mListener.onError(new DialogError(e.getMessage(), -1, verifier));
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mSpinner.dismiss();
						TwDialog.this.dismiss();
					}
				});
			}
		}.start();
	}

	private class TwWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirect URL: " + url);
			if (url.startsWith(Twitter.CALLBACK_URI)) {
				retrieveAccessToken(url);
				return true;
			} else if (url.startsWith(Twitter.CANCEL_URI)) {
				mListener.onCancel();
				TwDialog.this.dismiss();
				return true;
			}
			// launch non-dialog URLs in a full browser
			getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// mListener.onError(new DialogError(description, errorCode, failingUrl));
			TwDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "WebView loading URL: " + url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mSpinner.dismiss();
			mContent.setBackgroundColor(Color.TRANSPARENT);
			mWebView.setVisibility(View.VISIBLE);
			mCrossImage.setVisibility(View.VISIBLE);
		}

	}
}
