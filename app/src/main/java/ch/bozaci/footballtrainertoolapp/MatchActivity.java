package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

public class MatchActivity extends Activity
{
    private static final String LOG_TAG = MatchActivity.class.getSimpleName();

    private List<Player> mSelectedPlayerList;
    private List<Event> mEventList;

    private PlayerSelectedListAdapter mSelectedPlayerListAdapter;
    private EventListAdapter mEventListAdapter;

    private ListView mSelectPlayerListView;
    private TableLayout mTableLayout;
    private ListView mEventListView;

    private TextView mScoreHomeTeamTextView;
    private TextView mScoreGuestTeamTextView;
    private TextView mFullTimerMinTextView;
    private TextView mFullTimerSecTextView;
    private TextView mPartTimerMinTextView;
    private TextView mPartTimerSecTextView;

    private Button mGoalGuestTeamButton;
    private Button mStartMatchButton;
    private Button mPauseMatchButton;
    private Button mFinishMatchButton;

    private Integer mElapsedFullTimeInSeconds;
    private Integer mElapsedPartTimeInSeconds;

    private Thread mTimerThread;

    private Match mMatch;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");

        setContentView(R.layout.activity_match);

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            mMatch = (Match)bundle.getSerializable(PlayerSelectListActivity.CONST_INTENT_VALUE_MATCH);

            TextView homeTeam  = (TextView) findViewById(R.id.textview_hometeam);
            TextView guestTeam = (TextView) findViewById(R.id.textview_guestteam);
            homeTeam.setText(mMatch.getHomeTeam());
            guestTeam.setText(mMatch.getGuestTeam());

            List<Integer> playerIdList = (ArrayList<Integer>)bundle.getSerializable(PlayerSelectListActivity.CONST_INTENT_VALUE_PLAYER_ID_LIST);
            loadDBPlayerList(playerIdList);
        }

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TAB 1");
        tab1.setIndicator(getString(R.string.match_event));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("TAB 2");
        tab2.setIndicator(getString(R.string.history));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        //TAB 1
        mTimerThread = new Thread(timer);

        mSelectedPlayerListAdapter = new PlayerSelectedListAdapter(this, mSelectedPlayerList, new MyPlayerClickListener(this));
        mSelectPlayerListView = (ListView) findViewById(R.id.listview_selected_player);
        mSelectPlayerListView.setAdapter(mSelectedPlayerListAdapter);

        mScoreHomeTeamTextView  = (TextView) findViewById(R.id.textview_score_hometeam);
        mScoreGuestTeamTextView = (TextView) findViewById(R.id.textview_score_guestteam);

        mFullTimerMinTextView = (TextView)findViewById(R.id.textview_match_timer_min);
        mFullTimerSecTextView = (TextView)findViewById(R.id.textview_match_timer_sec);

        mGoalGuestTeamButton = (Button)findViewById(R.id.button_goal_guestteam);
        mGoalGuestTeamButton.setEnabled(false);
        mGoalGuestTeamButton.setOnClickListener(new MyGoalGuestTeamClickListener());

        mStartMatchButton  = (Button)findViewById(R.id.button_start_match);
        mPauseMatchButton  = (Button)findViewById(R.id.button_pause_match);
        mFinishMatchButton = (Button)findViewById(R.id.button_finish_match);

        mPauseMatchButton.setEnabled(false);
        mFinishMatchButton.setEnabled(false);

        mStartMatchButton.setOnClickListener(new MyStartMatchClickListener());
        mPauseMatchButton.setOnClickListener(new MyPauseMatchClickListener());
        mFinishMatchButton.setOnClickListener(new MyFinishMatchClickListener());

        resetTimer();

        //TAB 2
        mEventList = new ArrayList<>();
        mEventListAdapter = new EventListAdapter(this, mEventList, new MyEventClickListener(), new MyDeleteEventLongClickListener());
        mEventListView = (ListView) findViewById(R.id.listview_event);
        mEventListView.setAdapter(mEventListAdapter);
    }

    private void loadDBPlayerList(List<Integer> playerIdList)
    {
        mSelectedPlayerList = new ArrayList<>();

        for (Integer playerId : playerIdList)
        {
            Player player = databaseAdapter.getPlayer(playerId);
            mSelectedPlayerList.add(player);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(LOG_TAG, "onStart()");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(LOG_TAG, "onResume()");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(LOG_TAG, "onPause()");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(LOG_TAG, "onStop()");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy()");
    }


    Runnable timer = new Runnable()
    {
        @Override
        public void run()
        {
            while(! Thread.currentThread().isInterrupted())
            {
                try
                {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            updateTimer();
                        }
                    });
                }
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    };

    private void handleAddNoPlayerEvent(Event.EventType eventType)
    {
        handleAddEvent(null, eventType);
    }

    private void handleAddPlayerEvent(Player player, Event.EventType eventType)
    {
        handleAddEvent(player, eventType);
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

        showDeleteEventDialog(event);
        // call updateGUI() in showDeleteEventDialog method after confirm dialog is closed;
    }

    private Event createEvent(Player player, Event.EventType eventType)
    {
        Event event = new Event();

        event.setMatch(mMatch);
        event.setMatchId(mMatch.getId());

        if (player != null)
        {
            event.setPlayerId(player.getId());
            event.setPlayer(player);
        }
        else
        {
            event.setPlayerId(null);
            event.setPlayer(null);
        }

        event.setDate(new Date());
        event.setType(eventType.getType());

        return event;
    }

    private void saveMatchEvents()
    {
        for (Event event : mEventList)
        {
            Log.i(LOG_TAG, "event added to db : " + event.getType());
            databaseAdapter.addEvent(event);
        }
    }

    private void addEventToList(Event event)
    {
        Log.i(LOG_TAG, "event added to list");
        mEventList.add(event);
        mEventListAdapter.notifyDataSetChanged();
    }

    private void deleteEventFromList(Event event)
    {
        Log.i(LOG_TAG, "event deleted from list" );
        mEventList.remove(event);
        mEventListAdapter.notifyDataSetChanged();
    }

    private void showDeleteEventDialog(final Event event)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_delete_event));
        alertDialog.setMessage(getString(R.string.message_delete_event));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                deleteEventFromList(event);
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

    private void showFinishMatchDialog()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_finish_match));
        alertDialog.setMessage(getString(R.string.message_save_match));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                saveMatchEvents();
                alertDialog.dismiss();
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
        Integer scoreOwnTeam = 0;
        Integer scoreOpposingTeam = 0;

        for (Event event : mEventList)
        {
            if (event.getType().equals(Event.EventType.OWN_PLAYER_GOAL.getType()))
            {
                scoreOwnTeam ++;
            }
            if (event.getType().equals(Event.EventType.OPPOSING_TEAM_GOAL.getType()))
            {
                scoreOpposingTeam ++;
            }
        }

        if (mMatch.getLocationType().equals(Match.LocationType.HOME_GAME.getType()))
        {
            mScoreHomeTeamTextView.setText(String.valueOf(scoreOwnTeam));
            mScoreGuestTeamTextView.setText(String.valueOf(scoreOpposingTeam));
        }
        else
        {
            mScoreHomeTeamTextView.setText(String.valueOf(scoreOpposingTeam));
            mScoreGuestTeamTextView.setText(String.valueOf(scoreOwnTeam));
        }
    }

    private void resetTimer()
    {
        mElapsedFullTimeInSeconds = 0;
        mElapsedPartTimeInSeconds = 0;
        mFullTimerMinTextView.setText(String.format("%02d", 0));
        mFullTimerSecTextView.setText(String.format("%02d", 0));
        //mPartTimerMinTextView.setText(String.format("%02d", 0));
        //mPartTimerSecTextView.setText(String.format("%02d", 0));
    }

    private void updateTimer()
    {
        mElapsedFullTimeInSeconds++;
        mElapsedPartTimeInSeconds++;

        Integer minFullTime = mElapsedFullTimeInSeconds / 60;
        Integer secFullTime = mElapsedFullTimeInSeconds % 60;
        Integer minPartTime = mElapsedPartTimeInSeconds / 60;
        Integer secPartTime = mElapsedPartTimeInSeconds % 60;

        String formattedMinFullTime = String.format("%02d", minFullTime);
        String formattedSecFullTime = String.format("%02d", secFullTime);
        String formattedMinPartTime = String.format("%02d", minPartTime);
        String formattedSecPartTime = String.format("%02d", secPartTime);

        mFullTimerMinTextView.setText(formattedMinFullTime);
        mFullTimerSecTextView.setText(formattedSecFullTime);
        //mPartTimerMinTextView.setText(formattedMinPartTime);
        //mPartTimerSecTextView.setText(formattedSecPartTime);
    }

    private void changeGuiElementsOnStartMatch()
    {
        for (Player player : mSelectedPlayerList)
        {
            PlayerSelectedListAdapter.ViewHolder viewHolder = mSelectedPlayerListAdapter.getViewHolder(player);
            viewHolder.textViewPlayerName.setEnabled(true);
            viewHolder.textViewPlayerNo.setEnabled(true);
        }
        //------------------------------------------------------------------------------------------
        mGoalGuestTeamButton.setEnabled(true);
        mStartMatchButton.setEnabled(false);
        mPauseMatchButton.setEnabled(true);
        mFinishMatchButton.setEnabled(true);
        //------------------------------------------------------------------------------------------
        mTimerThread.start();
    }

    private void changeGuiElementsOnPauseMatch()
    {
        for (Player player : mSelectedPlayerList)
        {
            PlayerSelectedListAdapter.ViewHolder viewHolder = mSelectedPlayerListAdapter.getViewHolder(player);
            viewHolder.textViewPlayerName.setEnabled(false);
            viewHolder.textViewPlayerNo.setEnabled(false);
        }
        //------------------------------------------------------------------------------------------
        mGoalGuestTeamButton.setEnabled(false);
        mStartMatchButton.setEnabled(true);
        mPauseMatchButton.setEnabled(false);
        mFinishMatchButton.setEnabled(false);

        mTimerThread.interrupt();
    }

    private void changeGuiElementsOnFinishMatch()
    {
        for (Player player : mSelectedPlayerList)
        {
            PlayerSelectedListAdapter.ViewHolder viewHolder = mSelectedPlayerListAdapter.getViewHolder(player);
            viewHolder.textViewPlayerName.setEnabled(false);
            viewHolder.textViewPlayerNo.setEnabled(false);
        }

        mGoalGuestTeamButton.setEnabled(false);
        mStartMatchButton.setEnabled(false);
        mPauseMatchButton.setEnabled(false);
        mFinishMatchButton.setEnabled(false);

        mTimerThread.interrupt();
    }

    //---------------------------------------------------------------------------------------------
    // EVENT HANDLING
    //---------------------------------------------------------------------------------------------

    private interface PlayerClickListener
    {
        void onPlayerClicked(Player player);
    }

    private interface EventClickListener
    {
        void onEventClicked(Event event);
    }

    private interface EventLongClickListener
    {
        void onLongEventClicked(Event event);
    }

    class MyStartMatchClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            changeGuiElementsOnStartMatch();
            handleAddNoPlayerEvent(Event.EventType.MATCH_START);
        }
    }

    class MyPauseMatchClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            changeGuiElementsOnPauseMatch();
            handleAddNoPlayerEvent(Event.EventType.MATCH_PAUSE);
        }
    }

    class MyFinishMatchClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            changeGuiElementsOnFinishMatch();
            handleAddNoPlayerEvent(Event.EventType.MATCH_FINISH);
            showFinishMatchDialog();
        }
    }

    class MyPlayerClickListener implements PlayerClickListener
    {
        private Activity mActivity;

        public MyPlayerClickListener(Activity activity)
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
            Button faultButton      = (Button) eventTypeDialog.findViewById(R.id.button_eventtype_fault);
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
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_GOAL);
                    eventTypeDialog.dismiss();
                }
            });

            assistButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_ASSIST);
                    eventTypeDialog.dismiss();
                }
            });

            faultButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_FAULT);
                    eventTypeDialog.dismiss();
                }
            });

            playerInButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_IN);
                    eventTypeDialog.dismiss();
                }
            });

            playerOutButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_OUT);
                    eventTypeDialog.dismiss();
                }
            });

            yellowCardButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_YELLOW_CARD);
                    eventTypeDialog.dismiss();
                }
            });

            redCardButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_RED_CARD);
                    eventTypeDialog.dismiss();
                }
            });

            injuredButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    handleAddPlayerEvent(player, Event.EventType.OWN_PLAYER_INJURED);
                    eventTypeDialog.dismiss();
                }
            });

            eventTypeDialog.show();
        }
    }

    class MyGoalGuestTeamClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            handleAddNoPlayerEvent(Event.EventType.OPPOSING_TEAM_GOAL);
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
