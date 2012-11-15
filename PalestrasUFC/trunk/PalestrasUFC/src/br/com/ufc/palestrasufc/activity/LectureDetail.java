package br.com.ufc.palestrasufc.activity;

import java.sql.SQLException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.ufc.palestrasufc.db.DatabaseManager;
import br.com.ufc.palestrasufc.model.Lecture;
import br.com.ufc.palestrasufc.util.AdMobUtil;
import br.com.ufc.palestrasufc.util.AplicationContext;
import br.com.ufc.palestrasufc.util.FacebookCall;
import br.com.ufc.palestrasufc.util.Functions;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LectureDetail extends ActionBar {

	private TextView tvAuthor;

	private TextView tvDate;

	private TextView tvTime;

	private TextView tvContent;

	private Lecture lecture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lecturedetail);
		AdMobUtil.addBanner(this);

		tvAuthor = (TextView) findViewById(R.id.tvAuthor);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvContent = (TextView) findViewById(R.id.tvContent);

		((ImageButton) findViewById(R.id.ibFavorite)).setVisibility(View.VISIBLE);
		((ImageButton) findViewById(R.id.ibFacebook)).setVisibility(View.VISIBLE);
		((ImageButton) findViewById(R.id.ibTwitter)).setVisibility(View.VISIBLE);

		lecture = AplicationContext.getInstance().getCurrentLecture();

		setStar();

		try {
			lecture.refresh();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (lecture.getAuthors() != null && lecture.getAuthors().size() > 0) {
			tvAuthor.setText(lecture.getAuthors().toString().replace("[", "").replace("]", ""));
		} else {
			((LinearLayout) findViewById(R.id.llAuthor)).setVisibility(View.GONE);
		}

		if (lecture.getDate() != null && !"".equals(lecture.getDate())) {
			tvDate.setText(lecture.getDate());
		} else {
			((LinearLayout) findViewById(R.id.llDate)).setVisibility(View.GONE);
		}

		if (lecture.getTitle() != null && !"".equals(lecture.getTitle())) {
			tvTime.setText(lecture.getTitle());
		} else {
			((LinearLayout) findViewById(R.id.llTime)).setVisibility(View.GONE);
		}

		if (lecture.getContent() != null && !"".equals(lecture.getContent())) {
			tvContent.setText(lecture.getContent());
		} else {
			((LinearLayout) findViewById(R.id.llContent)).setVisibility(View.GONE);
		}
	}

	private void setStar() {
		if (lecture.isFavorite()) {
			((ImageButton) findViewById(R.id.ibFavorite)).setImageResource(android.R.drawable.btn_star_big_on);
		} else {
			((ImageButton) findViewById(R.id.ibFavorite)).setImageResource(android.R.drawable.btn_star_big_off);
		}
	}

	@Override
	public void doFavorite() {
		if (lecture.isFavorite()) {
			((ImageButton) findViewById(R.id.ibFavorite)).setImageResource(android.R.drawable.btn_star_big_off);
			lecture.setFavorite(false);
		} else {
			((ImageButton) findViewById(R.id.ibFavorite)).setImageResource(android.R.drawable.btn_star_big_on);
			lecture.setFavorite(true);
		}
		DatabaseManager.getInstance(getApplicationContext()).insertLecture(lecture);
	}

	FacebookCall facebookUtil = new FacebookCall(this);
	@Override
	public void doFacebook() {
		facebookUtil.facebookShare(lecture.getTitle(), dialogListener);
	}

	DialogListener dialogListener = new DialogListener() {

		@Override
		public void onComplete(Bundle values) {
			SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(FacebookCall.KEY, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putString(FacebookCall.TOKEN, facebookUtil.getAccessToken());
			editor.putLong(FacebookCall.EXPIRES, facebookUtil.getAccessExpires());
			editor.commit();
			facebookUtil.postToWall("");
		}

		@Override
		public void onFacebookError(FacebookError error) {
			Functions.showToast(LectureDetail.this, "A autenticação com o Facebook falhou! Favor verifique sua conta!");
		}

		@Override
		public void onError(DialogError e) {
			Functions.showToast(LectureDetail.this, "A autenticação com o Facebook falhou! Favor verifique sua conta!");
		}

		@Override
		public void onCancel() {
			Functions.showToast(LectureDetail.this, "A autenticação com o Facebook foi cancelada!");
		}
	};
	
}
