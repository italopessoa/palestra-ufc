package br.com.ufc.palestrasufc.db;

import android.content.Context;

public class ConnectionFactory {

	private static DatabaseHelper helper;

	public static DatabaseHelper getDatabaseHelper(Context context) {
		if (ConnectionFactory.helper == null) {
			ConnectionFactory.helper = new DatabaseHelper(context);
		}
		return ConnectionFactory.helper;
	}

	private ConnectionFactory() {
	}

}
