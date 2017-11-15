package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;

public class StatisticActivity extends Activity
{
    private static final String LOG_TAG = StatisticActivity.class.getSimpleName();

    private DatabaseAdapter mDatabaseAdapter;

    String[] mColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        mColumns = new String[]{
                "",
                getString(R.string.eventtype_own_player_goal),
                getString(R.string.eventtype_own_player_assist),
                getString(R.string.eventtype_own_player_goodplay),
                getString(R.string.eventtype_own_player_badplay),
                getString(R.string.eventtype_own_player_in),
                getString(R.string.eventtype_own_player_out),
                getString(R.string.eventtype_own_player_injured),
        };

        Bundle bundle = getIntent().getExtras();
        Match match = (Match)bundle.getSerializable(PlayerSelectListActivity.INTENTVALUE_MATCH);

        mDatabaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout_statistic);
        createTable(tableLayout, match);
    }

    private void createTable(TableLayout tableLayout, Match match)
    {
        createHeaderRow(tableLayout);
        createDataRows(tableLayout, match);
    }

    private void createHeaderRow(TableLayout tableLayout)
    {
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1,1,1,1);
        tableRowParams.weight = 1;

        TableRow tableRow = new TableRow(this);
        tableRowParams.width = 100;

        for (int n = 0; n < mColumns.length; n++)
        {
            TextView textView = createTextView(mColumns[n]);
            tableRow.addView(textView, tableRowParams);
        }

        tableLayout.addView(tableRow);
    }

    private void createDataRows(TableLayout tableLayout, Match match)
    {
        List<Player> playerList = mDatabaseAdapter.getPlayerList();

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1,1,1,1);
        tableRowParams.weight = 1;
        tableRowParams.width = 1;

        try
        {
            TableRow tableRow = null;
            Integer amount;
            TextView textView;

            for (Player player : playerList)
            {
                tableRow = new TableRow(this);

                Boolean b = mDatabaseAdapter.hasEvent(match, player, Event.EventType.OWN_PLAYER_PRESENT);

                // player name
                if (b)
                {
                    textView = createTextView(player.getFirstName(), Color.GREEN);
                }
                else
                {
                    textView = createTextView(player.getFirstName(), Color.LTGRAY, Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                }
                tableRow.addView(textView, tableRowParams);

                if (b)
                {
                    // goal
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_GOAL);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // assist
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_ASSIST);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // goodplay
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_GOODPLAY);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // badplay
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_BADPLAY);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // in
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_IN);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // out
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_OUT);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);

                    // injured
                    amount = mDatabaseAdapter.eventAmount(match, player, Event.EventType.OWN_PLAYER_INJURED);
                    textView = createTextView(String.valueOf(amount));
                    tableRow.addView(textView, tableRowParams);
                }
                else
                {
                    // goal
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // assist
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // goodplay
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // badplay
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // in
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // out
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);

                    // injured
                    textView = createTextView("-");
                    tableRow.addView(textView, tableRowParams);
                }

                tableLayout.addView(tableRow);
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private TextView createTextView(String text)
    {
        return createTextView(text, Color.WHITE, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
    }

    private TextView createTextView(String text, Integer backgroundColor)
    {
        return createTextView(text, backgroundColor, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
    }

    private TextView createTextView(String text, Typeface typeface)
    {
        return createTextView(text, Color.WHITE, typeface);
    }

    private TextView createTextView(String text, Integer backgroundColor, Typeface typeface)
    {
        TextView textView = new TextView(this);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextSize(10);
        textView.setText(text);
        textView.setTypeface(typeface);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void setCellData(TableLayout tableLayout, int rowIndex, int columnIndex, String text){

        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        textView.setText(text);
    }
}
