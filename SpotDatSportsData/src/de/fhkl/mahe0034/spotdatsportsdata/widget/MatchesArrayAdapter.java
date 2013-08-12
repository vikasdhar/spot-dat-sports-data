package de.fhkl.mahe0034.spotdatsportsdata.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fhkl.mahe0034.spotdatsportsdata.R;
import de.fhkl.mahe0034.spotdatsportsdata.objects.Match;

public class MatchesArrayAdapter extends ArrayAdapter<Match> {
	
	public MatchesArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.listitem_match, null);
		}
		
		Match match = getItem(position);
		TextView team1nameView = (TextView) view.findViewById(R.id.team1name);
		TextView team2nameView = (TextView) view.findViewById(R.id.team2name);
		//ImageView team1emblemView = (ImageView) view.findViewById(R.id.team1emblem);
		//ImageView team2emblemView = (ImageView) view.findViewById(R.id.team2emblem);
		TextView team1goalsView = (TextView) view.findViewById(R.id.team1goals);
		TextView team2goalsView = (TextView) view.findViewById(R.id.team2goals);
		
		// team names
		team1nameView.setText(match.getTeam1().getName());
		team2nameView.setText(match.getTeam2().getName());
		
		// team emblems
		//team1emblemView.setImageDrawable(match.getTeam1().getEmblem());
		//team2emblemView.setImageDrawable(match.getTeam2().getEmblem());
		
		// team goals
		String team1goals = "-";
		String team2goals = "-";
		if (match.isScoreAvailable()) {
			team1goals = Short.toString(match.getTeam1goals());
			team2goals = Short.toString(match.getTeam2goals());
		}
		team1goalsView.setText(team1goals);
		team2goalsView.setText(team2goals);
		
		return view;
	}
}
