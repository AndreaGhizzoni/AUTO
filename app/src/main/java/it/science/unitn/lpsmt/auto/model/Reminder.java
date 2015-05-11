package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

/**
 * TODO add doc
 */
public class Reminder {
    private Long id;
    private Date date;
    private Integer calendarID;
    //<Something> periodic

    public Reminder( Date date, Integer calendarID, Long id ){
        this.setDate(date);
        this.setCalendarID(calendarID);
        this.setId(id);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId( Long id ){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

    public void setDate( Date date ){
        // TODO check if is an expired date
        if( date != null )
            this.date = date;
    }

    public void setCalendarID( Integer calendarID ){
        if( calendarID != null )
            this.calendarID = calendarID;
    }

//==================================================================================================
// GETTER
//==================================================================================================
    public Long getId() { return id; }

    public Date getDate() { return date; }

    public Integer getCalendarID() { return calendarID; }

//==================================================================================================
//  INNER CLASS
//==================================================================================================
    /**
     * TODO add doc
     */
    public static final class SQLData{
        public static final String TABLE_NAME  = Reminder.class.getSimpleName().toLowerCase();
        public static final String ID          = "id";
        public static final String DATE        = "date";
        public static final String CALENDAR_ID = "calendar_id";
    }
}
