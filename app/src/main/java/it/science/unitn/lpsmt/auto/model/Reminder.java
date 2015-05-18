package it.science.unitn.lpsmt.auto.model;

import java.util.Date;

import it.science.unitn.lpsmt.auto.model.util.Const;

/**
 * TODO add doc
 */
public class Reminder {
    private Long id;
    private Date date;
    private Integer calendarID;

    public Reminder( Date date, Integer calendarID, Long id ){
        this.setDate(date);
        this.setCalendarID(calendarID);
        this.setId(id);
    }

//==================================================================================================
// SETTER
//==================================================================================================
    public void setId( Long id ){
        if( id == null )
            this.id = Const.NO_DB_ID_SET;
        else if( id < Const.NO_DB_ID_SET )
                throw new IllegalArgumentException("ID of Reminder can not be less then "+Const.NO_DB_ID_SET);
        else
            this.id = id;
    }

    public void setDate( Date date ){
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

        public static final String SQL_CREATE =
                "create table "+TABLE_NAME+" ( "+
                    ID+" integer primary key autoincrement, "+
                    DATE+" datetime, "+
                    CALENDAR_ID+" integer "+
                ");";
    }
}
