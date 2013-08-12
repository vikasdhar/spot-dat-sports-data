package de.fhkl.mahe0034.spotdatsportsdata.widget;

import java.util.Collection;
import java.util.List;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.fhkl.mahe0034.spotdatsportsdata.R;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Competition;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Matchday;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBDataProvider;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBError;
import de.fhkl.mahe0034.spotdatsportsdata.openligadb.OpenLigaDBOperation;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.CompetitionsReceiver;
import de.fhkl.mahe0034.spotdatsportsdata.receiver.MatchdaysReceiver;

public class MainActivity extends Activity implements CompetitionsReceiver, MatchdaysReceiver {
	
	private OpenLigaDBDataProvider dataProvider;
	private ArrayAdapter<Competition> competitionsAdapter;
	private ContextMenu contextMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dataProvider = OpenLigaDBDataProvider.getInstance(this);
		competitionsAdapter = new ArrayAdapter<Competition>(this, android.R.layout.simple_list_item_1);
		
		setContentView(R.layout.activity_main);
		final ListView list = (ListView) findViewById(R.id.competitionsListView);
		list.setAdapter(competitionsAdapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView,
					android.view.View childView, int position, long id) {
				Competition competition = (Competition) list.getItemAtPosition(position);
				Intent competitionIntent = new Intent(getApplicationContext(), MatchdayActivity.class);
				competitionIntent.putExtra(MatchdayActivity.EXTRA_COMPETITION, competition);
				//competitionIntent.putExtra(MatchdayActivity.EXTRA_CURRENT_MATCHDAY, current_matchday);
				startActivity(competitionIntent);
			}
		});
		
		// register ContextMenu for competitions
		registerForContextMenu(list);
		
		try {
			dataProvider.requestAvailableCompetitions(this);
		} catch (NetworkErrorException e) {
			Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// add menu item to keep menu open until matchdays are loaded and to prevent onItemClick action to perform
		MenuItem menuItem = menu.add("Loading...");
		menuItem.setEnabled(false);
		try {
			// request matchdays
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			dataProvider.requestAvailableMatchdays(MainActivity.this, (Competition) ((ListView)v).getItemAtPosition(info.position));
			this.contextMenu = menu;
		} catch (NetworkErrorException e) {
			Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			// TODO: close menu if possible.
			// menu.close() doesn't work, menu.clear() works, but onItemClick is executed instead
		}
	}

	@Override
	public void setCompetitions(Collection<Competition> competitions) {
		competitionsAdapter.clear();
		competitionsAdapter.addAll(competitions);
	}
	
	@Override
	public void setMatchdays(List<Matchday> matchdays) {
		assert(this.contextMenu != null);
		// create contextMenu
		this.contextMenu.clear();
		for (Matchday matchday : matchdays) {
			MenuItem menuItem = this.contextMenu.add(matchday.getName());
			// set intent
			Intent competitionIntent = new Intent(getApplicationContext(), MatchdayActivity.class);
			competitionIntent.putExtra(MatchdayActivity.EXTRA_COMPETITION, matchday.getCompetition());
			competitionIntent.putExtra(MatchdayActivity.EXTRA_MATCHDAY, matchday);
			menuItem.setIntent(competitionIntent);
		}
		this.contextMenu = null;
	}
	
	@Override
	public void setNetworkError(OpenLigaDBOperation failedOperation,
			OpenLigaDBError error) {
		Toast.makeText(this, error.name(), Toast.LENGTH_LONG).show();
	}
}
