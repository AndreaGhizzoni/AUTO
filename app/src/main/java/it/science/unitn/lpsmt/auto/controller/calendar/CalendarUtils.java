package it.science.unitn.lpsmt.auto.controller.calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import it.science.unitn.lpsmt.auto.ui.MainActivity;

import static android.provider.CalendarContract.Events.CALENDAR_ID;
import static android.provider.CalendarContract.Events.CONTENT_URI;
import static android.provider.CalendarContract.Events.DESCRIPTION;
import static android.provider.CalendarContract.Events.DTEND;
import static android.provider.CalendarContract.Events.DTSTART;
import static android.provider.CalendarContract.Events.EVENT_LOCATION;
import static android.provider.CalendarContract.Events.EVENT_TIMEZONE;
import static android.provider.CalendarContract.Events.HAS_ALARM;
import static android.provider.CalendarContract.Events.RDATE;
import static android.provider.CalendarContract.Events.RRULE;
import static android.provider.CalendarContract.Events.TITLE;
import static android.provider.CalendarContract.Events._ID;

/**
 * TODO add doc
 */
public class CalendarUtils {
    public static final int REMINDER_ONE_DAY = (24*60);
    public static final int REMINDER_FIVE_DAYS = REMINDER_ONE_DAY * 5;
    public static final int REMINDER_FIFTEEN_DAYS = REMINDER_ONE_DAY * 15;

    private static final String FREQ_COUNT = "COUNT=20";
    public static final String FREQ_YEARLY = "FREQ=YEARLY;"+FREQ_COUNT;

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

    public long putEvent( CalendarUtils.Holder data ){
        ContentResolver cr = context.getContentResolver();
        ContentValues cv = data.toContentValue();
        cv.put(CALENDAR_ID, DEFAULT_CALENDAR_ID);
        // Set Event in calendar.
        Uri eventUri = cr.insert(CONTENT_URI, cv);
        // Getting ID of event in Long.
        data.eventID = Long.parseLong(eventUri.getLastPathSegment());
        return data.eventID;
    }

    public long putEventWithReminder( CalendarUtils.Holder data, int nDaysBeforeReminder ){
        long eventID = putEvent(data);
        ContentResolver cr = context.getContentResolver();
        // String to access default google calendar of device for reminder setting.
        ContentValues rv = new ContentValues();
        rv.put(CalendarContract.Reminders.EVENT_ID, eventID);
        rv.put(CalendarContract.Reminders.MINUTES, nDaysBeforeReminder);
        rv.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //Setting reminder in calendar on Event.
        Uri reminderUri = cr.insert(CalendarContract.Reminders.CONTENT_URI, rv);
        data.reminderID = Long.parseLong(reminderUri.getLastPathSegment());
        return eventID;
    }

    public static String today(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String format = "%d/%d/%d";
        return String.format(format, day, month + 1, year);
    }

    public static String yesterday(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String format = "%d/%d/%d";
        return String.format(format, day, month + 1, year);
    }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * This class hold the data from reading an event from a calendar.
     */
    public static class Holder{
        public Integer calendarID;
        public Long eventID;
        public Long reminderID;
        public String title;
        public String location;
        public String description;
        public Date dStart;
        public Date dEnd;
        public String rRule;
        public Date rDate;
        public boolean hasAlarm;

        private ContentValues toContentValue(){
            ContentValues cv = new ContentValues();
            if( calendarID != null )
                cv.put(CALENDAR_ID, calendarID);
            if( eventID != null )
                cv.put(_ID, eventID);
            if( reminderID != null )
                cv.put(CalendarContract.Reminders._ID, reminderID);
            cv.put(TITLE, title);
            if( location != null )
                cv.put(EVENT_LOCATION, location);
            cv.put(DESCRIPTION, description);
            cv.put(DTSTART, dStart.getTime());
            cv.put(DTEND, dEnd.getTime());
            if( rRule != null )
                cv.put(RRULE, rRule);
            if( rDate != null )
                cv.put(RDATE, rDate.getTime());
            cv.put(HAS_ALARM, hasAlarm ? 1 : 0);
            cv.put(EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
            return cv;
        }
    }
}
