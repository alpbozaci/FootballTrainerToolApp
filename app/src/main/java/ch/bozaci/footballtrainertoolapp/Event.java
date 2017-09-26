package ch.bozaci.footballtrainertoolapp;

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
        PLAYER_PRESENT("PLAYER PRESENT"),
        PLAYER_ABSENT("PLAYER ABSENT"),
        PLAYER_GOAL_OWNTEAM("PLAYER GOAL OWN TEAM"),
        PLAYER_GOAL_OPPOSINGTEAM("PLAYER GOAL OPPOSING TEAM"),
        PLAYER_ASSIST("PLAYER ASSIST"),
        PLAYER_IN("PLAYER IN"),
        PLAYER_OUT("PLAYER OUT"),
        PLAYER_YELLOW_CARD("PLAYER YELLOW CARD"),
        PLAYER_RED_CARD("PLAYER RED CARD"),
        PLAYER_INJURED("PLAYER INJURED");

        private String type;

        EventType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    private Integer id;
    private Integer matchId;
    private Integer playerId;
    private String type;
    private Date date;

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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
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

    @Override
    public String toString()
    {
        return getType() + " : " + getDate();
    }
}
