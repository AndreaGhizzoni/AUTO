package it.science.unitn.lpsmt.auto.controller.calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;

import it.science.unitn.lpsmt.auto.ui.MainActivity;

import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.TITLE;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTEND;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.EVENT_LOCATION;
import static android.provider.CalendarContract.Events.RDATE;
import static android.provider.CalendarContract.Events.ALL_DAY;
import static android.provider.CalendarContract.Events.RRULE;

/**
 * TODO add doc
 */
public class CalendarUtils {
    private static CalendarUtils instance;

    private static final String CALENDAR_EVENTS_URI = "content://com.android.calendar/events";
    private Context context;

    public static CalendarUtils getInstance(){
        if(instance == null)
            instance = new CalendarUtils();
        return instance;
    }

    private CalendarUtils(){
        this.context = MainActivity.getApp().getApplicationContext();
    }

    /**
     * https://goo.gl/CyLlXx
     * This method build the Intent object to create a new calendar event.
     * @return
     */
    public Intent newEvent( String title ){
        Intent i = new Intent(Intent.ACTION_INSERT);
        i.putExtra(TITLE, title);
        i.setType("vnd.android.cursor.item/event");
        Calendar c = Calendar.getInstance();
        i.putExtra(DTSTART, c.getTimeInMillis());
        i.putExtra(DTEND, c.getTimeInMillis()+DateUtils.DAY_IN_MILLIS);
        i.putExtra(ALL_DAY, false);
        i.putExtra(RRULE, "FREQ=DAILY");
        //check out event location
        //add description
        return i;
    }

    // https://goo.gl/C0hXD1
    public CalendarUtils.Holder getEventDataById( Integer calID ){
        ContentResolver cr = this.context.getContentResolver();
        Cursor cursor = cr.query(
            Uri.parse(CALENDAR_EVENTS_URI),
            new String[]{CALENDAR_ID, TITLE, EVENT_LOCATION, DESCRIPTION, DTSTART, DTEND, RDATE},
            CALENDAR_ID + " = ?",
            new String[]{calID + ""},
            null
        );

        if( cursor.getCount() == 0 )
            return null;

        cursor.moveToFirst();
        CalendarUtils.Holder h = new CalendarUtils.Holder();
        while( cursor.moveToNext() ){
            h.calendarID = cursor.getInt(0);
            h.title = cursor.getString(1);
            h.location = cursor.getString(2);
            h.description = cursor.getString(3);
            h.dStart = new Date(cursor.getLong(4));
            h.dEnd = new Date(cursor.getLong(5));
            h.rDate = new Date(cursor.getLong(6));
        }
        cursor.close();
        return h;
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * This class hold the data from reading an event from a calendar.
     */
    public class Holder{
        public Integer calendarID;
        public String title;
        public String location;
        public String description;
        public Date dStart;
        public Date dEnd;
        public Date rDate;
    }
}
