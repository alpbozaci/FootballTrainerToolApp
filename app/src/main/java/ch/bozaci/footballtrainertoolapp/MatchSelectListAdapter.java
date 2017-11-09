package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class MatchSelectListAdapter extends BaseAdapter
{
    private List<Match> mMatchList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyPlayerClickListener mPlayerClickListener;

    public MatchSelectListAdapter(
            Context context,
            List<Match> matchList)
    {
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

        viewHolder.textViewMatchInfo.setText(match.toString());
        viewHolder.textViewMatchScore.setText(" - : - ");

        return convertView;
    }

}
