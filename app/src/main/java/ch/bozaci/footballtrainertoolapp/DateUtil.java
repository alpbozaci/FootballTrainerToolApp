package ch.bozaci.footballtrainertoolapp;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alp.Bozaci on 28.08.2017.
 */

public class DateUtil
{
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static Date getDate(DatePicker datePicker, TimePicker timePicker)
    {
        int year   = datePicker.getYear();
        int month  = datePicker.getMonth();
        int day    = datePicker.getDayOfMonth();
        int hour   = timePicker.getHour();
        int minute = timePicker.getMinute();
        int second = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);

        return cal.getTime();
    }
}
