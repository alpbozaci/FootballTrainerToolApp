package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Match;

public class MatchSelectListActivity extends Activity
{
    private static final String LOG_TAG = MatchSelectListActivity.class.getSimpleName();

    private List<Match> mMatchList;
    private MatchSelectListAdapter mSelectMatchAdapter;
    private ListView mMatchListView;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");

        setContentView(R.layout.activity_match_select_list);

        mMatchList = new ArrayList<>();
        mSelectMatchAdapter = new MatchSelectListAdapter(this, mMatchList);

        mMatchListView = (ListView) findViewById(R.id.listview_match);
        mMatchListView.setAdapter(mSelectMatchAdapter);

        mMatchListView.setOnItemClickListener(new MatchSelectListActivity.MatchClickListener());

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadMatchList();
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

    private void loadMatchList()
    {
        try
        {
            mMatchList.clear();
            List<Match> dbMatchList = databaseAdapter.getMatchList();
            for (Match match : dbMatchList)
            {
                List<Event> eventList = databaseAdapter.getEventList(match);
                match.setEventList(eventList);
                mMatchList.add(match);
            }
            mSelectMatchAdapter.notifyDataSetChanged();
        }
        catch (ParseException ex)
        {
            String errText = "Wrong date format: " + ex.getMessage();
            Log.e(LOG_TAG, errText);
            Toast.makeText(this, errText, Toast.LENGTH_LONG);
        }
    }

    class MatchClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Match match = mMatchList.get(position);
            Intent intent = new Intent(MatchSelectListActivity.this, PlayerSelectListActivity.class);
            intent.putExtra("match", match);
            startActivity(intent);
        }
    }
}
