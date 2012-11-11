package br.com.ufc.palestrasufc.util;

import android.app.Activity;
import android.widget.LinearLayout;
import br.com.ufc.palestrasufc.activity.R;
import br.com.ufc.palestrasufc.constants.Constants;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdMobUtil {

	public static void addBanner(Activity activity) {

		AdView adView = new AdView(activity, AdSize.BANNER, Constants.MY_AD_UNIT_ID);

		LinearLayout layout = (LinearLayout) activity.findViewById(R.id.admob);
		layout.addView(adView);

		adView.loadAd(getTestDeviceRequest());

	}
	
	protected static AdRequest getTestDeviceRequest() {
		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		adRequest.addTestDevice("TEST_DEVICE_ID");
		return adRequest;
	}

}
