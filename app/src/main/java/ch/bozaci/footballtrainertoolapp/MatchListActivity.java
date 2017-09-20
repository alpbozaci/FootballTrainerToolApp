package ch.bozaci.footballtrainertoolapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MatchListActivity extends AppCompatActivity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String MATCH_TYPE_CHAMPIONSHIP = "CHAMPIONSHIP";
    private final String MATCH_TYPE_TEST         = "TEST";

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
        mMatchListAdapter = new ArrayAdapter<Match>(this, R.layout.item_match, R.id.textview_match, mMatchList);

        mMatchListView = (ListView) findViewById(R.id.listview_match);
        mMatchListView.setAdapter(mMatchListAdapter);

        mMatchListView.setOnItemClickListener(new MatchClickListener());
        mMatchListView.setOnItemLongClickListener(new MatchLongClickListener());

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadMatchList();
    }

    private void loadMatchList()
    {
        mMatchList.clear();
        List<Match> dbMatchList = databaseAdapter.getMatchList();

        for (Match match : dbMatchList)
        {
            mMatchList.add(match);
        }
        mMatchListAdapter.notifyDataSetChanged();
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
            showEditOrDeleteMatchDialog(match);
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
            Intent intent = new Intent(MatchListActivity.this, MatchActivity.class);
            intent.putExtra("match", match);
            startActivity(intent);

            return true;
        }
    }

    private void showAddMatchDialog()
    {
        final Dialog matchDialog = new Dialog(this);
        matchDialog.setContentView(R.layout.dialog_match);
        matchDialog.setTitle(R.string.add_match);

        Button save   = (Button) matchDialog.findViewById(R.id.button_match_dialog_save);
        Button delete = (Button) matchDialog.findViewById(R.id.button_match_dialog_delete);
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

        delete.setEnabled(false);

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

    private void showEditOrDeleteMatchDialog(final Match match)
    {
        final Dialog matchDialog = new Dialog(this);
        matchDialog.setContentView(R.layout.dialog_match);
        matchDialog.setTitle(R.string.add_match);

        EditText editTextHometeam  = (EditText)matchDialog.findViewById(R.id.edittext_match_hometeam);
        EditText editTextGuestteam = (EditText)matchDialog.findViewById(R.id.edittext_match_guestteam);
        RadioButton radioButtonMatchTypeChampionship = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_championship);
        RadioButton radioButtonMatchTypeTest         = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_test);
        DatePicker datePicker      = (DatePicker)matchDialog.findViewById(R.id.datepicker_match_date);
        TimePicker timePicker      = (TimePicker)matchDialog.findViewById(R.id.timepicker_match_time);

        editTextHometeam.setText(match.getHomeTeam());
        editTextGuestteam.setText(match.getGuestTeam());
        if (match.getType().equals(MATCH_TYPE_CHAMPIONSHIP))
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
        Button delete = (Button) matchDialog.findViewById(R.id.button_match_dialog_delete);
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

        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showConfirmDeleteMatchDialog(match);
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
        RadioButton radioButtonMatchTypeChampionship = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_championship);
        RadioButton radioButtonMatchTypeTest         = (RadioButton)matchDialog.findViewById(R.id.radiobutton_match_type_test);
        DatePicker datePicker      = (DatePicker)matchDialog.findViewById(R.id.datepicker_match_date);
        TimePicker timePicker      = (TimePicker)matchDialog.findViewById(R.id.timepicker_match_time);

        String homeTeam = editTextHometeam.getText().toString();
        String guestTeam = editTextGuestteam.getText().toString();
        String matchType;

        if (radioButtonMatchTypeChampionship.isChecked())
        {
            matchType = MATCH_TYPE_CHAMPIONSHIP;
        }
        else
        {
            matchType = MATCH_TYPE_TEST;
        }

        Date date = Util.getDate(datePicker, timePicker);

        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setDate(date);
        match.setType(matchType);

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
