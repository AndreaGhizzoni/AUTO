package it.science.unitn.lpsmt.auto.controller.calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import it.science.unitn.lpsmt.auto.ui.MainActivity;

import static android.provider.CalendarContract.Events.ALL_DAY;
import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.CONTENT_URI;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTEND;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.EVENT_LOCATION;
import static android.provider.CalendarContract.Events.EVENT_TIMEZONE;
import static android.provider.CalendarContract.Events.RDATE;
import static android.provider.CalendarContract.Events.TITLE;
import static android.provider.CalendarContract.Events.HAS_ALARM;

/**
 * TODO add doc
 */
public class CalendarUtils {
    private static CalendarUtils instance;

    private static final Integer DEFAULT_CALENDAR_ID = 1;
    private Context context;

    /**
     * TODO add doc
     * @return
     */
    public static CalendarUtils getInstance(){
        if(instance == null)
            instance = new CalendarUtils();
        return instance;
    }

    private CalendarUtils(){ this.context = MainActivity.getApp().getApplicationContext(); }

    /**
     * https://goo.gl/CyLlXx
     * This method build the Intent object to create a new calendar event.
     * @return
     */
    public Intent newEvent( String title ){
        Intent i = new Intent(Intent.ACTION_INSERT, CONTENT_URI);
        i.putExtra(TITLE, title);
        Calendar c = Calendar.getInstance();
        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, c.getTimeInMillis());
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, c.getTimeInMillis()+DateUtils.DAY_IN_MILLIS);
        i.putExtra(ALL_DAY, false);
//        i.putExtra(RRULE, "FREQ=DAILY");
        //check out event location
        //add description
        return i;
    }

    /**
     * TODO add doc
     * @param calID
     * @return
     */
    public CalendarUtils.Holder getEventDataById( Integer calID ){
        // https://goo.gl/C0hXD1
        ContentResolver cr = this.context.getContentResolver();
        Cursor cursor = cr.query(
                CONTENT_URI,
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

    public long putEvent( CalendarUtils.Holder data ){
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = data.toContentValue();
        cv.put(CALENDAR_ID, DEFAULT_CALENDAR_ID);

        // Set Event in calendar.
        Uri eventUri = cr.insert(CONTENT_URI, cv);
        // Getting ID of event in Long.
        return Long.parseLong(eventUri.getLastPathSegment());
    }

    public long putEventWithReminder( CalendarUtils.Holder data ){
        long eventID = putEvent(data);
        ContentResolver cr = context.getContentResolver();
        // String to access default google calendar of device for reminder setting.
        ContentValues rv = new ContentValues();
        rv.put(CalendarContract.Reminders.EVENT_ID, eventID);
        rv.put(CalendarContract.Reminders.MINUTES, (60*24)*2); // 2 days
        rv.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //Setting reminder in calendar on Event.
        cr.insert(CalendarContract.Reminders.CONTENT_URI, rv);
        return eventID;
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * This class hold the data from reading an event from a calendar.
     */
    public static class Holder{
        public Integer calendarID;
        public String title;
        public String location;
        public String description;
        public Date dStart;
        public Date dEnd;
        public Date rDate;
        public boolean hasAlarm;

        private ContentValues toContentValue(){
            ContentValues cv = new ContentValues();
            cv.put(TITLE, title);
            cv.put(EVENT_LOCATION, location);
            cv.put(DESCRIPTION, description);
            cv.put(DTSTART, dStart.getTime());
            cv.put(DTEND, dEnd.getTime());
            if( rDate != null )
                cv.put(RDATE, rDate.getTime());
            cv.put(HAS_ALARM, hasAlarm ? 1 : 0);
            cv.put(EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
            return cv;
        }
    }
}
