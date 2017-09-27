package ch.bozaci.footballtrainertoolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Button mMatchListButton;
    private Button mPlayerListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mMatchListButton = (Button) findViewById(R.id.button_match_list);
        mPlayerListButton = (Button) findViewById(R.id.button_player_list);

        mMatchListButton.setOnClickListener(new MatchListButtonClickListener());
        mPlayerListButton.setOnClickListener(new PlayerListButtonClickListener());

        DatabaseAdapter.getInstance(getApplicationContext()).open();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        DatabaseAdapter.getInstance(getApplicationContext()).close();
    }

    class MatchListButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, MatchListActivity.class);
            startActivity(intent);
        }
    }

    class PlayerListButtonClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(MainActivity.this, PlayerListActivity.class);
            startActivity(intent);
        }
    }
}