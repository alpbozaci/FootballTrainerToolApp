package ch.bozaci.footballtrainertoolapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchActivity extends AppCompatActivity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<Player> mPlayerList;
    private List<Event> mEventList;
    private SelectPlayerListAdapter mSelectPlayerListAdapter;
    private EventListAdapter mEventListAdapter;
    private ListView mPlayerListView;
    private ListView mEventListView;
    private Match mMatch;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TAB 1");
        tab1.setIndicator("Spieler Event");
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("TAB 2");
        tab2.setIndicator("Match Event");
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("TAB 3");
        tab3.setIndicator("Verlauf");
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            mMatch = (Match)bundle.getSerializable("match");
            System.out.println(mMatch.toString());

            TextView textView = (TextView) findViewById(R.id.textview_started_match);
            textView.setText(mMatch.getHomeTeam() + " - " + mMatch.getGuestTeam());
        }

        mPlayerList = new ArrayList<>();
        mEventList = new ArrayList<>();

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadPlayerList();

        mSelectPlayerListAdapter = new SelectPlayerListAdapter(this, mPlayerList, new MyPlayerClickListener(), new MyActivateDeactivatePlayerClickListener());
        mPlayerListView = (ListView) findViewById(R.id.listview_select_player);
        mPlayerListView.setAdapter(mSelectPlayerListAdapter);

        mEventListAdapter = new EventListAdapter(this, mEventList, new MyEventClickListener());
        mEventListView = (ListView) findViewById(R.id.listview_event);
        mEventListView.setAdapter(mEventListAdapter);
    }

    private void loadPlayerList()
    {
        mPlayerList.clear();
        List<Player> dbPlayerList = databaseAdapter.getPlayerList();

        for (Player player : dbPlayerList)
        {
            mPlayerList.add(player);
        }
    }


    private void showAddEventTypeDialog(final Player player)
    {
        final Dialog eventTypeDialog = new Dialog(this);
        eventTypeDialog.setContentView(R.layout.dialog_eventtype);

        Button goal       = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_goal);
        Button assist     = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_assist);
        Button yellowCard = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_yellow_card);
        Button redCard    = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_red_card);
        Button injured    = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_injured);

        goal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Event event = createEvent(player, Event.EventType.GOAL);
                addEvent(event);
                loadEventList();
                eventTypeDialog.dismiss();
            }
        });

        eventTypeDialog.show();
    }

    private Event createEvent(Player player, Event.EventType eventType)
    {
        Event event = new Event();
        event.setMatchId(mMatch.getId());
        event.setPlayerId(player.getId());
        event.setDate(new Date());
        event.setType(eventType.getType());

        return event;
    }

    private void addEvent(Event event)
    {
        databaseAdapter.addEvent(event);
        Log.i(LOG_TAG, "event added" );
    }

    private void loadEventList()
    {
        mEventList.clear();

        try
        {
            List<Event> dbEventList = databaseAdapter.getEventList(mMatch.getId());
            for (Event event : dbEventList)
            {
                mEventList.add(event);
            }
            mEventListAdapter.notifyDataSetChanged();
        }
        catch (ParseException ex)
        {
            Log.e(LOG_TAG, ex.getMessage());
        }
    }

    private interface PlayerClickListener
    {
        void onPlayerClicked(Player player);
    }

    private interface ActivateDeactivatePlayerClickListener
    {
        void onActivatePlayerClicked(Player player);
        void onDeactivatePlayerClicked(Player player);
    }

    private interface EventClickListener
    {
        void onEventClicked(Event event);
    }


    class MyPlayerClickListener implements PlayerClickListener
    {
        @Override
        public void onPlayerClicked(Player player)
        {
            System.out.println("clicked: " + player.toString());
            showAddEventTypeDialog(player);
        }
    }

    class MyActivateDeactivatePlayerClickListener implements ActivateDeactivatePlayerClickListener
    {
        @Override
        public void onActivatePlayerClicked(Player player)
        {
            System.out.println("activated: " + player.toString());
        }

        @Override
        public void onDeactivatePlayerClicked(Player player)
        {
            System.out.println("deactivated: " + player.toString());
        }
    }

    class MyEventClickListener implements EventClickListener
    {
        @Override
        public void onEventClicked(Event event)
        {
            System.out.println("clicked: " + event.toString());
        }
    }
}
