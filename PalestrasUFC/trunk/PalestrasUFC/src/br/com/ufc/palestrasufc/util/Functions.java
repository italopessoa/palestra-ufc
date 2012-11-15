package br.com.ufc.palestrasufc.util;

import android.content.Context;
import android.widget.Toast;

public class Functions {

	public static void showToast(Context context, Object message) {
		Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
	}

}
