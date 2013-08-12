package de.fhkl.mahe0034.spotdatsportsdata.widget;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.fhkl.mahe0034.spotdatsportsdata.R;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Match;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchevent;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBDataProvider;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBError;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBOperation;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatcheventsReceiver;

public class MatchActivity extends Activity implements MatcheventsReceiver {
	public static String EXTRA_MATCH = "match";
	
	private ArrayAdapter<Matchevent> matcheventsAdapter;
	private Match match;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		this.match = (Match) intent.getSerializableExtra(EXTRA_MATCH);
		
		setContentView(R.layout.activity_match);
		// Show the Up button in the action bar.
		setupActionBar();
		
		OpenLigaDBDataProvider dataProvider = OpenLigaDBDataProvider.getInstance(this);
		
		if (this.match.isScoreAvailable()) {
			try {
				dataProvider.requestGoals(this, this.match);
			} catch (NetworkErrorException e) {
				Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		
		TextView kickoffTimeView = (TextView) findViewById(R.id.kickoff_at); 
		TextView venueView = (TextView) findViewById(R.id.venue);
		TextView team1nameView = (TextView) findViewById(R.id.team1name);
		TextView team2nameView = (TextView) findViewById(R.id.team2name);
		TextView team1goalsView = (TextView) findViewById(R.id.team1goals);
		TextView team2goalsView = (TextView) findViewById(R.id.team2goals);
		
		DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
		kickoffTimeView.setText(dateFormat.format(this.match.getStartTime().getTime()));
		venueView.setText(this.match.getVenue().toString());
		team1nameView.setText(this.match.getTeam1().getName());
		team2nameView.setText(this.match.getTeam2().getName());
		if (this.match.isScoreAvailable()) {
			team1goalsView.setText(Short.toString(this.match.getTeam1goals()));
			team2goalsView.setText(Short.toString(this.match.getTeam2goals()));
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setTitle("Match details");

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
			Intent parentIntent = new Intent(getApplicationContext(), MatchdayActivity.class);
			parentIntent.putExtra(MatchdayActivity.EXTRA_COMPETITION, this.match.getMatchday().getCompetition());
			parentIntent.putExtra(MatchdayActivity.EXTRA_MATCHDAY, this.match.getMatchday());
			NavUtils.navigateUpTo(this, parentIntent);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setNetworkError(OpenLigaDBOperation failedOperation,
			OpenLigaDBError error) {
		Toast.makeText(this, error.name(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void setMatchevents(Collection<? extends Matchevent> matchevents) {
		this.matcheventsAdapter = new ArrayAdapter<Matchevent>(this, android.R.layout.simple_list_item_1); 
		ListView eventsList = (ListView) findViewById(R.id.list_match_events);
		eventsList.setAdapter(matcheventsAdapter);
		
		matcheventsAdapter.addAll(matchevents);
	}

}
