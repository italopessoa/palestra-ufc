package br.com.ufc.palestrasufc.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import br.com.ufc.palestrasufc.model.Lecture;

import com.j256.ormlite.stmt.PreparedQuery;

public class DatabaseManager {

	private static DatabaseManager instance;
	private DatabaseHelper helper;

	private DatabaseManager(Context ctx) {
		this.helper = ConnectionFactory.getDatabaseHelper(ctx);
	}

	static public DatabaseManager getInstance(Context ctx) {
		if (null == DatabaseManager.instance) {
			DatabaseManager.instance = new DatabaseManager(ctx);
		}

		return DatabaseManager.instance;
	}

	public void insertLecture(Lecture lecture) {
		if (lecture != null) {
			try {
				getHelper().getLecturesDao().create(lecture);
			} catch (SQLException e) {
				Log.e("", "insertLecture", e);
			}
		}
	}

	public void insertOrUpdateLectures(Lecture lecture) {
		if (lecture != null) {
			try {
				getHelper().getLecturesDao().createOrUpdate(lecture);
			} catch (SQLException e) {
				Log.e("", "insertOrUpdateLectures", e);
			}
		}
	}

	public List<Lecture> listLecturesFavorites() {
		List<Lecture> lectures = new ArrayList<Lecture>();

		try {
			PreparedQuery<Lecture> query = getHelper().getLecturesDao()
					.queryBuilder().where().eq("favorite", true).prepare();
			List<Lecture> tmp = getHelper().getLecturesDao().query(query);
			for (Lecture lecture : tmp) {
				if (lecture.isFavorite()) {
					lectures.add(lecture);
				}
			}
		} catch (SQLException e) {
			Log.e("", "listLecturesFavorites", e);
		}
		return lectures;
	}

	public List<Lecture> listLecturesPaginated(int begin, int amount) {
		List<Lecture> reminders = listLectures();
		if (reminders.size() > amount) {
			return reminders.subList(begin, amount);
		}
		return reminders;
	}

	public List<Lecture> listLectures() {
		List<Lecture> reminders = null;

		try {
			reminders = getHelper().getLecturesDao().queryForAll();
		} catch (SQLException e) {
			Log.e("", "listLectures", e);
		}
		return reminders;
	}

	public Lecture getLectureByTitle(String s) {
		Lecture l = new Lecture();
		try {
			PreparedQuery<Lecture> query = getHelper().getLecturesDao()
					.queryBuilder().where().eq("title", s).prepare();
				l = getHelper().getLecturesDao().query(query).get(0);
				return l;
		} catch (SQLException e) {
			Log.e("", "listLecturesFavorites", e);
			return null;
		}catch (IndexOutOfBoundsException e) {
			Log.e("", "listLecturesFavorites", e);
			return null;
		}
	}

	public void clearTables() {
		Log.d("", "Limpando Tabelas");
		getHelper().clearTables();
	}

	private DatabaseHelper getHelper() {
		return this.helper;
	}

}
