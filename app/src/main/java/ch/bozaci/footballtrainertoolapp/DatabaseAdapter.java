package ch.bozaci.footballtrainertoolapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Event;
import ch.bozaci.footballtrainertoolapp.dao.Match;
import ch.bozaci.footballtrainertoolapp.dao.Player;
import ch.bozaci.footballtrainertoolapp.util.DateUtil;

/**
 * Created by Alp.Bozaci on 24.08.2017.
 */

public class DatabaseAdapter
{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "footballtrainertoolapp";

    private Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqlDatabase;

    private static DatabaseAdapter mInstance;

    private DatabaseAdapter(Context context)
    {
        this.mContext = context;
        mDatabaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseAdapter getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new DatabaseAdapter(context);
        }
        return mInstance;
    }

    public void open()
    {
        mSqlDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close()
    {
        mSqlDatabase.close();
    }

    /**
     * only for unit tests
     */
    public void createAllTables()
    {
        mDatabaseHelper.createAllTables(mSqlDatabase);
    }

    /**
     * only for unit tests
     */
    public void dropAllTables()
    {
        mDatabaseHelper.dropAllTables(mSqlDatabase);
    }

    //==============================================================================================

    public Integer addPlayer(Player player)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Player.COLUMN_FIRSTNAME, player.getFirstName());
        contentValues.put(Player.COLUMN_LASTNAME, player.getLastName());
        contentValues.put(Player.COLUMN_PLAYERNUMBER, player.getPlayerNumber());
        contentValues.put(Player.COLUMN_PICTURE, player.getPicture());

        return (int)mSqlDatabase.insert(Player.TABLE, null, contentValues);
    }

    public Player getPlayer(Integer id)
    {
        Player player = null;

        String whereClause = Player.COLUMN_ID + " = " + id;
        Cursor cursor = mSqlDatabase.query(Player.TABLE, null, whereClause, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            player = new Player();
            player.setId(cursor.getInt(cursor.getColumnIndex(Player.COLUMN_ID)));
            player.setFirstName(cursor.getString(cursor.getColumnIndex(Player.COLUMN_FIRSTNAME)));
            player.setLastName(cursor.getString(cursor.getColumnIndex(Player.COLUMN_LASTNAME)));
            player.setPlayerNumber(cursor.getInt(cursor.getColumnIndex(Player.COLUMN_PLAYERNUMBER)));
            player.setPicture(cursor.getBlob(cursor.getColumnIndex(Player.COLUMN_PICTURE)));
        }

        return player;
    }

    public List<Player> getPlayerList()
    {
        List<Player> playerList = new ArrayList<>();

        Cursor cursor = mSqlDatabase.query(Player.TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Player player = new Player();
                player.setId(cursor.getInt(cursor.getColumnIndex(Player.COLUMN_ID)));
                player.setFirstName(cursor.getString(cursor.getColumnIndex(Player.COLUMN_FIRSTNAME)));
                player.setLastName(cursor.getString(cursor.getColumnIndex(Player.COLUMN_LASTNAME)));
                player.setPlayerNumber(cursor.getInt(cursor.getColumnIndex(Player.COLUMN_PLAYERNUMBER)));
                player.setPicture(cursor.getBlob(cursor.getColumnIndex(Player.COLUMN_PICTURE)));

                playerList.add(player);
            }
            while (cursor.moveToNext());
        }

        return playerList;
    }

    public Integer updatePlayer(Player player)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Player.COLUMN_FIRSTNAME, player.getFirstName());
        contentValues.put(Player.COLUMN_LASTNAME, player.getLastName());
        contentValues.put(Player.COLUMN_PLAYERNUMBER, player.getPlayerNumber());
        contentValues.put(Player.COLUMN_PICTURE, player.getPicture());

        String whereClause = Player.COLUMN_ID + " = " + player.getId();

        return mSqlDatabase.update(Player.TABLE, contentValues, whereClause, null);
    }

    public Integer deletePlayer(Integer id)
    {
        String whereClause = Player.COLUMN_ID + " = " + id;
        return mSqlDatabase.delete(Player.TABLE, whereClause, null);
    }

    public Integer deletePlayerList()
    {
        return mSqlDatabase.delete(Player.TABLE, null, null);
    }

    //==============================================================================================

    public Integer addMatch(Match match)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Match.COLUMN_HOMETEAM, match.getHomeTeam());
        contentValues.put(Match.COLUMN_GUESTTEAM, match.getGuestTeam());
        contentValues.put(Match.COLUMN_DATE, DateUtil.dateFormat.format(match.getDate()));
        contentValues.put(Match.COLUMN_TYPE, match.getType());
        contentValues.put(Match.COLUMN_LOCATION_TYPE, match.getLocationType());

        return (int)mSqlDatabase.insert(Match.TABLE, null, contentValues);
    }

    public Match getMatch(Integer id) throws ParseException
    {
        Match match = null;

        String whereClause = Match.COLUMN_ID + " = " + id;
        Cursor cursor = mSqlDatabase.query(Match.TABLE, null, whereClause, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            match = new Match();
            match.setId(cursor.getInt(cursor.getColumnIndex(Match.COLUMN_ID)));
            match.setHomeTeam(cursor.getString(cursor.getColumnIndex(Match.COLUMN_HOMETEAM)));
            match.setGuestTeam(cursor.getString(cursor.getColumnIndex(Match.COLUMN_GUESTTEAM)));
            match.setDate(DateUtil.dateFormat.parse(cursor.getString(cursor.getColumnIndex(Match.COLUMN_DATE))));
            match.setType(cursor.getString(cursor.getColumnIndex(Match.COLUMN_TYPE)));
            match.setLocationType(cursor.getString(cursor.getColumnIndex(Match.COLUMN_LOCATION_TYPE)));
        }

        return match;
    }

    public List<Match> getMatchList() throws ParseException
    {
        List<Match> matchList = new ArrayList<>();

        String orderBy = "date ASC";
        Cursor cursor = mSqlDatabase.query(Match.TABLE, null, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Match match = new Match();
                match.setId(cursor.getInt(cursor.getColumnIndex(Match.COLUMN_ID)));
                match.setHomeTeam(cursor.getString(cursor.getColumnIndex(Match.COLUMN_HOMETEAM)));
                match.setGuestTeam(cursor.getString(cursor.getColumnIndex(Match.COLUMN_GUESTTEAM)));
                match.setDate(DateUtil.dateFormat.parse(cursor.getString(cursor.getColumnIndex(Match.COLUMN_DATE))));
                match.setType(cursor.getString(cursor.getColumnIndex(Match.COLUMN_TYPE)));
                match.setLocationType(cursor.getString(cursor.getColumnIndex(Match.COLUMN_LOCATION_TYPE)));

                matchList.add(match);
            }
            while (cursor.moveToNext());
        }

        return matchList;
    }

    public Integer updateMatch(Match match)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Match.COLUMN_HOMETEAM, match.getHomeTeam());
        contentValues.put(Match.COLUMN_GUESTTEAM, match.getGuestTeam());
        contentValues.put(Match.COLUMN_DATE, DateUtil.dateFormat.format(match.getDate()));
        contentValues.put(Match.COLUMN_TYPE, match.getType());
        contentValues.put(Match.COLUMN_LOCATION_TYPE, match.getLocationType());

        String whereClause = Match.COLUMN_ID + " = " + match.getId();

        return mSqlDatabase.update(Match.TABLE, contentValues, whereClause, null);
    }

    public Integer deleteMatch(Integer id)
    {
        String whereClause = Match.COLUMN_ID + " = " + id;
        return mSqlDatabase.delete(Match.TABLE, whereClause, null);
    }

    public Integer deleteMatchList()
    {
       return mSqlDatabase.delete(Match.TABLE, null, null);
    }

    //==============================================================================================

    public Integer addEvent(Event event)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Event.COLUMN_MATCH_ID, event.getMatchId());
        contentValues.put(Event.COLUMN_PLAYER_ID, event.getPlayerId());
        contentValues.put(Event.COLUMN_TYPE, event.getType());
        contentValues.put(Event.COLUMN_DATE, DateUtil.dateFormat.format(event.getDate()));

        return (int)mSqlDatabase.insert(Event.TABLE, null, contentValues);
    }

    public Event getEvent(Integer id) throws ParseException
    {
        Event event = null;

        String whereClause = Event.COLUMN_ID + " = " + id;
        Cursor cursor = mSqlDatabase.query(Event.TABLE, null, whereClause, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_ID)));
            event.setMatchId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_MATCH_ID)));
            event.setPlayerId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_PLAYER_ID)));
            event.setType(cursor.getString(cursor.getColumnIndex(Event.COLUMN_TYPE)));
            event.setDate(DateUtil.dateFormat.parse(cursor.getString(cursor.getColumnIndex(Event.COLUMN_DATE))));
        }

        return event;
    }

    public List<Event> getEventList() throws ParseException
    {
        List<Event> eventList = new ArrayList<>();

        Cursor cursor = mSqlDatabase.query(Event.TABLE, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_ID)));
                event.setMatchId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_MATCH_ID)));
                event.setPlayerId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_PLAYER_ID)));
                event.setType(cursor.getString(cursor.getColumnIndex(Event.COLUMN_TYPE)));
                event.setDate(DateUtil.dateFormat.parse(cursor.getString(cursor.getColumnIndex(Event.COLUMN_DATE))));

                eventList.add(event);
            }
            while (cursor.moveToNext());
        }

        return eventList;
    }

    public List<Event> getEventList(Integer matchId) throws ParseException
    {
        List<Event> eventList = new ArrayList<>();

        String whereClause = Event.COLUMN_MATCH_ID + " = " + matchId;
        Cursor cursor = mSqlDatabase.query(Event.TABLE, null, whereClause, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                Event event = new Event();
                event.setId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_ID)));
                event.setMatchId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_MATCH_ID)));
                event.setPlayerId(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_PLAYER_ID)));
                event.setType(cursor.getString(cursor.getColumnIndex(Event.COLUMN_TYPE)));
                event.setDate(DateUtil.dateFormat.parse(cursor.getString(cursor.getColumnIndex(Event.COLUMN_DATE))));

                eventList.add(event);
            }
            while (cursor.moveToNext());
        }

        return eventList;
    }

    public Integer deleteEvent(Integer id)
    {
        String whereClause = Event.COLUMN_ID + " = " + id;
        return mSqlDatabase.delete(Event.TABLE, whereClause, null);
    }

    public Integer deleteEventList(Integer matchId)
    {
        String whereClause = Event.COLUMN_MATCH_ID + " = " + matchId;
        return mSqlDatabase.delete(Event.TABLE, null, null);
    }


    public Integer deleteEventList()
    {
        return mSqlDatabase.delete(Event.TABLE, null, null);
    }

    //==============================================================================================
    //==============================================================================================

    class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            createAllTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            dropAllTables(db);
            onCreate(db);
        }

        public void createAllTables(SQLiteDatabase db)
        {
            String query1 =
                    "CREATE TABLE IF NOT EXISTS " + Player.TABLE +
                        "(" +
                        Player.COLUMN_ID            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Player.COLUMN_FIRSTNAME     + " TEXT, " +
                        Player.COLUMN_LASTNAME      + " TEXT, " +
                        Player.COLUMN_PLAYERNUMBER  + " INTEGER, " +
                        Player.COLUMN_PICTURE       + " BLOB" +
                        ");"
                    ;

            String query2 =
                    "CREATE TABLE IF NOT EXISTS " + Match.TABLE +
                        "(" +
                        Match.COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Match.COLUMN_HOMETEAM       + " TEXT, " +
                        Match.COLUMN_GUESTTEAM      + " TEXT, " +
                        Match.COLUMN_DATE           + " DATE, " +
                        Match.COLUMN_TYPE           + " TEXT, " +
                        Match.COLUMN_LOCATION_TYPE  + " TEXT " +
                        ");"
                    ;

            String query3 =
                    "CREATE TABLE IF NOT EXISTS " + Event.TABLE +
                        "(" +
                        Event.COLUMN_ID             + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Event.COLUMN_MATCH_ID       + " INTEGER, " +
                        Event.COLUMN_PLAYER_ID      + " INTEGER, " +
                        Event.COLUMN_TYPE           + " TEXT, " +
                        Event.COLUMN_DATE           + " DATE " +
                        ");"
                    ;

            db.execSQL(query1);
            db.execSQL(query2);
            db.execSQL(query3);
        }

        public void dropAllTables(SQLiteDatabase db)
        {
            String query1 = "DROP TABLE IF EXISTS " + Player.TABLE;
            String query2 = "DROP TABLE IF EXISTS " + Match.TABLE;
            String query3 = "DROP TABLE IF EXISTS " + Event.TABLE;

            db.execSQL(query1);
            db.execSQL(query2);
            db.execSQL(query3);
        }
    }
}
