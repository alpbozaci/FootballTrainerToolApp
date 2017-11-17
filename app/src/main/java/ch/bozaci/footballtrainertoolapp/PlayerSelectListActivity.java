package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

public class PlayerSelectListActivity extends Activity
{
    private static final String LOG_TAG = MatchActivity.class.getSimpleName();

    private Match mMatch;
    private List<Player> mPlayerList;
    private List<Integer> mSelectedPlayerIdList;
    private List<Integer> mUnselectedPlayerIdList;
    private PlayerSelectListAdapter mSelectPlayerAdapter;
    private DatabaseAdapter mDatabaseAdapter;

    public static final String INTENTVALUE_MATCH = "match";
    public static final String INTENTVALUE_SElECTED_PLAYER_ID_LIST = "selectedPlayerIdList";
    public static final String INTENTVALUE_UNSElECTED_PLAYER_ID_LIST = "unselectedPlayerIdList";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_select_list);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            mMatch = (Match)bundle.getSerializable("match");
        }

        mDatabaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        mPlayerList = new ArrayList<>();
        mSelectedPlayerIdList = new ArrayList<>();
        mUnselectedPlayerIdList = new ArrayList<>();

        loadDBPlayerList();

        ListView listView = (ListView) findViewById(R.id.listview_select_player);
        mSelectPlayerAdapter = new PlayerSelectListAdapter(this, mPlayerList, new MyActivateDeactivatePlayerClickListener());
        listView.setAdapter(mSelectPlayerAdapter);

        Button button = (Button) findViewById(R.id.button_next);
        button.setOnClickListener(new MyNextButtonClickListener());
    }

    private void loadDBPlayerList()
    {
        List<Player> dbPlayerList = mDatabaseAdapter.getPlayerList(Player.PlayerState.ACTIVE);

        for (Player player : dbPlayerList)
        {
            mPlayerList.add(player);
            handlePlayerUnselected(player);
        }
    }

    private void handlePlayerSelected(Player player)
    {
        mSelectedPlayerIdList.add(player.getId());
        mUnselectedPlayerIdList.remove(player.getId());
    }

    private void handlePlayerUnselected(Player player)
    {
        mSelectedPlayerIdList.remove(player.getId());
        mUnselectedPlayerIdList.add(player.getId());
    }

    private interface ActivateDeactivatePlayerClickListener
    {
        void onActivatePlayerClicked(Player player);
        void onDeactivatePlayerClicked(Player player);
    }

    class MyActivateDeactivatePlayerClickListener implements ActivateDeactivatePlayerClickListener
    {
        @Override
        public void onActivatePlayerClicked(Player player)
        {
            Log.i(LOG_TAG, "selected: " + player.getFirstName());
            handlePlayerSelected(player);
        }

        @Override
        public void onDeactivatePlayerClicked(Player player)
        {
            Log.i(LOG_TAG, "deselected: " + player.getFirstName());
            handlePlayerUnselected(player);
        }
    }

    class MyNextButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (mSelectedPlayerIdList.isEmpty())
            {
                Toast.makeText(PlayerSelectListActivity.this, getResources().getText(R.string.player_list_empty), Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(PlayerSelectListActivity.this, MatchActivity.class);
            intent.putExtra(INTENTVALUE_MATCH, mMatch);
            intent.putIntegerArrayListExtra(INTENTVALUE_SElECTED_PLAYER_ID_LIST, (ArrayList)mSelectedPlayerIdList);
            intent.putIntegerArrayListExtra(INTENTVALUE_UNSElECTED_PLAYER_ID_LIST, (ArrayList)mUnselectedPlayerIdList);

            startActivity(intent);
        }
    }
}
