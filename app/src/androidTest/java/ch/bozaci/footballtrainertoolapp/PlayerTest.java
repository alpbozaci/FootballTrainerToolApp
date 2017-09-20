package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PlayerTest
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

    @Test
    public void testAddPlayerDB() throws Exception
    {
        dbAdapter.deletePlayers();

        Player player1 = new Player();
        player1.setFirstName("Sinan");
        player1.setLastName("Bozaci");
        player1.setPlayerNumber(new Integer(14));
        Integer playerId1 = dbAdapter.addPlayer(player1);

        Player player2 = new Player();
        player2.setFirstName("Alina");
        player2.setLastName("Bozaci");
        player2.setPlayerNumber(new Integer(42));
        Integer playerId2 = dbAdapter.addPlayer(player2);

        Player player3 = new Player();
        player3.setFirstName("Jonathan");
        player3.setLastName("Alesi");
        player3.setPlayerNumber(new Integer(13));
        Integer playerId3 = dbAdapter.addPlayer(player3);

        assertFalse(playerId1 == -1);
        assertFalse(playerId2 == -1);
        assertFalse(playerId3 == -1);

        List<Player> playerList1 = dbAdapter.getPlayerList();
        assertEquals(3, playerList1.size());
    }

    @Test
    public void testUpdatePlayerDB() throws Exception
    {
        dbAdapter.deletePlayers();

        Player player = new Player();
        player.setFirstName("Sinan");
        player.setLastName("Bozaci");
        player.setPlayerNumber(new Integer(14));
        Integer playerId = dbAdapter.addPlayer(player);

        Player playerdb = dbAdapter.getPlayer(playerId);

        playerdb.setFirstName("Nanis");
        playerdb.setLastName("Icazob");
        playerdb.setPlayerNumber(new Integer(41));
        dbAdapter.updatePlayer(playerdb);

        Player updatedPlayer = dbAdapter.getPlayer(playerId);

        assertEquals("Nanis", updatedPlayer.getFirstName());
        assertEquals("Icazob", updatedPlayer.getLastName());
        assertEquals(new Integer(41), updatedPlayer.getPlayerNumber());
    }

    @Test
    public void testDeletePlayerDB() throws Exception
    {
        dbAdapter.deletePlayers();

        Player player1 = new Player();
        player1.setFirstName("Sinan");
        player1.setLastName("Bozaci");
        player1.setPlayerNumber(new Integer(14));

        Player player2 = new Player();
        player2.setFirstName("Alina");
        player2.setLastName("Bozaci");
        player2.setPlayerNumber(new Integer(42));

        Integer id1 = dbAdapter.addPlayer(player1);
        Integer id2 = dbAdapter.addPlayer(player2);

        dbAdapter.deletePlayer(id1);
        Player player1db = dbAdapter.getPlayer(id1);
        assertNull(player1db);
        assertEquals(1, dbAdapter.getPlayerList().size());

        dbAdapter.deletePlayer(id2);
        Player player2db = dbAdapter.getPlayer(id1);
        assertNull(player2db);
        assertEquals(0, dbAdapter.getPlayerList().size());
    }

}
