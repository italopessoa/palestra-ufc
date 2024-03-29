/*
 ******************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0   *
 * Copyright (c) 2011, Android Open Handset Alliance. All rights reserved.    *
 *                                                                            *
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2011, Motorola Mobility, Inc. All rights reserved.           *
 *                                                                            *
 * For more details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf        *
 * in your installation folder.                                               *
 ******************************************************************************
 */

package br.com.ufc.palestrasufc.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import br.com.ufc.palestrasufc.db.DatabaseManager;
import br.com.ufc.palestrasufc.model.Lecture;
import br.com.ufc.palestrasufc.util.AdMobUtil;
import br.com.ufc.palestrasufc.util.AplicationContext;

/**
 * Class which holds an example of a endless list. It means a list will be displayed and it will always have items to be
 * displayed. <br>
 * New data is loaded asynchronously in order to provide a good user experience.
 */
public class ListLectures extends ListActivity {

	/**
	 * Adapter for endless list.
	 */
	private EndlessListAdapter arrayAdapter = null;

	/**
	 * The list header (Where is the loading and last updated labels)
	 */
	private LinearLayout listHeader = null;

	/**
	 * Last loaded item
	 */
	private TextView lastUpdated = null;

	/**
	 * Loading progress
	 */
	private ProgressBar loadingProgress = null;

	/**
	 * Variable which controls when new items are being loaded. If this variable is true, it means items are being
	 * loaded, otherwise it is set to false.
	 */
	private boolean isLoading = false;

	/**
	 * The number of elements which are retrieved every time the service is called for retrieving elements.
	 */
	private static final int BLOCK_SIZE = 2;

	/**
	 * Property to save the number of items already loaded
	 */
	private static final String PROP_ITEM_COUNT = "item_count";

	/**
	 * Property to save the top most index of the list
	 */
	private static final String PROP_TOP_ITEM = "top_list_item";

	/**
	 * Property to save the time of the last update
	 */
	private static final String PROP_LAST_UPDATED = "last_updated";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listlectures);
		AdMobUtil.addBanner(this);

		((TextView) findViewById(R.id.actionBarTitle)).setText(R.string.lectures);

		arrayAdapter = new EndlessListAdapter(this, R.layout.listrow, new ArrayList<Lecture>());

		listHeader = (LinearLayout) getLayoutInflater().inflate(R.layout.listheader, null);
		getListView().addHeaderView(listHeader);
		lastUpdated = (TextView) listHeader.findViewById(R.id.lastUpdated);
		loadingProgress = (ProgressBar) listHeader.findViewById(R.id.progressBar);
		loadingProgress.setVisibility(View.GONE);
		lastUpdated.setText(R.string.last_updated);
		lastUpdated.setText(lastUpdated.getText().toString()
				+ DateFormat.format("EEEE, MMMM dd, yyyy", Calendar.getInstance()));
		if (savedInstanceState == null
				|| (savedInstanceState != null && savedInstanceState.getInt(PROP_ITEM_COUNT, 0) == 0)) {
			// download asynchronously initial list
			Downloaditems downloadAction = new Downloaditems();
			downloadAction.execute(new Integer[] { BLOCK_SIZE });
		}

		setListAdapter(arrayAdapter);
		getListView().setOnTouchListener(new PullEventListener());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/*
		 * Save instance loaded items to be restored after rotate the device OR after you have pressed home button
		 */
		outState.putInt(PROP_ITEM_COUNT, arrayAdapter.getCount());
		for (int i = 0; i < arrayAdapter.getCount(); i++) {
			outState.putSerializable(Integer.toString(i), arrayAdapter.getItemAt(i));
		}
		outState.putInt(PROP_TOP_ITEM, getListView().getFirstVisiblePosition());
		outState.putString(PROP_LAST_UPDATED, lastUpdated.getText().toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		/*
		 * Restore state. Also restore lastAdded value since this class is a new instance on restore
		 */
		super.onRestoreInstanceState(state);
		int count = state.getInt(PROP_ITEM_COUNT);
		for (int i = 0; i < count; i++) {
			arrayAdapter.add((Lecture) state.get(Integer.toString(i)));
		}
		getListView().setSelection(state.getInt(PROP_TOP_ITEM));
		lastUpdated.setText(state.getString(PROP_LAST_UPDATED));
	}

	//TODO
	@Override
	protected void onListItemClick(ListView lv, View v, int position, long id) {
		int listIndex = position - 0;
		if (arrayAdapter.getItemAt(listIndex) != null) {
			AplicationContext.getInstance().setCurrentLecture(arrayAdapter.getItemAt(listIndex));
			startActivity(new Intent(getApplicationContext(), LectureDetail.class));
		}
	}

	/**
	 * This method represents a service which takes a long time to be executed. To simulate it, it is inserted a lag of
	 * 1 second. <br>
	 * This method basically creates a <i>cache</i> number of {@link ListElement} and returns it. It creates
	 * {@link ListElement}s with text higher than <i>itemNumber</i>.
	 * 
	 * @param itemNumber
	 *            Basic number to create other elements.
	 * @param numberOfItemsToBeCreated
	 *            Number of items to be created.
	 * 
	 * @return Returns the created list of {@link ListElement}s.
	 */
	protected List<Lecture> retrieveItems() {
		List<Lecture> results = null;
		try {
			// wait for 2 seconds in order to simulate the long service
			Thread.sleep(2000);
			// create items
			results = DatabaseManager.getInstance(getApplicationContext()).listLectures();
		} catch (InterruptedException e) {
			// treat exception here
		}
		return results;
	}

	/**
	 * Listener which handles the endless list. It is responsible for determining when the long service will be called
	 * asynchronously.
	 */

	/**
	 * Asynchronous job call. This class is responsible for calling the long service and managing the <i>isLoading</i>
	 * flag.
	 */
	class Downloaditems extends AsyncTask<Integer, Void, List<Lecture>> {

		@Override
		protected void onPreExecute() {
			// flag loading is being executed
			isLoading = true;
			loadingProgress.setVisibility(View.VISIBLE);
			lastUpdated.setText(R.string.loading_message);
		}

		@Override
		protected List<Lecture> doInBackground(Integer... params) {

			// execute the long service
			return retrieveItems();
		}

		@Override
		protected void onPostExecute(List<Lecture> result) {
			arrayAdapter.setNotifyOnChange(true);
			for (Lecture item : result) {
				// it is necessary to verify whether the item was already added
				// because this job is called many times asynchronously
				synchronized (arrayAdapter) {
					if (!arrayAdapter.contains(item)) {
						// Add items always in the beginning
						arrayAdapter.insert(item, 0);
					}
				}
			}

			loadingProgress.setVisibility(View.GONE);
			lastUpdated.setText(getString(R.string.last_updated,
					DateFormat.format("EEEE, MMMM dd, yyyy", Calendar.getInstance())));

			getListView().removeHeaderView(listHeader);

			// flag the loading is finished
			isLoading = false;
		}
	}

	/**
	 * Adapter which handles the list be be displayed.
	 */
	class EndlessListAdapter extends ArrayAdapter<Lecture> {

		private final Activity context;
		private final List<Lecture> items;
		private final int rowViewId;

		/**
		 * Instantiate the Adapter for an Endless List Activity.
		 * 
		 * @param context
		 *            {@link Activity} which holds the endless list.
		 * @param rowviewId
		 *            Identifier of the View which holds each row of the List.
		 * @param items
		 *            Initial set of items which are added to list being displayed.
		 */
		public EndlessListAdapter(Activity context, int rowviewId, List<Lecture> items) {
			super(context, rowviewId, items);
			this.context = context;
			this.items = items;
			this.rowViewId = rowviewId;
		}

		/**
		 * Check whether a {@link ListItem} is already in this adapter.
		 * 
		 * @param item
		 *            Item to be verified whether it is in the adapter.
		 * 
		 * @return Returns <code>true</code> in case the {@link ListElement} is in the adapter, <code>false</code>
		 *         otherwise.
		 */
		public boolean contains(Lecture item) {
			return items.contains(item);
		}

		/**
		 * Get a {@link ListElement} at a certain position.
		 * 
		 * @param index
		 *            Position where the {@link ListElement} is retrieved.
		 * 
		 * @return Returns the {@link ListElement} give a certain position.
		 */
		public Lecture getItemAt(int index) {
			return index < items.size() ? items.get(index) : null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			TextView textView;

			View rowView = convertView;

			/*
			 * We inflate the row using the determined layout. Also, we fill the necessary data in the text and image
			 * views.
			 */
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(rowViewId, null, true);
			textView = (TextView) rowView.findViewById(R.id.text01);
			imageView = (ImageView) rowView.findViewById(R.id.img01);
			textView.setText(items.get(position).getTitle());
			imageView.setImageResource(items.get(position).getImageId());

			return rowView;
		}
	}

	class PullEventListener implements OnTouchListener {

		private float firstEventY;
		private CharSequence previousLastUpdatedText;
		private boolean shouldConsiderRefresh = false;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			// Save the first touch position
			case MotionEvent.ACTION_DOWN:
				shouldConsiderRefresh = getListView().getFirstVisiblePosition() == 0 && listHeader.getTop() == 0;
				previousLastUpdatedText = lastUpdated.getText();
				if (shouldConsiderRefresh) {
					firstEventY = event.getY();
				}
				break;

			// Check if we can refresh with certain delta to update texts
			case MotionEvent.ACTION_MOVE:
				if (shouldConsiderRefresh) {
					float currentEventY = event.getY();
					if (currentEventY != firstEventY) {
						lastUpdated.setText(R.string.pull_to_refresh);
					}
					if (shouldRefresh(firstEventY, currentEventY) && !isLoading) {
						lastUpdated.setText(R.string.release_to_refresh);
					}
				}
				break;

			// Check if we can refresh with certain delta and go back to the
			// original text because the touch event is finished
			case MotionEvent.ACTION_UP:
				lastUpdated.setText(previousLastUpdatedText);
				if (shouldConsiderRefresh) {
					float currentEventY = event.getY();
					if (shouldRefresh(firstEventY, currentEventY) && !isLoading) {
						lastUpdated.setText(previousLastUpdatedText);
						Downloaditems downloadAction = new Downloaditems();
						downloadAction.execute(new Integer[] { BLOCK_SIZE });
						event.setAction(MotionEvent.ACTION_CANCEL);
						dispatchTouchEvent(event);
						shouldConsiderRefresh = false;
						return true;
					}
				}
				break;
			}
			return false;
		}

		/**
		 * Check if the difference between first and current touch positions are enough to dispatch a refresh
		 * 
		 * @param firstTapPosition
		 * @param currentPosition
		 * @return true if the difference is big enough to refresh, false otherwise
		 */
		private boolean shouldRefresh(float firstTapPosition, float currentPosition) {
			int threshold = getListView().getHeight() / 4;
			return ((currentPosition - firstTapPosition) / 2) > threshold;
		}

	}
}