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
        OWN_PLAYER_PRESENT("PLAYER PRESENT"),
        OWN_PLAYER_ABSENT("PLAYER ABSENT"),
        OWN_PLAYER_GOAL("PLAYER GOAL"),
        OWN_PLAYER_ASSIST("PLAYER ASSIST"),
        OWN_PLAYER_GOODPLAY("PLAYER GOOD PLAY"),
        OWN_PLAYER_BADPLAY("PLAYER BAD PLAY"),
        OWN_PLAYER_IN("PLAYER IN"),
        OWN_PLAYER_OUT("PLAYER OUT"),
        OWN_PLAYER_YELLOW_CARD("PLAYER YELLOW CARD"),
        OWN_PLAYER_RED_CARD("PLAYER RED CARD"),
        OWN_PLAYER_INJURED("PLAYER INJURED"),

        OPPOSING_TEAM_GOAL("OPPOSING PLAYER GOAL"),

        MATCH_START("MATCH START"),
        MATCH_PAUSE("MATCH PAUSE"),
        MATCH_FINISH("MATCH FINISH");

        private String type;

        EventType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }

        public static EventType fromString(String text) {
            for (EventType et : EventType.values()) {
                if (et.type.equalsIgnoreCase(text)) {
                    return et;
                }
            }
            return null;
        }
    }

    private Integer id;
    private Integer matchId;
    private Integer playerId;
    private String type;
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
