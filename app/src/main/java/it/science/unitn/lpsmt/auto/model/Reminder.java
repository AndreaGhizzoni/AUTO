package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

/**
 * TODO add doc
 */
public class Reminder {
    private Long id; //TODO move -1 in model/Const.java
    private Date date;
    private Integer calendarID;
    //<Something> periodic


    public Reminder( Date date, Integer calendarID ){
        this.setDate(date);
        this.setCalendarID(calendarID);
        this.setId(-1L);
    }

    public Reminder(){
        this(null, null);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId( Long id ){
        if( id != null && !id.equals(this.id) )
            this.id = id;
    }

    public void setDate( Date date ){
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
