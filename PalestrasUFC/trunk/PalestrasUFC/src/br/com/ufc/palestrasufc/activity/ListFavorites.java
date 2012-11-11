package br.com.ufc.palestrasufc.activity;

import java.util.List;

import android.widget.TextView;
import br.com.ufc.palestrasufc.db.DatabaseManager;
import br.com.ufc.palestrasufc.model.Lecture;

public class ListFavorites extends ListLectures {

	@Override
	protected void onStart() {
		super.onStart();
		((TextView) findViewById(R.id.actionBarTitle)).setText(R.string.favorites);
	}
	
	@Override
	protected List<Lecture> retrieveItems() {
		return DatabaseManager.getInstance(getApplicationContext()).listLecturesFavorites();
	}
	
}
