package ch.bozaci.footballtrainertoolapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MatchActivity extends AppCompatActivity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<Player> mPlayerList;
    private ArrayAdapter<Player> mPlayerListAdapter;
    private ListView mPlayerListView;
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


        Match match = null;
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            match = (Match)bundle.getSerializable("match");
            System.out.println(match.toString());

            TextView textView = (TextView) findViewById(R.id.textview_started_match);
            textView.setText(match.getHomeTeam() + " - " + match.getGuestTeam());
        }

        mPlayerList = new ArrayList<>();

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadPlayerList();

        SelectPlayerListAdapter selectPlayerListAdapter = new SelectPlayerListAdapter(this, mPlayerList, new MyPlayerClickListener(), new MyActivateDeactivatePlayerClickListener());

        mPlayerListView = (ListView) findViewById(R.id.listview_select_player);
        mPlayerListView.setAdapter(selectPlayerListAdapter);
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

    private interface PlayerClickListener
    {
        public void onPlayerClicked(Player player);
    }

    private interface ActivateDeactivatePlayerClickListener
    {
        public void onActivatePlayerClicked(Player player);
        public void onDeactivatePlayerClicked(Player player);
    }

    class MyPlayerClickListener implements PlayerClickListener
    {
        @Override
        public void onPlayerClicked(Player player)
        {
            System.out.println("clicked: " + player.toString());
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
}
