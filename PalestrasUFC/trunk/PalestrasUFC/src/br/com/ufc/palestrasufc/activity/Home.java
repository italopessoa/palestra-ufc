/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved.    *
 *                                                                            *                                                                         *
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved.           *
 *                                                                            *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        * 
 * in your installation folder.                                               *
 ******************************************************************************
 */

package br.com.ufc.palestrasufc.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.ufc.palestrasufc.db.DatabaseManager;
import br.com.ufc.palestrasufc.model.Lecture;
import br.com.ufc.palestrasufc.util.AdMobUtil;
import br.com.ufc.palestrasufc.xml.LocalDataService;
import br.com.ufc.palestrasufc.xml.PostProcessor;

public class Home extends ActionBar {

	private ViewGroup feature_1;
	private ViewGroup feature_2;
	private ViewGroup feature_3;
	private ViewGroup feature_4;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		AdMobUtil.addBanner(this);
		
		((TextView) findViewById(R.id.actionBarTitle)).setText(R.string.app_name);
		
		this.feature_1 = (ViewGroup) findViewById(R.id.feature_1);
		this.feature_2 = (ViewGroup) findViewById(R.id.feature_2);
		this.feature_3 = (ViewGroup) findViewById(R.id.feature_3);
		this.feature_4 = (ViewGroup) findViewById(R.id.feature_4);

		View.OnClickListener onClickListener = new DashboardClickListener();

		this.feature_1.setOnClickListener(onClickListener);
		this.feature_2.setOnClickListener(onClickListener);
		this.feature_3.setOnClickListener(onClickListener);
		this.feature_4.setOnClickListener(onClickListener);

		DatabaseManager.getInstance(getApplicationContext()).clearTables();

		PostProcessor<Lecture> processor = new PostProcessor<Lecture>(Lecture.class);
		new LocalDataService(getApplicationContext()).selectAll("lecture", processor);
		ArrayList<Lecture> Lectures = (ArrayList<Lecture>) processor.getResult();
		for (Lecture lecture : Lectures) {
			DatabaseManager.getInstance(getApplicationContext()).insertOrUpdateLectures(lecture);
		}

	}

	private class DashboardClickListener implements View.OnClickListener {

		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.feature_1:
				break;
			case R.id.feature_2:
				startActivity(new Intent(getApplicationContext(), ListLectures.class));
				break;
			case R.id.feature_3:
				startActivity(new Intent(getApplicationContext(), ListFavorites.class));
				break;
			case R.id.feature_4:
				startActivity(new Intent(getApplicationContext(), Settings.class));
				break;
			}
		}

	}

}