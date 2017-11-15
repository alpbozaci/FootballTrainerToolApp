package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

public class PlayerEvaluateListActivity extends Activity
{
    private static final String LOG_TAG = PlayerEvaluateListActivity.class.getSimpleName();

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

        loadDBPlayerList();

        ListView listView = (ListView) findViewById(R.id.listview_evaluate_player);
        mListAdapter = new PlayerEvaluateListAdapter(this, mPlayerList, new MyPlayerEvaluateClickListener());
        listView.setAdapter(mListAdapter);

    }

    private void loadDBPlayerList()
    {
        List<Player> dbPlayerList = mDatabaseAdapter.getPlayerList();

        for (Player player : dbPlayerList)
        {
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
}
