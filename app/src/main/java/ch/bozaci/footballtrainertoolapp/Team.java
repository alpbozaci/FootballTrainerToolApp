package ch.bozaci.footballtrainertoolapp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alp.Bozaci on 24.08.2017.
 */

public class Team implements Serializable
{
    public static final String TABLE = "team";

    public static final String COLUMN_ID        = "id";
    public static final String COLUMN_TEAM_ID   = "team_id";
    public static final String COLUMN_PLAYER_ID = "player_id";

    private Integer id;
    private Integer teamId; // pseudo key
    private List<Player> playerList;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getTeamId()
    {
        return teamId;
    }

    public void setTeamId(Integer teamId)
    {
        this.teamId = teamId;
    }

    public List<Player> getPlayerList()
    {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList)
    {
        this.playerList = playerList;
    }
}
