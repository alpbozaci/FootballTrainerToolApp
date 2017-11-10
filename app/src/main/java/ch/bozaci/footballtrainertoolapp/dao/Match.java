package ch.bozaci.footballtrainertoolapp.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.util.DateUtil;

/**
 * Created by Alp.Bozaci on 23.08.2017.
 */

public class Match implements Serializable
{
    public static final String TABLE  = "match";

    public static final String COLUMN_ID              = "id";
    public static final String COLUMN_HOMETEAM        = "hometeam";
    public static final String COLUMN_GUESTTEAM       = "guestteam";
    public static final String COLUMN_DATE            = "date";
    public static final String COLUMN_TYPE            = "type";
    public static final String COLUMN_LOCATION_TYPE   = "location_type";


    public static enum MatchType
    {
        CHAMPIONSSHIP("CHAMPIONSSHIP"),
        TEST("TEST");

        private String type;

        MatchType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    public static enum LocationType
    {
        HOME_GAME("HOME GAME"),
        AWAY_GAME("AWAY GAME");

        private String type;

        LocationType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return type;
        }
    }

    private Integer id;
    private String homeTeam;
    private String guestTeam;
    private Date date;
    private String type;
    private String locationType;

    private List<Event> eventList;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getHomeTeam()
    {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam)
    {
        this.homeTeam = homeTeam;
    }

    public String getGuestTeam()
    {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam)
    {
        this.guestTeam = guestTeam;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public List<Event> getEventList()
    {
        return eventList;
    }

    public void setEventList(List<Event> eventList)
    {
        this.eventList = eventList;
    }

    @Override
    public String toString()
    {
        return DateUtil.dateFormat.format(date) + System.getProperty("line.separator") + homeTeam + " - " + guestTeam + System.getProperty("line.separator") + type;
    }

}
