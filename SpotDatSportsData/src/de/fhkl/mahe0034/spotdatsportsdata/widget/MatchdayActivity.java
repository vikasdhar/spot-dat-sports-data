package de.fhkl.mahe0034.spotdatsportsdata.widget;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.fhkl.mahe0034.spotdatsportsdata.R;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Competition;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Match;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchday;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBDataProvider;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBError;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBOperation;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.CurrentMatchdayReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatchdaysReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatchesReceiver;

public class MatchdayActivity extends FragmentActivity implements MatchdaysReceiver, CurrentMatchdayReceiver {
	public static final String EXTRA_COMPETITION = "competition";
	public static final String EXTRA_MATCHDAY = "matchday";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	/** Matchday which is shown on start */
	private Matchday startMatchday;
	
	/** we need to temporarily save matchdays to use them in {@link #onPrepareOptionsMenu(Menu)} */
	private Collection<Matchday> matchdays;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Competition competition = (Competition) intent.getSerializableExtra(EXTRA_COMPETITION);
		this.startMatchday = (Matchday) intent.getSerializableExtra(EXTRA_MATCHDAY);
		
		setContentView(R.layout.activity_matchday);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(competition.getName());
		
		OpenLigaDBDataProvider dataProvider = OpenLigaDBDataProvider.getInstance(this);
		try {
			// Get current matchday, if startMatchday wasn't specified earlier
			if (this.startMatchday == null) {
				dataProvider.requestCurrentMatchday(this, competition);
			}
			dataProvider.requestAvailableMatchdays(this, competition);
		} catch (NetworkErrorException e) {
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.matchday, menu);
		
		// add matchdays to "go to" submenu, if matchdays available
		if (this.matchdays == null) {
			return true;
		}
		SubMenu gotoMenu = menu.findItem(R.id.action_goto).getSubMenu();
		gotoMenu.clear();
		// all matchdays
		for (final Matchday matchday : this.matchdays) {
			MenuItem item = gotoMenu.add(matchday.getName());
			item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					gotoMatchday(matchday);
					return true;
				}
			});
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void setMatchdays(List<Matchday> matchdays) {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), matchdays);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		// start with current matchday
		if (this.startMatchday != null) {
			gotoMatchday(this.startMatchday);
		}
		
		// add matchdays to "go to" options menu
		this.matchdays = matchdays; 
		invalidateOptionsMenu();
	}

	@Override
	public void setCurrentMatchday(Matchday matchday) {
		this.startMatchday = matchday;
		
		// start with current matchday
		gotoMatchday(this.startMatchday);
	}
	
	@Override
	public void setNetworkError(OpenLigaDBOperation failedOperation,
			OpenLigaDBError error) {
		Toast.makeText(this, error.name(), Toast.LENGTH_LONG).show();
	}
	
	private void gotoMatchday(Matchday matchday) {
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setCurrentItem(matchday.getOrderId() - 1);
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private List<Matchday> matchdays;

		public SectionsPagerAdapter(FragmentManager fm, List<Matchday> matchdays) {
			super(fm);
			if (matchdays == null) {
				throw new IllegalArgumentException("matchdays must not be null");
			}
			this.matchdays = matchdays;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a MatchdayFragment (defined as a static inner class
			// below) with the matchday as its lone argument.
			Fragment fragment = new MatchesListFragment();
			Bundle args = new Bundle();
			args.putSerializable(MatchesListFragment.ARG_MATCHDAY, matchdays.get(position));
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return matchdays.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return matchdays.get(position).getName();
		}
	}
	
	public static class MatchesListFragment extends ListFragment implements MatchesReceiver {
		/**
		 * The fragment argument representing the matchday for this fragment.
		 */
		public static final String ARG_MATCHDAY = "matchday";
		
		private ArrayAdapter<Match> matchesAdapter;
		private Matchday matchday;
		
		// Contextual Action Bar (CAB) for match actions (share, ...)
		private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
			// Called when the action mode is created; startActionMode() was called
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate a menu resource providing context menu items
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.matches_context_menu, menu);
				return true;
			}

			// Called each time the action mode is shown. Always called after onCreateActionMode, but
			// may be called multiple times if the mode is invalidated.
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false; // Return false if nothing is done
			}

			// Called when the user selects a contextual menu item
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_item_calendar_export:
						calendarExportSelectedMatch();
						mode.finish(); // Action picked, so close the CAB
						return true;
					default:
						return false;
				}
			}

			// Called when the user exits the action mode
			@Override
			public void onDestroyActionMode(ActionMode mode) {
			}
		};
		
		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private void calendarExportSelectedMatch() {
			int selectedPosition = getListView().getCheckedItemPosition();
			Match selectedMatch = (Match) getListAdapter().getItem(selectedPosition);
			
			Calendar beginTime = Calendar.getInstance();
			beginTime.setTimeInMillis(selectedMatch.getStartTime().getTimeInMillis());
			// endTime will be startTime + 2 hours
			Calendar endTime = Calendar.getInstance();
			endTime.setTimeInMillis(selectedMatch.getStartTime().getTimeInMillis());
			endTime.add(Calendar.HOUR_OF_DAY, 2);
			
			// intent to create calendar event
			Intent intent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI)
					.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
					.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
					.putExtra(Events.TITLE, selectedMatch.toString())
					.putExtra(Events.DESCRIPTION, selectedMatch.getMatchday().getName() + " " + selectedMatch.getMatchday().getCompetition().getName())
					.putExtra(Events.EVENT_LOCATION, selectedMatch.getVenue().toString());
			
			PackageManager packageManager = getActivity().getPackageManager();
			List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
			boolean isIntentSafe = activities.size() > 0;
			
			if (isIntentSafe) {
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "Error: Can't share match. No app with support for calendar events founds.", Toast.LENGTH_LONG).show();
			}
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			this.matchesAdapter = new MatchesArrayAdapter(activity, android.R.layout.simple_list_item_activated_1);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			ListView list = getListView();
			list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			list.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parentView, View childView,
						int position, long id) {
					return selectMatchForAction((AbsListView) parentView, position);
				}
				
			});
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			OpenLigaDBDataProvider dataProvider = OpenLigaDBDataProvider.getInstance(getActivity());
			this.matchday = (Matchday) getArguments().getSerializable(ARG_MATCHDAY);
			
			setListAdapter(matchesAdapter);
			
			try {
				dataProvider.requestMatches(this, matchday);
			} catch (NetworkErrorException e) {
				Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		
		@Override
		public void onListItemClick(ListView parentView, View childView, int position, long id) {
			showMatchDetails(parentView, position);
		}

		@Override
		public void setMatches(Collection<Match> matches) {
			matchesAdapter.clear();
			
			for (Match match : matches) {
				matchesAdapter.add(match);
			}
		}

		@Override
		public void setNetworkError(OpenLigaDBOperation failedOperation,
				OpenLigaDBError error) {
			Toast.makeText(getActivity(), error.name(), Toast.LENGTH_LONG).show();
		}
		
		private boolean selectMatchForAction(AbsListView listView, int position) {
			// calendar intents are only available since ice cream sandwich, so do nothing for older droids
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				return false;
			}
			
			getActivity().startActionMode(mActionModeCallback);
			// "set selection" variants don't work, "check" does
			//parentView.setSelection(position);
			//setSelection(position);
			//childView.setSelected(true);
			listView.setItemChecked(position, true);
			
			return true;
		}
		
		private void showMatchDetails(ListView list, int position) {
			Match match = (Match) list.getItemAtPosition(position);
			Intent intent = new Intent(getActivity().getApplicationContext(), MatchActivity.class);
			intent.putExtra(MatchActivity.EXTRA_MATCH, match);
			startActivity(intent);
		}
	}
}
