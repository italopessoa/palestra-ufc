package br.com.ufc.palestrasufc.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class ActionBar extends Activity {
	
	public void onActionBarItemSelected(View v) {

		switch (v.getId()) {

		case R.id.applicationIcon:
		case R.id.actionBarTitle:
			Intent intent = new Intent(this, br.com.ufc.palestrasufc.activity.Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
		}

	}

}