package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.DatabaseAdapter;
import ch.bozaci.footballtrainertoolapp.Player;
import ch.bozaci.footballtrainertoolapp.Team;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TeamTest
{
    private static DatabaseAdapter dbAdapter;

    @BeforeClass
    public static void beforeClass()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbAdapter = DatabaseAdapter.getInstance(appContext);
        dbAdapter.open();
    }

    @AfterClass
    public static void afterClass()
    {
        dbAdapter.close();
    }

    private void initDB()
    {
        dbAdapter.deleteTeams();
        dbAdapter.deletePlayers();

        Player player1 = new Player();
        player1.setFirstName("Sinan");
        player1.setLastName("Bozaci");
        player1.setPlayerNumber(new Integer(14));
        long id1 = dbAdapter.addPlayer(player1);

        Player player2 = new Player();
        player2.setFirstName("Alina");
        player2.setLastName("Bozaci");
        player2.setPlayerNumber(new Integer(42));
        long id2 = dbAdapter.addPlayer(player2);

        Player player3 = new Player();
        player3.setFirstName("Jonathan");
        player3.setLastName("Alesi");
        player3.setPlayerNumber(new Integer(13));
        long id3 = dbAdapter.addPlayer(player3);
    }

    @Test
    public void testAddTeamDB() throws Exception
    {
        initDB();

        List<Player> playerList = dbAdapter.getPlayerList();
        assertEquals(3, playerList.size());

        List<Player> playerListMatch1 = new ArrayList<>();
        List<Player> playerListMatch2 = new ArrayList<>();

        playerListMatch1.add(playerList.get(0));
        playerListMatch1.add(playerList.get(1));

        playerListMatch2.add(playerList.get(0));
        playerListMatch2.add(playerList.get(1));
        playerListMatch2.add(playerList.get(2));

        Team team1 = new Team();
        team1.setPlayerList(playerListMatch1);
        team1.setTeamId(1);

        Team team2 = new Team();
        team2.setPlayerList(playerListMatch2);
        team2.setTeamId(2);

        dbAdapter.addTeam(team1);
        dbAdapter.addTeam(team2);

        Team team1db = dbAdapter.getTeam(1);
        Team team2db = dbAdapter.getTeam(2);
        Team team3db = dbAdapter.getTeam(3); // does not exist

        assertNotNull(team1db);
        assertNotNull(team2db);
        assertNull(team3db);

        assertEquals(2,team1db.getPlayerList().size());
        assertEquals(3,team2db.getPlayerList().size());

        assertEquals(new Integer(1), team1db.getTeamId());
        assertEquals(new Integer(2), team2db.getTeamId());
    }

    @Test
    public void testDeleteTeamDB() throws Exception
    {
        initDB();

        List<Player> playerList = dbAdapter.getPlayerList();
        assertEquals(3, playerList.size());

        List<Player> playerListMatch1 = new ArrayList<>();
        List<Player> playerListMatch2 = new ArrayList<>();

        playerListMatch1.add(playerList.get(0));
        playerListMatch1.add(playerList.get(1));

        playerListMatch2.add(playerList.get(0));
        playerListMatch2.add(playerList.get(1));
        playerListMatch2.add(playerList.get(2));

        Team team1 = new Team();
        team1.setPlayerList(playerListMatch1);
        team1.setTeamId(1);

        Team team2 = new Team();
        team2.setPlayerList(playerListMatch2);
        team2.setTeamId(2);

        dbAdapter.addTeam(team1);
        dbAdapter.addTeam(team2);

        dbAdapter.deleteTeam(1);
        dbAdapter.deleteTeam(2);

        Team team1db = dbAdapter.getTeam(1);
        Team team2db = dbAdapter.getTeam(2);

        assertNull(team1db);
        assertNull(team2db);
    }

}
