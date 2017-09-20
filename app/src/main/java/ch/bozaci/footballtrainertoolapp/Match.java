package ch.bozaci.footballtrainertoolapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Alp.Bozaci on 23.08.2017.
 */

public class Match implements Serializable
{
    public static final String TABLE  = "match";

    public static final String COLUMN_ID                = "id";
    public static final String COLUMN_HOMETEAM          = "hometeam";
    public static final String COLUMN_GUESTTEAM         = "guestteam";
    public static final String COLUMN_DATE              = "date";
    public static final String COLUMN_TYPE              = "type";
    public static final String COLUMN_TEAM_ID           = "team_id";
    public static final String COLUMN_SCORE_HOMETEAM    = "score_hometeam";
    public static final String COLUMN_SCORE_GUESTTEAM   = "score_guestteam";

    private Integer id;
    private String homeTeam;
    private String guestTeam;
    private Date date;
    private String type;
    private Integer teamId;
    private Integer scoreHomeTeam;
    private Integer scoreGuestTeam;

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

    public Integer getTeamId()
    {
        return teamId;
    }

    public void setTeamId(Integer teamId)
    {
        this.teamId = teamId;
    }

    public Integer getScoreHomeTeam()
    {
        return scoreHomeTeam;
    }

    public void setScoreHomeTeam(Integer scoreHomeTeam)
    {
        this.scoreHomeTeam = scoreHomeTeam;
    }

    public Integer getScoreGuestTeam()
    {
        return scoreGuestTeam;
    }

    public void setScoreGuestTeam(Integer scoreGuestTeam)
    {
        this.scoreGuestTeam = scoreGuestTeam;
    }

    @Override
    public String toString()
    {
        return Util.dateFormat.format(date) + System.getProperty("line.separator") + homeTeam + " - " + guestTeam + System.getProperty("line.separator") + type;
    }

}
