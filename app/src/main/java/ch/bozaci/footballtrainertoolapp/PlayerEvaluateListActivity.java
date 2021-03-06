package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;
import ch.bozaci.footballtrainertoolapp.util.IntentConstants;

public class PlayerEvaluateListActivity extends Activity
{
    private static final String LOG_TAG = PlayerEvaluateListActivity.class.getSimpleName();

    private Match mMatch;
    private List<Player> mPlayerList;
    private PlayerEvaluateListAdapter mListAdapter;
    private DatabaseAdapter mDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_evaluate);

        mDatabaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        mPlayerList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        mMatch = (Match)bundle.getSerializable(IntentConstants.INTENTVALUE_MATCH);
        List<Integer> playerIdList = (ArrayList<Integer>)bundle.getSerializable(IntentConstants.INTENTVALUE_SElECTED_PLAYER_ID_LIST);
        loadDBPlayerList(playerIdList);

        ListView listView = (ListView) findViewById(R.id.listview_evaluate_player);
        mListAdapter = new PlayerEvaluateListAdapter(this, mPlayerList, new MyPlayerEvaluateClickListener());
        listView.setAdapter(mListAdapter);

        Button button = (Button) findViewById(R.id.button_evaluate_player_next);
        button.setOnClickListener(new PlayerEvaluateListActivity.MyNextButtonClickListener());
    }

    private void loadDBPlayerList(List<Integer> playerIdList)
    {
        for (Integer id : playerIdList)
        {
            Player player = mDatabaseAdapter.getPlayer(id);
            mPlayerList.add(player);
        }
    }

    private interface PlayerEvaluateClickListener
    {
        void onClicked(Player player);
    }

    class MyPlayerEvaluateClickListener implements PlayerEvaluateClickListener
    {
        @Override
        public void onClicked(Player player)
        {
            Log.i(LOG_TAG, "selected: " + player.getFirstName());

            showEvaluationDialog();
        }
    }

    private void showEvaluationDialog()
    {
        final Dialog evaluateDialog = new Dialog(this);
        evaluateDialog.setContentView(R.layout.dialog_evaluation);

        final TextView value = (TextView) evaluateDialog.findViewById(R.id.textview_evaluate_total_value);
        final SeekBar seekBar = (SeekBar) evaluateDialog.findViewById(R.id.seekBar_evaluate_total);
        final Button save = (Button) evaluateDialog.findViewById(R.id.button_evaluate_dialog_save);
        final Button cancel = (Button) evaluateDialog.findViewById(R.id.button_evaluate_dialog_cancel);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                System.out.println(Integer.toString(progress));
                value.setText(Integer.toString(progress * 25) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                evaluateDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                evaluateDialog.dismiss();
            }
        });

        evaluateDialog.show();
    }

    class MyNextButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(PlayerEvaluateListActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
