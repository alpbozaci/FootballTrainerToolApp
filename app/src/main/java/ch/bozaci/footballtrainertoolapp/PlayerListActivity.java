package ch.bozaci.footballtrainertoolapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton mAddButton;
    private List<Player> mPlayerList;
    private ArrayAdapter<Player> mPlayerListAdapter;
    private ListView mPlayerListView;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        mAddButton = (FloatingActionButton) findViewById(R.id.button_add_player);
        mAddButton.setOnClickListener(new AddPlayerButtonClickListener());

        mPlayerList = new ArrayList<>();
        mPlayerListAdapter = new ArrayAdapter<Player>(this, R.layout.item_player, R.id.textview_player, mPlayerList);

        mPlayerListView = (ListView) findViewById(R.id.listview_player);
        mPlayerListView.setAdapter(mPlayerListAdapter);

        mPlayerListView.setOnItemClickListener(new PlayerClickListener());
        mPlayerListView.setOnItemLongClickListener(new PlayerLongClickListener());

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        loadPlayerList();
    }

    private void loadPlayerList()
    {
        mPlayerList.clear();
        List<Player> dbPlayerList = databaseAdapter.getPlayerList();

        for (Player player : dbPlayerList)
        {
            mPlayerList.add(player);
        }
        mPlayerListAdapter.notifyDataSetChanged();
    }

    class AddPlayerButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            showAddPlayerDialog();
        }
    }

    class PlayerClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Player player = mPlayerList.get(position);
            showEditPlayerDialog(player);
        }
    }

    class PlayerLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            Player player = mPlayerList.get(position);
            showConfirmDeletePlayerDialog(player);
            return true;
        }
    }

    private void showAddPlayerDialog()
    {
        final Dialog playerDialog = new Dialog(this);
        playerDialog.setContentView(R.layout.dialog_player);
        playerDialog.setTitle(R.string.add_player);

        Button save     = (Button) playerDialog.findViewById(R.id.button_player_dialog_save);
        Button cancel = (Button) playerDialog.findViewById(R.id.button_player_dialog_cancel);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Player player = new Player();
                fillPlayerValuesFromDialog(playerDialog, player);
                addPlayer(player);
                playerDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playerDialog.dismiss();
            }
        });

        playerDialog.show();
    }

    private void showEditPlayerDialog(final Player player)
    {
        final Dialog playerDialog = new Dialog(this);
        playerDialog.setContentView(R.layout.dialog_player);
        playerDialog.setTitle(R.string.edit_player);

        EditText editTextFirstName    = (EditText) playerDialog.findViewById(R.id.edittext_player_firstname);
        EditText editTextLastName     = (EditText) playerDialog.findViewById(R.id.edittext_player_lastname);
        EditText editTextPlayerNumber = (EditText) playerDialog.findViewById(R.id.edittext_player_number);

        editTextFirstName.setText(player.getFirstName());
        editTextLastName.setText(player.getLastName());
        editTextPlayerNumber.setText(player.getPlayerNumber().toString());

        Button ok     = (Button) playerDialog.findViewById(R.id.button_player_dialog_save);
        Button cancel = (Button) playerDialog.findViewById(R.id.button_player_dialog_cancel);

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fillPlayerValuesFromDialog(playerDialog, player);
                updatePlayer(player);
                playerDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playerDialog.dismiss();
            }
        });

        playerDialog.show();
    }

    private void showConfirmDeletePlayerDialog(final Player player)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.title_delete_player));
        alertDialog.setMessage(getString(R.string.message_delete_player));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                deletePlayer(player);
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

    private Player fillPlayerValuesFromDialog(Dialog dialog, Player player)
    {
        EditText editTextFirstName    = (EditText) dialog.findViewById(R.id.edittext_player_firstname);
        EditText editTextLastName     = (EditText) dialog.findViewById(R.id.edittext_player_lastname);
        EditText editTextPlayerNumber = (EditText) dialog.findViewById(R.id.edittext_player_number);

        String firstName    = editTextFirstName.getText().toString();
        String lastName     = editTextLastName.getText().toString();
        String playerNumber = editTextPlayerNumber.getText().toString();

        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setPlayerNumber(Integer.valueOf(playerNumber));

        return player;
    }

    private void addPlayer(Player player)
    {
        databaseAdapter.addPlayer(player);
        Log.i(LOG_TAG, "player added: " + player.toString());
        loadPlayerList();
    }

    private void updatePlayer(Player player)
    {
        databaseAdapter.updatePlayer(player);
        Log.i(LOG_TAG, "player updated: " + player.toString());
        loadPlayerList();
    }

    private void deletePlayer(Player player)
    {
        databaseAdapter.deletePlayer(player.getId());
        Log.i(LOG_TAG, "player deleted: " + player.toString());
        loadPlayerList();
    }
}
