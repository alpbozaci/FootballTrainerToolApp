package ch.bozaci.footballtrainertoolapp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alp.Bozaci on 24.08.2017.
 */

public class Team implements Serializable
{
    private List<Player> playerList;

    public List<Player> getPlayerList()
    {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList)
    {
        this.playerList = playerList;
    }
}
