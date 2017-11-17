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
import ch.bozaci.footballtrainertoolapp.util.DateUtil;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class EventListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<Event> mEventList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyEventClickListener mEventClickListener;
    private MatchActivity.MyEventLongClickListener mEventLongClickListener;


    public EventListAdapter(
            Context context,
            List<Event> eventList,
            MatchActivity.MyEventClickListener eventClickListener,
            MatchActivity.MyEventLongClickListener eventLongClickListener)
    {
        this.mContext = context;
        this.mEventList = eventList;
        this.mEventClickListener = eventClickListener;
        this.mEventLongClickListener = eventLongClickListener;

        mLayoutInflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView textViewEventType;
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
            viewHolder.textViewEventType = (TextView) convertView.findViewById(R.id.textview_event_type);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Event event = (Event)getItem(position);

        if (event != null)
        {
            String text = getText(event);
            Drawable icon = getIcon(event);
            viewHolder.textViewEventType.setText(text);
            viewHolder.textViewEventType.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null);
        }

        viewHolder.textViewEventType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mEventClickListener.onEventClicked(event);
            }
        });

        viewHolder.textViewEventType.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                mEventLongClickListener.onLongEventClicked(event);
                return true;
            }
        });

        return convertView;
    }

    private Drawable getIcon(Event event)
    {
        Drawable icon;
        switch (event.getType())
        {
            case OWN_PLAYER_PRESENT:
                return null;
            case OWN_PLAYER_ABSENT:
                return null;
            case OWN_PLAYER_GOAL:
                return mContext.getResources().getDrawable(R.drawable.goal, null);
            case OWN_PLAYER_ASSIST:
                return mContext.getResources().getDrawable(R.drawable.assist, null);
            case OWN_PLAYER_GOODPLAY:
                return mContext.getResources().getDrawable(R.drawable.thumb_up, null);
            case OWN_PLAYER_BADPLAY:
                return mContext.getResources().getDrawable(R.drawable.thumb_down, null);
            case OWN_PLAYER_IN:
                return mContext.getResources().getDrawable(R.drawable.player_in, null);
            case OWN_PLAYER_OUT:
                return mContext.getResources().getDrawable(R.drawable.player_out, null);
            case OWN_PLAYER_YELLOW_CARD:
                return mContext.getResources().getDrawable(R.drawable.yellow_card, null);
            case OWN_PLAYER_RED_CARD:
                return mContext.getResources().getDrawable(R.drawable.red_card, null);
            case OWN_PLAYER_INJURED:
                return mContext.getResources().getDrawable(R.drawable.first_aid_kit, null);
            case OPPOSING_TEAM_GOAL:
                return mContext.getResources().getDrawable(R.drawable.goal, null);
            case MATCH_START:
                return null;
            case MATCH_PAUSE:
                return null;
            case MATCH_FINISH:
                return null;

            default:
                icon = null; break;
        }
        return icon;
    }

    private String getText(Event event)
    {
        String eventDate = DateUtil.dateFormat.format(event.getDate());

        switch (event.getType())
        {
            case OWN_PLAYER_PRESENT:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_present) + " : " + playerName + System.lineSeparator() + eventDate;
            }

            case OWN_PLAYER_ABSENT:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_absent) + " : " + playerName + System.lineSeparator() + eventDate;
            }

            case OWN_PLAYER_GOAL:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_goal) + " : " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_ASSIST:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_assist) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_GOODPLAY:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_goodplay) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_BADPLAY:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_badplay) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_IN:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_in) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_OUT:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_out) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_YELLOW_CARD:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_yellow_card) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_RED_CARD:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_red_card) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_INJURED:
            {
                String playerName = event.getPlayer().toString();
                return mContext.getResources().getString(R.string.eventtype_own_player_injured) + " : "  + playerName + System.lineSeparator() + eventDate;
            }
            case OPPOSING_TEAM_GOAL:
            {
                return mContext.getResources().getString(R.string.eventtype_opposing_team_goal) + " : "  + System.lineSeparator() + eventDate;
            }
            case MATCH_START:
            {
                return mContext.getResources().getString(R.string.eventtype_match_start) + " : "  + System.lineSeparator() + eventDate;
            }
            case MATCH_PAUSE:
            {
                return mContext.getResources().getString(R.string.eventtype_match_pause) + " : "  + System.lineSeparator() + eventDate;
            }
            case MATCH_FINISH:
            {
                return mContext.getResources().getString(R.string.eventtype_match_finish) + " : "  + System.lineSeparator() + eventDate;
            }
            default:
                return "?";
        }

    }
}
