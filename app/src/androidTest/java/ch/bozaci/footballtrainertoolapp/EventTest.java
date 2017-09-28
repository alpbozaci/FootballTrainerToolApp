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

import ch.bozaci.footballtrainertoolapp.dao.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EventTest
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
        dbAdapter.deleteEventList();

        Event event1 = new Event();
        event1.setMatchId(1);
        event1.setPlayerId(100);
        event1.setType("Goal");
        event1.setDate(new Date());

        Event event2 = new Event();
        event2.setMatchId(1);
        event2.setPlayerId(101);
        event2.setType("Assist");
        event2.setDate(new Date());

        Integer eventId1 = dbAdapter.addEvent(event1);
        Integer eventId2 = dbAdapter.addEvent(event2);

        assertFalse(eventId1 == -1);
        assertFalse(eventId2 == -1);

        List<Event> eventList = dbAdapter.getEventList();
        assertEquals(2, eventList.size());
    }

    @Test
    public void testDeletePlayerDB() throws Exception
    {
        dbAdapter.deleteEventList();

        Event event1 = new Event();
        event1.setMatchId(1);
        event1.setPlayerId(100);
        event1.setType("Goal");
        event1.setDate(new Date());

        Event event2 = new Event();
        event2.setMatchId(1);
        event2.setPlayerId(101);
        event2.setType("Assist");
        event2.setDate(new Date());

        Integer eventId1 = dbAdapter.addEvent(event1);
        Integer eventId2 = dbAdapter.addEvent(event2);

        dbAdapter.deleteEvent(eventId1);
        Event event1db = dbAdapter.getEvent(eventId1);
        assertNull(event1db);
        assertEquals(1, dbAdapter.getEventList().size());

        dbAdapter.deleteEvent(eventId2);
        Event event2db = dbAdapter.getEvent(eventId2);
        assertNull(event2db);
        assertEquals(0, dbAdapter.getEventList().size());
    }

}
