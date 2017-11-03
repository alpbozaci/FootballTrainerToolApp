package ch.bozaci.footballtrainertoolapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Player;
import ch.bozaci.footballtrainertoolapp.util.PictureUtil;

public class PlayerListActivity extends Activity
{
    private static final String LOG_TAG = PlayerListActivity.class.getSimpleName();

    private int REQUEST_IMAGE_CAPTURE = 1;

    private FloatingActionButton mAddButton;
    private List<Player> mPlayerList;
    private PlayerListAdapter mPlayerListAdapter;
    private ListView mPlayerListView;
    private ImageView mImageViewPicture;
    private DatabaseAdapter databaseAdapter;

    private enum DialogMode
    {
        ADD,
        EDIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate()");

        setContentView(R.layout.activity_player_list);

        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext());

        mAddButton = (FloatingActionButton) findViewById(R.id.button_add_player);
        mAddButton.setOnClickListener(new AddPlayerButtonClickListener());

        mPlayerList = new ArrayList<>();
        mPlayerListAdapter = new PlayerListAdapter(this, mPlayerList);

        mPlayerListView = (ListView) findViewById(R.id.listview_player);
        mPlayerListView.setAdapter(mPlayerListAdapter);

        mPlayerListView.setOnItemClickListener(new PlayerClickListener());
        mPlayerListView.setOnItemLongClickListener(new PlayerLongClickListener());

        loadPlayerList();
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
            showPlayerDialog(DialogMode.ADD, new Player());
        }
    }

    class PlayerClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Player player = mPlayerList.get(position);
            showPlayerDialog(DialogMode.EDIT, player);
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

    private void showPlayerDialog(final DialogMode dialogMode, final Player player)
    {
        final Dialog playerDialog = new Dialog(this);
        playerDialog.setContentView(R.layout.dialog_player);

        mImageViewPicture = (ImageView) playerDialog.findViewById(R.id.imageview_picture);

        if (dialogMode == DialogMode.ADD)
        {
            playerDialog.setTitle(R.string.title_add_player);
        }
        if (dialogMode == DialogMode.EDIT)
        {
            playerDialog.setTitle(R.string.title_edit_player);

            EditText editTextFirstName    = (EditText) playerDialog.findViewById(R.id.edittext_player_firstname);
            EditText editTextLastName     = (EditText) playerDialog.findViewById(R.id.edittext_player_lastname);
            EditText editTextPlayerNumber = (EditText) playerDialog.findViewById(R.id.edittext_player_number);

            editTextFirstName.setText(player.getFirstName());
            editTextLastName.setText(player.getLastName());
            editTextPlayerNumber.setText(player.getPlayerNumber().toString());

            Bitmap bitmap = PictureUtil.convertByteArrayToBitmap(player.getPicture());
            mImageViewPicture.setImageBitmap(bitmap);
        }

        Button camera = (Button) playerDialog.findViewById(R.id.button_camera);
        Button save   = (Button) playerDialog.findViewById(R.id.button_player_dialog_save);
        Button cancel = (Button) playerDialog.findViewById(R.id.button_player_dialog_cancel);

        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleTakePicture();
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fillPlayerValuesFromDialog(playerDialog, player);

                if (dialogMode == DialogMode.ADD)
                {
                    addPlayer(player);
                }
                if (dialogMode == DialogMode.EDIT)
                {
                    updatePlayer(player);
                }

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

        byte[] picture = new byte[0];
        if (mImageViewPicture.getDrawable() != null)
        {
            Bitmap bitmap = ((BitmapDrawable)mImageViewPicture.getDrawable()).getBitmap();
            picture = PictureUtil.convertBitmapToByteArray(bitmap);
        }

        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setPlayerNumber(Integer.valueOf(playerNumber));
        player.setPicture(picture);

        return player;
    }

    private void handleTakePicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
            Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            mImageViewPicture.setImageBitmap(bitmap);
        }
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
        // check if player has an registered event

        try
        {
            List<Event> eventList = databaseAdapter.getEventList(player);
            if (eventList.isEmpty())
            {
                databaseAdapter.deletePlayer(player.getId());
                Log.i(LOG_TAG, "player deleted: " + player.toString());
                loadPlayerList();
            }
            else
            {
                Toast.makeText(this,getResources().getText(R.string.delete_player_not_allowed), Toast.LENGTH_LONG).show();
            }
        }
        catch (ParseException ex)
        {
            Log.e(LOG_TAG, "error reading events: " + ex.getMessage());
        }
    }

}
