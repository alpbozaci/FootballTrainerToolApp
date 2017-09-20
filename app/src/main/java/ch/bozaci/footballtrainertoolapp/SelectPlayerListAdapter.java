package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class SelectPlayerListAdapter extends BaseAdapter
{
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyPlayerClickListener mPlayerClickListener;
    private MatchActivity.MyActivateDeactivatePlayerClickListener mActivateDeactivatePlayerClickListener;

    public SelectPlayerListAdapter(
            Context context,
            List<Player> playerList,
            MatchActivity.MyPlayerClickListener playerClickListener,
            MatchActivity.MyActivateDeactivatePlayerClickListener activateDeactivatePlayerClickListener)
    {
        this.mPlayerList = playerList;
        this.mPlayerClickListener = playerClickListener;
        this.mActivateDeactivatePlayerClickListener = activateDeactivatePlayerClickListener;

        mLayoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        CheckBox checkBox;
        TextView textView;
    }

    @Override
    public int getCount()
    {
        return mPlayerList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mPlayerList.get(position);
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

        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_select_player, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_select_player);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview_select_player);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Player player = (Player)getItem(position);

        viewHolder.textView.setText(player.toString());
        viewHolder.textView.setEnabled(false);
        viewHolder.checkBox.setChecked(false);

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewHolder.checkBox.isChecked())
                {
                    viewHolder.textView.setEnabled(true);
                    mActivateDeactivatePlayerClickListener.onActivatePlayerClicked(player);
                }
                else
                {
                    viewHolder.textView.setEnabled(false);
                    mActivateDeactivatePlayerClickListener.onDeactivatePlayerClicked(player);
                }
            }
        });

        viewHolder.textView.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               mPlayerClickListener.onPlayerClicked(player);
           }
        });

        return convertView;
    }
}
