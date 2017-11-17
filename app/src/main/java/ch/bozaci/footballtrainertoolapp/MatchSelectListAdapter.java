package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class MatchSelectListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<Match> mMatchList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyPlayerClickListener mPlayerClickListener;

    public MatchSelectListAdapter(
            Context context,
            List<Match> matchList)
    {
        this.mContext = context;
        this.mMatchList = matchList;

        mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder
    {
        TextView textViewMatchInfo;
        TextView textViewMatchScore;
    }

    @Override
    public int getCount()
    {
        return mMatchList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mMatchList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;
        final Match match = (Match)getItem(position);

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            convertView = mLayoutInflater.inflate(R.layout.item_match_select, parent, false);
            viewHolder.textViewMatchInfo  = (TextView) convertView.findViewById(R.id.textview_select_match_info);
            viewHolder.textViewMatchScore = (TextView) convertView.findViewById(R.id.textview_select_match_score);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        fillData(viewHolder, match);

        return convertView;
    }

    private void fillData(ViewHolder viewHolder, Match match)
    {
        viewHolder.textViewMatchInfo.setText(match.toString());

        // match has not played yet
        if(match.getEventList().isEmpty())
        {
            viewHolder.textViewMatchScore.setText(" - : - ");
        }
        else
        {
            Integer myTeamScore = new Integer(0);
            Integer otherTeamScore = new Integer(0);

            for (Event event : match.getEventList())
            {
                if (event.getType().equals(Event.EventType.OWN_PLAYER_GOAL))
                {
                    myTeamScore++;
                }
                if (event.getType().equals(Event.EventType.OPPOSING_TEAM_GOAL))
                {
                    otherTeamScore++;
                }
            }

            if (match.getLocationType().equals(Match.LocationType.HOME_GAME.getType()))
            {
                viewHolder.textViewMatchScore.setText(myTeamScore + " : " + otherTeamScore);
            }
            else
            {
                viewHolder.textViewMatchScore.setText(otherTeamScore + " : " + myTeamScore);
            }

            if (myTeamScore > otherTeamScore)
            {
                viewHolder.textViewMatchScore.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_background_light2dark_green, null));
            }
            else if (myTeamScore < otherTeamScore)
            {
                viewHolder.textViewMatchScore.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_background_light2dark_red, null));
            }
            else
            {
                viewHolder.textViewMatchScore.setBackground(mContext.getResources().getDrawable(R.drawable.gradient_background_light2dark_gray, null));
            }
        }
    }

}
