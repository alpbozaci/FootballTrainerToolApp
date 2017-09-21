package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
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

public class EventListAdapter extends BaseAdapter
{
    private List<Event> mEventList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyEventClickListener mEventClickListener;

    public EventListAdapter(
            Context context,
            List<Event> eventList,
            MatchActivity.MyEventClickListener eventClickListener)
    {
        this.mEventList = eventList;
        this.mEventClickListener = eventClickListener;

        mLayoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView textView;
    }

    @Override
    public int getCount()
    {
        return mEventList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mEventList.get(position);
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
            convertView = mLayoutInflater.inflate(R.layout.item_event, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview_event);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Event event = (Event)getItem(position);

        if (event != null)
        {
            viewHolder.textView.setText(event.getType());
        }

        viewHolder.textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mEventClickListener.onEventClicked(event);
            }
        });

        return convertView;
    }
}
