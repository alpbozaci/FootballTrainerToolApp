package ch.bozaci.footballtrainertoolapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

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
    private ListView mSelectPlayerListView;
    private ListView mEventListView;
    private TextView mScoreHomeTeamTextView;
    private TextView mScoreGuestTeamTextView;
    private Chronometer mChronometer;
    private Match mMatch;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            mMatch = (Match)bundle.getSerializable("match");
            System.out.println(mMatch.toString());

            TextView textView = (TextView) findViewById(R.id.textview_started_match);
            textView.setText(mMatch.getHomeTeam() + " - " + mMatch.getGuestTeam());
        }

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

        //TAB 1
        mPlayerList = new ArrayList<>();
        loadDBPlayerList();
        mSelectPlayerListAdapter = new SelectPlayerListAdapter(this, mPlayerList, new MyPlayerClickListener(this), new MyActivateDeactivatePlayerClickListener());
        mSelectPlayerListView = (ListView) findViewById(R.id.listview_select_player);
        mSelectPlayerListView.setAdapter(mSelectPlayerListAdapter);

        //TAB 2
        mScoreHomeTeamTextView = (TextView) findViewById(R.id.textview_score_hometeam);
        mScoreGuestTeamTextView = (TextView) findViewById(R.id.textview_score_guestteam);

        Button goalGuestTeamAddButton = (Button)findViewById(R.id.button_goal_guestteam);
        Button startTimerButton = (Button)findViewById(R.id.button_start_timer);
        Button pauseTimerButton = (Button)findViewById(R.id.button_pause_timer);
        Button stopTimerButton  = (Button)findViewById(R.id.button_stop_timer);
        mChronometer = (Chronometer)findViewById(R.id.chronometer_minutes);

        goalGuestTeamAddButton.setOnClickListener(new MyAddGoalGuestTeamClickListener());

        startTimerButton.setOnClickListener(new MyStartTimerClickListener());
        pauseTimerButton.setOnClickListener(new MyPauseTimerClickListener());
        stopTimerButton.setOnClickListener(new MyStopTimerClickListener());

        //TAB 3
        mEventList = new ArrayList<>();
        mEventListAdapter = new EventListAdapter(this, mEventList, new MyEventClickListener(), new MyDeleteEventLongClickListener());
        mEventListView = (ListView) findViewById(R.id.listview_event);
        mEventListView.setAdapter(mEventListAdapter);
    }

    private void loadDBPlayerList()
    {
        mPlayerList.clear();
        List<Player> dbPlayerList = databaseAdapter.getPlayerList();

        for (Player player : dbPlayerList)
        {
            mPlayerList.add(player);
        }
    }

    private void handleAddEvent(Player player, Event.EventType eventType)
    {
        Log.i(LOG_TAG, "ADD EVENT: " + eventType.getType());

        Event event = createEvent(player, eventType);
        addEventToList(event);
        updateGUI();
    }

    private void handleDeleteEvent(final Event event)
    {
        Log.i(LOG_TAG, "DELETE EVENT: " + event.getType());

        deleteEvent(event);
        // call updateGUI() in deleteEvent method after confirm dialog is closed;
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

    private void addEventToDB(Event event)
    {
        Log.i(LOG_TAG, "event added to db");
        databaseAdapter.addEvent(event);
    }

    private void addEventToList(Event event)
    {
        Log.i(LOG_TAG, "event added to list");
        mEventList.add(event);
        mEventListAdapter.notifyDataSetChanged();
    }

    private void deleteEvent(final Event event)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_delete_player));
        alertDialog.setMessage(getString(R.string.message_delete_event));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Log.i(LOG_TAG, "event deleted from list" );
                mEventList.remove(event);
                mEventListAdapter.notifyDataSetChanged();
                alertDialog.dismiss();

                updateGUI();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void updateGUI()
    {
        Integer scoreHomeTeam = 0;
        Integer scoreGuestTeam = 0;

        for (Event event : mEventList)
        {
            if (event.getType().equals(Event.EventType.PLAYER_GOAL_HOMETEAM.getType()))
            {
                scoreHomeTeam ++;
            }
            if (event.getType().equals(Event.EventType.PLAYER_GOAL_GUESTTEAM.getType()))
            {
                scoreGuestTeam ++;
            }
        }

        mScoreHomeTeamTextView.setText(String.valueOf(scoreHomeTeam));
        mScoreGuestTeamTextView.setText(String.valueOf(scoreGuestTeam));
    }

    //---------------------------------------------------------------------------------------------
    // EVENT HANDLING
    //---------------------------------------------------------------------------------------------

    private interface PlayerClickListener
    {
        void onPlayerClicked(Player player);
    }

    private interface ActivateDeactivatePlayerClickListener
    {
        void onActivatePlayerClicked(Player player, SelectPlayerListAdapter.ViewHolder viewHolder);
        void onDeactivatePlayerClicked(Player player, SelectPlayerListAdapter.ViewHolder viewHolder);
    }

    private interface EventClickListener
    {
        void onEventClicked(Event event);
    }

    private interface EventLongClickListener
    {
        void onLongEventClicked(Event event);
    }


    class MyActivateDeactivatePlayerClickListener implements ActivateDeactivatePlayerClickListener
    {
        @Override
        public void onActivatePlayerClicked(Player player, SelectPlayerListAdapter.ViewHolder viewHolder)
        {
            handleAddEvent(player, Event.EventType.PLAYER_PRESENT);
            viewHolder.textView.setEnabled(true);
        }

        @Override
        public void onDeactivatePlayerClicked(Player player, SelectPlayerListAdapter.ViewHolder viewHolder)
        {
            handleAddEvent(player, Event.EventType.PLAYER_ABSENT);
            viewHolder.textView.setEnabled(false);
        }
    }

    class MyGoalHomeTeamClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {

        }
    }

    class MyAssistClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {

        }
    }

    class MyPlayerClickListener implements PlayerClickListener
    {
        private AppCompatActivity mActivity;

        public MyPlayerClickListener(AppCompatActivity activity)
        {
            this.mActivity = activity;
        }

        @Override
        public void onPlayerClicked(final Player player)
        {
            final Dialog eventTypeDialog = new Dialog(mActivity);
            eventTypeDialog.setContentView(R.layout.dialog_eventtype);

            Button goalButton       = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_goal);
            Button assistButton     = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_assist);
            Button playerInButton   = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_player_in);
            Button playerOutButton  = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_player_out);
            Button yellowCardButton = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_yellow_card);
            Button redCardButton    = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_red_card);
            Button injuredButton    = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_injured);

            goalButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddEvent(player, Event.EventType.PLAYER_GOAL_HOMETEAM);
                    eventTypeDialog.dismiss();
                }
            });

            assistButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddEvent(player, Event.EventType.PLAYER_ASSIST);
                    eventTypeDialog.dismiss();
                }
            });

            eventTypeDialog.show();
        }
    }

    class MyAddGoalGuestTeamClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Player guestPlayer = new Player();
            guestPlayer.setId(null);
            handleAddEvent(guestPlayer, Event.EventType.PLAYER_GOAL_GUESTTEAM);
        }
    }

    class MyStartTimerClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            System.out.println("start timer clicked");
            mChronometer.start();
        }
    }

    class MyPauseTimerClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            System.out.println("pause timer clicked");
            mChronometer.stop();
        }
    }

    class MyStopTimerClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            System.out.println("stop timer clicked");
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.stop();
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

    class MyDeleteEventLongClickListener implements EventLongClickListener
    {
        @Override
        public void onLongEventClicked(Event event)
        {
            handleDeleteEvent(event);
        }
    }
}
