package br.com.ufc.palestrasufc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.ufc.palestrasufc.model.Lecture;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "lectures.sqlite";
	private static final int DATABASE_VERSION = 1;

	private Dao<Lecture, Integer> lecturesDao = null;

	public DatabaseHelper(Context context) {
		super(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
	}

	public void clearTables() {
		try {
			TableUtils.clearTable(getConnectionSource(), Lecture.class);
		} catch (SQLException e) {
			Log.e("", "Can't clear databases", e);
		} catch (java.sql.SQLException e) {
			Log.e("", "Can't create database", e);
		}
	}

	public Dao<Lecture, Integer> getLecturesDao() {
		if (null == this.lecturesDao) {
			try {
				this.lecturesDao = getDao(Lecture.class);
			} catch (SQLException e) {
				Log.e("", "Can't create DAO", e);
			} catch (java.sql.SQLException e) {
				Log.e("", "Can't create database", e);
			}
		}
		return this.lecturesDao;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			Log.d("", "Criando tabelas.");
			TableUtils.createTableIfNotExists(connectionSource, Lecture.class);
			Log.d("", "Tabelas criadas.");
		} catch (SQLException e) {
			Log.e("", "Can't create database", e);
		} catch (java.sql.SQLException e) {
			Log.e("", "Can't create database", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			List<String> allSql = new ArrayList<String>();
			switch (oldVersion) {
				case 1:
					// allSql.add("alter table AdData add column `new_col` VARCHAR");
					// allSql.add("alter table AdData add column `new_col2` VARCHAR");
			}
			for (String sql : allSql) {
				db.execSQL(sql);
			}
		} catch (SQLException e) {
			Log.e("", "exception during onUpgrade", e);
		}
	}

}
