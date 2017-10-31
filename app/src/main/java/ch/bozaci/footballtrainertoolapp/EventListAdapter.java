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
    private MatchActivity.MyDeleteEventLongClickListener mEventLongClickListener;


    public EventListAdapter(
            Context context,
            List<Event> eventList,
            MatchActivity.MyEventClickListener eventClickListener,
            MatchActivity.MyDeleteEventLongClickListener eventLongClickListener)
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
        Event.EventType eventType = Event.EventType.fromString(event.getType());

        Drawable icon;
        switch (eventType)
        {
            case OWN_PLAYER_PRESENT:
                return null;
            case OWN_PLAYER_ABSENT:
                return null;
            case OWN_PLAYER_GOAL:
                return mContext.getResources().getDrawable(R.drawable.goal, null);
            case OWN_PLAYER_ASSIST:
                return mContext.getResources().getDrawable(R.drawable.assist, null);
            case OWN_PLAYER_FAULT:
                return mContext.getResources().getDrawable(R.drawable.error, null);
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
        Event.EventType eventType = Event.EventType.fromString(event.getType());
        String eventDate = DateUtil.dateFormat.format(event.getDate());

        switch (eventType)
        {
            case OWN_PLAYER_PRESENT:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER AKTIVIERT: " + playerName + System.lineSeparator() + eventDate;
            }

            case OWN_PLAYER_ABSENT:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER DEAKTIVIERT: " + playerName + System.lineSeparator() + eventDate;
            }

            case OWN_PLAYER_GOAL:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER SCHIESST GOAL: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_ASSIST:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER ASSISTIERT: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_FAULT:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER FEHLER: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_IN:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER EINGEWECHSELT: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_OUT:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER AUSGEWECHSELT: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_YELLOW_CARD:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER GELBE KARTE: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_RED_CARD:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER ROTE KARTE: " + playerName + System.lineSeparator() + eventDate;
            }
            case OWN_PLAYER_INJURED:
            {
                String playerName = event.getPlayer().toString();
                return "SPIELER VERLETZT: " + playerName + System.lineSeparator() + eventDate;
            }
            case OPPOSING_TEAM_GOAL:
            {
                return "GEGENMANNSCHAFT SCHIESST GOAL" + System.lineSeparator() + eventDate;
            }
            case MATCH_START:
            {
                return "MATCH START" + System.lineSeparator() + eventDate;
            }
            case MATCH_PAUSE:
            {
                return "MATCH PAUSE" + System.lineSeparator() + eventDate;
            }
            case MATCH_FINISH:
            {
                return "MATCH ENDE" + System.lineSeparator() + eventDate;
            }
            default:
                return "?";
        }

    }
}
