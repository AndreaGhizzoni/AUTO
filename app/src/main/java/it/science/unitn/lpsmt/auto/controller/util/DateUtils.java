package it.science.unitn.lpsmt.auto.controller.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TODO add doc
 */
public final class DateUtils {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Returns the string according to the format given. Leave null to use DEFAULT_FORMAT.
     * @param d Date to format.
     * @param format String of format
     * @return String the formatted string..
     */
    public static String getStringFromDate( Date d, String format ){
        if( format == null ) format = DEFAULT_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return d == null? null : sdf.format(d);
    }

    /**
     * Returns the date object from a String and his format.
     * @param date String the date
     * @param hisFormat String the date format
     * @return Date object corresponding to the string given formatted with "hisFormat" argument
     */
    public static Date getDateFromString( String date, String hisFormat ){
        if( date == null ) return null;
        if( hisFormat == null ) hisFormat = DEFAULT_FORMAT;

        SimpleDateFormat sdf = new SimpleDateFormat(hisFormat, Locale.getDefault());
        try{
            return sdf.parse(date);
        }catch( ParseException e ){
            Log.e(DateUtils.class.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Returns the string according the format of DEFAULT_FORMAT;
     * @param d Date to format
     * @return String the formatted string.
     */
    public static String getStringFromDate( Date d ){
        return getStringFromDate( d, DEFAULT_FORMAT );
    }

    /**
     * Return the date object from a String using DEFAULT_FORMAT
     * @param date String date
     * @return Date object corresponding to the string given formatted with DEFAULT_FORMAT
     */
    public static Date getDateFromString( String date ){
        return getDateFromString( date, DEFAULT_FORMAT );
    }
}
