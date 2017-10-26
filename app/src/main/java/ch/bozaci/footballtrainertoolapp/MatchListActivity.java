package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.util.DateUtil;

public class MatchListActivity extends Activity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton mAddButton;
    private List<Match> mMatchList;
    private ArrayAdapter<Match> mMatchListAdapter;
    private ListView mMatchListView;

    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        mAddButton = (FloatingActionButton) findViewById(R.id.button_add_match);
        mAddButton.setOnClickListener(new AddMatchClickListener());

        mMatchList = new ArrayList<>();
        mMatchListAdapter = new ArrayAdapter<>(this, R.layout.item_match, R.id.textview_match, mMatchList);

        mMatchListView = (ListView) findViewById(R.id.listview_match);
        mMatchListView.setAdapter(mMatchListAdapter);

        mMatchListView.setOnItemClickListener(new MatchClickListener());
        mMatchListView.setOnItemLongClickListener(new MatchLongClickListener());

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadMatchList();
    }

    private void loadMatchList()
    {
        try
        {
            mMatchList.clear();
            List<Match> dbMatchList = databaseAdapter.getMatchList();
            for (Match match : dbMatchList)
            {
                mMatchList.add(match);
            }
            mMatchListAdapter.notifyDataSetChanged();
        }
        catch (ParseException ex)
        {
            String errText = "Wrong date format: " + ex.getMessage();
            Log.e(LOG_TAG, errText);
            Toast.makeText(this, errText, Toast.LENGTH_LONG);
        }
    }

    class AddMatchClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            showAddMatchDialog();
        }
    }

    class MatchClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Match match = mMatchList.get(position);
            showEditMatchDialog(match);
        }
    }

    /**
     * Start SelectPlayerListActivity
     */
    class MatchLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            Match match = mMatchList.get(position);
            showConfirmDeleteMatchDialog(match);
            return true;
        }
    }

    private void showAddMatchDialog()
    {
        final Dialog matchDialog = new Dialog(this);
        matchDialog.setContentView(R.layout.dialog_match);
        matchDialog.setTitle(R.string.title_add_match);

        Button save   = (Button) matchDialog.findViewById(R.id.button_match_dialog_save);
        Button cancel = (Button) matchDialog.findViewById(R.id.button_match_dialog_cancel);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Match match = new Match();
                fillMatchValuesFromDialog(matchDialog, match);
                addMatch(match);
                matchDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                matchDialog.dismiss();
            }
        });

        matchDialog.show();
    }

    private void showEditMatchDialog(final Match match)
    {
        final Dialog matchDialog = new Dialog(this);
        matchDialog.setContentView(R.layout.dialog_match);
        matchDialog.setTitle(R.string.title_add_match);

        EditText editTextHometeam  = (EditText)matchDialog.findViewById(R.id.edittext_match_hometeam);
        EditText editTextGuestteam = (EditText)matchDialog.findViewById(R.id.edittext_match_guestteam);
        RadioButton radioButtonMatchLocationTypeHomeGame = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_locationtype_homegame);
        RadioButton radioButtonMatchLocationTypeAwayGame = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_locationtype_awaygame);
        RadioButton radioButtonMatchTypeChampionship = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_championship);
        RadioButton radioButtonMatchTypeTest         = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_test);
        DatePicker datePicker = (DatePicker)matchDialog.findViewById(R.id.datepicker_match_date);
        TimePicker timePicker = (TimePicker)matchDialog.findViewById(R.id.timepicker_match_time);

        editTextHometeam.setText(match.getHomeTeam());
        editTextGuestteam.setText(match.getGuestTeam());

        if (match.getLocationType().equals(Match.LocationType.HOME_GAME.getType()))
        {
            radioButtonMatchLocationTypeHomeGame.setChecked(true);
        }
        else
        {
            radioButtonMatchLocationTypeAwayGame.setChecked(true);
        }

        if (match.getType().equals(Match.MatchType.CHAMPIONSSHIP.getType()))
        {
            radioButtonMatchTypeChampionship.setChecked(true);
        }
        else
        {
            radioButtonMatchTypeTest.setChecked(true);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(match.getDate());

        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        timePicker.setHour(cal.get(Calendar.HOUR));
        timePicker.setMinute(cal.get(Calendar.MINUTE));

        Button save   = (Button) matchDialog.findViewById(R.id.button_match_dialog_save);
        Button cancel = (Button) matchDialog.findViewById(R.id.button_match_dialog_cancel);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fillMatchValuesFromDialog(matchDialog, match);
                updateMatch(match);
                matchDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                matchDialog.dismiss();
            }
        });

        matchDialog.show();
    }

    private void showConfirmDeleteMatchDialog(final Match match)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_delete_match));
        alertDialog.setMessage(getString(R.string.message_delete_match));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                deleteMatch(match);
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

    private void fillMatchValuesFromDialog(Dialog matchDialog, Match match)
    {
        EditText editTextHometeam  = (EditText)matchDialog.findViewById(R.id.edittext_match_hometeam);
        EditText editTextGuestteam = (EditText)matchDialog.findViewById(R.id.edittext_match_guestteam);
        RadioButton radioButtonMatchLocationTypeHomeGame = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_locationtype_homegame);
        RadioButton radioButtonMatchLocationTypeAwayGame = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_locationtype_awaygame);
        RadioButton radioButtonMatchTypeChampionship = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_championship);
        RadioButton radioButtonMatchTypeTest         = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_test);
        DatePicker datePicker      = (DatePicker)matchDialog.findViewById(R.id.datepicker_match_date);
        TimePicker timePicker      = (TimePicker)matchDialog.findViewById(R.id.timepicker_match_time);

        String homeTeam = editTextHometeam.getText().toString();
        String guestTeam = editTextGuestteam.getText().toString();

        Match.LocationType locationType;
        if (radioButtonMatchLocationTypeHomeGame.isChecked())
        {
            locationType = Match.LocationType.HOME_GAME;
        }
        else
        {
            locationType = Match.LocationType.AWAY_GAME;
        }

        Match.MatchType matchType;
        if (radioButtonMatchTypeChampionship.isChecked())
        {
            matchType = Match.MatchType.CHAMPIONSSHIP;
        }
        else
        {
            matchType = Match.MatchType.TEST;
        }

        Date date = DateUtil.getDate(datePicker, timePicker);

        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setDate(date);
        match.setType(matchType.getType());
        match.setLocationType(locationType.getType());

        Log.v(LOG_TAG, "match: " + match.toString());
    }

    private void addMatch(Match match)
    {
        databaseAdapter.addMatch(match);
        Log.i(LOG_TAG, "match added: " + match.toString());
        loadMatchList();
    }

    private void updateMatch(Match match)
    {
        databaseAdapter.updateMatch(match);
        Log.i(LOG_TAG, "match updated: " + match.toString());
        loadMatchList();
    }

    private void deleteMatch(Match match)
    {
        databaseAdapter.deleteMatch(match.getId());
        Log.i(LOG_TAG, "match deleted: " + match.toString());
        loadMatchList();
    }

}
