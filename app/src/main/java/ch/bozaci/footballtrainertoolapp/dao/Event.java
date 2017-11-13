package ch.bozaci.footballtrainertoolapp.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Alp.Bozaci on 25.08.2017.
 */

public class Event implements Serializable
{
    public static final String TABLE  = "event";

    public static final String COLUMN_ID        = "id";
    public static final String COLUMN_MATCH_ID  = "match_id";
    public static final String COLUMN_PLAYER_ID = "player_id";
    public static final String COLUMN_TYPE      = "type";
    public static final String COLUMN_DATE      = "date";


    public static enum EventType
    {
        OWN_PLAYER_PRESENT,
        OWN_PLAYER_ABSENT,
        OWN_PLAYER_GOAL,
        OWN_PLAYER_ASSIST,
        OWN_PLAYER_GOODPLAY,
        OWN_PLAYER_BADPLAY,
        OWN_PLAYER_IN,
        OWN_PLAYER_OUT,
        OWN_PLAYER_YELLOW_CARD,
        OWN_PLAYER_RED_CARD,
        OWN_PLAYER_INJURED,

        OPPOSING_TEAM_GOAL,

        MATCH_START,
        MATCH_PAUSE,
        MATCH_FINISH
    }

    private Integer id;
    private Integer matchId;
    private Integer playerId;
    private EventType type;
    private Date date;

    private Match match;
    private Player player;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getMatchId()
    {
        return matchId;
    }

    public void setMatchId(Integer matchId)
    {
        this.matchId = matchId;
    }

    public Integer getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(Integer playerId)
    {
        this.playerId = playerId;
    }

    public EventType getType()
    {
        return type;
    }

    public void setType(EventType type)
    {
        this.type = type;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Match getMatch()
    {
        return match;
    }

    public void setMatch(Match match)
    {
        this.match = match;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    @Override
    public String toString()
    {
        return getType() + " : " + getDate();
    }

}
