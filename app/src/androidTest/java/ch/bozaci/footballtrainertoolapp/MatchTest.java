package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Match;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MatchTest
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
    public void testAddMatchDB() throws Exception
    {
        dbAdapter.deleteMatchList();

        Match match1 = new Match();
        match1.setHomeTeam("Dürrenast");
        match1.setGuestTeam("Steffisburg");
        match1.setDate(new Date());
        match1.setType("Match");
        match1.setLocationType(Match.LocationType.HOME_GAME.getType());

        Match match2 = new Match();
        match2.setHomeTeam("Steffisburg");
        match2.setGuestTeam("Dürrenast");
        match2.setDate(new Date());
        match2.setType("Match");
        match2.setLocationType(Match.LocationType.AWAY_GAME.getType());

        Integer matchId1 = dbAdapter.addMatch(match1);
        Integer matchId2 = dbAdapter.addMatch(match2);

        assertNotNull(matchId1);
        assertNotNull(matchId2);

        assertFalse(matchId1 == -1);
        assertFalse(matchId2 == -1);

        List<Match> matchList = dbAdapter.getMatchList();
        assertNotNull(matchList);
        assertEquals(2, matchList.size());
    }

    @Test
    public void testDeleteMatchDB() throws Exception
    {
        Match match1 = new Match();
        match1.setId(1);
        match1.setHomeTeam("Dürrenast");
        match1.setGuestTeam("Steffisburg");
        match1.setDate(new Date());

        Match match2 = new Match();
        match2.setId(2);
        match2.setHomeTeam("Steffisburg");
        match2.setGuestTeam("Dürrenast");
        match2.setDate(new Date());

        Integer matchId1 = dbAdapter.addMatch(match1);
        Integer matchId2 = dbAdapter.addMatch(match2);

        assertEquals(2, dbAdapter.getMatchList().size());

        dbAdapter.deleteMatch(matchId1);
        Match match1db = dbAdapter.getMatch(matchId1);
        assertNull(match1db);
        assertEquals(1, dbAdapter.getMatchList().size());

        dbAdapter.deleteMatch(matchId2);
        Match match2db = dbAdapter.getMatch(matchId2);
        assertNull(match2db);
        assertEquals(0, dbAdapter.getMatchList().size());
    }

}
